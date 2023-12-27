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

    @Inject
    FormFactory formFactory;

    public Result listRecipes(Http.Request req) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        List<RecipeModel> recipeModelList = RecipeModel.getRecipeList();

        List<RecipeResource> recipeResList = new ArrayList<>();
        for (RecipeModel recipeModel : recipeModelList) {
            RecipeResource recipeRes = new RecipeResource(recipeModel);
            recipeResList.add(recipeRes);
        }

        return outputOkRecipeList(req, recipeResList);
    }

    public Result getRecipe(Long recipeID, Http.Request req) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        RecipeModel recipeModel = RecipeModel.findByID(recipeID);
        if (recipeModel == null) {
            return Results.notFound();
        }

        RecipeResource recipeRes = new RecipeResource(recipeModel);

        return returnOkRecipe(req, recipeRes);
    }

    public Result updateRecipeName(Long recipeID, String newRecipeName) {
        RecipeModel recipeModel = RecipeModel.findByID(recipeID);
        if (recipeModel == null) {
            return Results.notFound();
        }

        recipeModel.setName(newRecipeName);
        recipeModel.update();

        return Results.ok();
    }

    public Result createRecipe(Http.Request req) {
        if (req.header("Accept").isEmpty()) {
            return Results.notAcceptable();
        }

        Form<RecipeResource> form = formFactory.form(RecipeResource.class).bindFromRequest(req);

        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        }

        RecipeResource recipeRes = form.get();

        RecipeModel recipeModel = recipeRes.toModel();
        recipeModel.save();

        return outputCreatedRecipe(req, recipeRes);
    }

    public Result deleteRecipe(Long recipeID) {
        RecipeModel recipeModel = RecipeModel.findByID(recipeID);
        if (recipeModel == null) {
            return Results.notFound();
        }

        boolean ok = recipeModel.delete();
        return ok ? Results.ok() : Results.internalServerError();
    }

    private Result returnOkRecipe(Http.Request req, RecipeResource recipeRes) {
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

    private Result outputOkRecipeList(Http.Request req, List<RecipeResource> recipeResList) {
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

    private Result outputCreatedRecipe(Http.Request req, RecipeResource recipeRes) {
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
}
