import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.requestSpecification;

public abstract class BaseTest {
    //Esto se ejecuta una vez por cada clase de Test que exista en el proyecto

    private static final Logger logger = LogManager.getLogger(RestAssuredClass.class);

    @BeforeAll
    public static void setup() throws FileNotFoundException {

        //Añadimos comentarios para saber donde empieza el lanzamiento y el fin de la configuración
        logger.info("Iniciando configuración");
        requestSpecification = defaultRequestSpecification();
        logger.info("Configuración exitosa");

    }

    private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException {
        //indicamos los ambientes para poder crear variables de ambiente
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());

        return new RequestSpecBuilder()
                .setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
                .addFilters(filters)
                .setContentType(ContentType.JSON).build();
    }

    private RequestSpecification prodRequestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getHost())
                .setContentType(ContentType.JSON).build();
    }

    public ResponseSpecification defaultResponseSpecification(){
    // es poblic porque llama desde nuestro testing
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();
    }

}

/*import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.requestSpecification;
public abstract class BaseTest {
    @BeforeAll
    public static void setup() throws FileNotFoundException {
        requestSpecification = defaultRequestSpecification();
    }

    private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException{
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());
        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api/")
                .addFilters(filters)
                .setContentType(ContentType.JSON).build();
    }

    private RequestSpecification prodRequestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api/")
                .setContentType(ContentType.JSON)
                .build();
    }

    public ResponseSpecification defaultResponseSpecification(){
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();
    }
}*/