package it.mikedmc.digimedia.service;

import it.mikedmc.digimedia.model.*;
import it.mikedmc.digimedia.repository.CategoryRepository;
import it.mikedmc.digimedia.repository.ComponentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Map<Category, List<Component>> getComponentsGroupedByCategory(CategoryType categoryType) {
        // Recupera tutte le categorie associate al CategoryType
        List<Category> categories = categoryRepository.findByCategoryType(categoryType);

        // Recupera tutti i componenti filtrati per CategoryType
        List<Component> components = componentRepository.findByCategoryType(categoryType);

        // Creo una TreeMap per avere i vari componenti ordinati, e non ottenere un ordine casuale ogni volta
        Map<Category, List<Component>> groupedComponents = new TreeMap<>(Comparator.comparing(Category::getName));
        groupedComponents.putAll(
                components.stream().collect(Collectors.groupingBy(Component::getCategory))
        );

        // Aggiungo le categorie vuote
        for (Category category : categories) {
            groupedComponents.putIfAbsent(category, new ArrayList<>());
        }

        return groupedComponents;
    }

    public Component findById(long id) {
        return componentRepository.findById(id).orElse(new Component());
    }

    public List<Component> searchComponents(String query, Long categoryTypeId, Long categoryId) {
        return componentRepository.searchComponents(query, categoryTypeId, categoryId);
    }

    // Nuovo metodo per ottenere una lista di RepairableItem
    public List<RepairableItem> getRepairableItemsFromComponent(Component component) {
        if (component == null || component.getRepairableItemComponents() == null) {
            return List.of(); // Ritorna una lista vuota se il componente è nullo o non ha associazioni
        }

        // Estrai i RepairableItem dalla lista di RepairableItemComponent
        return component.getRepairableItemComponents().stream()
                .map(RepairableItemComponent::getRepairableItem) // Mappa ogni elemento al RepairableItem
                .collect(Collectors.toList());
    }

    public Component save(Component component) {
        return componentRepository.save(component);
    }

    public void delete(Long componentId) {
        componentRepository.deleteById(componentId);
    }

    /***
     * decrementa la quantità di un componente,
     * se il componente ha 0 in quantità, ritorna 0;
     * se il comonente viene decrementato ritorna 1;
     * se il componente viene decrementato e rimane 0, ritorna 2;
     * @param componentId
     * @return code
     */
    @Transactional
    public int decrementQuantity(Long componentId) {
        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Componente con ID " + componentId + " non trovato."));

        if (component.getQuantity() > 0) {
            int update = component.getQuantity() - 1;
            component.setQuantity(update);
            componentRepository.save(component);
            if (update == 0) {
                return 2;
            }
            return 1;
        } else {
            return 0;
        }
    }

    public List<Component> findComponentsNotLinkedToRepairableItem (Long categoryId, Long repairableItemId) {
        return componentRepository.findComponentsNotLinkedToRepairableItem(categoryId, repairableItemId);
    }
}

