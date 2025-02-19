import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class GoRestAutomation {

    private static final String BASE_URL = "https://gorest.co.in/public/v2/users";
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
    }

    public static int createUser() {
        System.out.println("\nPOST /users");

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Test User");
        requestParams.put("email", "testuser" + System.currentTimeMillis() + "@example.com");
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
        }
    }
}
