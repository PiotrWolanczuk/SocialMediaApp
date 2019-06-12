package wat.projectsi.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Image implements Serializable {
    @SerializedName("pictureId")
    private long id;
    @SerializedName("hashCode")
    private String url;
    //    @SerializedName("tags")
    private List<String> tags;

    public Image(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
