package me.volkovd.library.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;

    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(max = 300, message = "Название должно быть до 300 символов длиной")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @Size(max = 150, message = "Имя автора должно быть до 150 символов длиной")
    @Column(name = "author_name")
    private String authorName;

    @Column(name = "year_of_publication")
    private int yearOfPublication;

    @Column(name = "delay_date")
    private LocalDate delayDate;

    @Transient
    private Boolean isExpired = null;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    public Book() {
    }

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

    public LocalDate getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(LocalDate delayDate) {
        this.delayDate = delayDate;
    }

    public boolean isExpired() {
        if (isExpired == null) {
            if (delayDate == null)
                isExpired = false;
            else
                isExpired = LocalDate.now().isAfter(delayDate);
        }
        return isExpired;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

}
