package it.mikedmc.digimedia.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryType", cascade = CascadeType.ALL)
    private List<Category> categories;

    @Transient // Non viene salvato nel database, ma calcolato dinamicamente
    public List<RepairableItem> getRepairableItems() {
        List<RepairableItem> repairableItems = new ArrayList<>();
        if (categories != null) {
            for (Category category : categories) {
                repairableItems.addAll(category.getRepairableItems());
            }
        }
        return repairableItems;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
