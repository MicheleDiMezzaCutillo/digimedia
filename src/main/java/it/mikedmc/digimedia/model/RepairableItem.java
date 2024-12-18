package it.mikedmc.digimedia.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RepairableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryType categoryType;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private int quantity;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "repairableItem", cascade = CascadeType.ALL)
    private List<RepairableItemComponent> components;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<RepairableItemComponent> getComponents() {
        return components;
    }

    public void setComponents(List<RepairableItemComponent> components) {
        this.components = components;
    }

}
