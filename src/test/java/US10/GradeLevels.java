package US10;

import Utility.Utility;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class GradeLevels extends Utility {

    Faker faker=new Faker();
    String gradeName;
    String shortName;
    String gradeID;


    @Test
    public void createGradeLevel(){

        gradeName=faker.university().name();
        shortName=faker.country().countryCode2()+faker.number().digits(3);
        Map<String, String> grade=new HashMap<>();

        grade.put("name",gradeName);
        grade.put("shortName",shortName);
        grade.put("order","22");
        grade.put("active","true");

        gradeID=  given()
                .spec(reqSpec)
                .body(grade)
                .log().body()
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
        ;

        System.out.println("gradeID = " + gradeID);
    }
    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative(){


        Map<String, String> grade=new HashMap<>();

        grade.put("name",gradeName);
        grade.put("shortName",shortName);
        // grade.put("order","22");
        grade.put("active","true");

        given()
                .spec(reqSpec)
                .body(grade)
                .log().body()
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsString("already"))
        ;


    }

    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void getGradeLevel(){
        given()
                .spec(reqSpec)
                .pathParam("gradeID",gradeID)
                .when()
                .get("school-service/api/grade-levels/{gradeID}")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }


    @Test(dependsOnMethods = "getGradeLevel")
    public void updateGradeLevel(){
        Map<String ,String > gradeUpdate=new HashMap<>();
        gradeName=faker.university().name()+faker.number().digits(2);
        shortName=faker.country().countryCode3()+faker.number().digits(3);

        gradeUpdate.put("id",gradeID);
        gradeUpdate.put("name",gradeName);
        gradeUpdate.put("shortName",shortName);


        given()
                .spec(reqSpec)
                .body(gradeUpdate)
                .when()
                .put("school-service/api/grade-levels/")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(gradeName))
                .body("shortName",equalTo(shortName))
        ;

    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel()
    {
        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .delete("school-service/api/grade-levels/"+gradeID)
                .then()
                .log().body()
                .statusCode(200)
        ;


    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void getGradeLevelNegative(){

        given()
                .spec(reqSpec)
                .when()
                .get("school-service/api/grade-levels/"+gradeID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Can't find Grade Level"))

        ;


    }

    @Test(dependsOnMethods = "getGradeLevelNegative")
    public void deleteGradeLevelNegative()
    {
        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .delete("school-service/api/grade-levels/"+gradeID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Grade Level not found."))
        ;


    }
}
