import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.BurgerRestClient;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.*;

public class AuthorizationTest extends BurgerRestClient {

    public String userToken;
    User user = new User();
    private String email = getRandomEmail();
    private String password = getRandomPassword();
    private String name = getRandomName();

    @Before
    public void createUser() {
        ValidatableResponse response = user.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
    }

    @Test
    @Description("Этот тест проверяет что можно авторизоваться")
    public void authorizationTest() {
        ValidatableResponse loginResponse = user.loginUser(email, password);
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("User is not authorization", isUserAuthorization);
    }

    @Test
    @Description("Этот тест проверяет что нельзя авторизоваться с неправильным логином")
    public void wrongLoginTest() {
        ValidatableResponse loginResponse = user.loginUser("qweqweqwe@qweqwe.ru", password);
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
        assertFalse("User authorization", isUserAuthorization);
    }

    @Test
    @Description("Этот тест проверяет что нельзя авторизоваться с неправильным паролем")
    public void wrongPasswordTest() {
        ValidatableResponse loginResponse = user.loginUser(email, "qweqweqwe");
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
        assertFalse("User authorization", isUserAuthorization);
    }

    @After
    public void clearData() {
        user.delete(userToken);
    }
}
