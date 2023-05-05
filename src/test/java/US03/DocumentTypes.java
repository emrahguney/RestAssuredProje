package US03;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import Utility.Utility;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentTypes extends Utility {

    String dtID;
    Faker faker = new Faker();
    String documentName;

    List <String> attachmentStages = new ArrayList<>();


    @Test
    public void createDocumentType(){

        documentName = faker.name().fullName().toUpperCase();
        attachmentStages.add("EXAMINATION");


        Map<String, Object> keys = new HashMap<>();
        keys.put("name",documentName);
        keys.put("attachmentStages",attachmentStages);
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");


        dtID = given()
                .spec(reqSpec)
                .body(keys)

                .when()
                .post("/school-service/api/attachments/create")

                .then()
                .statusCode(201)

                .log().body()
                .extract().path("id")
                ;


        System.out.println(dtID);
    }

    @Test(dependsOnMethods = "createDocumentType")
    public void createDocumentTypeNegative(){

        List<String> value = new ArrayList<>();

        Map<String, Object> keys = new HashMap<>();
        keys.put("name",documentName);
        keys.put("attachmentStages",attachmentStages);
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .body(keys)

                .when()
                .post("/school-service/api/attachments/create")

                .then()
                .statusCode(400)
                .log().body()
                ;


    }

    @Test(dependsOnMethods = "createDocumentType")
    public void editDocumentType(){

        documentName=faker.name().fullName().toLowerCase();
        attachmentStages.add("CERTIFICATE");
        Map<String, Object> keys = new HashMap<>();

        keys.put("id",dtID);
        keys.put("name",documentName);
        keys.put("attachmentStages",attachmentStages);
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .body(keys)

                .when()
                .put("/school-service/api/attachments")

                .then()
                .statusCode(200)
                .log().body()
                .body("name",equalTo(documentName));


    }

    @Test(dependsOnMethods = "editDocumentType")
    public void deleteDocumentType(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attachments/"+dtID)

                .then()
                .statusCode(200)
                .log().body();

    }

    @Test(dependsOnMethods = "deleteDocumentType")
    public void deleteDocumentTypeNegative(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attachments/"+dtID)

                .then()
                .statusCode(400)
                .log().body();

    }



}
