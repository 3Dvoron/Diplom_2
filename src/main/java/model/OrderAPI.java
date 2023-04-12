package model;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import util.BurgerRestClient;

import java.util.HashMap;
import java.util.List;

public class OrderAPI extends BurgerRestClient {

    @Step("Create order")
    public ValidatableResponse createOrder(HashMap<String, List<String>> ingredients) {
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .body(ingredients)
                .when()
                .post(BurgerRestClient.CREATE_ORDER_URI)
                .then();
    }

    @Step("Create order authorization")
    public ValidatableResponse createOrder(HashMap<String, List<String>> ingredients, String token) {
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .body(ingredients)
                .when()
                .post(BurgerRestClient.CREATE_ORDER_URI)
                .then();
    }

    @Step("Get order list")
    public ValidatableResponse getOrderList(String token) {
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .when()
                .get(BurgerRestClient.ORDER_ALL_URI)
                .then();
    }

    @Step("Get ingredients list")
    public ValidatableResponse getIngredientList() {
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .when()
                .get(BurgerRestClient.INGREDIENTS_URI)
                .then();
    }

}