package util;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BurgerRestClient {

    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";
    protected static final String CREATE_USER_URI = BASE_URI + "auth/register";
    protected static final String DELETE_USER_URI = BASE_URI + "auth/user";
    protected static final String AUTHORIZATION_URI = BASE_URI + "auth/login";
    protected static final String CREATE_ORDER_URI = BASE_URI + "orders";
    protected static final String ORDER_ALL_URI = CREATE_ORDER_URI + "/all";
    protected static final String INGREDIENTS_URI = BASE_URI + "ingredients";


    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }


}
