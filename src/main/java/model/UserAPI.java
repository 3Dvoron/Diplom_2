package model;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import pojo.UserField;
import util.BurgerRestClient;

public class UserAPI extends BurgerRestClient {

    @Step("Create user")
    public ValidatableResponse createUser(String email, String password, String name) {
        UserField userField = new UserField(email, password, name);
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .body(userField)
                .when()
                .post(BurgerRestClient.CREATE_USER_URI)
                .then();
    }

    @Step("Authorization")
    public ValidatableResponse loginUser(String email, String password) {
        UserField userField = new UserField(email, password);
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .body(userField)
                .when()
                .post(BurgerRestClient.AUTHORIZATION_URI)
                .then();
    }

    @Step("Update user information")
    public ValidatableResponse updateUser(String email, String password, String name, String token) {
        UserField userField = new UserField(email, password, name);
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .body(userField)
                .when()
                .patch(BurgerRestClient.DELETE_USER_URI)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String token) {
        return RestAssured.given()
                .spec(getBaseReqSpec())
                .header("authorization", token)
                .when()
                .delete(BurgerRestClient.DELETE_USER_URI)
                .then();
    }
}
