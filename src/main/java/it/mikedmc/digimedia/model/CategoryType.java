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


    @OneToMany(mappedBy = "categoryType", cascade = CascadeType.ALL)
    private List<RepairableItem> repairableItems;

   @OneToMany(mappedBy = "categoryType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Component> components;

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

    public List<RepairableItem> getRepairableItems() {
        return repairableItems;
    }

    public void setRepairableItems(List<RepairableItem> repairableItems) {
        this.repairableItems = repairableItems;
    }


    // Getter e Setter
    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
