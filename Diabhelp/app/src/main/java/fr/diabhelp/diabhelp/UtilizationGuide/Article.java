package fr.diabhelp.diabhelp.UtilizationGuide;

public class Article {
        String category;
        String title;
        String[] content;
        Article(String category, String title, String content) {
            this.category = category;
            this.title = title;
            this.content[0] = content;
        }
        Article(String category, String title, String[] content) {
            this.category = category;
            this.title = title;
            this.content = content;
        }
    public String getTitle() {return title;}
    public String getCategory() {return category;}
    public String[] getContent() {return content;}
}
