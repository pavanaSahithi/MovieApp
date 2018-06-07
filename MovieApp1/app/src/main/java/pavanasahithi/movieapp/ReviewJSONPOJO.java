package pavanasahithi.movieapp;

/**
 * Created by Lenovo on 16-05-2018.
 */

public class ReviewJSONPOJO {
    String author;
    String content;
    String id;
    String url;

    ReviewJSONPOJO(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
