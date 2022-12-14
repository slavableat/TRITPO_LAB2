package com.example.springboot.Service.book;

import com.example.springboot.Controller.BookController;
import com.example.springboot.Model.Author;
import com.example.springboot.Model.Book;
import com.example.springboot.Model.Genre;
import com.example.springboot.Repository.AuthorRepository;
import com.example.springboot.Repository.BookRepository;
import com.example.springboot.Repository.GenreRepository;
import com.example.springboot.Service.accessCounter.AccessCounterService;
import com.example.springboot.Service.cacheForBooks.CacheService;
import com.example.springboot.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AccessCounterService accessCounterService;

    private final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Override
    public List<Book> findAllBooks() {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        List<Book> books = cacheService.getBooks();
        if (!books.isEmpty()) {
            LOGGER.info("Gets from memory");
            return books;
        }
        books = bookRepository.findAll();
        LOGGER.info("Gets from database");
        cacheService.setBooks(books);
        return books;
    }

    @Override
    public Book addBook(Book book) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        validateBook(book);
        Genre findGenre = genreRepository.findByName(book.getGenre().getName());
        if (findGenre != null) {
            book.setGenre(findGenre);
        }
        book.getGenre().getBooks().add(book);
        genreRepository.save(book.getGenre());
        bookRepository.save(book);
        for (Author author : book.getAuthors()) {
            if (authorRepository.findByName(author.getName()) != null) {
                author = authorRepository.findByName(author.getName());
            }
            author.getBooks().add(book);
            authorRepository.save(author);
        }
        cacheService.clearCache();
        return book;
    }

    @Override
    public Book getBookById(Long id) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        Book book = cacheService.getIfContainsElseGetNull(id);
        if (book != null) {
            LOGGER.info("Gets from memory");
            return book;
        }
        Optional<Book> bookByDatabase = bookRepository.findById(id);
        if (bookByDatabase.isEmpty()) {
            throw new CustomException("Invalid book id: " + id);
        }
        LOGGER.info("Gets from database");
        return bookByDatabase.get();
    }

    @Override
    public Book editBook(Book book) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        validateBook(book);
        if(book.getId() == null) throw new CustomException("Book doesnt have id");
        Book oldBook = bookRepository.findById(book.getId()).orElse(null);
        if (oldBook == null) {
            throw new CustomException("Database doesnt have this book");
        }
        if (!book.getName().equals(oldBook.getName())) {
            oldBook.setName(book.getName());
        }
        List<Author> mustBEDeleted = new ArrayList<>();
        oldBook.getAuthors().stream().forEach(author -> {
            mustBEDeleted.add(author);
            author.getBooks().remove(oldBook);
        });
//        for (Author author : oldBook.getAuthors()) {
//            mustBEDeleted.add(author);
//            author.getBooks().remove(oldBook);
//        }
        oldBook.getAuthors().clear();
        mustBEDeleted.stream().forEach(author -> {
            if(author.getBooks().isEmpty()){
                authorRepository.deleteById(author.getId());
            }
        });
//        for (Author author : mustBEDeleted) {
//            if (author.getBooks().isEmpty()) {
//                authorRepository.deleteById(author.getId());
//            }
//        }
        mustBEDeleted.clear();
        book.getAuthors().stream().forEach(author -> {
            Author oldAuthor = authorRepository.findByName(author.getName());
            if (oldAuthor != null) {
                author = oldAuthor;
            }
            author.getBooks().add(oldBook);
            authorRepository.save(author);
        });
//        for (Author author : book.getAuthors()) {
//            Author oldAuthor = authorRepository.findByName(author.getName());
//            if (oldAuthor != null) {
//                author = oldAuthor;
//            }
//            author.getBooks().add(oldBook);
//            authorRepository.save(author);
//        }
        long mustBeDeletedGenreId = 0;
        if (!book.getGenre().getName().equals(oldBook.getGenre().getName())) {
            oldBook.getGenre().getBooks().remove(oldBook);
            if (oldBook.getGenre().getBooks().isEmpty()) {
                mustBeDeletedGenreId = oldBook.getGenre().getId();
            }
            Genre oldGenre = genreRepository.findByName(book.getGenre().getName());
            if (oldGenre != null) {
                oldBook.setGenre(oldGenre);
            } else {
                oldBook.setGenre(book.getGenre());
            }
            oldBook.getGenre().getBooks().add(oldBook);
            genreRepository.save(oldBook.getGenre());
        }
        if (mustBeDeletedGenreId != 0) {
            genreRepository.deleteById(mustBeDeletedGenreId);
        }
        cacheService.clearCache();
        return oldBook;
    }

    @Override
    public void deleteById(Long id) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new CustomException("Invalid book id");
        }
        List<Author> mustBEDeleted = new ArrayList<>();
        book.getAuthors().stream().forEach(author -> {
            mustBEDeleted.add(author);
            author.getBooks().remove(book);
        });
//        for (Author author : book.getAuthors()) {
//            mustBEDeleted.add(author);
//            author.getBooks().remove(book);
//        }
        book.getAuthors().clear();
        bookRepository.save(book);
        mustBEDeleted.stream().forEach(author -> {
            if (author.getBooks().isEmpty()) {
                authorRepository.deleteById(author.getId());
            }
        });
//        for (Author author : mustBEDeleted) {
//            if (author.getBooks().isEmpty()) {
//                authorRepository.deleteById(author.getId());
//            }
//        }
        mustBEDeleted.clear();
        book.getGenre().getBooks().remove(book);
        if (book.getGenre().getBooks().isEmpty()) {
            genreRepository.deleteById(book.getGenre().getId());
        } else {
            genreRepository.save(book.getGenre());
        }
        book.setGenre(null);
        bookRepository.delete(book);
        cacheService.clearCache();
    }

    @Override
    public void deleteAllBooks() throws CustomException{
        for (Book book :
                this.findAllBooks()) {
            this.deleteById(book.getId());
        }
    }

    private void validateBook(Book book) throws CustomException {
        if(book.getName() == null) throw new CustomException("Book doesnt have name");
        if (book.getGenre() == null) throw new CustomException("Book doesnt have genre");
        if (book.getAuthors() == null ||
                book.getAuthors().isEmpty()) throw new CustomException("Book doesnt have authors");
    }
}
