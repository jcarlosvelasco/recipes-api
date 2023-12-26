package views;

import com.fasterxml.jackson.databind.JsonNode;
import models.RecipeModel;
import play.libs.Json;

public class RecipeResource {
    //Long id;
    String name;

    public RecipeResource() {

    }

    public RecipeResource(String recipeName) {
        //this.id = UUID.randomUUID().getMostSignificantBits();
        this.name = recipeName;
    }

    public RecipeResource(RecipeModel recipeModel) {
        super();
        this.name = recipeModel.getName();
    }

    /*
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public JsonNode toJson() {
        return Json.toJson(this);
    }

    public RecipeModel toModel() {
        RecipeModel recipeModel = new RecipeModel();

        recipeModel.setName(this.name);

        return recipeModel;
    }
}
