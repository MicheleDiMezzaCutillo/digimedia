package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.model.RepairableItemComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairableItemComponentRepository extends JpaRepository<RepairableItemComponent,Long> {
    List<RepairableItemComponent> findByRepairableItem(RepairableItem repairableItem);
    void deleteByRepairableItemAndComponent(RepairableItem repairableItem, Component component);
}
