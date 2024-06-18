import ObjectModel.Data;
import ObjectModel.Product;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredTest {

    private static String itemId;
    private APITestMethods _apiMethods;

    @BeforeEach
    public void setup(){
        _apiMethods = new APITestMethods();
    }

    @Test
    @Order(1)
    public void VerifyGettingListOfAllItems(){
        Response response = _apiMethods.GetListOfAllItems();

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("find { it.id == '1' }.name"), "Google Pixel 6 Pro");
        Assertions.assertEquals(response.jsonPath().getString("find { it.id == '3' }.data.color"), "Cloudy White");
        Assertions.assertEquals(response.jsonPath().getDouble("find { it.id == '5' }.data.price"), 689.99);
    }

    @Test
    @Order(2)
    public void VerifyGettingItemById(){
        Response response =_apiMethods.GetItemById("4");

        Assertions.assertAll("Verify object with id 4",
                () -> Assertions.assertEquals(response.statusCode(), 200),
                () -> Assertions.assertEquals(response.jsonPath().getString("name"), "Apple iPhone 11, 64GB"),
                () -> Assertions.assertEquals(response.jsonPath().getString("data.color"), "Purple"),
                () -> Assertions.assertEquals(response.jsonPath().getDouble("data.price"), 389.99)
        );
    }

    @Test
    @Order(3)
    public void VerifyGettingMultipleItemsByTheirIds(){
        Response response = _apiMethods.GetMultipleItemsByIds("2", "6", "10");

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("find { it.id == '2' }.name"), "Apple iPhone 12 Mini, 256GB, Blue");
        Assertions.assertEquals(response.jsonPath().getString("find { it.id == '6' }.data.generation"), "3rd");
        Assertions.assertEquals(response.jsonPath().getDouble("find { it.id == '10' }.data.'Screen size'"), 7.9);
    }

    @Test
    @Tag("integrated")
    @Order(4)
    public void VerifyCreationOfNewItem(){
        Response response = _apiMethods.AddItem(new Product("Best smartphone ever", new Data(2030, 999.99, "Super fast", "5TB")));
        itemId = response.getBody().jsonPath().getString("id");

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("name"), "Best smartphone ever");
        Assertions.assertEquals(response.jsonPath().getInt("data.year"), 2030);
        Assertions.assertEquals(response.jsonPath().getDouble("data.price"), 999.99);
        Assertions.assertEquals(response.jsonPath().getString("data.cpuModel"), "Super fast");
        Assertions.assertEquals(response.jsonPath().getString("data.hardDiskSize"), "5TB");
    }

    @Test
    @Tag("integrated")
    @Order(5)
    public void VerifyOverwritingOfExistingItemById(){
        Response response = _apiMethods.ReplaceItem(new Product("Better smartphone", new Data(2035, 777.77, "Super fast++", "10TB")), itemId);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("id"), itemId);
        Assertions.assertEquals(response.jsonPath().getString("name"), "Better smartphone");
        Assertions.assertEquals(response.jsonPath().getInt("data.year"), 2035);
        Assertions.assertEquals(response.jsonPath().getDouble("data.price"), 777.77);
        Assertions.assertEquals(response.jsonPath().getString("data.cpuModel"), "Super fast++");
        Assertions.assertEquals(response.jsonPath().getString("data.hardDiskSize"), "10TB");
    }

    @Test
    @Tag("integrated")
    @Order(6)
    public void VerifyPartialUpdatingOfExistingItemById(){
        Response response = _apiMethods.UpdateItemWithId(new Product("Better smartphone updated"), itemId);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("name"), "Better smartphone updated");
        Assertions.assertEquals(response.jsonPath().getInt("data.year"), 2040);
        Assertions.assertEquals(777.77, response.jsonPath().getDouble("data.price"));
        Assertions.assertEquals(response.jsonPath().getString("data.cpuModel"), "Super fast++");
        Assertions.assertEquals(response.jsonPath().getString("data.hardDiskSize"), "10TB");
    }

    @Test
    @Order(7)
    @Tag("integrated")
    public void VerifyDeletionOfItemById(){
        Response response = _apiMethods.DeleteItemById(itemId);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.jsonPath().getString("message"), String.format("Object with %s, has been deleted.", itemId));
    }
}
