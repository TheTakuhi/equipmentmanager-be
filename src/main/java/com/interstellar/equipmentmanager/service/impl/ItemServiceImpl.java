package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemCreateDTO;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemEditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.filter.ItemFilter;
import com.interstellar.equipmentmanager.model.filter.ItemSpecifications;
import com.interstellar.equipmentmanager.repository.ItemRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.ItemService;
import com.interstellar.equipmentmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserAuthorizationService userAuthorizationService;
    private final ModelMapper mapper;

    @Override
    public ItemDTO createItem(ItemCreateDTO itemCreateDTO) {
        Item item = new Item();
        mapper.map(itemCreateDTO, item);
        item.setOwner(mapper.map(userAuthorizationService.getCurrentUser(), User.class));
        item.setState(State.AVAILABLE);
        return mapper.map(itemRepository.save(item), ItemDTO.class);
    }

    @Override
    public ItemDTO updateItem(UUID id, ItemEditDTO itemEditDTO) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Item.class.getName(), "id", id.toString()));
        item.setSerialCode(itemEditDTO.getSerialCode());
        item.setComment(itemEditDTO.getComment());
        item.setType(itemEditDTO.getType());
        item.setQualityState(itemEditDTO.getQualityState());
        item.setState(itemEditDTO.getState());

        if (itemEditDTO.getOwnerId() != null) {
            User user = userService.getOriginalUser(itemEditDTO.getOwnerId());
            item.getOwner().getOwnedItems().remove(item);
            item.setOwner(user);
            user.getOwnedItems().add(item);
        }
        return mapper.map(itemRepository.save(item), ItemDTO.class);
    }

    @Override
    public Boolean isOwner(UUID id) {

        if (getItemById(id).getOwner().getId().equals(userAuthorizationService.getCurrentUser().getId())) {
            return true;
        } else return false;
    }


    @Override
    public ItemDTO getItemDTOById(UUID id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Item.class.getName(), "id", id.toString()));
        return mapper.map(item, ItemDTO.class);
    }

    @Override
    public Item getItemById(UUID id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Item.class.getName(), "id", id.toString()));
        return item;
    }


    @Override
    @Transactional
    public void deleteItemById(UUID itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException(Item.class.getName(), "id", itemId.toString()));
        if (!item.getState().equals(State.BORROWED)) {
            item.setState(State.DISCARDED);
        } else {
            throw new ResourceConflictException(String.format("Item with id %s cannot be deleted, because it is currently borrowed.", itemId.toString()));
        }
        itemRepository.save(item);
    }


    //todo manager can see only his and team members items
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Page<ItemDTO> getAllItems(@Nullable ItemFilter filter, @Nullable Pageable pageable) {
        if (pageable == null) pageable = Pageable.ofSize(Integer.MAX_VALUE);
        if (filter == null) filter = new ItemFilter();

        Specification<Item> spec = ItemSpecifications.filterItems(
                filter.getSerialCode() == null ? null : String.format("%%%s%%", filter.getSerialCode()),
                filter.getType(),
                filter.getState(),
                filter.getQualityState()
        );

        var items = itemRepository.findAll(spec, pageable);
        return items.map(u -> mapper.map(u, ItemDTO.class));
    }

    @Override
    public List<ItemDTO> findAllItemsByOwnerIdNotDiscarded(UUID id) {

        return itemRepository.findAllByOwnerIdAndBeingDiscarded(id).stream().map(i -> mapper.map(i, ItemDTO.class)).collect(Collectors.toList());
    }


    @Override
    public void changeOwnerOfItems(UUID fromUserId, UUID toUserId) {
        List<Item> items = itemRepository.findAllByOwnerIdAndBeingDiscarded(fromUserId);
        User toUser = userService.getOriginalUser(toUserId);
        items.forEach(i -> {
            if (!i.getState().equals(State.DISCARDED)) {
                i.setOwner(toUser);
            }
        });

        itemRepository.saveAll(items);
    }
}
