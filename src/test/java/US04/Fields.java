package US04;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Utility.Utility;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Fields extends Utility {
    Faker faker = new Faker();
    String fieldName;
    String fieldCode;
    String fieldID;

    @Test
    public void createField() {
        fieldName = faker.name().name() + faker.number().digits(3);
        fieldCode = faker.number().digits(3);
        Map<String, String> fieldMap = new HashMap<>();

        fieldMap.put("name", fieldName);
        fieldMap.put("code", fieldCode);
        fieldMap.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        fieldMap.put("type", "STRING");

        fieldID =
                given()
                        .spec(reqSpec)
                        .body(fieldMap)

                        .when()
                        .post("/school-service/api/entity-field")

                        .then()
                        .log().body()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .body("name", equalTo(fieldName))
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createField")
    public void createFieldNegative() {

        Map<String, String> fieldMap2 = new HashMap<>();
        fieldMap2.put("name", fieldName);
        fieldMap2.put("code", fieldCode);
        fieldMap2.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        fieldMap2.put("type", "STRING");

        given()
                .spec(reqSpec)
                .body(fieldMap2)

                .when()
                .post("/school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exist"))
        ;
    }

    @Test(dependsOnMethods = "createFieldNegative")
    public void getField() {
        given()
                .spec(reqSpec)
                .pathParam("fieldID", fieldID)
                .when()
                .get("/school-service/api/entity-field/{fieldID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(fieldID));

    }

    @Test(dependsOnMethods = "getField")
    public void putField() {
        String updatedName = faker.name().title() + faker.country().capital();
        Map<String, String> fieldMap3 = new HashMap<>();

        fieldMap3.put("id", fieldID);
        fieldMap3.put("name", updatedName);
        fieldMap3.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        fieldMap3.put("type", "STRING");

        given()
                .spec(reqSpec)
                .body(fieldMap3)

                .when()
                .put("/school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updatedName))
        ;
    }

    @Test(dependsOnMethods = "putField")
    public void deleteField() {

        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("/school-service/api/entity-field/" + fieldID)

                .then()
                .log().body()
                .statusCode(204) // should be 200 but responses 204 after delete
        ;
    }

    @Test(dependsOnMethods = "deleteField")
    public void getFieldNegative(){

        given()
                .spec(reqSpec)
                .pathParam("fieldID",fieldID)

                .when()
                .get("/school-service/api/entity-field/{fieldID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message" , containsString("Can't find"));
    }

    @Test(dependsOnMethods = "getFieldNegative")
    public void deleteFieldNegative(){

        given()
                .spec(reqSpec)
                .pathParam("fieldID" , fieldID)

                .when()
                .delete("/school-service/api/entity-field/{fieldID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message" , containsString("not found"))
                ;
    }
}
