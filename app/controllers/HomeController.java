package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.*;
import views.RecipeResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public Result index() {
        return ok(views.html.index.render());
    }


    List<RecipeResource> recipeResList = new ArrayList<>();

    public Result listRecipes() {
        JsonNode json = Json.toJson(recipeResList);
        return ok(json);
    }

    public Result getRecipe(String recipeID) {
        for (RecipeResource recipeResource : recipeResList) {
            if (Objects.equals(recipeResource.getId(), recipeID)) {
                JsonNode json = Json.toJson(recipeResource);
                return ok(json);
            }
        }

        return notFound();
    }

    public Result updateRecipeName(String recipeID, String newRecipeName) {
        int index = searchRecipeIndexByID(recipeID);

        if (index == -1) {
            return notFound();
        }

        recipeResList.get(index).setName(newRecipeName);
        return ok();
    }

    public Result createRecipe() {
        RecipeResource recipeRes = new RecipeResource("Pollo frito");

        recipeResList.add(recipeRes);

        JsonNode json = Json.toJson(recipeRes);

        return created(json);
    }

    public Result deleteRecipe(String recipeID) {
        int index = searchRecipeIndexByID(recipeID);

        if (index == -1) {
            return notFound();
        }

        recipeResList.remove(index);
        return ok();
    }

    private int searchRecipeIndexByID(String recipeID) {
        for (int i = 0; i < recipeResList.size(); i++) {
            if (Objects.equals(recipeResList.get(i).getId(), recipeID)) {
                return i;
            }
        }

        return -1;
    }
}
