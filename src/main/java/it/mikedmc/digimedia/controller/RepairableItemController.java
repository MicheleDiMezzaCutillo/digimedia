package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.dto.RepairableItemDto;
import it.mikedmc.digimedia.dto.builder.RepairableItemDtoBuilder;
import it.mikedmc.digimedia.model.*;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import it.mikedmc.digimedia.repository.RepairableItemRepository;
import it.mikedmc.digimedia.service.CategoryTypeService;
import it.mikedmc.digimedia.service.RepairableItemComponentService;
import it.mikedmc.digimedia.service.RepairableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/repairable-item")
public class RepairableItemController {

    @Autowired
    private RepairableItemService repairableItemService;

    @Autowired
    private RepairableItemComponentService repairableItemComponentService;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @GetMapping("/findById/{repairableItemId}")
    public String showRepairableItemDetails(@PathVariable Long repairableItemId, Model model) {
        // Recupera il RepairableItem in base all'ID
        RepairableItem repairableItem = repairableItemService.findById(repairableItemId);
        if (repairableItem == null) {
            model.addAttribute("errorMessage", "L'oggetto che stai cercando non esiste.");
            return "error";
        }

        // Recupera il CategoryType associato al RepairableItem
        CategoryType categoryType = repairableItem.getCategoryType();

        if (categoryType == null) {
            model.addAttribute("errorMessage", "La categoria a cui stai provando ad accedere non esiste.");
            return "error"; // Pagina di errore se il CategoryType non esiste
        }

        Map<Category, List<Component>> componentsGroupedByCategory = repairableItemComponentService.getComponentsByCategory(repairableItem);

        model.addAttribute("categoryType", categoryType);
        model.addAttribute("repairableItem", repairableItem);
        model.addAttribute("componentsGroupedByCategory", componentsGroupedByCategory);

        return "repairable-item/repairable-item-detail";
    }

    @GetMapping("/searchRepairableItems")
    public String searchRepairableItems(@RequestParam("query") String query,
                                        @RequestParam("categoryTypeId") Long categoryTypeId,
                                        Model model) {
        // Esegui la ricerca nel repository
        List<RepairableItem> searchResults = repairableItemService.searchRepairableItems(query, categoryTypeId);

        if (searchResults.size() == 1) {
            // restituisco la pagina con i dettagli
            return "redirect:/repairable-item/findById/" + searchResults.get(0).getId();
        }
        // altrimenti mostro tutti i risultati

        // Aggiungi i risultati alla view
        model.addAttribute("repairableItems", searchResults);
        model.addAttribute("categoryType", categoryTypeRepository.findById(categoryTypeId).orElse(new CategoryType()));

        return "repairable-item/repairable-item-list";
    }

    @GetMapping("/edit/{repairableItemId}")
    public String showEditForm(@PathVariable Long repairableItemId, Model model) {
        RepairableItemDto dto = RepairableItemDtoBuilder.fromEntityToDto(repairableItemService.findById(repairableItemId));
        model.addAttribute("repairableItemDto", dto);
        return "repairable-item/repairable-item-edit-or-create";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam("categoryTypeId") Long categoryTypeId) {
        RepairableItemDto repairableItemDto = new RepairableItemDto();
        repairableItemDto.setCategoryType(categoryTypeRepository.findById(categoryTypeId).orElseThrow());
        model.addAttribute("repairableItemDto", repairableItemDto);
        return "repairable-item/repairable-item-edit-or-create";
    }

    @PostMapping("/edit/{repairableItemId}")
    public String updateRepairableItem(@PathVariable Long repairableItemId, @ModelAttribute RepairableItemDto repairableItemDto) {
        repairableItemService.save(RepairableItemDtoBuilder.fromDtoToEntity(repairableItemDto));
        return "redirect:/repairable-item/findById/"+repairableItemDto.getId();
    }

    @PostMapping("/create")
    public String createRepairableItem(@ModelAttribute RepairableItemDto repairableItemDto) {
        return "redirect:/repairable-item/findById/"+repairableItemService.save(RepairableItemDtoBuilder.fromDtoToEntity(repairableItemDto)).getId();
    }

    @PostMapping("/delete/{repairableItemId}")
    public String deleteRepairableItem(@PathVariable Long repairableItemId, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        repairableItemService.delete(repairableItemId);
        redirectAttributes.addFlashAttribute("successMessage", "Oggetto eliminato con successo.");
        return "redirect:" + (referer != null ? referer : "/default-page");
    }

    @PostMapping("/decrement-quantity/{repairableItemId}")
    public String decrementQuantityRepairableItem(@PathVariable Long repairableItemId, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        switch (repairableItemService.decrementQuantity(repairableItemId)) {
            case 2:
                redirectAttributes.addFlashAttribute("errorMessage", "Scorte dell'oggetto terminate.");
            case 1:
                redirectAttributes.addFlashAttribute("successMessage", "Quantità aggiornata con successo.");
                break;
            default:
                redirectAttributes.addFlashAttribute("errorMessage", "Quantità non aggiornata, errore.");
                break;
        }
        return "redirect:" + (referer != null ? referer : "/default-page");
    }

}
