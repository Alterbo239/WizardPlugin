public class Pui_widget_typeDTO {

    private Object id;
    private String name;
    private String type;
    private String component;
    private String definition;

    public Pui_widget_typeDTO() {}

    public Pui_widget_typeDTO(
            Object id,
            String name,
            String type,
            String component,
            String definition
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.component = component;
        this.definition = definition;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }


    @Override
    public String toString() {
        return 
            "id=" + getId() + 
        
            "name=" + getName() + 
        
            "type=" + getType() + 
        
            "component=" + getComponent() + 
        
            "definition=" + getDefinition()
        ;
    }
}