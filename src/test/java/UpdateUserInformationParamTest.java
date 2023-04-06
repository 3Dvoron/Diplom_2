import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.BurgerRestClient;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UpdateUserInformationParamTest extends BurgerRestClient {
    protected String userToken;
    User user = new User();
    private String newEmail;
    private String newPassword;
    private String newName;
    private String email = getRandomEmail();
    private String password = getRandomPassword();
    private String name = getRandomName();

    public UpdateUserInformationParamTest(String newEmail, String newPassword, String newName) {
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.newName = newName;
    }

    @Parameterized.Parameters()
    public static Object[][] data() {
        return new Object[][]{
                {"voro@sjl.com", "qwerty", "alex"},
                {"qwert@mail.ru", "asdfgh", "alex"},
                {"afdssdsd@mail.ru", "qwerty", "lex"},
        };
    }

    @Before
    public void setUp() {
        ValidatableResponse response = user.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
    }

    @Test
    @Description("Этот тест проверяет что можно поменять данные пользователя с авторизацией")
    public void updateUserInformationAuthorization() {
        ValidatableResponse updateResponse = user.updateUser(newEmail, newPassword, newName, userToken);
        int statusCode = updateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что нельзя поменять данные пользователя без авторизации")
    public void updateUserInformationNonAuthorization() {
        userToken = "";
        ValidatableResponse updateResponse = user.updateUser(newEmail, newPassword, newName, userToken);
        int statusCode = updateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
    }

    @After
    public void clearData() {
        user.delete(userToken);
    }
}
