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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.IngredientTypeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;


@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateInventory {
	@Autowired
    private InventoryService iService;
    
    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private IngredientService recipeService;
    
    @Autowired
    private IngredientTypeService typeService;

    @Test
    @Transactional
    public void testCreateInventory() {
    	
    	recipeService.deleteAll();
    	iService.deleteAll();
        ingredientService.deleteAll();
        typeService.deleteAll();
        
        IngredientType coffee = new IngredientType("Coffee");
        IngredientType milk = new IngredientType("Milk");
      
        typeService.save( coffee );
        typeService.save( milk );

        Inventory ivt = iService.getInventory();
        ivt.deleteAll();
        ivt.add(new Ingredient (coffee, 300));
        ivt.add(new Ingredient (milk, 300));
        iService.save( ivt );
   
        ivt = iService.getInventory();
        Assertions.assertEquals(ivt.getIngredient(typeService.findByName("Coffee")).getAmount(), 300);
        Assertions.assertEquals(ivt.getIngredient(milk).getAmount(), 300);
    }
        
}