package US07;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolLocations extends Utility {
    String locID;
    Faker faker = new Faker();
    String locationName;
    String locationShortName;
    String locationCapacity;

    List<String> attachmentStages = new ArrayList<>();

    @Test
    public void createSchoolLocation() {
        locationName = faker.name().username().toUpperCase();
        locationShortName = faker.name().prefix();
        locationCapacity = faker.number().digit();

        Map<String, Object> list = new HashMap<>();
        list.put("active","true");
        list.put("capacity", locationCapacity);
        list.put("name", locationName);
        list.put("school","6390f3207a3bcb6a7ac977f9");
        list.put("shortName", locationShortName);
        list.put("type","CLASS");
        locID =
                given()
                        .spec(reqSpec)
                        .body(list)

                        .when()
                        .post("/school-service/api/location")
                        .then()
                        .statusCode(201)

                        .log().body()
                        .extract().path("id");
        System.out.println("locID = " + locID);
    }
    @Test(dependsOnMethods = "createSchoolLocation")
    public void createLocationNegative() {

        Map<String, Object> list = new HashMap<>();
        list.put("active","true");
        list.put("capacity", locationCapacity);
        list.put("name", locationName);
        list.put("school","6390f3207a3bcb6a7ac977f9");
        list.put("shortName", locationShortName);
        list.put("type","CLASS");

                given()
                        .spec(reqSpec)
                        .body(list)

                        .when()
                        .post("/school-service/api/location")

                        .then()
                        .statusCode(400)
                        .log().body();
    }
    @Test(dependsOnMethods = "createSchoolLocation")
    public void editLocation(){
        locationName=faker.name().username().toLowerCase();
        Map<String, Object> list = new HashMap<>();
        list.put("id",locID);
        list.put("name", locationName);
        list.put("school","6390f3207a3bcb6a7ac977f9");
        list.put("shortName", locationShortName);
        list.put("type","CLASS");

        given()
                .spec(reqSpec)
                .body(list)

                .when()
                .put("/school-service/api/location")

                .then()
                .statusCode(200)
                .log().body()
                .body("name",equalTo(locationName));

    }
    @Test(dependsOnMethods = "editLocation")
    public void deleteLocations(){
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/location/"+locID)

                .then()
                .statusCode(200)
                .log().body();
    }
    @Test(dependsOnMethods = "deleteLocations")
    public void deleteLocationsNegative(){
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/location/"+locID)

                .then()
                .statusCode(400)
                .log().body();
    }
}
