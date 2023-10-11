package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
/**
 * IngredientTypeRepository is used to provide CRUD operations for the Recipe model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
	
	/**
     * Finds a Ingredient Type object with the provided name. Spring will generate code
     * to make this happen.
     * 
     * @param name
     *            Name of the IngredientType
     * @return Found recipe, null if none.
     */
    IngredientType findByName ( String name );

}
