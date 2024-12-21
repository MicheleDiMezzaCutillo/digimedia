package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.RepairableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepairableItemRepository extends JpaRepository<RepairableItem,Long> {


    /**
     * Conta il numero totale di oggetti riparabili (es. TV, aspirapolvere).
     * @return il numero totale di oggetti.
     */
    @Query("SELECT COUNT(r) FROM RepairableItem r")
    int countItems();

    /**
     * Calcola la somma di tutte le quantità disponibili in magazzino.
     * @return la quantità totale in stock.
     */
    @Query("SELECT COALESCE(SUM(r.quantity), 0) FROM RepairableItem r")
    int sumQuantities();

    List<RepairableItem> findByCategoryTypeId(Long categoryTypeId);

    List<RepairableItem> findByCategoryType(CategoryType categoryType);

    @Query("SELECT r FROM RepairableItem r WHERE LOWER(r.code) LIKE LOWER(CONCAT('%', :query, '%')) AND r.categoryType.id = :categoryTypeId")
    List<RepairableItem> searchRepairableItems(@Param("query") String query, @Param("categoryTypeId") Long categoryTypeId);

    @Query("""
        SELECT r
        FROM RepairableItem r
        WHERE r.categoryType.id = :categoryTypeId
        AND r.id NOT IN (
            SELECT ric.repairableItem.id
            FROM RepairableItemComponent ric
            WHERE ric.component.id = :componentId
        )
    """)
    List<RepairableItem> findRepairableItemsNotLinkedToComponent(@Param("categoryTypeId") Long categoryTypeId,
                                                                 @Param("componentId") Long componentId);
}
