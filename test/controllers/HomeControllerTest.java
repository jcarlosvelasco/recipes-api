package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.test.WithApplication;
import views.RecipeResource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    /*
    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }*/

    @Test
    public void testGetRecipe() {
        HomeController homeController = new HomeController();

        //Recipe not in list
        Result result = homeController.getRecipe("testID");
        assertEquals(404, result.status());

        RecipeResource recipeRes1 = new RecipeResource("recipe1");
        RecipeResource recipeRes2 = new RecipeResource("recipe2");

        homeController.recipeResList.add(recipeRes1);
        homeController.recipeResList.add(recipeRes2);

        //Happy path -> recipe in list
        result = homeController.getRecipe(recipeRes1.getId());
        assertEquals(200, result.status());
    }

    @Test
    public void testUpdateRecipeName() {
        HomeController homeController = new HomeController();

        //Recipe not in list
        Result result = homeController.updateRecipeName("testID", "newRecipeName");
        assertEquals(404, result.status());

        //Happy path -> recipe in list
        RecipeResource recipeRes1 = new RecipeResource("oldRecipeName");
        homeController.recipeResList.add(recipeRes1);

        result = homeController.updateRecipeName(recipeRes1.getId(), "newRecipeName");
        assertEquals(200, result.status());

        assertEquals("newRecipeName", recipeRes1.getName());
    }

    @Test
    public void testCreateRecipe() {
        HomeController homeController = new HomeController();
        Result result = homeController.createRecipe();

        assertEquals(201, result.status());
        assertEquals(1, homeController.recipeResList.size());
    }

    @Test
    public void testDeleteRecipe() {
        HomeController homeController = new HomeController();

        //Recipe not in list
        Result result = homeController.deleteRecipe("testID");
        assertEquals(404, result.status());

        //Happy path -> recipe in list
        RecipeResource recipeRes1 = new RecipeResource("recipe1");
        homeController.recipeResList.add(recipeRes1);

        result = homeController.deleteRecipe(recipeRes1.getId());
        assertEquals(200, result.status());
        assertEquals(0, homeController.recipeResList.size());
    }
}
