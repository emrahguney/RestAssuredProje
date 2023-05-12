package US11;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Discount extends Utility {

    Faker faker = new Faker();
    String discountID;
    String discountDescription;
    String discountCode;
    String discountPriority;

    @Test
    public void createDiscount() {

        discountDescription = "SDET Bootcamp" + faker.number().numberBetween(1, 1000)
                + faker.country().name();

        discountCode = faker.number().digits(3);
        discountPriority = faker.number().digits(1) + faker.number().numberBetween(1, 100);

        Map<String, String> discMap = new HashMap<>();
        discMap.put("description", discountDescription);
        discMap.put("code", discountCode);
        discMap.put("priority", discountPriority);
        discMap.put("tenantId", "6390ef53f697997914ec20c2");

        discountID =
                given()
                        .spec(reqSpec)
                        .body(discMap)

                        .when()
                        .post("/school-service/api/discounts")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createDiscount")
    public void createDiscountNegative() {

        Map<String, String> discMap2 = new HashMap<>();
        discMap2.put("description", discountDescription);
        discMap2.put("code", discountCode);
        discMap2.put("priority", discountPriority);
        discMap2.put("tenantId", "6390ef53f697997914ec20c2");

        given()
                .spec(reqSpec)
                .body(discMap2)

                .when()
                .post("/school-service/api/discounts")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exist"))
        ;

    }

    @Test(dependsOnMethods = "createDiscountNegative")
    public void getDiscount() {
        given()
                .spec(reqSpec)

                .when()
                .get("/school-service/api/discounts/" + discountID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(discountID))
        ;
    }

    @Test(dependsOnMethods = "getDiscount")
    public void putDiscount() {
        String updatedDescription = "QA Bootcamp" + faker.number().numberBetween(1, 200)
                + " " + faker.country().name();

        Map<String, String> discMap3 = new HashMap<>();
        discMap3.put("id", discountID);
        discMap3.put("description", updatedDescription);
        discMap3.put("code", discountCode);
        discMap3.put("priority", discountPriority);
        discMap3.put("tenantId", "6390ef53f697997914ec20c2");

        given()
                .spec(reqSpec)
                .body(discMap3)

                .when()
                .put("/school-service/api/discounts")

                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(updatedDescription))
        ;
    }

    @Test(dependsOnMethods = "putDiscount")
    public void deleteDiscount(){

        given()
                .spec(reqSpec)
                .pathParam("discountID" , discountID)
                .log().uri()

                .when()
                .delete("/school-service/api/discounts/{discountID}")

                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDiscount")
    public void getDiscountNegative(){

        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .get("/school-service/api/discounts/" + discountID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message" , containsString("Can't find"))
                ;

    }

    @Test(dependsOnMethods = "getDiscountNegative")
    public void deleteDiscountNegative(){

        given()
                .spec(reqSpec)
                .pathParam("discountID" , discountID)

                .when()
                .delete("/school-service/api/discounts/{discountID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message" , containsString("not found"))
                ;
    }
}
