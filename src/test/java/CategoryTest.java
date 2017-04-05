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

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void category_instantiatesCorrectly_true() {
    Category category = new Category("Name");
    assertTrue(category instanceof Category);
  }

  @Test
  public void getters_returnsCorrectly_true() {
    Category category = new Category("Name");
    assertTrue(category.getName().equals("Name"));
  }

  @Test
  public void save_savesToDatabase_true() {
    Category category = new Category("Name");
    category.save();
    assertTrue(Category.all().get(0).equals(category));
  }

  @Test
  public void find_returnsInstanceById_category2() {
    Category category1 = new Category("Name");
    category1.save();
    Category category2 = new Category("Namer");
    category2.save();
    assertEquals(category2, Category.find(category2.getId()));
  }

  @Test
  public void updateName_changesNameOfCategory_true() {
    Category category = new Category("Name");
    category.save();
    category.updateName("New Name");
    assertTrue(Category.find(category.getId()).getName().equals("New Name"));
  }
}
