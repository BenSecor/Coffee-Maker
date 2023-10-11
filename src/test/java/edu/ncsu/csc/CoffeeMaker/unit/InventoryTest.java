package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.IngredientTypeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private IngredientTypeService typeService;
        

    
    
    @Test
    @Transactional
    public void testConsumeInventory () {
         IngredientType coffeeType = new IngredientType("Coffee");
      
         IngredientType sugarType = new IngredientType("Sugar");
        
         IngredientType milkType = new IngredientType("Milk");
 
         IngredientType chocolateType = new IngredientType("Chocolate");
         
         IngredientType greenTeaType = new IngredientType("Green Tea");
   
        final Inventory ivt = new Inventory();
        ivt.add(new Ingredient (coffeeType, 300));
        ivt.add(new Ingredient (milkType, 200));
        ivt.add(new Ingredient (sugarType, 1000));
        ivt.add(new Ingredient (chocolateType, 500));
        ivt.add(new Ingredient (greenTeaType, 500));
       
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        Ingredient coffee = new Ingredient(coffeeType, 4);
        Ingredient chocolate = new Ingredient(chocolateType, 2);
        Ingredient sugar = new Ingredient(sugarType, 1); 
        recipe.addIngredient(coffee);
        recipe.addIngredient(chocolate);
        recipe.addIngredient(sugar);
        recipe.setPrice( 5 );

        ivt.useIngredients(recipe);

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 296, (int) ivt.getIngredient(coffeeType).getAmount() );
        Assertions.assertEquals( 498, (int) ivt.getIngredient(chocolateType).getAmount() );
        Assertions.assertEquals( 999, (int) ivt.getIngredient(sugarType).getAmount() );
    }

    @Test
    @Transactional
    public void testUpdateIngredients () {

        IngredientType coffeeType = new IngredientType("Coffee");
        IngredientType chocolateType = new IngredientType("Chocolate");
        IngredientType milkType = new IngredientType("Milk"); 
        

        IngredientType sugarType = new IngredientType("Sugar");
        
        IngredientType greenTeaType = new IngredientType("Green Tea");
  
        
        final Inventory ivt = new Inventory();
        ivt.add(new Ingredient (coffeeType, 300));
        ivt.add(new Ingredient (milkType, 200));
        ivt.add(new Ingredient (sugarType, 1000));
        ivt.add(new Ingredient (chocolateType, 500));
        ivt.add(new Ingredient (greenTeaType, 500));
        
        ivt.update(coffeeType, 300);
        ivt.update(chocolateType, 300);
        ivt.update(milkType, 300);
        
        

        Assertions.assertEquals( 600, ivt.getIngredient(coffeeType).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 800, ivt.getIngredient(chocolateType).getAmount(),
                "Adding to the inventory should result in correctly-updated values for chocolate" );
        Assertions.assertEquals( 500, ivt.getIngredient(milkType).getAmount(),
                "Adding to the inventory should result in correctly-updated values milk" );

    }
    
    @Test
    @Transactional
    public void testAddInventory () {
    	
    	 IngredientType coffeeType = new IngredientType("Coffee");
         
         IngredientType sugarType = new IngredientType("Sugar");
        
         IngredientType milkType = new IngredientType("Milk");
 
         IngredientType chocolateType = new IngredientType("Chocolate");
         
         IngredientType greenTeaType = new IngredientType("Green Tea");
   
        final Inventory ivt = new Inventory();
        ivt.add(new Ingredient (coffeeType, 300));
        ivt.add(new Ingredient (milkType, 200));
        ivt.add(new Ingredient (sugarType, 1000));
        ivt.add(new Ingredient (chocolateType, 500));
        ivt.add(new Ingredient (greenTeaType, 500));
 
    	IngredientType chaiType = new IngredientType("Chai");
        Ingredient chai = new Ingredient(chaiType, 200);
    	ivt.add(chai);
    	
    	Assertions.assertEquals(6, ivt.count());
    	Assertions.assertEquals(200, ivt.getIngredient(chaiType).getAmount());
    }

    @Test
    @Transactional
    public void testInvalidAddInventory () {
    	 IngredientType coffeeType = new IngredientType("Coffee");
         
         IngredientType sugarType = new IngredientType("Sugar");
        
         IngredientType milkType = new IngredientType("Milk");
 
         IngredientType chocolateType = new IngredientType("Chocolate");
         
         IngredientType greenTeaType = new IngredientType("Green Tea");
   
        final Inventory ivt = new Inventory();
        ivt.add(new Ingredient (coffeeType, 300));
        ivt.add(new Ingredient (milkType, 200));
        ivt.add(new Ingredient (sugarType, 1000));
        ivt.add(new Ingredient (chocolateType, 500));
        ivt.add(new Ingredient (greenTeaType, 500));
        
    	IngredientType chaiType = new IngredientType("Chai");
        Ingredient chai = new Ingredient(chaiType, -2);
       
        try {
            ivt.add( chai );
            Assertions.fail("Trying to add an invalid amount of ingredient");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 300, ivt.getIngredient(coffeeType).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 200, ivt.getIngredient(milkType).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 1000, ivt.getIngredient(sugarType).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredient(greenTeaType).getAmount(),
                    "Trying to update the Inventory with an invalid value for green tea should result in no changes -- green tea" );
            Assertions.assertEquals( 500, ivt.getIngredient(chocolateType).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );
            Assertions.assertNull(ivt.getIngredient(chaiType));
        }
    }

    @Test
    @Transactional
    public void testToString() {
    	
    	 IngredientType coffeeType = new IngredientType("Coffee");
         
         IngredientType sugarType = new IngredientType("SUGAR");
        
         IngredientType milkType = new IngredientType("Milk");
 
         IngredientType chocolateType = new IngredientType("chocolate");
         
         IngredientType greenTeaType = new IngredientType("Green Tea");
   
        final Inventory ivt = new Inventory();
        ivt.add(new Ingredient (coffeeType, 300));
        ivt.add(new Ingredient (milkType, 200));
        ivt.add(new Ingredient (sugarType, 1000));
        ivt.add(new Ingredient (chocolateType, 500));
        ivt.add(new Ingredient (greenTeaType, 500));
        Ingredient chai = new Ingredient(new IngredientType("Chai"), 200);
    	ivt.add(chai);
   		Assertions.assertEquals(ivt.toString(), "Coffee: 300\nMilk: 200\nSugar: 1000\nChocolate: 500\nGreen tea: 500\nChai: 200\n", "To strings do not match");

    }
    		

}