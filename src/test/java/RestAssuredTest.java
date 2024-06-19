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

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Google Pixel 6 Pro", response.jsonPath().getString("find { it.id == '1' }.name"));
        Assertions.assertEquals("Cloudy White", response.jsonPath().getString("find { it.id == '3' }.data.color"));
        Assertions.assertEquals(689.99, response.jsonPath().getDouble("find { it.id == '5' }.data.price"));
    }

    @Test
    @Order(2)
    public void VerifyGettingItemById(){
        Response response =_apiMethods.GetItemById("4");

        Assertions.assertAll("Verify object with id 4",
                () -> Assertions.assertEquals(200, response.statusCode()),
                () -> Assertions.assertEquals("Apple iPhone 11, 64GB", response.jsonPath().getString("name")),
                () -> Assertions.assertEquals("Purple", response.jsonPath().getString("data.color")),
                () -> Assertions.assertEquals(389.99, response.jsonPath().getDouble("data.price"))
        );
    }

    @Test
    @Order(3)
    public void VerifyGettingMultipleItemsByTheirIds(){
        Response response = _apiMethods.GetMultipleItemsByIds("2", "6", "10");

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Apple iPhone 12 Mini, 256GB, Blue", response.jsonPath().getString("find { it.id == '2' }.name"));
        Assertions.assertEquals("3rd", response.jsonPath().getString("find { it.id == '6' }.data.generation"));
        Assertions.assertEquals(7.9, response.jsonPath().getDouble("find { it.id == '10' }.data.'Screen size'"));
    }

    @Test
    @Tag("integrated")
    @Order(4)
    public void VerifyCreationOfNewItem(){
        Response response = _apiMethods.AddItem(new Product("Best smartphone ever", new Data(2030, 999.99, "Super fast", "5TB")));
        itemId = response.getBody().jsonPath().getString("id");

        Assertions.assertAll(String.format("Verify object with id %s", itemId),
                () -> Assertions.assertEquals(200, response.statusCode()),
                () -> Assertions.assertEquals("Best smartphone ever", response.jsonPath().getString("name")),
                () -> Assertions.assertEquals(2030, response.jsonPath().getInt("data.year")),
                () -> Assertions.assertEquals(999.99, response.jsonPath().getDouble("data.price")),
                () -> Assertions.assertEquals("Super fast", response.jsonPath().getString("data.cpuModel")),
                () -> Assertions.assertEquals("5TB", response.jsonPath().getString("data.hardDiskSize"))
        );
    }

    @Test
    @Tag("integrated")
    @Order(5)
    public void VerifyOverwritingOfExistingItemById(){
        Response response = _apiMethods.ReplaceItem(new Product("Better smartphone", new Data(2035, 777.77, "Super fast++", "10TB")), itemId);

        Assertions.assertAll(String.format("Verify object with id %s", itemId),
                () -> Assertions.assertEquals(200, response.statusCode()),
                () -> Assertions.assertEquals("Better smartphone", response.jsonPath().getString("name")),
                () -> Assertions.assertEquals(2035, response.jsonPath().getInt("data.year")),
                () -> Assertions.assertEquals(777.77, response.jsonPath().getDouble("data.price")),
                () -> Assertions.assertEquals("Super fast++", response.jsonPath().getString("data.cpuModel")),
                () -> Assertions.assertEquals("10TB", response.jsonPath().getString("data.hardDiskSize"))
        );
    }

    @Test
    @Tag("integrated")
    @Order(6)
    public void VerifyPartialUpdatingOfExistingItemById(){
        Response response = _apiMethods.UpdateItemWithId(new Product("Better smartphone updated"), itemId);

        Assertions.assertAll(String.format("Verify object with id %s", itemId),
                () -> Assertions.assertEquals(200, response.statusCode()),
                () -> Assertions.assertEquals("Better smartphone updated", response.jsonPath().getString("name")),
                () -> Assertions.assertEquals(2035, response.jsonPath().getInt("data.year")),
                () -> Assertions.assertEquals(777.77, response.jsonPath().getDouble("data.price")),
                () -> Assertions.assertEquals("Super fast++", response.jsonPath().getString("data.cpuModel")),
                () -> Assertions.assertEquals("10TB", response.jsonPath().getString("data.hardDiskSize"))
        );
    }

    @Test
    @Order(7)
    @Tag("integrated")
    public void VerifyDeletionOfItemById(){
        Response response = _apiMethods.DeleteItemById(itemId);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(String.format("Object with id = %s has been deleted.", itemId), response.jsonPath().getString("message"));
    }
}
