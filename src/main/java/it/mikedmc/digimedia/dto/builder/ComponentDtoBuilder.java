package it.mikedmc.digimedia.dto.builder;

import it.mikedmc.digimedia.dto.ComponentDto;
import it.mikedmc.digimedia.dto.RepairableItemDto;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;

public class ComponentDtoBuilder {

    public static Component fromDtoToEntity(ComponentDto componentDto) {
        Component component = new Component();
        component.setId(componentDto.getId());
        component.setCode(componentDto.getCode());
        component.setLocation(componentDto.getLocation());

        String s = componentDto.getNotes();
        if (s != null && !s.isBlank()) {
            component.setNotes(s);
        } else {
            component.setNotes(null);
        }

        component.setQuantity(componentDto.getQuantity());
        component.setCategoryType(componentDto.getCategoryType());
        component.setCategory(componentDto.getCategory());
        return component;
    }

    public static ComponentDto fromEntityToDto (Component component) {
        ComponentDto componentDto = new ComponentDto();
        componentDto.setId(component.getId());
        componentDto.setCode(component.getCode());
        componentDto.setLocation(component.getLocation());
        String s = component.getNotes();

        if (s != null && !s.isBlank()) {
            componentDto.setNotes(s);
        } else {
            componentDto.setNotes(null);
        }

        componentDto.setQuantity(component.getQuantity());
        componentDto.setCategoryType(component.getCategoryType());
        componentDto.setCategory(component.getCategory());
        return componentDto;
    }

}
