package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import it.mikedmc.digimedia.repository.ComponentRepository;
import it.mikedmc.digimedia.repository.RepairableItemRepository;
import it.mikedmc.digimedia.service.CategoryTypeService;
import it.mikedmc.digimedia.service.ComponentService;
import it.mikedmc.digimedia.service.RepairableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private RepairableItemRepository repairableItemRepository;

    @Autowired
    private CategoryTypeService categoryTypeService;

    @Autowired
    private RepairableItemService repairableItemService;

    @Autowired
    private ComponentService componentService;

    @GetMapping("/")
    public String showDashboard(Model model) {

        List<CategoryType> categories = categoryTypeService.findAll();
        model.addAttribute("categories", categories);

        return "dashboard";
    }

    @GetMapping("/repairable-item/{categoryTypeId}")
    public String showCategoryTypeDetails(@PathVariable Long categoryTypeId, Model model) {
        CategoryType categoryType = categoryTypeService.findById(categoryTypeId);

        if (categoryType == null) {
            model.addAttribute("errorMessage", "La categoria a cui stai provando ad accedere non esiste.");
            return "error";
        }

        // Map di Category -> Component
        Map<Category, List<Component>> componentsGroupedByCategory = componentService.getComponentsGroupedByCategory(categoryType);

        // Aggiunta al modello
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("repairableItems", categoryType.getRepairableItems());
        model.addAttribute("componentsGroupedByCategory", componentsGroupedByCategory);

        return "repairable-item-and-comonents";
    }
    
}
