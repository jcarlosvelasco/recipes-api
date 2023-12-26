package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.RecipeModel;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import views.RecipeResource;

import java.util.ArrayList;
import java.util.List;
//import java.util.Objects;

import play.twirl.api.Xml;

import javax.inject.Inject;


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

    /*
    public Result index() {
        return ok(views.html.index.render());
    }
    */

    List<RecipeResource> recipeResList = new ArrayList<>();

    @Inject
    FormFactory formFactory;

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

    public Result getRecipe(Long recipeID, Http.Request req) {
        RecipeModel recipeModel = RecipeModel.findByID(recipeID);
        if (recipeModel == null) {
            return Results.notFound();
        }

        RecipeResource recipeRes = new RecipeResource(recipeModel);

        return returnOkResult(req, recipeRes);
    }

    public Result updateRecipeName(Long recipeID, String newRecipeName, Http.Request req) {
        int index = searchRecipeIndexByID(recipeID);

        if (index == -1) {
            return notFound();
        }

        recipeResList.get(index).setName(newRecipeName);
        return returnOkResult(req, recipeResList.get(index));
    }

    public Result createRecipe(Http.Request req) {
        Form<RecipeResource> form = formFactory.form(RecipeResource.class).bindFromRequest(req);

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RecipeResource recipeRes = form.get();

        RecipeModel recipeModel = recipeRes.toModel();
        recipeModel.save();

        return Results.created(recipeRes.toJson()).as("application/json");
    }

    public Result deleteRecipe(Long recipeID, Http.Request req) {
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

    private int searchRecipeIndexByID(Long recipeID) {
        /*
        for (int i = 0; i < recipeResList.size(); i++) {
            if (Objects.equals(recipeResList.get(i).getId(), recipeID)) {
                return i;
            }
        }*/

        return -1;
    }
}
