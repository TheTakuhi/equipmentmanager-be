package com.interstellar.equipmentmanager.controller;

import com.interstellar.equipmentmanager.helper.RequestHelper;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.Type;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerTest {
    
    private final RequestHelper requestHelper;
    private final TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @Autowired
    public ItemControllerTest(RequestHelper requestHelper, TestRestTemplate restTemplate) {
        this.requestHelper = requestHelper;
        this.restTemplate = restTemplate;
    }
    
    @Test
    void itShouldGetItemById() {
        UUID itemId = UUID.fromString("0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d");
        
        ResponseEntity<ItemDTO> res = restTemplate.exchange(
                requestHelper.createRequestURL("/items/%s", port).formatted(itemId),
                HttpMethod.GET,
                requestHelper.getSecuredRequest(),
                ItemDTO.class
        );
        
        assertAll(
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(res.getBody().getId()).isEqualTo(itemId),
                () -> assertThat(res.getBody().getType()).isEqualTo(Type.TABLE)
        );
    }
    
    @Test
    void itShouldNotDeleteBorrowedItemById() {
        UUID itemId = UUID.fromString("0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d");
        
        ResponseEntity<Void> res = restTemplate.exchange(
                requestHelper.createRequestURL("/items/%s", port).formatted(itemId),
                HttpMethod.DELETE,
                requestHelper.getSecuredRequest(),
                Void.class
        );
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
    
    @Test
    void itShouldCreateItem() throws JSONException {
        UUID ownerId = UUID.fromString("5f81c201-d43a-4c91-80ca-0f02b43202de");
        
        JSONObject newItem = new JSONObject();
        newItem.put("serialCode", "gfemk");
        newItem.put("comment", "Item description");
        newItem.put("type", Type.TABLE);
        newItem.put("qualityState", QualityState.NEW);
        newItem.put("ownerId", ownerId);
        
        ResponseEntity<ItemDTO> res = restTemplate.exchange(
                requestHelper.createRequestURL("/items", port),
                HttpMethod.POST,
                requestHelper.getSecuredRequest(newItem),
                ItemDTO.class
        );
        
        assertAll(
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(res.getBody().getOwner().getId()).isEqualTo(ownerId),
                () -> assertThat(res.getBody().getType()).isEqualTo(Type.TABLE),
                () -> assertThat(res.getBody().getQualityState()).isEqualTo(QualityState.NEW)
        );
        
    }
    
    @Test
    void itShouldNotCreateItemAndReturnValidationError() throws JSONException {
        UUID ownerId = UUID.fromString("5f81c201-d43a-4c91-80ca-0f02b43202de");
        
        JSONObject newItem = new JSONObject();
        newItem.put("serialCode", "gfemk");
        newItem.put("comment", "Item description");
        newItem.put("type", Type.TABLE);
        newItem.put("qualityState", "shitty");
        newItem.put("ownerId", ownerId);
        
        ResponseEntity<ItemDTO> res = restTemplate.exchange(
                requestHelper.createRequestURL("/items", port),
                HttpMethod.POST,
                requestHelper.getSecuredRequest(newItem),
                ItemDTO.class
        );
        
        assertAll(
                () -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }
}