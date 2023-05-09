package US01;

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


public class PositionCategories extends Utility {

    Faker faker = new Faker();

    String positionName;
    String positionID;



    @Test
    public void createPosition(){

        Map<String,String> position=new HashMap<>();

        positionName=faker.name().username()+faker.number().digits(2);

        position.put("name",positionName);

        positionID=  given()
                .spec(reqSpec)
                .body(position)
                .log().body()
                .when()
                .post("school-service/api/position-category")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;

        System.out.println("positionID = " + positionID);


    }
    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative(){

        Map<String,String> position=new HashMap<>();



        position.put("name",positionName);

        given()
                .spec(reqSpec)
                .body(position)
                .log().body()
                .when()
                .post("school-service/api/position-category")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsString("already"))
                .extract().path("id")

        ;

    }



    @Test(dependsOnMethods = "createPositionNegative")
    public void updatePosition(){
        positionName=faker.name().username()+faker.number().digits(3);
        Map<String,String> positionUpdate=new HashMap<>();
        positionUpdate.put("name",positionName);
        positionUpdate.put("id",positionID);


        given()
                .spec(reqSpec)
                .body(positionUpdate)
                .when()
                .put("school-service/api/position-category/")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(positionName))


        ;

    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition(){

        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .delete("school-service/api/position-category/"+positionID)
                .then()
                .log().body()
                .statusCode(204)
        ;

    }
    @Test(dependsOnMethods = "deletePosition")
    public void deletePositionNegative(){

        given()
                .spec(reqSpec)
                .log().uri()
                .when()
                .delete("school-service/api/position-category/"+positionID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("PositionCategory not  found"))
        ;

    }
}
