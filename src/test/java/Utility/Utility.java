package Utility;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Utility {
    Faker faker = new Faker();
   public static RequestSpecification reqSpec;


    @BeforeClass
    public void login(){
        baseURI = "https://test.mersys.io";
        Map<String,String> loginInfos = new HashMap<>();
        loginInfos.put("username" , "turkeyts");
        loginInfos.put("password" , "TechnoStudy123");
        loginInfos.put("rememberMe" , "true");

        Cookies cookies =
        given()
                .contentType(ContentType.JSON)
                .body(loginInfos)

                .when()
                .post("/auth/login")

                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies()
                ;

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }
}
