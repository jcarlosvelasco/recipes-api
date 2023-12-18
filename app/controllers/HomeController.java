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
        System.out.println("Listar recetas");

        JsonNode json = Json.toJson(recipeResList);

        return ok(json);
    }

    public Result getRecipe(String recipeID) {
        return ok();
    }

    public Result updateRecipe(String recipeID) {
        return ok();
    }

    public Result createRecipe() {
        RecipeResource recipeRes = new RecipeResource();
        recipeRes.setName("Pollo frito");

        recipeResList.add(recipeRes);

        JsonNode json = Json.toJson(recipeRes);

        return created(json);
    }

    public Result deleteRecipe(String recipeID) {
        int index = -1;

        for (int i = 0; i < recipeResList.size(); i++) {
            if (Objects.equals(recipeResList.get(i).getId(), recipeID)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return notFound();
        }

        recipeResList.remove(index);
        return ok();
    }
}
