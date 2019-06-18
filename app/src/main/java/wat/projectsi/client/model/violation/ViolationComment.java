package wat.projectsi.client.model.violation;

public class ViolationComment {
    private String content;
    private String description;

    public ViolationComment(String content, String description) {
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
