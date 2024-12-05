package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.RepairableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
