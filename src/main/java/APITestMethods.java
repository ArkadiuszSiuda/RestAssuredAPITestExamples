import ObjectModel.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class APITestMethods {

    String baseUrl = "https://api.restful-api.dev/objects";

    public Response GetListOfAllItems() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl);
        response.then().log().all();
        return response;
    }

    public Response GetItemById(String id) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/" + id);
        response.then().log().all();
        return response;
    }

    public Response GetMultipleItemsByIds(String... ids) {
        StringBuilder url = new StringBuilder(baseUrl + "?");
        for (String id : ids) {
            url.append("id=").append(id).append("&");
        }
        url.setLength(url.length() - 1);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(url.toString());
        response.then().log().all();
        return response;
    }

    public Response AddItem(Product product) {
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(json)
                    .when()
                    .post(baseUrl);
            response.then().log().all();
            return response;
    }

    public Response ReplaceItem(Product product, String id) {
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(json)
                .when()
                .put(baseUrl + "/" + id);
        response.then().log().all();
        return response;
    }

    public Response UpdateItemWithId(Product product, String id) {
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            json = objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(json)
                .when()
                .patch(baseUrl + "/" + id);
        response.then().log().all();
        return response;
    }

    public Response DeleteItemById(String id) {
        Response response = RestAssured.given()
                .when()
                .delete(baseUrl + "/" + id);
        response.then().log().all();
        return response;
    }
}
