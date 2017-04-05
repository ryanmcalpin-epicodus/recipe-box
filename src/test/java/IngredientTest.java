import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class IngredientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();


  @Test
  public void ingredient_instantiatesCorrectly_true() {
    Ingredient ingredient = new Ingredient("Name");
    assertTrue(ingredient instanceof Ingredient);
  }

  @Test
  public void getters_returnsCorrectly_true() {
    Ingredient ingredient = new Ingredient("Name");
    assertTrue(ingredient.getName().equals("Name"));
  }

  @Test
  public void save_savesToDatabase_true() {
    Ingredient ingredient = new Ingredient("Name");
    ingredient.save();
    assertTrue(Ingredient.all().get(0).equals(ingredient));
  }

  @Test
  public void find_returnsInstanceById_ingredient2() {
    Ingredient ingredient1 = new Ingredient("Name");
    ingredient1.save();
    Ingredient ingredient2 = new Ingredient("Namer");
    ingredient2.save();
    assertEquals(ingredient2, Ingredient.find(ingredient2.getId()));
  }
}
