package de.aswggmbh.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

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
	public String append(@Valid @ModelAttribute("shoppingListItem") ShoppingListItem item, BindingResult errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("shoppingList", repository.findAll());
			return "shoppinglist";
		}
		repository.save(item);
		return "redirect:/?focus#name";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		repository.deleteById(id);
		return "redirect:/";
	}

	@PostMapping("/check/{id}")
	public String check(@PathVariable Long id) {
		repository.findById(id).ifPresent(item -> {
			item.setCompleted(true);
			repository.save(item);
		});
		return "redirect:/#item-" + id;
	}

	@PostMapping("/reset/{id}")
	public String reset(@PathVariable Long id) {
		repository.findById(id).ifPresent(item -> {
			item.setCompleted(false);
			repository.save(item);
		});
		return "redirect:/#item-" + id;
	}

	@PostMapping("/item/{id}/increase")
	public String inc(@PathVariable Long id) {
		repository.findById(id).ifPresent(item -> {
			item.setDemand(item.getDemand() + 1);
			repository.save(item);
		});
		return "redirect:/#item-" + id;
	}

	@PostMapping("/item/{id}/decrease")
	public String dec(@PathVariable Long id) {
		repository.findById(id).ifPresent(item -> {
			if (item.getDemand() == 1) {
				item.setCompleted(true);
			} else {
				item.setDemand(item.getDemand() - 1);
			}
			repository.save(item);
		});
		return "redirect:/#item-" + id;
	}
}
