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
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {


    public static final String WORK_BOOK_NAME = "Книги";
    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final CacheService cacheService;

    private final AccessCounterService accessCounterService;
    private final BooksReportService workBookService;

    private final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository, CacheService cacheService, AccessCounterService accessCounterService, BooksReportService workBookService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.cacheService = cacheService;
        this.accessCounterService = accessCounterService;
        this.workBookService = workBookService;
    }

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
        book.setAuthors(book.getAuthors().stream().map(author -> {
            Author existstAuthor = authorRepository.findByName(author.getName());
            return Objects.requireNonNullElse(existstAuthor, author);
        }).collect(Collectors.toSet()));
        bookRepository.save(book);
        cacheService.clearCache();
        return book;
    }

    @Override
    public Book getBookById(Long id) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        Book book = cacheService.getOrNull(id);
        if (book != null) {
            LOGGER.info("Gets from memory");
            return book;
        }
        Optional<Book> bookFromDatabase = bookRepository.findById(id);
        if (bookFromDatabase.isEmpty()) {
            throw new CustomException("Invalid book id: " + id);
        }
        LOGGER.info("Gets from database");
        return bookFromDatabase.get();
    }

    @Override
    public Book editBook(Book newBook) throws CustomException {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        validateBook(newBook);
        if (newBook.getId() == null) throw new CustomException("Book doesnt have id");
        Book oldBook = bookRepository.findById(newBook.getId()).orElse(null);
        if (oldBook == null) {
            throw new CustomException("Database doesnt have this book");
        }
        if (!newBook.getName().equals(oldBook.getName())) {
            oldBook.setName(newBook.getName());
        }

        if (!newBook.getGenre().getName().equals(oldBook.getGenre().getName())) {
            Genre genreFromDatabase = genreRepository.findByName(newBook.getGenre().getName());
            if (genreFromDatabase != null) {
                oldBook.setGenre(genreFromDatabase);
            } else {
                oldBook.setGenre(newBook.getGenre());
            }
        }


        oldBook.setAuthors(newBook.getAuthors().stream().map(author -> {
            Author existstAuthor = authorRepository.findByName(author.getName());
            return Objects.requireNonNullElse(existstAuthor, author);
        }).collect(Collectors.toSet()));

        bookRepository.save(oldBook);

        cacheService.clearCache();
        return null;
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("Service accessed " + accessCounterService.incrementAccessCounterAndGetResult() + " times");
        bookRepository.deleteById(id);
        cacheService.clearCache();
    }

    @Override
    public void getWorkBook(HttpServletResponse response) {
        try {
            Workbook workbook = workBookService.createWorkbook(findAllBooks());
            workBookService.writeToServletResponse(response, workbook, WORK_BOOK_NAME);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllBooks() {
        for (Book book :
                this.findAllBooks()) {
            this.deleteById(book.getId());
        }
    }

    private void validateBook(Book book) throws CustomException {
        if (book.getName() == null) throw new CustomException("Book doesnt have name");
        if (book.getGenre() == null) throw new CustomException("Book doesnt have genre");
        if (book.getAuthors() == null ||
                book.getAuthors().isEmpty()) throw new CustomException("Book doesnt have authors");
    }
}
