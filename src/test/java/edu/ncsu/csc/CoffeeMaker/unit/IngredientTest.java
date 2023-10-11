package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientType;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @BeforeEach
    public void setup () {
    }
    
    @Test
    @Transactional
    public void testCreateIngredientInstance() {
    	IngredientType coffee = new IngredientType("Coffee");
    	Ingredient i =  new Ingredient (coffee, 300);
    	Assertions.assertEquals(i.getAmount(), 300);
    	Assertions.assertEquals(i.getIngredientType(),coffee);
    	
    }
    
}