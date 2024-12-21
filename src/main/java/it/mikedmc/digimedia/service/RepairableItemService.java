package it.mikedmc.digimedia.service;

import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.repository.RepairableItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairableItemService {

    @Autowired
    private RepairableItemRepository repairableItemRepository;

    public RepairableItem findById(long id) {
        return repairableItemRepository.findById(id).orElse(new RepairableItem());
    }

    public List<RepairableItem> findByCategoryTypeId(Long categoryTypeId) {
        return repairableItemRepository.findByCategoryTypeId(categoryTypeId);
    }

    public List<RepairableItem> findByCategoryType(CategoryType categoryType) {
        return repairableItemRepository.findByCategoryType(categoryType);
    }

    public List<RepairableItem> searchRepairableItems(String query, Long categoryTypeId) {
        return repairableItemRepository.searchRepairableItems(query, categoryTypeId);
    }

    public RepairableItem save(RepairableItem repairableItem) {
        return repairableItemRepository.save(repairableItem);
    }

    public void delete(Long repairableItemId) {
        repairableItemRepository.deleteById(repairableItemId);
    }


    /***
     * decrementa la quantità di un repairableItem,
     * se il repairableItem ha 0 in quantità, ritorna 0;
     * se il repairableItem viene decrementato ritorna 1;
     * se il repairableItem viene decrementato e rimane 0, ritorna 2;
     * @param repairableItemId
     * @return code
     */
    @Transactional
    public int decrementQuantity(Long repairableItemId) {
        RepairableItem repairableItem = repairableItemRepository.findById(repairableItemId)
                .orElseThrow(() -> new IllegalArgumentException("Oggetto con ID " + repairableItemId + " non trovato."));

        if (repairableItem.getQuantity() > 0) {
            int update = repairableItem.getQuantity() - 1;

            repairableItem.setQuantity(update);
            repairableItemRepository.save(repairableItem);
            if (update == 0) {
                return 2;
            }
            return 1;
        } else {
            return 0;
        }
    }

    public List<RepairableItem> findRepairableItemsNotLinkedToComponent(Long categoryTypeId, Long componentId) {
        return repairableItemRepository.findRepairableItemsNotLinkedToComponent(categoryTypeId, componentId);
    }
}
