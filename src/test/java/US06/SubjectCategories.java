package US06;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SubjectCategories extends Utility {

    Faker faker = new Faker();
    String categoryName;

    String subjectID;

    @Test
    public void createSubjectCategory() {

        Map<String, String> subjectCategory = new HashMap<>();
        categoryName = faker.name().fullName();
        subjectCategory.put("name", categoryName);
        subjectCategory.put("code", faker.number().digits(4));

        subjectID =
                given()
                        .spec(reqSpec)
                        .body(subjectCategory)
                        .log().body()

                        .when()
                        .post("/school-service/api/subject-categories")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("subjectID = " + subjectID);
    }

    @Test(dependsOnMethods = "createSubjectCategory")
    public void createSubjectCategoryNegative() {

        Map<String, String> subjectCategory = new HashMap<>();
        subjectCategory.put("name", categoryName);
        subjectCategory.put("code", faker.number().digits(4));

        given()
                .spec(reqSpec)
                .body(subjectCategory)
                .log().body()

                .when()
                .post("/school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createSubjectCategoryNegative")
    public void editSubjectCategory(){

        Map<String, String> subjectCategory = new HashMap<>();
        subjectCategory.put("id", subjectID);

        categoryName =faker.witcher().character();
        subjectCategory.put("name", categoryName);
        subjectCategory.put("code", faker.number().digits(4));


        given()
                .spec(reqSpec)
                .body(subjectCategory)
                .log().body()

                .when()
                .put("/school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(categoryName));
    }

    @Test(dependsOnMethods = "editSubjectCategory")
    public void deleteSubjectCategory(){

        given()
                .spec(reqSpec)
                .pathParam("subjectID", subjectID)
                .log().uri()

                .when()
                .delete("/school-service/api/subject-categories/{subjectID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteSubjectCategory")
    public void deleteSubjectCategoryNegative(){

        given()
                .spec(reqSpec)
                .pathParam("subjectID", subjectID)
                .log().uri()

                .when()
                .delete("/school-service/api/subject-categories/{subjectID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("SubjectCategory not  found"))
        ;
    }
}







