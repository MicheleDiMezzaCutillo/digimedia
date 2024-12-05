package it.mikedmc.digimedia.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private int quantity;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    private List<RepairableItemComponent> repairableItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<RepairableItemComponent> getRepairableItems() {
        return repairableItems;
    }

    public void setRepairableItems(List<RepairableItemComponent> repairableItems) {
        this.repairableItems = repairableItems;
    }
}