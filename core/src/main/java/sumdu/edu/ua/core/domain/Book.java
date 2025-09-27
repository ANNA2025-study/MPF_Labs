package sumdu.edu.ua.core.domain;

public class Book {
    long id;
    String title;
    String author;
    int pubYear;

    public int getPubYear() {
        return pubYear;
    }

    public Book() {
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    public Book(Long id, String title, String author, int pubYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pubYear = pubYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
