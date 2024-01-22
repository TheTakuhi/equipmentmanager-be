package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.item.in.ItemCreateDTO;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemEditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.filter.ItemFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    ItemDTO createItem(ItemCreateDTO itemCreateDTO);

    ItemDTO updateItem(UUID id, ItemEditDTO itemEditDTO);

    Boolean isOwner(UUID id);

    ItemDTO getItemDTOById(UUID id);

    Item getItemById(UUID id);

    @Transactional
    void deleteItemById(UUID itemId);

    @Transactional(propagation = Propagation.REQUIRED)
    Page<ItemDTO> getAllItems(@Nullable ItemFilter filter, @Nullable Pageable pageable);

    List<ItemDTO> findAllItemsByOwnerIdNotDiscarded(UUID id);

    void changeOwnerOfItems(UUID fromUserId, UUID toUserId);
}
