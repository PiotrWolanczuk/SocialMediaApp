package wat.projectsi.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {

    @SerializedName("getUserDTO")
    private User user;
    @SerializedName("posts")
    private List<Post> posts;

    public Profile(User user, List<Post> postList) {
        this.user=user;
        posts=postList;
    }

    public User getUser() {
        return user;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
