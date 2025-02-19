import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ApiTest {

    @Test

        // Step 1: GET All Users
    void getuser() {

        Response response = RestAssured.get("https://gorest.co.in/public/v2/users");
        System.out.println("Response :" + response.asString());
        System.out.println("Response :" + response.getStatusCode());
        System.out.println("Body :" + response.getBody());
        System.out.println("Time Taken : :" + response.getTime());
        System.out.println("Header :" + response.getHeader("content-type"));
        int StatusCode = response.getStatusCode();

        Assert.assertEquals(StatusCode,200);


    }



}
