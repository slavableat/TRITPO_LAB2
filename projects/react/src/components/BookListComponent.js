import {useEffect, useState} from "react";
import BookService from "../services/BookService";
import {Link} from "react-router-dom";
import bookService from "../services/BookService";
import FilterComponent from "./FilterComponent";

const BookListComponent = () => {
    const title = "Библиотека"
    const [books, setBooks] = useState([]);
    const [savebooks, setSaveBooks] = useState([]);
    const [status, setStatus] = useState('')

    useEffect(() => {
        setStatus("Загрузка...")
        getBooks()
        console.log(books)
    }, [])

    const getBooks = () => {
        BookService.getBooks().then(
            (res) => {
                setBooks(res.data)
                setSaveBooks(res.data)
                setStatus("Пусто")
            }).catch(error => {
            console.log(error);
        })
    }

    const reloadBooks = () => {
        setBooks(savebooks)
    }

    const deleteBook = (id) => {
        // setBooks(books=> books.filter(book=> book.id!=id))
         bookService.deleteBook(id).then(() => {
            getBooks()
         }).catch(error => {
             console.log(error);
         })
    }

    const returnedValue = (genre, author) => {
        setBooks(books.filter(book =>
            book.genre.name.includes(genre) &&
            (book.authors.filter(auth =>
                    auth.name.toLowerCase().includes(author.toLowerCase())).length >= 1
            )
        ))
    }

    return (
        <div>
            <h1 className="text-center" style={{paddingTop: 30}}> {title}</h1>
            <div className="grid" style={{rowGap: 10}}>
                <div className="row gx-5">
                    <div className=" col-lg-9">
                        <div className="card">
                            <div className="card-body">
                                <table className="table table-bordered table-striped " style={{textAlign: "left"}}>
                                    <thead className="thead-dark bg-info">
                                    <tr className="text-center col-auto">
                                        <th scope="col">Имя книги</th>
                                        <th scope="col">Жанр</th>
                                        <th scope="col">Авторы</th>
                                        <th scope="col">Действия</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {
                                        books.length !== 0 ?
                                            books.map(
                                                book =>
                                                    <tr>
                                                        <td>{book.name}</td>
                                                        <td>{book.genre.name}</td>
                                                        <td>
                                                            {
                                                                book.authors.map(
                                                                    author =>
                                                                        <p key={author.id}>{author.name}</p>
                                                                )
                                                            }
                                                        </td>
                                                        <td>
                                                            <Link to={`/edit-book/${book.id}`}
                                                                  className="btn btn-secondary "
                                                                  style={{margin: 2}}>Изменить</Link>
                                                            <br/>
                                                            <button className="btn btn-danger" style={{margin: 2}}
                                                                    onClick={() => deleteBook(book.id)}>Удалить
                                                            </button>
                                                        </td>
                                                    </tr>
                                            ) :
                                            <tr>
                                                <td className="text-center" colSpan="4">
                                                    {status}
                                                </td>
                                            </tr>
                                    }
                                    </tbody>
                                </table>
                                <div className="row-cols-auto" style={{textAlign: "right",}}>
                                    <Link to="/add-book" className="btn btn-primary">Добавить книгу</Link>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className=" my-5 col-3">
                        <FilterComponent reload={reloadBooks} returnValue={returnedValue} />
                    </div>
                </div>
            </div>
        </div>
    )
}
export default BookListComponent;