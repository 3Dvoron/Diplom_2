import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.UserAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.BurgerRestClient;
import util.GenerateRandomData;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UpdateUserInformationParamTestAPI {
    private String userToken;
    UserAPI userAPI = new UserAPI();
    private String newEmail;
    private String newPassword;
    private String newName;
    GenerateRandomData randomData = new GenerateRandomData();
    private String email = randomData.getRandomEmail();
    private String password = randomData.getRandomPassword();
    private String name = randomData.getRandomName();

    public UpdateUserInformationParamTestAPI(String newEmail, String newPassword, String newName) {
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
        ValidatableResponse response = userAPI.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
    }

    @Test
    @Description("Этот тест проверяет что можно поменять данные пользователя с авторизацией")
    public void updateUserInformationAuthorization() {
        ValidatableResponse updateResponse = userAPI.updateUser(newEmail, newPassword, newName, userToken);
        int statusCode = updateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что нельзя поменять данные пользователя без авторизации")
    public void updateUserInformationNonAuthorization() {
        userToken = "";
        ValidatableResponse updateResponse = userAPI.updateUser(newEmail, newPassword, newName, userToken);
        int statusCode = updateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_UNAUTHORIZED, statusCode);
    }

    @After
    public void clearData() {
        userAPI.delete(userToken);
    }
}
