package com.interstellar.equipmentmanager.config.mapper;

import com.interstellar.equipmentmanager.model.dto.item.in.ItemEditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemDTO;
import com.interstellar.equipmentmanager.model.entity.Item;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ItemMapperConfig {
    private final ModelMapper mapper;

    private final Converter<Item, UUID> itemToId = ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

    private final Converter<ItemCroppedDTO, UUID> itemCroppedToId = ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

    private final Converter<UUID, Item> idToItem = ctx -> {
        if (ctx.getSource() == null) {
            return null;
        }
        var item = new Item();
        item.setId(ctx.getSource());
        return item;
    };

    @Bean
    public void ItemConverting() {
        mapper.typeMap(Item.class, UUID.class).setConverter(itemToId);
        mapper.typeMap(UUID.class, Item.class).setConverter(idToItem);
        mapper.typeMap(ItemCroppedDTO.class, UUID.class).setConverter(itemCroppedToId);
        mapper.typeMap(Item.class, ItemDTO.class).addMappings(
                em -> {
                    em.map(Item::getOwner, ItemDTO::setOwner);
                    em.map(Item::getLoans, ItemDTO::setLoans);
                }
        );
        mapper.typeMap(ItemDTO.class, Item.class).addMappings(
                em -> {
                    em.map(ItemDTO::getOwner, Item::setOwner);
                    em.map(ItemDTO::getLoans, Item::setLoans);
                }
        );
        mapper.typeMap(Item.class, ItemCroppedDTO.class).addMappings(
                em -> {
                    em.map(Item::getOwner, ItemCroppedDTO::setOwnerId);
                    em.map(Item::getLoans, ItemCroppedDTO::setLoansId);
                }
        );
        mapper.typeMap(ItemEditDTO.class, Item.class).addMappings(
                em ->{
                    em.skip(Item::setId);
                    em.skip(Item::setOwner);

                }
        );
    }
}
