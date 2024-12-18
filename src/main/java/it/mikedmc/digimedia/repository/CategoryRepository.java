package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    @Query("SELECT c FROM Category c WHERE c.categoryType = :categoryType")
    List<Category> findByCategoryType(@Param("categoryType") CategoryType categoryType);
}
