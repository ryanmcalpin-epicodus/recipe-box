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

  @Test
  public void addCategory_associatesRecipeWithCategory_category() {
    Recipe recipe1 = new Recipe("Name", "Instructions");
    recipe1.save();
    Recipe recipe2 = new Recipe("Namer", "Instructionsr");
    recipe2.save();
    Category category1 = new Category("Name");
    category1.save();
    Category category2 = new Category("Namer");
    category2.save();
    recipe1.addCategory(category1);
    recipe1.addCategory(category2);
    recipe2.addCategory(category2);
    assertEquals(category1, Recipe.find(recipe1.getId()).getCategories().get(0));
    assertEquals(category2, Recipe.find(recipe1.getId()).getCategories().get(1));
    assertEquals(category2, Recipe.find(recipe2.getId()).getCategories().get(0));
  }

  @Test
  public void removeCategory_disassociatesCategoryWithRecipe_0() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    Category category = new Category("Name");
    category.save();
    recipe.addCategory(category);
    assertTrue(Recipe.find(recipe.getId()).getCategories().get(0).equals(category));
    recipe.removeCategory(category);
    assertEquals(0, Recipe.find(recipe.getId()).getCategories().size());
  }

  @Test
  public void remove_removesRecipe_0() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    assertEquals(1, Recipe.all().size());
    recipe.remove();
    assertEquals(0, Recipe.all().size());
  }

  @Test
  public void updateName_changesNameOfRecipe_true() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    recipe.updateName("New Name");
    assertTrue(Recipe.find(recipe.getId()).getName().equals("New Name"));
  }

  @Test
  public void updateInstructions_changesInstructions_true() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    recipe.updateInstructions("New Instructions");
    assertTrue(Recipe.find(recipe.getId()).getInstructions().equals("New Instructions"));
  }

  @Test
  public void addIngredient_associatesIngredientWithRecipe_recipe() {
    Ingredient ingredient1 = new Ingredient("Name");
    ingredient1.save();
    Ingredient ingredient2 = new Ingredient("Namer");
    ingredient2.save();
    Recipe recipe1 = new Recipe("Name", "Instructions");
    recipe1.save();
    Recipe recipe2 = new Recipe("Namer", "Instructionsr");
    recipe2.save();
    recipe1.addIngredient(ingredient1);
    recipe1.addIngredient(ingredient2);
    recipe2.addIngredient(ingredient2);
    assertEquals(ingredient1, Recipe.find(recipe1.getId()).getIngredients().get(0));
    assertEquals(ingredient2, Recipe.find(recipe1.getId()).getIngredients().get(1));
    assertEquals(ingredient2, Recipe.find(recipe2.getId()).getIngredients().get(0));
  }

  @Test
  public void removeIngredient_disassociatesIngredientWithRecipe_0() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    Ingredient ingredient = new Ingredient("Name");
    ingredient.save();
    recipe.addIngredient(ingredient);
    assertTrue(Recipe.find(recipe.getId()).getIngredients().get(0).equals(ingredient));
    recipe.removeIngredient(ingredient);
    assertEquals(0, Recipe.find(recipe.getId()).getIngredients().size());
  }

  @Test
  public void vote_addsVoteToDatabase_5() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    recipe.vote(5);
    recipe.vote(3);
    assertEquals("5", Recipe.find(recipe.getId()).getVotes().get(0).toString());
    assertEquals("3", Recipe.find(recipe.getId()).getVotes().get(1).toString());
  }

  @Test
  public void averageVotes_returnsMeanAverageOfAllVotes_4() {
    Recipe recipe = new Recipe("Name", "Instructions");
    recipe.save();
    recipe.vote(5);
    recipe.vote(3);
    assertEquals(4, Recipe.find(recipe.getId()).averageVotes(), 0);
  }

  // @Test
  // public void listRanked_ordersRecipeFromHighToLowRank_true() {
  //   Recipe recipe1 = new Recipe("Name", "Instructions");
  //   recipe1.save();
  //   recipe1.vote(3);
  //   Recipe recipe2 = new Recipe("Name", "Instructions");
  //   recipe2.save();
  //   recipe2.vote(1);
  //   Recipe recipe3 = new Recipe("Name", "Instructions");
  //   recipe3.save();
  //   recipe3.vote(5);
  //   assertTrue(Recipe.listRanked().get(0).equals(recipe3));
  //   assertTrue(Recipe.listRanked().get(1).equals(recipe1));
  //   assertTrue(Recipe.listRanked().get(2).equals(recipe2));
  // }

}
