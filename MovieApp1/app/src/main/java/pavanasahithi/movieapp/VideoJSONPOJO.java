package pavanasahithi.movieapp;

/**
 * Created by Lenovo on 16-05-2018.
 */

public class VideoJSONPOJO {
    String id;
    String iso_639_1;
    String iso_3166_1;
    String key;
    String name;
    String site;
    String size;
    String type;

    VideoJSONPOJO(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, String size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
