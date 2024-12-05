package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.dto.CategoryDto;
import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.repository.CategoryRepository;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("categoryTypes", categoryTypeRepository.findAll());
        return "category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("categoryDto") CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setCategoryType(categoryTypeRepository.findById(dto.getCategoryTypeId()).orElseThrow());
        categoryRepository.save(category);
        return "redirect:/category/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCategoryTypeId(category.getCategoryType().getId());
        model.addAttribute("categoryDto", dto);
        model.addAttribute("categoryTypes", categoryTypeRepository.findAll());
        return "category/category-edit";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute("categoryDto") CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId()).orElseThrow();
        category.setName(dto.getName());
        category.setCategoryType(categoryTypeRepository.findById(dto.getCategoryTypeId()).orElseThrow());
        categoryRepository.save(category);
        return "redirect:/category/list";
    }

    @GetMapping("/list")
    public String listCategories(Model model) {
        List<CategoryDto> dtos = categoryRepository.findAll().stream()
                .map(cat -> {
                    CategoryDto dto = new CategoryDto();
                    dto.setId(cat.getId());
                    dto.setName(cat.getName());
                    dto.setCategoryTypeId(cat.getCategoryType().getId());
                    return dto;
                })
                .toList();
        model.addAttribute("categories", dtos);
        return "category-list";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Prodotto eliminato con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del prodotto.");
        }
        return "redirect:/dashboard";
    }
}
