package wat.projectsi.client.model;

public class Violation {
    private String content;
    private String description;

    public Violation(String content, String description) {
        this.content = content;
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }
}
