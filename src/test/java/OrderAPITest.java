import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.OrderAPI;
import model.UserAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.BurgerRestClient;
import util.GenerateRandomData;

import java.util.HashMap;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderAPITest {

    private String userToken;
    protected List<String> ingredients;
    UserAPI userAPI = new UserAPI();
    OrderAPI orderAPI = new OrderAPI();
    GenerateRandomData randomData = new GenerateRandomData();
    private String email = randomData.getRandomEmail();
    private String password = randomData.getRandomPassword();
    private String name = randomData.getRandomName();
    private HashMap<String, List<String>> ingredientMap = new HashMap<>();

    @Before
    public void setUp() {
        ValidatableResponse response = userAPI.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
        ValidatableResponse ingredientsResponse = orderAPI.getIngredientList();
        ingredients = ingredientsResponse.extract().path("data._id");
        ingredientMap.put("ingredients", ingredients);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список ингридиентов")
    public void getIngredientsList() {
        ValidatableResponse response = orderAPI.getIngredientList();
        boolean ingredientList = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Success false", ingredientList);
    }

    @Test
    @Description("Этот тест проверяет что возможно созать заказ без авторизации")
    public void createOrderNonAuthorization() {
        ValidatableResponse response = orderAPI.createOrder(ingredientMap);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Success false", orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что возможно созать заказ с авторизацией")
    public void createOrderAuthorization() {
        ValidatableResponse response = orderAPI.createOrder(ingredientMap, userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Success false", orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что если передать неправильных хеш ингридиента, то мы получим ошибку")
    public void createOrderWrongHashIngredients() {
        ingredientMap.clear();
        ingredients.add("61c0c5a71d1f82001bdaaa6");
        ingredientMap.put("ingredients", ingredients);
        ValidatableResponse response = orderAPI.createOrder(ingredientMap);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_INTERNAL_ERROR, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что если мы не передадим ингридименты, то получим ошибку")
    public void createOrderNullIngredients() {
        ingredientMap.clear();
        ValidatableResponse response = orderAPI.createOrder(ingredientMap);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_BAD_REQUEST, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список заказов с авторизацией")
    public void getOrderListAuthorization() {
        ValidatableResponse response = orderAPI.getOrderList(userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Success false", orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список заказов без авторизации")
    public void getOrderListNonAuthorization() {
        userToken = "";
        ValidatableResponse response = orderAPI.getOrderList(userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue("Success false", orderStatus);
    }

    @After
    public void clearData() {
        userAPI.delete(userToken);
        ingredientMap.clear();
    }

}
