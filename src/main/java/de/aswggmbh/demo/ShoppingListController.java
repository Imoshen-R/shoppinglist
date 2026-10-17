package de.aswggmbh.demo;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ShoppingListController {
    private final ShoppingListItemRepository repository;

    public ShoppingListController(ShoppingListItemRepository repository) {
        this.repository = repository;
    }

    @InitBinder("shoppingListItem")
    public void allowedFields(WebDataBinder binder) {
        binder.setAllowedFields("name", "demand");
    }

    @GetMapping
    public String shoppingList(Model model) {
        model.addAttribute("shoppingList", repository.findAll());
        model.addAttribute("shoppingListItem", new ShoppingListItem());
        return "shoppinglist";
    }

    @PostMapping
    public String append(
            @Valid @ModelAttribute("shoppingListItem") ShoppingListItem item,
            BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("shoppingList", repository.findAll());
            return "shoppinglist";
        }
        repository.save(item);
        return "redirect:/";
    }
}
