package US08;

import Utility.Utility;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Departments extends Utility {
    String userID;
    Faker faker = new Faker();
    String departmentName;
    String departmentCode;
    @Test
    public void addSchoolDepartment(){
        departmentName = faker.name().fullName();
        departmentCode = faker.code().asin();
        Map<String,String> keys = new HashMap<>();
        keys.put("name",departmentName);
        keys.put("code",departmentCode);
        keys.put("school","6390f3207a3bcb6a7ac977f9");

        userID =
                given()
                        .spec(reqSpec)
                        .body(keys)
                        .when()
                        .post("/school-service/api/department")

                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "addSchoolDepartment")
    public void addSchoolDepartmentNegative(){
        Map<String,String> keys = new HashMap<>();
        keys.put("name",departmentName);
        keys.put("code",departmentCode);
        keys.put("school","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .body(keys)
                .when()
                .post("/school-service/api/department")

                .then()
                .statusCode(400)
                .log().body()
        ;
    }
    @Test(dependsOnMethods = "addSchoolDepartmentNegative")
    public void editSchoolDepartment(){
        departmentName = faker.name().fullName();
        departmentCode = faker.code().asin();
        Map<String,String> keys = new HashMap<>();
        keys.put("id",userID);
        keys.put("name",departmentName);
        keys.put("school","6390f3207a3bcb6a7ac977f9");
        keys.put("code",departmentCode);

        given()
                .spec(reqSpec)
                .body(keys)

                .when()
                .put("/school-service/api/department")
                .then()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "editSchoolDepartment" )
    public void deleteSchoolDepartment(){
        given()
                .spec(reqSpec)
                .when()
                .delete("https://test.mersys.io/school-service/api/department/"+userID)
                .then()
                .statusCode(204)
        ;
    }
    @Test(dependsOnMethods = "deleteSchoolDepartment" )
    public void deleteSchoolDepartmentNegative(){
        given()
                .spec(reqSpec)
                .when()
                .delete("https://test.mersys.io/school-service/api/department/"+userID)
                .then()
                .statusCode(204)
        ;
    }
}
