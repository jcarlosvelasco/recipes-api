package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.*;
import views.RecipeResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import play.twirl.api.Xml;


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

    public Result listRecipes(Http.Request req) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        if (req.accepts("application/json")) {
            JsonNode json = Json.toJson(recipeResList);
            return ok(json);
        }

        if (req.accepts("application/xml")) {
            Xml listXml = views.xml._recipes.render(recipeResList);
            Xml content = views.xml._header.render(listXml);
            return ok(content);
        }

        return Results.notAcceptable();
    }

    public Result getRecipe(String recipeID, Http.Request req) {
        for (RecipeResource recipeResource : recipeResList) {
            if (Objects.equals(recipeResource.getId(), recipeID)) {
                return returnOkResult(req, recipeResource);
            }
        }

        return notFound();
    }

    public Result updateRecipeName(String recipeID, String newRecipeName, Http.Request req) {
        int index = searchRecipeIndexByID(recipeID);

        if (index == -1) {
            return notFound();
        }

        recipeResList.get(index).setName(newRecipeName);
        return returnOkResult(req, recipeResList.get(index));
    }

    public Result createRecipe(Http.Request req) {
        RecipeResource recipeRes = new RecipeResource("Pollo frito");
        recipeResList.add(recipeRes);

        return returnCreatedResult(req, recipeRes);
    }

    public Result deleteRecipe(String recipeID, Http.Request req) {
        int index = searchRecipeIndexByID(recipeID);

        if (index == -1) {
            return notFound();
        }

        RecipeResource recipeRes = recipeResList.remove(index);

        return returnOkResult(req, recipeRes);
    }

    private Result returnOkResult(Http.Request req, RecipeResource recipeRes) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        if (req.accepts("application/json")) {
            JsonNode json = Json.toJson(recipeRes);
            return ok(json);
        }

        if (req.accepts("application/xml")) {
            Xml content = views.xml._recipe.render(recipeRes);
            Xml headerContent = views.xml._header.render(content);
            return ok(headerContent);
        }

        return Results.notAcceptable();
    }

    private Result returnCreatedResult(Http.Request req, RecipeResource recipeRes) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        if (req.accepts("application/json")) {
            JsonNode json = Json.toJson(recipeRes);
            return created(json);
        }

        if (req.accepts("application/xml")) {
            Xml content = views.xml._recipe.render(recipeRes);
            Xml headerContent = views.xml._header.render(content);
            return created(headerContent);
        }

        return Results.notAcceptable();
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
