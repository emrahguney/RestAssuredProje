package US12;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Nationalities extends Utility {

    Faker faker = new Faker();
    String nationalID;
    String nationalName;


    @Test
    public void createNationalitiesTest() {

        Map<String, String> nationalitiData = new HashMap<>();
        nationalName = faker.name().fullName();
        nationalitiData.put("name", nationalName);

        nationalID =
                given()
                        .spec(reqSpec)
                        .body(nationalitiData)
                        .log().body()
                        .when()
                        .post("/school-service/api/nationality")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

    }

    @Test(dependsOnMethods = "createNationalitiesTest")
    public void createNationalitiesTestNegative() {

        Map<String, String> nationalitiData = new HashMap<>();
        nationalitiData.put("name", nationalName);


        given()
                .spec(reqSpec)
                .body(nationalitiData)
                .log().body()
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));

    }

    @Test(dependsOnMethods = "createNationalitiesTestNegative")
    public void updateNationalitiesTest() {

        Map<String, String> nationalitiData = new HashMap<>();
        nationalName = "MR_" + faker.number().digits(7);
        nationalitiData.put("id", nationalID);
        nationalitiData.put("name", nationalName);


        given()
                .spec(reqSpec)
                .body(nationalitiData)
                .log().body()
                .when()
                .put("/school-service/api/nationality/")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(nationalName));

    }

    @Test(dependsOnMethods = "updateNationalitiesTest")
    public void deleteNationalitiesTest() {


        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("/school-service/api/nationality/" + nationalID)

                .then()
                .log().body()
                .statusCode(200)
        ;

    }

    @Test(dependsOnMethods = "deleteNationalitiesTest")
    public void deleteNationalitiesTestNegative() {


        given()
                .spec(reqSpec)
                .pathParam("nationalID", nationalID)
                .log().uri()

                .when()
                .delete("/school-service/api/nationality/{nationalID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Nationality not  found"))
        ;

    }
}


