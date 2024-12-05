package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.dto.CategoryTypeDto;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import it.mikedmc.digimedia.service.CategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category-type")
public class CategoryTypeController {

    @Autowired
    private CategoryTypeService categoryTypeService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categoryTypeDto", new CategoryTypeDto());
        return "category-type/category-type-create";
    }

    @PostMapping("/create")
    public String createCategoryType(@ModelAttribute("categoryTypeDto") CategoryTypeDto dto) {
        CategoryType categoryType = new CategoryType();
        categoryType.setName(dto.getName());
        categoryTypeService.save(categoryType);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        CategoryType categoryType = categoryTypeService.findById(id);
        CategoryTypeDto dto = new CategoryTypeDto();
        dto.setId(categoryType.getId());
        dto.setName(categoryType.getName());
        model.addAttribute("categoryTypeDto", dto);
        return "category-type/category-type-edit";
    }

    @PostMapping("/edit")
    public String editCategoryType(@ModelAttribute("categoryTypeDto") CategoryTypeDto dto) {
        CategoryType categoryType = categoryTypeService.findById(dto.getId());
        categoryType.setName(dto.getName());
        categoryTypeService.save(categoryType);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String listCategoryTypes(Model model) {
        List<CategoryTypeDto> dtos = categoryTypeService.findAll().stream()
                .map(ct -> {
                    CategoryTypeDto dto = new CategoryTypeDto();
                    dto.setId(ct.getId());
                    dto.setName(ct.getName());
                    return dto;
                })
                .toList();
        model.addAttribute("categoryTypes", dtos);
        return "category-type/category-type-list";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategoryType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryTypeService.deleteCategoryTypeById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Tipologia eliminata con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione della tipologia.");
        }
        return "redirect:/";
    }
}
