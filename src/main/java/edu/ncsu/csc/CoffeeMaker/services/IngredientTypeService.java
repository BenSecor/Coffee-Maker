package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientTypeRepository;
/**
 * The IngredientTypeService is used to handle CRUD operations on the Ingredient 
 * type model. 
 * @author Ben Secor
 *
 */
@Component
@Transactional
public class IngredientTypeService extends Service<IngredientType, Long> {
	/**
	 * IngredientType repository instance
	 */
	@Autowired
	private IngredientTypeRepository ingredientTypeRepository;
	
	@Override
	protected JpaRepository<IngredientType, Long> getRepository() {
		
		return ingredientTypeRepository;
	}
	
	/**
     * Find a IngredientType with the provided name
     * 
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public IngredientType findByName ( final String name ) {
        return ingredientTypeRepository.findByName( name );
    }


}
