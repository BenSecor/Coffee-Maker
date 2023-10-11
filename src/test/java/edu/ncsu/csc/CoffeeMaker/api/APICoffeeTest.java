package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientType;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.IngredientTypeService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    @Autowired
    private MockMvc          mvc;

    @Autowired
    private RecipeService service;
    
    @Autowired
    private IngredientTypeService typeService;
    
    @Autowired
    private InventoryService iService;
    
    @Autowired 
    private IngredientService ingredientService;

    

    /**
     * Sets up the tests.
     */
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
          
          
          final Inventory ivt = iService.getInventory();
          ivt.add(new Ingredient (coffee, 300));
          ivt.add(new Ingredient (milk, 300));
          ivt.add(new Ingredient (sugar, 300));
          ivt.add(new Ingredient (chocolate, 300));
          iService.save( ivt );

           
         

          final Recipe recipe = new Recipe();
          recipe.setName( "Coffee" );
          recipe.setPrice( 50 );
          recipe.addIngredient(new Ingredient (coffee, 3));
          recipe.addIngredient(new Ingredient (milk, 3));
          recipe.addIngredient(new Ingredient (sugar, 3));
          recipe.addIngredient(new Ingredient (chocolate, 3));
          service.save( recipe );
     }

    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( 10 ) );

    }

    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }

    @Test
    @Transactional
    public void testPurchaseBeverage3 () throws Exception {
        /* Insufficient inventory */

        final Inventory ivt = iService.getInventory();
        ivt.deleteIngredient(new Ingredient(typeService.findByName("Coffee"), 0));
       
        iService.save( ivt );

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

}
