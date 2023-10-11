package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long  id;
    /** amount of coffee */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients =  new ArrayList<Ingredient>();

   
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

   
    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
    	for( Ingredient i : r.getIngredients()) {
    		 if (!useIngredient(i.getIngredientType(), i.getAmount())) {
    	            return false;
    	        }
    	}
    	return true;
    }
    /**
     * updates an ingredient based on its type
     * @param ingredientType to be updated
     * @param amount to update to
     * @return whether the update was successful
     */
    public boolean update( IngredientType ingredientType, Integer amount) {
    	for(Ingredient i : ingredients) {
    		if(i.getIngredientType().equals(ingredientType)){
    			i.add(amount);
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * updates the inventory based on another inventory
     * @param inventory to be modified to match
     * @return true if the ingredients to be added aren't in the type service 
     */
    public boolean updateIngredients ( final Inventory inventory) {
    	for( Ingredient i : inventory.getIngredients()) {
    		 if (!update(i.getIngredientType(), i.getAmount())) {
    			 System.out.println(i.getIngredientType().getName() + "= Failed");
    	         return false;
    	     }
    	}
    	return true;
    }
    
    private boolean useIngredient(IngredientType ingredientType, Integer quantity ) {
    	for(Ingredient i : ingredients) {
    		if(i.getIngredientType().equals(ingredientType) &&  i.use(quantity)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredient to be added
     * @return true if successful, false if not
     */

    public boolean add( Ingredient ingredient) {
    	if(ingredient.getAmount() <= 0) {
    		throw new IllegalArgumentException("Ingredient must have positive value");
    	}
    	ingredients.add(ingredient);
    	return true;
    }

    /**
     * deletes an ingredient with the same type from the inventory
     * @param ingredient to be deleted
     * @return true if ingredient was sucessfully deleted
     */
    public boolean deleteIngredient(Ingredient ingredient) {
        	for(Ingredient i : ingredients) {
        		if (i.getIngredientType().equals(ingredient.getIngredientType())){
        			ingredients.remove(i);
        			return true;
        		}
        	}
        	return false;
    }
    
    /**
     * clears the ingredient list
     */
    public void deleteAll() {
    	ingredients = new ArrayList();
    }
    
    /**
     * Gets the ingredient of the specified type
     * @param ingredientType of the ingredient we want to get
     * @return the ingredient with the tye
     */
    public Ingredient getIngredient(IngredientType ingredientType) {
    	for(Ingredient i : ingredients) {
    		if (i.getIngredientType().equals(ingredientType)){
    			return i;
    		}
    	}
    	return null;
    }
    

    /**
     * getter for Ingredients ArrayLis for Invetory API Controller
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients() {
		return ingredients;
	}

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for(Ingredient i : ingredients) {
        	buf.append( i.getIngredientType().getName() + ": ");
        	buf.append( i.getAmount() );
        	buf.append( "\n" );
    		}
        return buf.toString();
    }

    /**
     * counts the number of ingredient in the inventory
     * @return the size of te inventory
     */
	public Integer count() {
		return ingredients.size();
	}
}
