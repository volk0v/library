package me.volkovd.library.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {

    private int id;

    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(max = 300, message = "Название должно быть до 300 символов длиной")
    private String title;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @Size(max = 150, message = "Имя автора должно быть до 150 символов длиной")
    private String authorName;

    private int yearOfPublication;

    public Book() {}

    public Book(String title, String authorName, int yearOfPublication) {
        this.title = title;
        this.authorName = authorName;
        this.yearOfPublication = yearOfPublication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

}
