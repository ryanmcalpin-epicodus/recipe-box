import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;

public class Recipe implements DatabaseManagement {
  private String name;
  private String instructions;
  private int id;

  public Recipe(String name, String instructions) {
    this.name = name;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }

  public String getInstructions() {
    return instructions;
  }

  public int getId() {
    return id;
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (name, instructions) VALUES (:name, :instructions)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("instructions", this.instructions)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Recipe> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes";
      return con.createQuery(sql)
        .executeAndFetch(Recipe.class);
    }
  }

  @Override
  public boolean equals(Object otherRecipe) {
    if (!(otherRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) otherRecipe;
      return this.getName().equals(newRecipe.getName()) && this.getId() == newRecipe.getId();
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Recipe.class);
    }
  }

  public void addCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_recipes (category_id, recipe_id) VALUES (:category_id, :recipe_id)";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("recipe_id", this.id)
        .executeUpdate();
    }
  }

  public List<Category> getCategories() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT categories.* FROM recipes JOIN categories_recipes ON (recipes.id = categories_recipes.recipe_id) JOIN categories ON (categories.id = categories_recipes.category_id) WHERE recipes.id = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Category.class);
    }
  }

  public void removeCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM categories_recipes WHERE category_id = :category_id AND recipe_id = :recipe_id";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("recipe_id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public void remove() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM recipes WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateName(String newName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE recipes SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateInstructions(String newInstructions) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE recipes SET instructions = :instructions WHERE id = :id";
      con.createQuery(sql)
        .addParameter("instructions", newInstructions)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void addIngredient(Ingredient ingredient) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes_ingredients (recipe_id, ingredient_id) VALUES (:recipe_id, :ingredient_id)";
      con.createQuery(sql)
        .addParameter("recipe_id", this.id)
        .addParameter("ingredient_id", ingredient.getId())
        .executeUpdate();
    }
  }

  public List<Ingredient> getIngredients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT ingredients.* FROM recipes JOIN recipes_ingredients ON (recipes.id = recipes_ingredients.recipe_id) JOIN ingredients ON (ingredients.id = recipes_ingredients.ingredient_id) WHERE recipes.id = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Ingredient.class);
    }
  }

  public void removeIngredient(Ingredient ingredient) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM recipes_ingredients WHERE recipe_id = :recipe_id AND ingredient_id = :ingredient_id";
      con.createQuery(sql)
        .addParameter("recipe_id", this.id)
        .addParameter("ingredient_id", ingredient.getId())
        .executeUpdate();
    }
  }

  public void vote(int rating) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO votes (rating, recipe_id) VALUES (:rating, :recipe_id)";
      con.createQuery(sql)
        .addParameter("rating", rating)
        .addParameter("recipe_id", this.id)
        .executeUpdate();
    }
  }

  public List<Integer> getVotes() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT rating FROM votes WHERE recipe_id = :recipe_id";
      return con.createQuery(sql)
        .addParameter("recipe_id", this.id)
        .executeAndFetch(Integer.class);
    }
  }

  public float averageVotes() {
    List<Integer> votes = this.getVotes();
    if (votes.size() == 0) {
      return 0;
    } else {
      float ratingsSum = 0;
      for (int rating : votes) {
        ratingsSum += rating;
      }
      return ratingsSum / votes.size();
    }
  }

  // public static List<Recipe> listRanked() {
  //   List<Recipe> recipes = Recipe.all();
  //   for (int i = 0; i < Recipe.all().size(); i++) {
  //     if ()
  //   }
  //
  //   return recipes;
  // }

}
