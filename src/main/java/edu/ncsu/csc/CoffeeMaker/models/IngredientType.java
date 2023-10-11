package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** Ingredient Type Class */
@Entity
public class IngredientType extends DomainObject {
	
	
	/** id for ingredient entry */
	@Id
	@GeneratedValue
	private Long id;
    
    /** name for the ingredient type */
	private String name; 
	
	/** Constructor for Ingredient type */
	public IngredientType() {	
	}

	/** 
	 * Constructor for Ingredient type that takes in a name
	 * 
	 * @param name name of ingredient
	*/
	public IngredientType(String name) {
		super();
		this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
	
	/**
	 * Gets the id of ingredient type
	 * 
	 * @return none
	 */
	@Override
	public Serializable getId() {
		return id;
	}

    /**
     * Set the ID of the IngredientType (Used by Hibernate)
     *
     * @param id the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * Get name of ingredients
     * 
     * @return String
     */
	public String getName() {
		return name;
	}
	
	/** 
	 * Hashcode Method
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	/**
	 * Method to check equivalence
	 * 
	 * 
	 * @param obj ingredient object
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		return name.equals(((IngredientType) obj).getName());
	}
	
}
