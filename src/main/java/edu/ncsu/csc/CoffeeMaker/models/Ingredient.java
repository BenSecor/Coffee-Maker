package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

/** Ingredient class */
@SuppressWarnings("unused")
@Entity
public class Ingredient extends DomainObject {
	
	/** id for ingredient entry */
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
    private Long id;
   
	/** Ingredient Type */
	@OneToOne 
    private IngredientType ingredientType;
    
    /** Amount of ingredient **/
    @Min(0)
    private Integer amount;
    
    /** 
     * Empty Constructor for Ingredient 
     * 
     * */
    public Ingredient() {
    	
    }
    
    /** 
     * Ingredient Constructor that takes in a type and the amount
     * 
     * @param type ingredient type
     * @param amount amount of ingredient 
     */
	public Ingredient(IngredientType type, int amount){
		super();
		setIngredientType(type);
 		setAmount(amount);
		
	}
	
	/**
	 * Get the amount of ingredient
	 * 
	 * @return Integer
	 */
	public Integer getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount for ingredient
	 * 
	 * @param amount amount of ingredient
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	/**
	 * Checks if we have enough stock and if so, subtract from quantity
	 * 
	 * @param quantity amount we are using
	 * @return boolean
	 */
	public boolean use(Integer quantity) {
		if(amount >= quantity) {
			amount -= quantity;
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Sets the ingredient type
	 * 
	 * @param type ingredient type
	 */
	public void setIngredientType(IngredientType type) {
		if(type == null) {
			throw new IllegalArgumentException("INGREDIENTS MUST HAVE A TYPE");
		} else {
			this.ingredientType = type;
		}
	}
	
	/**
	 * Gets the ingredient type
	 * 
	 * @return IngredientType
	 */
	public IngredientType getIngredientType() {
		return this.ingredientType;
	}
	
	/**
     * Set the ID of the Ingredient (Used by Hibernate)
     *
     * @param id id of ingredient
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * To String method
     * 
     * @return String
     */
	@Override
	public String toString() {
		return "Ingredients [id=" + id + ", type=" + ingredientType.getName() + ", amount=" + amount + "]";
	}
	
	/**
	 * Get the id of this ingredient
	 * 
	 * @return Serializable
	 */
	@Override
	public Serializable getId() {
		return id;
	}
	
	/**
	 * Add amount to existing amount
	 * 
	 * @param amount amount to add
	 */
	public void add(Integer amount) {
		this.amount += amount;
	}
}