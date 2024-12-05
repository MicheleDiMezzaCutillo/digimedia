package it.mikedmc.digimedia.service;

import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryTypeService {

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    public void save(CategoryType categoryType) {
        categoryTypeRepository.save(categoryType);
    }

    public CategoryType findById(Long id) {
        return categoryTypeRepository.findById(id).orElse(new CategoryType());
    }

    public List<CategoryType> findAll() {
        return categoryTypeRepository.findAll();
    }

    public void deleteCategoryTypeById(Long id) {
        CategoryType categoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipologia non trovata"));

        // Verifica se ci sono categorie associate
        if (!categoryType.getCategories().isEmpty()) {
            throw new IllegalStateException("Impossibile eliminare: ci sono categorie associate a questa tipologia.");
        }

        categoryTypeRepository.delete(categoryType);
    }
}
