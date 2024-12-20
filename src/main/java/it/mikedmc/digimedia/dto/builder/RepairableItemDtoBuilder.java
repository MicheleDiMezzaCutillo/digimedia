package it.mikedmc.digimedia.dto.builder;

import it.mikedmc.digimedia.dto.RepairableItemDto;
import it.mikedmc.digimedia.model.RepairableItem;

public class RepairableItemDtoBuilder {
    public static RepairableItem fromDtoToEntity(RepairableItemDto repairableItemDto) {
        RepairableItem repairableItem = new RepairableItem();
        repairableItem.setId(repairableItemDto.getId());
        repairableItem.setCode(repairableItemDto.getCode());
        repairableItem.setLocation(repairableItemDto.getLocation());

        String s = repairableItemDto.getNotes();
        if (s != null && !s.isBlank()) {
            repairableItem.setNotes(s);
        } else {
            repairableItem.setNotes(null);
        }

        repairableItem.setQuantity(repairableItemDto.getQuantity());
        repairableItem.setCategoryType(repairableItemDto.getCategoryType());
        return repairableItem;
    }

    public static RepairableItemDto fromEntityToDto (RepairableItem repairableItem) {
        RepairableItemDto repairableItemDto = new RepairableItemDto();
        repairableItemDto.setId(repairableItem.getId());
        repairableItemDto.setCode(repairableItem.getCode());
        repairableItemDto.setLocation(repairableItem.getLocation());
        String s = repairableItem.getNotes();

        if (s != null && !s.isBlank()) {
            repairableItemDto.setNotes(s);
        } else {
            repairableItemDto.setNotes(null);
        }

        repairableItemDto.setQuantity(repairableItem.getQuantity());
        repairableItemDto.setCategoryType(repairableItem.getCategoryType());
        return repairableItemDto;
    }
}
