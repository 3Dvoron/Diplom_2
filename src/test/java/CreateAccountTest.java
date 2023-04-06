import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Test;
import util.BurgerRestClient;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

public class CreateAccountTest extends BurgerRestClient {
    public String userToken;
    User user = new User();
    private String email = getRandomEmail();
    private String password = getRandomPassword();
    private String name = getRandomName();

    @Test
    @Description("Этот тест проверяет что можно создать пользователя проверяет статус код и ответ")
    public void createUser() {
        ValidatableResponse createResponse = user.createUser(email, password, name);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        userToken = createResponse.extract().path("accessToken");
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Model.Courier is not created", isUserCreated);
    }

    @Test
    @Description("Этот тест проверяет что невозможно создать два одинаковых аккаунта")
    public void createDuplicateAccount() {
        ValidatableResponse createResponse = user.createUser(email, password, name);
        int statusCode = createResponse.extract().statusCode();
        userToken = createResponse.extract().path("accessToken");
        ValidatableResponse createDuplicate = user.createUser(email, password, name);
        int statusCodeDuplicate = createDuplicate.extract().statusCode();
        boolean message = createDuplicate.extract().path("success");
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertEquals("Status code is incorrect", HTTP_FORBIDDEN, statusCodeDuplicate);
        assertFalse(message);
    }

    @After
    public void clearData() {
        user.delete(userToken);
    }
}