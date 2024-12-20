package it.mikedmc.digimedia.service;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.model.RepairableItemComponent;
import it.mikedmc.digimedia.repository.CategoryRepository;
import it.mikedmc.digimedia.repository.RepairableItemComponentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RepairableItemComponentService {

    @Autowired
    private RepairableItemComponentRepository repairableItemComponentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Map<Category, List<Component>> getComponentsByCategory(RepairableItem repairableItem) {
        // Recupera tutte le categorie per il tipo di categoria
        List<Category> categories = categoryRepository.findByCategoryType(repairableItem.getCategoryType());

        // Recupera i componenti associati
        List<RepairableItemComponent> components = repairableItemComponentRepository.findByRepairableItem(repairableItem);

        // Usa una TreeMap per mantenere le categorie ordinate
        Map<Category, List<Component>> componentsByCategory = new TreeMap<>(Comparator.comparing(Category::getName));

        // Aggiungi tutte le categorie alla TreeMap con liste vuote
        for (Category category : categories) {
            componentsByCategory.put(category, new ArrayList<>());
        }

        // Assegna i componenti alle loro categorie
        for (RepairableItemComponent component : components) {
            Category category = component.getComponent().getCategory();
            componentsByCategory.get(category).add(component.getComponent());
        }

        return componentsByCategory;
    }


    public void save(RepairableItemComponent repairableItemComponent) {
        repairableItemComponentRepository.save(repairableItemComponent);
    }

    @Transactional
    public void deleteRepairableItemComponent(RepairableItem repairableItem, Component component) {
        repairableItemComponentRepository.deleteByRepairableItemAndComponent(repairableItem, component);
    }
}
