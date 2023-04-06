import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.BurgerRestClient;

import java.util.HashMap;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderTest extends BurgerRestClient {

    public String userToken;
    protected List<String> ingredients;
    User user = new User();
    Order order = new Order();
    private String email = getRandomEmail();
    private String password = getRandomPassword();
    private String name = getRandomName();
    private HashMap<String, List<String>> ingredientMap = new HashMap<>();

    @Before
    public void setUp() {
        ValidatableResponse response = user.createUser(email, password, name);
        userToken = response.extract().path("accessToken");
        ValidatableResponse ingredientsResponse = order.getIngredientList();
        ingredients = ingredientsResponse.extract().path("data._id");
        ingredientMap.put("ingredients", ingredients);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список ингридиентов")
    public void getIngredientsList() {
        ValidatableResponse response = order.getIngredientList();
        boolean ingredientList = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue(ingredientList);
    }

    @Test
    @Description("Этот тест проверяет что возможно созать заказ без авторизации")
    public void createOrderNonAuthorization() {
        ValidatableResponse response = order.createOrder(ingredientMap);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue(orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что возможно созать заказ с авторизацией")
    public void createOrderAuthorization() {
        ValidatableResponse response = order.createOrder(ingredientMap, userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue(orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что если передать неправильных хеш ингридиента, то мы получим ошибку")
    public void createOrderWrongHashIngredients() {
        ingredientMap.clear();
        ingredients.add("61c0c5a71d1f82001bdaaa6");
        ingredientMap.put("ingredients", ingredients);
        ValidatableResponse response = order.createOrder(ingredientMap);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_INTERNAL_ERROR, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что если мы не передадим ингридименты, то получим ошибку")
    public void createOrderNullIngredients() {
        ingredientMap.clear();
        ValidatableResponse response = order.createOrder(ingredientMap);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_BAD_REQUEST, statusCode);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список заказов с авторизацией")
    public void getOrderListAuthorization() {
        ValidatableResponse response = order.getOrderList(userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue(orderStatus);
    }

    @Test
    @Description("Этот тест проверяет что можно получить список заказов без авторизации")
    public void getOrderListNonAuthorization() {
        userToken = "";
        ValidatableResponse response = order.getOrderList(userToken);
        boolean orderStatus = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", HTTP_OK, statusCode);
        assertTrue(orderStatus);
    }

    @After
    public void clearData() {
        user.delete(userToken);
        ingredientMap.clear();
    }

}
