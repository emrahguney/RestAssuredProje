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


    @Test
    public void createDocumentType(){

        documentName = faker.name().fullName().toUpperCase();
        List <String> attachmentStages = new ArrayList<>();
        attachmentStages.add("EXAMINATION");


        Map<String, Object> keys = new HashMap<>();
        keys.put("name",documentName);
        keys.put("attachmentStages",attachmentStages);
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");
        System.out.println(attachmentStages);

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
        keys.put("attachmentStages","EXAMINATION");
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(requestSpecification)
                .body(keys)

                .when()
                .post("/school-service/api/attachments/create")

                .then()
                .statusCode(400)
                .log().body()
                ;


    }



}
