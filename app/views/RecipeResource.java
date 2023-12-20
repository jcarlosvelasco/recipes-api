package views;

import java.util.UUID;

public class RecipeResource {
    String id;
    String name;

    public RecipeResource(String recipeName) {
        this.id = UUID.randomUUID().toString();
        this.name = recipeName;
    }

    public String getId() {
        return id;
    }

    /*
    public void setId(String id) {
        this.id = id;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
