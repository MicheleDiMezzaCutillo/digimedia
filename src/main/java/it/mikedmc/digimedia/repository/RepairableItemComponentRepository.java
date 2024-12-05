package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.RepairableItemComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairableItemComponentRepository extends JpaRepository<RepairableItemComponent,Long> {
}
