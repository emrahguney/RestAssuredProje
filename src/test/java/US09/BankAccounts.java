package US09;

import Utility.Utility;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import Utility.Utility;
import com.github.javafaker.Faker;
import com.sun.org.apache.bcel.internal.generic.Select;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccounts extends Utility {

    Faker faker = new Faker();
    String name;
    String iban;
    String id;


    @Test
    public void createBankAccountTest() {
        Map<String, String> bankAccountData = new HashMap<>();
        name = faker.name().fullName();
        iban = faker.finance().iban();
        bankAccountData.put("name", name);
        bankAccountData.put("iban", iban);
        bankAccountData.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccountData.put("currency", "EUR");


        id =
                given()
                        .spec(reqSpec)
                        .body(bankAccountData)
                        .log().body()

                        .when()
                        .post("/school-service/api/bank-accounts")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("id = " + id);


    }

    @Test(dependsOnMethods = "createBankAccountTest")
    public void createBankAccountNegativeTest() {
        Map<String, String> bankAccountData = new HashMap<>();
        //name = faker.name().fullName();
        bankAccountData.put("name", name);
        bankAccountData.put("iban", iban);
        bankAccountData.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccountData.put("currency", "EUR");


        given()
                .spec(reqSpec)
                .body(bankAccountData)
                .log().body()

                .when()
                .post("/school-service/api/bank-accounts")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;


    }

    @Test(dependsOnMethods = "createBankAccountNegativeTest")
    public void createBankAccountEditTest() {
        Map<String, String> bankAccountData = new HashMap<>();
        bankAccountData.put("id", id);
        //name = faker.name().fullName();
        bankAccountData.put("name", name + faker.number().digits(4));
        bankAccountData.put("iban", iban);
        bankAccountData.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccountData.put("currency", "EUR");


        given()
                .spec(reqSpec)
                .body(bankAccountData)
                .log().body()

                .when()
                .put("/school-service/api/bank-accounts")

                .then()
                .log().body()
                .statusCode(200)
                .body("iban", equalTo(iban))
        ;

    }

    @Test(dependsOnMethods = "createBankAccountEditTest")
    public void createBankAccountDelete() {
        given()
                .spec(reqSpec)
                .pathParam("id", id)
                .log().uri()

                .when()
                .delete("/school-service/api/bank-accounts/{id}")

                .then()
                .log().body()
                .statusCode(200)
        ;


    }

    @Test(dependsOnMethods = "createBankAccountDelete")
    public void createBankAccountDeleteNegative() {
        given()
                .spec(reqSpec)
                .pathParam("id", id)
                .log().uri()

                .when()
                .delete("/school-service/api/bank-accounts/{id}")

                .then()
                .log().body()
                .statusCode(400)
        ;


    }
}


