import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.UserAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.BurgerRestClient;
import util.GenerateRandomData;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.*;

public class AuthorizationTest {

    private String userToken;
    UserAPI userAPI = new UserAPI();
    GenerateRandomData randomData = new GenerateRandomData();
    private String email = randomData.getRandomEmail();
    private String password = randomData.getRandomPassword();
    private String name = randomData.getRandomName();

    @Before
    public void createUser() {
        ValidatableResponse response = userAPI.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
    }

    @Test
    @Description("Этот тест проверяет что можно авторизоваться")
    public void authorizationTest() {
        ValidatableResponse loginResponse = userAPI.loginUser(email, password);
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("UserAPI is not authorization", isUserAuthorization);
    }

    @Test
    @Description("Этот тест проверяет что нельзя авторизоваться с неправильным логином")
    public void wrongLoginTest() {
        ValidatableResponse loginResponse = userAPI.loginUser("qweqweqwe@qweqwe.ru", password);
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
        assertFalse("UserAPI authorization", isUserAuthorization);
    }

    @Test
    @Description("Этот тест проверяет что нельзя авторизоваться с неправильным паролем")
    public void wrongPasswordTest() {
        ValidatableResponse loginResponse = userAPI.loginUser(email, "qweqweqwe");
        boolean isUserAuthorization = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
        assertFalse("UserAPI authorization", isUserAuthorization);
    }

    @After
    public void clearData() {
        userAPI.delete(userToken);
    }
}
