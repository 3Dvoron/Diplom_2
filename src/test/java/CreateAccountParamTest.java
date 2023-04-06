import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.BurgerRestClient;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class CreateAccountParamTest extends BurgerRestClient {
    User user = new User();
    private String email;
    private String password;
    private String name;

    public CreateAccountParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters()
    public static Object[][] data() {
        return new Object[][]{
                {"", "qwerty", "alex"},
                {"qwerty@mail.ru", "", "anna"},
                {"asdf@mail.ru", "qwerty", ""}
        };
    }

    @Test
    @Description("Этот тест проверяет что можно создать пользователя проверяет статус код и ответ")
    public void createUser() {
        ValidatableResponse createResponse = user.createUser(email, password, name);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        assertEquals("Status code is incorrect", HTTP_FORBIDDEN, statusCode);
        assertFalse("User is not created", isUserCreated);
    }
}
