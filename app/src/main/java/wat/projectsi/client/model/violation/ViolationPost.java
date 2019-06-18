package wat.projectsi.client.model.violation;

public class ViolationPost {
    private String content;
    private String description;
    private long postId;

    public ViolationPost(String content, String description,  long postId) {
        this.content = content;
        this.description = description;
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public long getPostId() {
        return postId;
    }
}