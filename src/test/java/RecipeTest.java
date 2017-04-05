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

public class RecipeTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void recipe_instantiatesCorrectly_true() {
    Recipe recipe = new Recipe("Name", "Instructions");
    assertTrue(recipe instanceof Recipe);
  }

  @Test
  public void getters_returnsCorrectly_true() {
    Recipe recipe = new Recipe("Name", "Instructions");
    assertTrue(recipe.getName().equals("Name"));
    assertTrue(recipe.getInstructions().equals("Instructions"));
  }

  @Test
  public void save_savesToDatabase_true() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    assertTrue(Recipe.all().get(0).equals(recipe));
  }

  @Test
  public void find_returnsInstanceById_recipe2() {
    Recipe recipe1 = new Recipe("Name", "Instructions");
    recipe1.save();
    Recipe recipe2 = new Recipe("Namer", "Instructionsr");
    recipe2.save();
    assertEquals(recipe2, Recipe.find(recipe2.getId()));
  }

}
