package ru.otus.migration.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.migration.model.nosql.Author;
import ru.otus.migration.model.nosql.Book;
import ru.otus.migration.model.nosql.Genre;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "FirstRoman", runAlways = true)
    public void dropDb(MongoDatabase mongoDatabase) {
        mongoDatabase.drop();
    }

    @ChangeSet(order = "002", id = "insertData", author = "FirstRoman", runAlways = true)
    public void insertData(MongockTemplate template) {
        Author author = new Author();
        author.setId("1");
        author.setLastName("Толстой");
        author.setMiddleName("Николаевич");
        author.setFirstName("Лев");
        Genre genre = new Genre();
        genre.setId("1");
        genre.setGenreName("Классика");
        Book book = new Book();
        book.setId("1");
        book.setGenre(genre);
        book.setAuthor(author);
        book.setTitle("Война и мир");
        Book bookTolstoy = new Book();
        bookTolstoy.setId("3");
        bookTolstoy.setTitle("Анна Каренина");
        bookTolstoy.setGenre(genre);
        bookTolstoy.setAuthor(author);
        Author author1 = new Author();
        author1.setId("2");
        author1.setFirstName("Александр");
        author1.setLastName("Пушкин");
        author1.setMiddleName("Сергеевич");
        Book bookPushkin = new Book();
        bookPushkin.setId("2");
        bookPushkin.setTitle("Евгений Онегин");
        bookPushkin.setAuthor(author1);
        bookPushkin.setGenre(genre);
        template.save(book);
        template.save(bookPushkin);
        template.save(bookTolstoy);
    }

}
