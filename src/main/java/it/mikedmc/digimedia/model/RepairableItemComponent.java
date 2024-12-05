package it.mikedmc.digimedia.model;

import jakarta.persistence.*;

@Entity
public class RepairableItemComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repairable_item_id")
    private RepairableItem repairableItem;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RepairableItem getRepairableItem() {
        return repairableItem;
    }

    public void setRepairableItem(RepairableItem repairableItem) {
        this.repairableItem = repairableItem;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
