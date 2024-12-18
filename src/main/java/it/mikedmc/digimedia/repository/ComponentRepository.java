package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component,Long> {
    @Query("SELECT c FROM Component c WHERE c.category.categoryType = :categoryType")
    List<Component> findByCategoryType(@Param("categoryType") CategoryType categoryType);

    @Query("SELECT c FROM Component c WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :query, '%')) AND c.categoryType.id = :categoryTypeId AND c.category.id = :categoryId")
    List<Component> searchComponents(@Param("query") String query, @Param("categoryTypeId") Long categoryTypeId, @Param("categoryId") Long categoryId);


}
