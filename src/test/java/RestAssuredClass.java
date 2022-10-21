import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static  io.restassured.response.Response.*; //se ha hecho un import no static en las lineas 5 y 6
import static io.restassured.path.json.JsonPath.from;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestAssuredClass extends BaseTest {
    //extends significa que RestAssuredClass hereda de BaseTest
    //movemos el BeforeAll a la clase  BaseTest


    @Test
    public void LoginRequest() {
                given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());

        //System.out.printIn(response);

    }

    @Test
    public void getSingleUserTest() {
                given()
                .get("users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2));
    }

    @Test
    public void deleteUserTest(){
        given()
                .delete("users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    //patch modifica un usuario existente sin mandar el objeto entero
    //put modifica pero manda el objeto entero para reemplazar
    @Test
    public void patchUserTest(){
        String nameUpdated = given()
                .when() //pone condiciones al then. Opcional
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().
                jsonPath().getString("name");

                assertThat(nameUpdated, equalTo("morpheus"));

    }

    @Test
    public void putUserTest(){
        String jobUpdated = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().
                jsonPath().getString("job");

        assertThat(jobUpdated, equalTo("zion resident"));
    }

    @Test
    public void getAllUsersTest(){
        Response response = given().get("users?page=2");
        //respuesta almacenada

        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        System.out.println(headers);
        System.out.println((statusCode));
        System.out.println((body));
        System.out.println(contentType);

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void getUsersTest(){
        String response = given().when().get("users?page=2").then().extract().body().asString();

        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirstUser = from(response).get("data[0].id");

        System.out.println("page" + page);
        System.out.println("total pages" + totalPages);
        System.out.println("id First User" + idFirstUser);

        //filtro para los ids mayores de 10
        List<Map> idsMayoresa10 = from(response).get("data.findAll { user  -> user.id > 10}");
        //filtro mayores a 10 y un apellido concreto
        List<Map> usuario = from(response).get("data.findAll { user  -> user.id > 10 && user.last_name == 'Howell'}");

        String email = usuario.get(0).get("email").toString();

        System.out.println("Email del primer usuario de id mayor a 10 " +email);
    }

    @Test //ejercicio
    public void getArrayElementsTest(){
        String response = given().when().get("users?page=2").then().extract().body().asString();

        //String usersList = from(response).get("data.length");
        List<Map> listaUsuarios = from(response).get("data.findAll");
        int longitud = listaUsuarios.size();
        System.out.println("total usuarios " + longitud);

        assertThat(longitud, equalTo(6));
    }

    @Test
    public void createUserTest(){
        CreateRequest user = new CreateRequest();
        user.setName("morpheus");
        user.setJob("leader");

        CreateResponse response = given()
                .when()
                .body(user)
                .post("users")
                .then()
                .extract()
                .body()
                .as(CreateResponse.class);

        assertThat(response.getJob(), equalTo("leader"));
    }

    @Test
    public void RegisterUserTest(){
        RegisterRequest regis = new RegisterRequest();
        regis.setEmail("eve.holt@reqres.in");
        regis.setPassword("pistol");

        RegisterResponse response = given()
                .when()
                .body(regis)
                .post("register")
                .then()
                .extract()
                .body()
                .as(RegisterResponse.class);

        assertThat(response.getId(), equalTo(4));
        assertThat(response.getToken(), equalTo("QpwL5tke4Pnpja7X4"));
    }


}
