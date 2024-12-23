package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.dto.CategoryDto;
import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
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
    public String createForm(Model model, @RequestParam("categoryTypeId") Long categoryTypeId) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryType(categoryTypeRepository.findById(categoryTypeId).orElseThrow());
        model.addAttribute("categoryDto", categoryDto);
        return "category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("categoryDto") CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setCategoryType(dto.getCategoryType());
        categoryRepository.save(category);
        return "redirect:/category/list?categoryTypeId=" + dto.getCategoryType().getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCategoryType(category.getCategoryType());
        model.addAttribute("categoryDto", dto);
        return "category/category-edit";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute("categoryDto") CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId()).orElseThrow();
        category.setName(dto.getName());
        categoryRepository.save(category);
        return "redirect:/category/list?categoryTypeId=" + dto.getCategoryType().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow();
            categoryRepository.delete(category);
            redirectAttributes.addFlashAttribute("successMessage", "Prodotto eliminato con successo!");
            return "redirect:/category/list?categoryTypeId=" + category.getCategoryType().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del prodotto.");
        }
        return "redirect:/";
    }

    @GetMapping("/list")
    public String listCategory (Model model, @RequestParam("categoryTypeId") Long categoryTypeId) {
        CategoryType categoryType = categoryTypeRepository.findById(categoryTypeId).orElseThrow();
        model.addAttribute("categories",categoryRepository.findByCategoryType(categoryType));
        model.addAttribute("categoryType", categoryType);
        return "category/category-list";
    }
}
