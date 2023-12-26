package views;

import java.util.UUID;

public class RecipeResource {
    Long id;
    String name;

    public RecipeResource(String recipeName) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.name = recipeName;
    }

    public Long getId() {
        return id;
    }

    /*
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
}
