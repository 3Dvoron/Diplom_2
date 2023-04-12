import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.UserAPI;
import org.junit.After;
import org.junit.Test;
import util.BurgerRestClient;
import util.GenerateRandomData;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

public class CreateAccountTest {
    private String userToken;
    UserAPI userAPI = new UserAPI();
    GenerateRandomData randomData = new GenerateRandomData();
    private String email = randomData.getRandomEmail();
    private String password = randomData.getRandomPassword();
    private String name = randomData.getRandomName();

    @Test
    @Description("Этот тест проверяет что можно создать пользователя проверяет статус код и ответ")
    public void createUser() {
        ValidatableResponse createResponse = userAPI.createUser(email, password, name);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        userToken = createResponse.extract().path("accessToken");
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("UserAPI is not created", isUserCreated);
    }

    @Test
    @Description("Этот тест проверяет что невозможно создать два одинаковых аккаунта")
    public void createDuplicateAccount() {
        ValidatableResponse createResponse = userAPI.createUser(email, password, name);
        int statusCode = createResponse.extract().statusCode();
        userToken = createResponse.extract().path("accessToken");
        ValidatableResponse createDuplicate = userAPI.createUser(email, password, name);
        int statusCodeDuplicate = createDuplicate.extract().statusCode();
        boolean message = createDuplicate.extract().path("success");
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertEquals("Status code is incorrect", HTTP_FORBIDDEN, statusCodeDuplicate);
        assertFalse(message);
    }

    @After
    public void clearData() {
        userAPI.delete(userToken);
    }
}