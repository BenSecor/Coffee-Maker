package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;
    
    @Autowired
    private IngredientTypeService typeService;
    
    @Autowired
    private InventoryService iService;
    
    @Autowired 
    private IngredientService ingredientService;

    @Autowired
    private MockMvc       mvc;
    

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
        ivt.deleteAll();
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
    public void ensureRecipe () throws Exception {
       
 

        final Recipe r = createRecipe("Coffee", 50, 3, 4, 8, 5);
        r.setPrice( 10 );
        r.setName( "Mocha" );
        service.save(r);

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().is4xxClientError() );

    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();
        
        /* Tests getting all recipes */

        final Recipe recipe = createRecipe( "Delicious Not-Coffee", 5, 1, 20, 5, 10 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        
        
        String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipes.contains( "Milk" ) ) {
        	final Recipe newRecipe = createRecipe( "Milk", 4, 0, 20, 0, 0 );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( newRecipe ) ) ).andExpect( status().isOk() );
        }
        Assertions.assertTrue( recipes.contains( "Delicious not-coffee" ), "GET recipes doesn't collect all drinks!" );
        
        
        recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        
        Assertions.assertTrue( recipes.contains( "Delicious not-coffee" ), "GET recipes doesn't collect all drinks!" );
        Assertions.assertTrue( recipes.contains( "Milk" ), "GET /recipes doesn't collect all drinks!" );
    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        
    	service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {
        service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }
    
    @Test
    @Transactional
    public void testDeleteRecipe() throws Exception {
        service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );
        
        service.delete(r3);

        Assertions.assertEquals( 2, service.count(),
                "Creating three recipes then deleting 1 should result in 2 recipes in the database" );

        final Recipe r4 = createRecipe("Cappucino", 10, 0, 8, 1, 2);
        service.save( r4 );
        
        Assertions.assertEquals( 3, service.count(), "adding another recipe should get saved" );
        
        final Recipe r5 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );
        
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r5 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
        
        service.delete(r1);

        Assertions.assertEquals( 2, service.count(),
                "deleting r1 should result in 2 recipes in the database" );
        
        service.delete(r2);

        Assertions.assertEquals( 1, service.count(),
                "deleting r2 should result in 1 recipe in the database" );
        
        service.delete(r4);

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker after deleting r5" );
    }
    
    @Test
    @Transactional
    public void testInvalidUpdateRecipe() throws Exception {
    	
    	service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        
        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isConflict() );

        
    }
    
    @SuppressWarnings("unlikely-arg-type")
	@Test
    @Transactional 
    public void testEqualRecipe() {
  
    	
    	final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    	service.save(r1);
    	final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    	service.save(r2);
    	final Recipe r4 = createRecipe( "Mocha", 50, 3, 1, 1, 0 );
    	service.save(r4);

    	Assertions.assertTrue(r1.equals(r2));
    	Assertions.assertTrue(r1.equals(r1));
    	Assertions.assertFalse(r1.equals(null));
    	Assertions.assertFalse(r1.equals(r4));

    }
    
    @Test
    @Transactional 
    public void testUpdateRecipe() throws Exception {
 
    	service.deleteAll();
    	/* Tests that updates to a recipe are done correcty */
    	
    	String recipes;
    	
    	final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    	service.save(r1);
    	final Recipe r2 = createRecipe( "Coffee", 45, 4, 2, 2, 1 );
    	
    	mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
    	
    	recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
    	
		Assertions.assertTrue(recipes.contains( "45" ), "PUT /recipes did not update Coffee recipe properly");
		

    }
    
    @Test
    @Transactional 
    public void testCheckRecipe() {
    	
    	final Recipe r1 = createRecipe ("Mocha", 45, 0, 0, 0, 0);
    	service.save(r1);
    	final Recipe r2 = createRecipe("Mocha", 45, 1, 0, 0, 0);
    	service.save(r2);
    	final Recipe r3 = createRecipe("Mocha", 45, 0, 1, 0, 0);
    	service.save(r3);
    	final Recipe r4 = createRecipe("Mocha", 45, 0, 0, 1, 0);
    	service.save(r4);
    	final Recipe r5 = createRecipe("Mocha", 45, 0, 0, 0, 1);
    	service.save(r5); 	
    }
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
    		final Integer sugar, final Integer chocolate ) {
        
    	final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient(new Ingredient (typeService.findByName("Coffee"), coffee));
        recipe.addIngredient(new Ingredient (typeService.findByName("Milk"), milk));
        recipe.addIngredient(new Ingredient (typeService.findByName("Sugar"), sugar));
        recipe.addIngredient(new Ingredient (typeService.findByName("Chocolate"), chocolate));
    
        return recipe;
    }
    
}