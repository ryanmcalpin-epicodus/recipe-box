import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Ingredient implements DatabaseManagement {
  private String name;
  private int id;

  public Ingredient(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

@Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Ingredient> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients";
      return con.createQuery(sql)
        .executeAndFetch(Ingredient.class);
    }
  }

  @Override
  public boolean equals(Object otherIngredient) {
    if (!(otherIngredient instanceof Ingredient)) {
      return false;
    } else {
      Ingredient newIngredient = (Ingredient) otherIngredient;
      return this.getName().equals(newIngredient.getName()) && this.getId() == newIngredient.getId();
    }
  }

  public static Ingredient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Ingredient.class);
    }
  }

  @Override
  public void remove() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM ingredients WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public List<Recipe> getRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT recipes.* FROM ingredients JOIN recipes_ingredients ON (ingredients.id = recipes_ingredients.ingredient_id) JOIN recipes ON (recipes.id = recipes_ingredients.recipe_id) WHERE ingredients.id = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Recipe.class);
    }
  }

}
