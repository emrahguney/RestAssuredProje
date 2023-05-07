package US05;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Positions extends Utility {

    Faker faker = new Faker();
    String positionName;
    String positionID;
    String shortName;

    @Test
    public void createPosition () {

        Map<String, String> positionData=new HashMap<>();
        positionName=faker.address().cityName()+faker.number().digits(2);
        shortName=faker.address().cityName()+faker.number().digits(2);
        positionData.put("name", positionName);
        positionData.put("shortName", shortName);
        positionData.put("tenantId", "6390ef53f697997914ec20c2");

        positionID=
        given()
                .spec(reqSpec)
                .body(positionData)
                .log().body()

                .when()
                .post("/school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");

        System.out.println("positionID = " + positionID);

    }

    @Test (dependsOnMethods ="createPosition" )
    public void createPositionNegative () {
        Map<String, String> positionData=new HashMap<>();
       // positionName=faker.address().cityName()+faker.number().digits(2);
        positionData.put("name", positionName);
        positionData.put("shortName",shortName);
        positionData.put("tenantId", "6390ef53f697997914ec20c2");

                given()
                        .spec(reqSpec)
                        .body(positionData)
                        .log().body()

                        .when()
                        .post("/school-service/api/employee-position")

                        .then()
                       // .log().body()
                        .statusCode(400)
                        .body("message", containsString("already exists"));


    }

    @Test (dependsOnMethods = "createPositionNegative")
    public void EditPosition () {
        Map<String, String> positionData=new HashMap<>();
        positionData.put("id", positionID);

        positionData.put("name", positionName+faker.name().username());
        positionData.put("shortName", shortName);
        positionData.put("tenantId", "6390ef53f697997914ec20c2");

        given()
                .spec(reqSpec)
                .body(positionData)
                .when()
                .put("/school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("shortName", equalTo(shortName));


    }


    @Test (dependsOnMethods = "EditPosition")
    public void DeletePosition () {
        given()
                .spec(reqSpec)
                .pathParam("positionID", positionID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204)
        ;


    }

    @Test (dependsOnMethods = "DeletePosition")
    public void DeletePositionNegative () {
        given()
                .spec(reqSpec)
                .pathParam("positionID", positionID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204)
               // .body("message",equalTo("Position not found"))
        ;


    }
}
