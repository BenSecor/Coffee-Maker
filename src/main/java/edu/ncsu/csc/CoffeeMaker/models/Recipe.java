package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;
    
    /**
     *  List of ingredients in recipe 
     */
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new ArrayList<Ingredient>();
    }
    
    /**
     * Creates a recipe constructor with various arguments
     * 
     * @param name name of recipe
     * @param price price of ingredient
     * @param ingredient list of ingredients in recipe
     */
    public Recipe (String name, Integer price, List<Ingredient> ingredient) {
        setName(name);
        setPrice(price);
        this.ingredients = ingredient;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }
    
    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Updates the fields to be equal to the passed Recipe
     *
     * @param r
     *            with updated fields
     */
    public void updateRecipe ( final Recipe r ) {
        setPrice( r.getPrice() );
        ingredients.clear();
        ingredients.addAll(r.getIngredients());
    }
    
    /**
     * Add ingredients to recipe
     * 
     * @param ingredient ingredient
     */
    public void addIngredient(Ingredient ingredient) {
    	ingredients.add(ingredient);
    }
    
    /**
     * Get a list of ingredients in this recipe
     * 
     * @return List
     */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
    
    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
    	String allIngredients = "The ingredients in the recipe are: ";
    	for (Ingredient i : ingredients) {
    		allIngredients += i.toString() + ", ";
    		
    	}
        return allIngredients;
    }
    
    /**
     * Get hashCode of this object
     * 
     * @return int
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }




}
