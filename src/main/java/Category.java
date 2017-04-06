import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Category implements DatabaseManagement {
  private String name;
  private int id;

  public Category(String name) {
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
      String sql = "INSERT INTO categories (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Category> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories";
      return con.createQuery(sql)
        .executeAndFetch(Category.class);
    }
  }

  @Override
  public boolean equals(Object otherCategory) {
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName()) && this.getId() == newCategory.getId();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
    }
  }

  @Override
  public void remove() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM categories WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateName(String newName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE categories SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

}
