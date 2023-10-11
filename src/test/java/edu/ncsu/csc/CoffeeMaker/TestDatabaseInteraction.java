package edu.ncsu.csc.CoffeeMaker;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;


@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )


public class TestDatabaseInteraction {
	@Autowired
	private RecipeService recipeService;
	
	/* Global recipe to test against **/
	Recipe r = new Recipe();
	
	/**
	 * fills a recipe object with given ingredients
	 * 
	 * @param newRecipe object we want to update
	 * @param name of recipe
	 * @param choco to add
	 * @param coffee to add
	 * @param milk to add
	 * @param sugar to add
	 * @param price of recipe
	 */
	private void createRecipe(Recipe newRecipe, String name, int choco, int coffee, int milk, int sugar, int price) {
		//set all default ingredients
		newRecipe.setName(name);
//		newRecipe.setChocolate(choco);
//		newRecipe.setCoffee(coffee);
//		newRecipe.setMilk(milk);
//		newRecipe.setSugar(sugar);
		newRecipe.setPrice(price);
	}
	
	
	/**
	 * initialized r with a real recipe and verify DB contains recipe
	 */
	private List<Recipe> setUp() {
		//clear DB from any other recipes
		recipeService.deleteAll();
		
		//create Mocha Recipe and save to DB
		createRecipe(r, "Mocha", 1, 1, 1, 1, 350);
	    recipeService.save( r );
	    
	    //test database get recipe
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
	    
	    assertEquals(1, dbRecipes.size());
	    Recipe dbRecipe = dbRecipes.get(0);
	    assertEquals(r.getName(), dbRecipe.getName());
	    
//	    assertEquals(dbRecipe.getSugar(), r.getSugar());
//	    assertEquals(dbRecipe.getMilk(), r.getMilk());
//	    assertEquals(dbRecipe.getCoffee(), r.getCoffee());
//	    assertEquals(dbRecipe.getChocolate(), r.getChocolate());
	    assertEquals(dbRecipe.getPrice(), r.getPrice());
	    
	    return dbRecipes;
	}
	
	@Test
	@Transactional
	public void testRecipes(){
		
	    //test database get recipe
	    List<Recipe> dbRecipes = setUp();
	    
	    Recipe dbRecipe = dbRecipes.get(0);
	    
	    assertEquals(r.getName(), dbRecipe.getName());
	    
//	    assertEquals(dbRecipe.getSugar(), r.getSugar());
//	    assertEquals(dbRecipe.getMilk(), r.getMilk());
//	    assertEquals(dbRecipe.getCoffee(), r.getCoffee());
//	    assertEquals(dbRecipe.getChocolate(), r.getChocolate());
	    assertEquals(dbRecipe.getPrice(), r.getPrice());
	    
	    //test findByName
	    dbRecipe = recipeService.findByName("Mocha");
	    
	    assertEquals(r.getName(), dbRecipe.getName());
//	    assertEquals(dbRecipe.getSugar(), r.getSugar());
//	    assertEquals(dbRecipe.getMilk(), r.getMilk());
//	    assertEquals(dbRecipe.getCoffee(), r.getCoffee());
//	    assertEquals(dbRecipe.getChocolate(), r.getChocolate());
	    assertEquals(dbRecipe.getPrice(), r.getPrice());
	    
	    // test edit recipe
	    dbRecipe.setPrice(15);
//	    dbRecipe.setSugar(12);
	    recipeService.save(dbRecipe);
	    
	    dbRecipes = recipeService.findAll();
	    
	    assertEquals(1, dbRecipes.size());
	    dbRecipe = dbRecipes.get(0);
	    assertEquals(r.getName(), dbRecipe.getName());
	    
//	    assertEquals(dbRecipe.getSugar(), 12);
	    assertEquals(dbRecipe.getPrice(), 15);
	   
	}
	
	@Test
	@Transactional
	public void testDeleteRecipe(){
		
		//test database get recipe
	    List<Recipe> dbRecipes = setUp();
	    
	    Recipe dbRecipe = dbRecipes.get(0); // Mocha Recipe
	    
	    assertEquals(r.getName(), dbRecipe.getName());
	    
	    
	    /* delete single recipe */
	    assertEquals(1, recipeService.count());
	    recipeService.delete(r);
	    assertEquals(0, recipeService.count());
	    
	    /* delete multiple recipes */
	    //reset database
	    dbRecipes = setUp();
	    assertEquals(1, recipeService.count());
	    
	    List<Recipe> newRecipes = new ArrayList<Recipe>();
	    
	    Recipe chocolatte = new Recipe();
	    createRecipe(chocolatte, "Chocolate Latte", 6, 1, 1, 2, 400);
	    newRecipes.add(chocolatte);
	    
	    Recipe dark = new Recipe();
	    createRecipe(dark, "Dark Roast", 0, 6, 0, 0, 350);
	    newRecipes.add(dark);
	    
	    Recipe creamy = new Recipe();
	    createRecipe(creamy, "Creamy", 0, 2, 4, 3, 300);
	    newRecipes.add(creamy);
	    
	    Recipe simple = new Recipe();
	    createRecipe(simple, "Simple", 0, 2, 2, 4, 300);
	    newRecipes.add(simple);
	    
	    //add all new recipes to DB
	    assertEquals(1, recipeService.count());
	    recipeService.saveAll(newRecipes);
	    assertEquals(5, recipeService.count());
	    
	    //verify all recipes were saved correctly to DB
	    for (Recipe recipe: newRecipes) {
	    	dbRecipe = recipeService.findByName(recipe.getName());
		    
		    assertEquals(recipe.getName(), dbRecipe.getName());
//		    assertEquals(recipe.getSugar(), dbRecipe.getSugar());
//		    assertEquals(recipe.getMilk(), dbRecipe.getMilk());
//		    assertEquals(recipe.getCoffee(), dbRecipe.getCoffee());
//		    assertEquals(recipe.getChocolate(), dbRecipe.getChocolate());
		    assertEquals(recipe.getPrice(), dbRecipe.getPrice());
	    }
	    
	    assertEquals(5, recipeService.count());
	    for (Recipe recipe: newRecipes) {
	    	recipeService.delete(recipe);
	    }
	    assertEquals(1, recipeService.count());
	    
	    //verify r -> mocha recipe is the last in DB
	    dbRecipes = (List<Recipe>) recipeService.findAll();
	    assertEquals(1, dbRecipes.size());
	    dbRecipe = dbRecipes.get(0);
	    assertEquals(r.getName(), dbRecipe.getName());
	    
//	    assertEquals(dbRecipe.getSugar(), r.getSugar());
//	    assertEquals(dbRecipe.getMilk(), r.getMilk());
//	    assertEquals(dbRecipe.getCoffee(), r.getCoffee());
//	    assertEquals(dbRecipe.getChocolate(), r.getChocolate());
	    assertEquals(dbRecipe.getPrice(), r.getPrice());
	}
}
