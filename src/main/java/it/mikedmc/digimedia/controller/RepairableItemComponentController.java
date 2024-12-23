package it.mikedmc.digimedia.controller;

import it.mikedmc.digimedia.model.Component;
import it.mikedmc.digimedia.model.RepairableItem;
import it.mikedmc.digimedia.model.RepairableItemComponent;
import it.mikedmc.digimedia.service.ComponentService;
import it.mikedmc.digimedia.service.RepairableItemComponentService;
import it.mikedmc.digimedia.service.RepairableItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/repairable-item-component")
public class RepairableItemComponentController {

    @Autowired
    private ComponentService componentService;
    @Autowired
    private RepairableItemService repairableItemService;
    @Autowired
    private RepairableItemComponentService repairableItemComponentService;

    @GetMapping("/link")
    public String linkRepairableItemComponent (HttpSession session, RedirectAttributes redirectAttributes, @RequestParam("componentId") Long componentId, @RequestParam("repairableItemId") Long repairableItemId) {
        RepairableItemComponent repairableItemComponent = new RepairableItemComponent();
        repairableItemComponent.setComponent(componentService.findById(componentId));
        repairableItemComponent.setRepairableItem(repairableItemService.findById(repairableItemId));
        repairableItemComponentService.save(repairableItemComponent);

        redirectAttributes.addFlashAttribute("successMessage","Collegamento fatto con successo.");
        String returnTo = (String) session.getAttribute("returnTo");
        return "redirect:" + (returnTo != null ? returnTo : "/");
    }

    @PostMapping("/unlink")
    public String unlinkRepairableItemComponent (@RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes, @RequestParam("componentId") Long componentId, @RequestParam("repairableItemId") Long repairableItemId) {
        RepairableItem repairableItem = repairableItemService.findById(repairableItemId);
        Component component = componentService.findById(componentId);
        repairableItemComponentService.deleteRepairableItemComponent(repairableItem, component);

        redirectAttributes.addFlashAttribute("successMessage","Scollegato con successo!");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/from-component")
    public String linkRepairableItemComponentFromComponent (Model model, @RequestHeader(value = "Referer", required = false) String referer, HttpSession session, @RequestParam("categoryId") Long categoryId, @RequestParam("repairableItemId") Long repairableItemId, @RequestParam("categoryTypeId") Long categoryTypeId) {
        session.setAttribute("returnTo", referer);
        model.addAttribute("categoryTypeId",categoryTypeId);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("repairableItemId",repairableItemId);
        model.addAttribute("components", componentService.findComponentsNotLinkedToRepairableItem(categoryId, repairableItemId));
        return "repairable-item-component/select-component-for-repairable-item";
    }

    @GetMapping("/from-repairable-item")
    public String linkRepairableItemComponentFromRepairableItem (Model model, @RequestHeader(value = "Referer", required = false) String referer, HttpSession session, @RequestParam("categoryTypeId") Long categoryTypeId, @RequestParam("componentId") Long componentId) {
        session.setAttribute("returnTo", referer);
        model.addAttribute("componentId",componentId);
        model.addAttribute("categoryTypeId",categoryTypeId);
        model.addAttribute("repairableItems", repairableItemService.findRepairableItemsNotLinkedToComponent(categoryTypeId, componentId));
        return "repairable-item-component/select-repairable-item-for-component";
    }

}
