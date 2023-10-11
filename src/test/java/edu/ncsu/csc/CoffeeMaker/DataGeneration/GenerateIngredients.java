package edu.ncsu.csc.CoffeeMaker.DataGeneration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.IngredientTypeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;


@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateIngredients {

    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private IngredientTypeService typeService;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private InventoryService inventoryService;

    @Test
    @Transactional
    public void testCreateIngredients() {
    	inventoryService.deleteAll();
    	recipeService.deleteAll();
        ingredientService.deleteAll();
        typeService.deleteAll();
        
        IngredientType coffee = new IngredientType("Coffee");
        IngredientType milk = new IngredientType("Milk");
       
        typeService.save( coffee );

        typeService.save( milk );

        
        final Ingredient i1 = new Ingredient( coffee, 5 );

        ingredientService.save( i1 );

        final Ingredient i2 = new Ingredient( milk, 3 );

        ingredientService.save( i2 );
        
        Assertions.assertEquals( 2, ingredientService.count() );

    }
}