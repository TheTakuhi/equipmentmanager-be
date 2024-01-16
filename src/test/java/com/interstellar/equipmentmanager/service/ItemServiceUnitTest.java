package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemCreateDTO;
import com.interstellar.equipmentmanager.model.dto.item.in.ItemEditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import com.interstellar.equipmentmanager.model.filter.ItemFilter;
import com.interstellar.equipmentmanager.repository.ItemRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ItemServiceUnitTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserAuthorizationService userAuthorizationService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem() {
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO();

        UserCroppedDTO currentUser = new UserCroppedDTO();
        User user = new User();
        ItemDTO itemDTO = new ItemDTO();

        when(userAuthorizationService.getCurrentUser()).thenReturn(currentUser);

        Item item = new Item();

        when(mapper.map(itemCreateDTO, Item.class)).thenReturn(item);
        when(mapper.map(currentUser, User.class)).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setId(UUID.randomUUID());
            return savedItem;
        });
        when(mapper.map(any(Item.class), eq(ItemDTO.class))).thenReturn(itemDTO);

        ItemDTO result = itemService.createItem(itemCreateDTO);

        assertNotNull(result);
    }

    @Test
    void updateItem() {
        UUID itemId = UUID.randomUUID();
        ItemEditDTO editDTO = new ItemEditDTO();
        Item item = new Item();
        item.setId(itemId);
        ItemDTO itemDTO = new ItemDTO();
        Optional<Item> optionalItem = Optional.of(item);

        when(itemRepository.findById(itemId)).thenReturn(optionalItem);
        when(userService.getOriginalUser(any(UUID.class))).thenReturn(new User());
        when(itemRepository.save(item)).thenReturn(item);
        when(mapper.map(item, ItemDTO.class)).thenReturn(itemDTO);

        ItemDTO result = itemService.updateItem(itemId, editDTO);

        assertNotNull(result);
    }

    @Test
    void isOwner_itemBelongsToCurrentUser_shouldReturnTrue() {
        UUID itemId = UUID.randomUUID();
        UserCroppedDTO currentUser = new UserCroppedDTO();
        currentUser.setId(UUID.randomUUID());

        Mockito.doReturn(currentUser)
                .when(userAuthorizationService)
                .getCurrentUser(Mockito.any());

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setOwner(currentUser);
        Item item = new Item();
        item.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(mapper.map(item, ItemDTO.class)).thenReturn(itemDTO);
        when(userAuthorizationService.getCurrentUser()).thenReturn(currentUser);

        boolean result = itemService.isOwner(itemId);

        Assertions.assertTrue(result);
    }

    @Test
    void isOwner_itemDoesNotBelongToCurrentUser_shouldReturnFalse() {
        UUID itemId = UUID.randomUUID();
        UserCroppedDTO currentUser = new UserCroppedDTO();
        currentUser.setId(UUID.randomUUID());

        Mockito.doReturn(currentUser)
                .when(userAuthorizationService)
                .getCurrentUser(Mockito.any());

        UserCroppedDTO owner = new UserCroppedDTO();
        owner.setId(UUID.randomUUID());

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setOwner(owner);
        Item item = new Item();
        item.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(mapper.map(item, ItemDTO.class)).thenReturn(itemDTO);
        when(userAuthorizationService.getCurrentUser()).thenReturn(currentUser);

        boolean result = itemService.isOwner(itemId);

        Assertions.assertFalse(result);
    }

    @Test
    void isOwner_itemNotFound_shouldThrowResourceNotFoundException() {
        UUID itemId = UUID.randomUUID();

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.isOwner(itemId));
    }

    @Test
    void deleteItemById_itemNotBorrowed_shouldDelete() {
        UUID itemId = UUID.randomUUID();
        Item item = new Item();
        item.setState(State.AVAILABLE);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        assertDoesNotThrow(() -> itemService.deleteItemById(itemId));
    }

    @Test
    void deleteItemById_itemBorrowed_shouldThrowConflictException() {
        UUID itemId = UUID.randomUUID();
        Item item = new Item();
        item.setState(State.BORROWED);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(ResourceConflictException.class, () -> itemService.deleteItemById(itemId));
    }

    @Test
    void getAllItems_withFilter_shouldReturnFilteredItems() {
        ItemFilter filter = new ItemFilter();
        filter.setSerialCode("ABC");
        filter.setType(Type.CHAIR);
        filter.setState(State.AVAILABLE);
        filter.setQualityState(QualityState.DAMAGED);

        List<Item> filteredItems = Collections.singletonList(new Item());
        Page<Item> page = new PageImpl<>(filteredItems);
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<ItemDTO> result = itemService.getAllItems(filter, null);

        Assertions.assertEquals(filteredItems.size(), result.getContent().size());
    }

    @Test
    void getAllItems_withoutFilter_shouldReturnAllItems() {
        List<Item> allItems = Arrays.asList(new Item(), new Item(), new Item());
        Page<Item> page = new PageImpl<>(allItems);
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<ItemDTO> result = itemService.getAllItems(null, null);

        Assertions.assertEquals(allItems.size(), result.getContent().size());
    }
}
