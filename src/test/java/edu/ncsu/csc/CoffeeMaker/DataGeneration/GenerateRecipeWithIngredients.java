package edu.ncsu.csc.CoffeeMaker.DataGeneration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.IngredientTypeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {
    
	@Autowired
    private RecipeService service;
    
    @Autowired
    private IngredientTypeService typeService;
    
    @Autowired
    private InventoryService iService;
    
    @Autowired 
    private IngredientService ingredientService;

    
    @BeforeEach
    public void setup () {
	    iService.deleteAll();
	    service.deleteAll();
	    ingredientService.deleteAll();
		typeService.deleteAll();
		
		
        IngredientType coffee = new IngredientType("Coffee");
        typeService.save(coffee);
        IngredientType sugar = new IngredientType("Sugar");
        typeService.save(sugar);
        IngredientType milk = new IngredientType("Milk");
        typeService.save(milk);
        IngredientType chocolate = new IngredientType("Chocolate");
        typeService.save(chocolate);
        IngredientType pumpkinSpice = new IngredientType("Pumpkin_Spice");
        typeService.save(pumpkinSpice);
    }
    
    
    @Test
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient(new Ingredient (typeService.findByName("Coffee"), 10));
        r1.addIngredient(new Ingredient (typeService.findByName("Pumpkin_Spice"), 3));
        
        service.save( r1 );
        
        printRecipes();
    }
    
    private void printRecipes () {
        for ( DomainObject r : service.findAll() ) {
            System.out.println( r );
        }
    }

}