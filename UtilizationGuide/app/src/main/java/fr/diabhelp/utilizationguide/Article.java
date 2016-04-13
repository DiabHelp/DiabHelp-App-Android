package fr.diabhelp.utilizationguide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 10/03/2016.
 */
public class Article {
        String category;
        String title;
        String content;
        int photoId;
        Article(String category, String title, String content, int photoId) {
            this.category = category;
            this.title = title;
            this.content = content;
            this.photoId = photoId;
        }
    public String getTitle() {return title;}
    public String getCategory() {return category;}
    public String getContent() {return content;}
}
