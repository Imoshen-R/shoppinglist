package de.aswggmbh.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Bitte einen Namen eingeben.")
    @Size(max = 100, message = "Der Name darf höchstens 100 Zeichen enthalten.")
    private String name;

    @Min(value = 0, message = "Der Bedarf darf nicht negativ sein.")
    private int demand;

    @Min(value = 0, message = "Der Vorrat darf nicht negativ sein.")
    private int supply;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDemand() { return demand; }
    public void setDemand(int demand) { this.demand = demand; }
    public int getSupply() { return supply; }
    public void setSupply(int supply) { this.supply = supply; }
}
