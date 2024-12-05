package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import it.mikedmc.digimedia.repository.ComponentRepository;
import it.mikedmc.digimedia.repository.RepairableItemRepository;
import it.mikedmc.digimedia.service.CategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private RepairableItemRepository repairableItemRepository;

    @Autowired
    private CategoryTypeService categoryTypeService;

    @GetMapping
    public String showDashboard(Model model) {
        List<CategoryType> categories = categoryTypeService.findAll();
        int totalItems = repairableItemRepository.countItems();
        int totalQuantity = repairableItemRepository.sumQuantities();

        model.addAttribute("categories", categories);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalQuantity", totalQuantity);

        return "dashboard";
    }

    @GetMapping("/{categoryTypeId}")
    public String showCategoryTypeDetails(
            @PathVariable Long categoryTypeId,
            Model model) {
        // Recupera la CategoryType selezionata
        CategoryType categoryType = categoryTypeService.findById(categoryTypeId);

        if (categoryType == null) {
            return "error";
        }

        // Recupera tutte le categorie associate
        List<Category> categories = categoryType.getCategories();

        // Organizza i dati per le sezioni
        Map<Category, List<RepairableItem>> repairableItemsByCategory = new HashMap<>();
        Map<Category, List<Component>> componentsByCategory = new HashMap<>();

        for (Category category : categories) {
            repairableItemsByCategory.put(category, category.getRepairableItems());
            componentsByCategory.put(category, category.getComponents());
        }

        // Aggiungi dati al modello
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("repairableItemsByCategory", repairableItemsByCategory);
        model.addAttribute("componentsByCategory", componentsByCategory);

        return "category-type-details";
    }

}
