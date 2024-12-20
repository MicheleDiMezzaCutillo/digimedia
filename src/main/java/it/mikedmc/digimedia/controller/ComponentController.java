package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.dto.ComponentDto;
import it.mikedmc.digimedia.dto.RepairableItemDto;
import it.mikedmc.digimedia.dto.builder.ComponentDtoBuilder;
import it.mikedmc.digimedia.dto.builder.RepairableItemDtoBuilder;
import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.CategoryType;
import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.repository.CategoryRepository;
import it.mikedmc.digimedia.repository.CategoryTypeRepository;
import it.mikedmc.digimedia.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/findById/{repairableItemId}")
    public String showRepairableItemDetails(@PathVariable Long repairableItemId, Model model) {
        // Recupera il RepairableItem in base all'ID
        Component component = componentService.findById(repairableItemId);
        if (component == null) {
            return "error"; // Pagina di errore se l'oggetto non esiste
        }

        // Recupera il CategoryType associato al RepairableItem
        CategoryType categoryType = component.getCategory().getCategoryType();

        if (categoryType == null) {
            return "error"; // Pagina di errore se il CategoryType non esiste
        }
        List<RepairableItem> repairableItems = componentService.getRepairableItemsFromComponent(component);

        model.addAttribute("categoryType", categoryType);
        model.addAttribute("repairableItems",repairableItems);
        model.addAttribute("component", component);

        return "component/components-and-repairable-item";
    }

    @GetMapping("/searchRepairableItems")
    public String searchRepairableItems(@RequestParam("query") String query,
                                        @RequestParam("categoryTypeId") Long categoryTypeId,
                                        @RequestParam("categoryId") Long categoryId,
                                        Model model) {
        // Esegui la ricerca nel repository
        List<Component> searchResults = componentService.searchComponents(query, categoryTypeId, categoryId);

        if (searchResults.size() == 1) {
            // restituisco la pagina con i dettagli
            return "redirect:/component/findById/" + searchResults.get(0).getId();
        }
        // altrimenti mostro tutti i risultati

        // Aggiungi i risultati alla view
        model.addAttribute("components", searchResults);
        model.addAttribute("categoryType", categoryTypeRepository.findById(categoryTypeId).orElse(new CategoryType()));
        model.addAttribute("categoryId", categoryId);

        return "component/component-list";
    }


    @GetMapping("/edit/{componentId}")
    public String showEditForm(@PathVariable Long componentId, Model model) {
        ComponentDto dto = ComponentDtoBuilder.fromEntityToDto(componentService.findById(componentId));
        model.addAttribute("componentDto", dto);
        return "component/component-edit-or-create";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam("categoryTypeId") Long categoryTypeId, @RequestParam("categoryId") Long categoryId) {
        ComponentDto componentDto = new ComponentDto();
        componentDto.setCategoryType(categoryTypeRepository.findById(categoryTypeId).orElseThrow());
        componentDto.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        model.addAttribute("componentDto", componentDto);
        return "component/component-edit-or-create";
    }

    @PostMapping("/edit/{componentId}")
    public String updateComponent(@PathVariable Long componentId, @ModelAttribute ComponentDto componentDto) {
        componentService.save(ComponentDtoBuilder.fromDtoToEntity(componentDto));
        return "redirect:/component/findById/"+componentDto.getId();
    }

    @PostMapping("/create")
    public String createComponent(@ModelAttribute ComponentDto componentDto) {
        return "redirect:/component/findById/"+componentService.save(ComponentDtoBuilder.fromDtoToEntity(componentDto)).getId();
    }

    @PostMapping("/delete/{componentId}")
    public String deleteComponent(@PathVariable Long componentId, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        componentService.delete(componentId);
        redirectAttributes.addFlashAttribute("successMessage", "Componente eliminato con successo.");
        redirectAttributes.addFlashAttribute("errorMessage", "Componente eliminato ses.");

        return "redirect:" + (referer != null ? referer : "/default-page");
    }

    @PostMapping("/decrement-quantity/{componentId}")
    public String decrementComponent(@PathVariable Long componentId, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        switch (componentService.decrementQuantity(componentId)) {
            case 2:
                redirectAttributes.addFlashAttribute("errorMessage", "Scorte del componente terminate.");
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
