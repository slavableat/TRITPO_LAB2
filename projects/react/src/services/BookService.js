import axios from "axios";

const page_url_books = "http://localhost:8080/books";
const page_url_genres = "http://localhost:8080/genres";
const page_url_authors = "http://localhost:8080/authors";

class BookService {
    async getBooks() {
        return await axios.get(page_url_books + "/all");
    }

    async getGenres() {
        return await axios.get(page_url_genres + "/all");
    }

    async getAuthors() {
        return await axios.get(page_url_authors + "/all");
    }

    async addBook(book) {
        return await axios.post(page_url_books + "/create", book);
    }

    async deleteBook(id) {
        return await axios.delete(page_url_books + `/delete/${id}`);
    }

    async getBookById(id) {
        return await axios.get(page_url_books + `/edit/${id}`)
    }

    async updateBook(book) {
        return await axios.put(page_url_books + `/edit`, book);
    }
}

export default new BookService;