package de.aswggmbh.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;

@Entity
public class ShoppingListItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Bitte einen Namen eingeben.")
	@Size(max = 50, message = "Der Name darf höchstens 50 Zeichen enthalten.")
	private String name;

	@Min(value = 1, message = "Der Bedarf darf nicht kleiner als 1 sein.")
	@Max(value = 9999, message = "Soviel braucht doch kein Mensch!")
	private int demand = 1;

	private boolean completed = false;

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}
}
