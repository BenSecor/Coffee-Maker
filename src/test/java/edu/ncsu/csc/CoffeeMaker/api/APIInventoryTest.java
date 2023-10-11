package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class APIInventoryTest { 
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	@Autowired
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private IngredientService ingredientService;
	
	@Autowired
	private RecipeService recipeService;
    
    @Autowired
    private IngredientTypeService typeService;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
		mvc = MockMvcBuilders.webAppContextSetup( context ).build();
		typeService.deleteAll();
		ingredientService.deleteAll();
		recipeService.deleteAll();
		inventoryService.deleteAll();
        
	}
	
	@Test
	@Transactional
	public void APIUpdateInventory() throws Exception {
        Ingredient coffee = new Ingredient(new IngredientType("Coffee"), 300);
        Ingredient sugar = new Ingredient(new IngredientType("Sugar"), 200);
        Ingredient milk = new Ingredient(new IngredientType("Milk"), 100);
        Ingredient chocolate = new Ingredient(new IngredientType("Chocolate"), 600);
        
		mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( coffee ))).andExpect( status().isOk() );
	    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( sugar
		            		)) ).andExpect( status().isOk() );
	    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( milk
		            		)) ).andExpect( status().isOk() );
	    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( chocolate
		            		 ) ) ).andExpect( status().isOk() );
	
	    
		String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
		        .andReturn().getResponse().getContentAsString();
		
		Assertions.assertTrue(inventory.contains("Coffee"));
		Assertions.assertTrue(inventory.contains("Milk"));
		Assertions.assertTrue(inventory.contains("Sugar"));
		Assertions.assertTrue(inventory.contains("Chocolate"));
		
		Inventory ivt = new Inventory();
		ivt.add(new Ingredient (typeService.findByName("Coffee"), 100));
	    ivt.add(new Ingredient (typeService.findByName("Milk"), 600));
		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ivt ) ) ).andExpect( status().isOk() );
		
		inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
        .andReturn().getResponse().getContentAsString();
		Assertions.assertTrue(inventory.contains("\"name\":\"Coffee\"},\"amount\":400"));
		Assertions.assertTrue(inventory.contains("\"name\":\"Milk\"},\"amount\":700"));
	}
	
	/**
	 * Tests getting the recipe from API
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@Test
	@Transactional
	public void APIInventoryTest() throws UnsupportedEncodingException, Exception {
		
        
		mvc.perform( get( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	             ).andExpect( status().isOk() );
		 Ingredient coffee = new Ingredient(new IngredientType("Coffee"), 300);
	        Ingredient sugar = new Ingredient(new IngredientType("Sugar"), 200);
	        Ingredient milk = new Ingredient(new IngredientType("Milk"), 100);
	        Ingredient chocolate = new Ingredient(new IngredientType("Chocolate"), 600);
	        
			mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
			            .content( TestUtils.asJsonString( coffee ))).andExpect( status().isOk() );
		    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
			            .content( TestUtils.asJsonString( sugar
			            		)) ).andExpect( status().isOk() );
		    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
			            .content( TestUtils.asJsonString( milk
			            		)) ).andExpect( status().isOk() );
		    mvc.perform( post( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
			            .content( TestUtils.asJsonString( chocolate
			            		 ) ) ).andExpect( status().isOk() );
		
		final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
	      
	    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
	        
		final Recipe r = createRecipe("Mocha", 10, 3, 4, 8, 5);

		mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
		    
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
		        .andReturn().getResponse().getContentAsString();
		
		
		
		Assertions.assertTrue(recipe.contains("Mocha"));
		
	   
	    mvc.perform( get( "/api/v1/recipes/mocha" ) ).andDo( print() ).andExpect( status().isOk() );
	   
	   
	        
	    mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
	             ).andExpect( status().isOk() ); 
	    
	    mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
	             ).andExpect( status().isNotFound() ); 
	    
	    String recipes = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
	            .andReturn().getResponse().getContentAsString();
	        
	    Assertions.assertFalse(recipes.contains("Mocha"),
	            "mocha should not be int the database " ); 
	    
	    Assertions.assertTrue(recipes.contains("Coffee"),
	            "Coffee should not be in the database " );
	    	
	    
	    mvc.perform( delete( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	    		.content( TestUtils.asJsonString(sugar))).andExpect( status().isOk() );
	    
	    mvc.perform( delete( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	    		.content( TestUtils.asJsonString(sugar))).andExpect( status().isConflict() );
	    }
	
	 	private Recipe createRecipe ( final String name, final Integer price, final Integer coffeeAmt, final Integer milkAmt,
	    		final Integer sugarAmt, final Integer chocolateAmt ) {
	        
	    	final Recipe recipe = new Recipe();
	    	 
	        recipe.setName( name );
	        recipe.setPrice( price );
	        Ingredient coffee = new Ingredient (typeService.findByName("Coffee"), coffeeAmt);
	        Ingredient milk = new Ingredient (typeService.findByName("Milk"), milkAmt);
	        Ingredient sugar = new Ingredient (typeService.findByName("Sugar"), sugarAmt);
	        Ingredient chocolate = new Ingredient (typeService.findByName("Chocolate"), chocolateAmt);
	        
//	        ingredientService.save(chocolate);
//	        ingredientService.save(sugar);
//	        ingredientService.save(milk);
//	        ingredientService.save(chocolate);
	        
	        recipe.addIngredient(coffee);
	        recipe.addIngredient(milk);
	        recipe.addIngredient(sugar);
	        recipe.addIngredient(chocolate);
	    
	    
	        return recipe;
	    }
	}     
