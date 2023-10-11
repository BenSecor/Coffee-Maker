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
public class GenerateIngredientTypes {


    @Autowired
    private RecipeService service;
    
    @Autowired
    private IngredientTypeService typeService;
    
    @Autowired
    private InventoryService iService;
    
    @Autowired 
    private IngredientService ingredientService;


    @Test
    @Transactional
    public void testCreateIngredients() {
    	
    	iService.deleteAll();
        service.deleteAll();
        ingredientService.deleteAll();
    	typeService.deleteAll();
    	
        Assertions.assertEquals(0,  typeService.count());
        
        IngredientType coffee = new IngredientType("Coffee");
        IngredientType milk = new IngredientType("Milk");
        
        typeService.save( coffee );
        typeService.save( milk );
        
        Assertions.assertEquals( 2, typeService.count() );

    }
}