package ru.otus.migration.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.migration.model.nosql.Author;
import ru.otus.migration.model.nosql.Book;
import ru.otus.migration.model.nosql.Genre;


@Component
public class ConvertBookToRelationalBook implements Converter<Book, ru.otus.migration.model.relational.Book> {
    @Override
    public ru.otus.migration.model.relational.Book convert(Book book) {
        Author author = book.getAuthor();
        Genre genre = book.getGenre();
        ru.otus.migration.model.relational.Author authorTarget = new ru.otus.migration.model.relational.Author();
        ru.otus.migration.model.relational.Genre genreTarget = new ru.otus.migration.model.relational.Genre();
        ru.otus.migration.model.relational.Book bookTarget = new ru.otus.migration.model.relational.Book();
        authorTarget.setFirstName(author.getFirstName());
        authorTarget.setLastName(author.getLastName());
        authorTarget.setMiddleName(author.getMiddleName());
        authorTarget.setId(Long.valueOf(author.getId()));
        genreTarget.setGenreName(genre.getGenreName());
        genreTarget.setId(Long.valueOf(genre.getId()));
        bookTarget.setAuthor(authorTarget);
        bookTarget.setGenre(genreTarget);
        bookTarget.setTitle(book.getTitle());
        return bookTarget;
    }
}
