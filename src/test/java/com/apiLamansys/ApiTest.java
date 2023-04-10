package com.apiLamansys;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;



public class ApiTest {


    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://api.instantwebtools.net";
        RestAssured.basePath = "/v1";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void consultAirlinesTest(){

        given()
                .get("airlines/11100001")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(11100001));
    }

    @Test
    public void agregarSkuTest(){

        given()
                .body("{\n" +
                        "    \"id\": 11100001,\n" +
                        "    \"name\": \"Sri Lankan Airways\",\n" +
                        "    \"country\": \"Sri Lanka\",\n" +
                        "    \"logo\": \"https://www.aviacionnews.com/wp-content/uploads/2022/02/alas-la-rioja-lineas-.jpg\",\n" +
                        "    \"slogan\": \"From La Rioja\",\n" +
                        "    \"head_quaters\": \"Chilecito, Alas\",\n" +
                        "    \"website\": \"https://alaslarioja.com.ar/\",\n" +
                        "    \"established\": \"2022\"\n" +
                        "}")
                .post("https://api.instantwebtools.net/v1/airlines")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void  createPassenger(){

        given()
                .body("{\n" +
                        "    \"name\": \"Walter Verdud\",\n" +
                        "    \"trips\": 2464,\n" +
                        "    \"airline\": 7\n" +
                        "}")
                .post("passenger")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void consultPassengerTest(){

        given()
                .get("passenger/642740404b031e04df19e98d")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void deletePassengerTest(){

        given()
                .delete("passenger/642740404b031e04df19e98d")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void patchPassengerTest(){

        String nameUpdated = given()
                .when()
                .body("{\n" +
                        "\t\"name\": \"Felipe Verdud\"\n" +
                        "}")
                .patch("passenger/642740404b031e04df19e98d")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("name");

        assertThat(nameUpdated, equalTo("Felipe Verdud"));
    }

    @Test
    public void putPassengerTest(){

        given()
                .when()
                .body("{\n" +
                        "\t\"name\": \"Felipe Verdud\",\n" +
                        "\t\"job\": \"I a Baby and not Work\"\n" +
                        "}")
                .patch("passenger/642740404b031e04df19e98d")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("I a Baby and not Work");

    }

    @Test
    void getAllPokemon(){
        Response response = given()
                .get("https://reqres.in/api/users?page=2");
        Headers headers = response.getHeaders();
        int statusCode = response.statusCode();
        String body = response.getBody().asString();
        String contenType = response.getContentType();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("body: " + body);
        System.out.println("content type: " + contenType);
        System.out.println("headers: " + headers);

        System.out.println("--------------------------------------");

        System.out.println(headers.get("Content-Type"));
        System.out.println(headers.get("Transfer-Encoding"));

    }

    @Test
    public void getAllUser() {
        String response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract()
                .body()
                .asString();

        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");

        int idUser = from(response).get("data[3].id");

        System.out.println("Page: " + page );
        System.out.println("Total pages: " + totalPages);
        System.out.println("Id User: " + idUser);

        List<Map> userWithIdGreaterThan10 = from(response).get("data.findAll {user -> user.id >10 }");
        String email = userWithIdGreaterThan10.get(0).get("email").toString();

        List<Map> user = from(response).get("data.findAll {user -> user.id >10 && user.lastName = 'Howell'}");
        int id = Integer.valueOf(user.get(8).get("id").toString());

    }


}
