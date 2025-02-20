import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static org.hamcrest.Matchers.*;

public class GoRestAutomation {

    private static final String BASE_URL = "https://gorest.co.in/public/v2/";
    private static final String TOKEN = "841f2e7225a0e641fd991af772a706b9637b130964516a0e6376b3e1c0cd0bfb"; // Replace with a valid token

    public static void main(String[] args) {
        // Step 1: GET Users
        getUsers();

        // Step 2: POST Create a new user
        int userId = createUser();

        // Step 3: PUT Update the user's details
        updateUser(userId);

        // Step 4: DELETE the user
        deleteUser(userId);
    }

    public static void getUsers() {
        System.out.println("GET /users");
        Response response = RestAssured
                .given()
                .auth().oauth2(TOKEN)
                .get(BASE_URL + "/users");

        response.prettyPrint();
        System.out.println("Status Code: " + response.getStatusCode());
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0)); // Ensure there are users in the list
    }

    public static int createUser() {
        System.out.println("\nPOST /users");

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Satrio Wibisono");
        requestParams.put("email", "satriowbsn" + System.currentTimeMillis() + "@yopmail.com");
        requestParams.put("gender", "male");
        requestParams.put("status", "active");

        Response response = RestAssured
                .given()
                .auth().oauth2(TOKEN)
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .post(BASE_URL + "/users");

        response.prettyPrint();
        System.out.println("Status Code: " + response.getStatusCode());

        return response.jsonPath().getInt("id");
    }

    public static void updateUser(int userId) {
        System.out.println("\nPUT /users/" + userId);

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Updated User");
        requestParams.put("status", "inactive");

        Response response = RestAssured
                .given()
                .auth().oauth2(TOKEN)
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .put(BASE_URL + "/users/" + userId);

        response.prettyPrint();
        // Validate response
        response.then()
                .statusCode(200)
                .body("name", equalTo("Updated User"))
                .body("status", equalTo("inactive"));
        System.out.println("Status Code: " + response.getStatusCode());
    }

    public static void deleteUser(int userId) {
        System.out.println("\nDELETE /users/" + userId);

        Response response = RestAssured
                .given()
                .auth().oauth2(TOKEN)
                .delete(BASE_URL + "/users/" + userId);

        System.out.println("Status Code: " + response.getStatusCode());
        if (response.getStatusCode() == 204) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
 
            //Verify the user no longer exists
            Response getUserResponse = RestAssured
                    .given()
                    .auth().oauth2(TOKEN)
                    .get(BASE_URL + "/users/" + userId);
            getUserResponse.then().statusCode(404);

            
        }
    }
}
