import {useEffect, useState} from "react";
import bookService from "../services/BookService";
import {Link, useNavigate, useParams} from "react-router-dom";

const AddBookComponent = () => {
    const {id} = useParams();
    const [name, setName] = useState('');    //hook
    const [genre, setGenre] = useState({name: ''});//hook
    const [authors, setAuthors] = useState([{name: ''}]);
    const [operation, setOperation] = useState('')
    const navigate = useNavigate();

    useEffect(() => {
        if (id !== null && id !== undefined) {
            setOperation("Изменить")
            bookService.getBookById(id).then((response) => {
                setName(response.data.name)
                setGenre(response.data.genre)
                setAuthors(response.data.authors)
            }).catch(error => {
                console.log(error)
            })
        } else setOperation("Добавить")
    }, [])

    const authorChange = (index, author) => {
        const auths = authors.slice();
        auths[index] = {name: author}
        setAuthors(auths)
    };

    const saveOrUpdate = (e) => {
        e.preventDefault()
        const book = {name: name, genre: genre, authors: authors.filter(auth => auth.name !== '')}
        if (id !== undefined && id !== null) {
            const updBook = Object.assign({}, book, {id: id})
            bookService.updateBook(updBook).then(() => {
                navigate("/books")
            })
        } else {
            console.log(book)
            bookService.addBook(book).then(() => {
                navigate("/books")
            });
        }
    }

    const validData = () => {
        console.log(authors)
        console.log("массив без пустых", authors.filter(auth => auth.name !== ''))
        return (name !== '' || genre.name !== '' || authors.filter(auth => auth.name !== '').length === 0);
    }

    const addAuthor = () => {
        setAuthors([...authors, {name: ''}]);
        console.log("размер массива без фильтра", authors.length + 1)
    }

    const deleteAuthor = (index) => {
        setAuthors(authors.filter((author, indx) => index !== indx))
    }

    return (<div>
        <div className="container col-8">
            <div className="row">
                <h1 className="text-center " style={{margin: 20}}>{operation} книгу</h1>
                <div className="col-6 mx-auto" style={{borderRadius: 10, backgroundColor: "lightblue"}}>
                    <div className="card-body">
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label"> Имя книги:
                                    <input
                                        type="text"
                                        required={true}
                                        name="firstName"
                                        className="form-control"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                    >
                                    </input></label>

                            </div>
                            <div className="form-group mb-2">
                                <label className="form-label"> Жанр:
                                    <input
                                        type="text"
                                        name="genre"
                                        required={true}
                                        className="form-control"
                                        value={genre.name}
                                        onChange={(e) => setGenre({name: e.target.value})}
                                    >
                                    </input></label>

                            </div>
                            {authors.map((author, index) => (<div className="form-group mb-2">
                                <label className="form-label">
                                    Автор:
                                    <div className="input-group">
                                        <input
                                            type="text"
                                            name="author"
                                            className="form-control"
                                            value={author.name}
                                            required={true}
                                            onChange={e => {
                                                authorChange(index, e.target.value)
                                            }}
                                        />
                                        {index === 0 ? null : <button className="btn btn-outline-danger" type="button"
                                                                      onClick={() => {
                                                                          deleteAuthor(index)
                                                                      }}>Удалить</button>}
                                    </div></label>

                            </div>))}

                            <div>
                                <button className="btn btn-secondary" onClick={e => {
                                    addAuthor()
                                    e.preventDefault()
                                }}>Добавить автора
                                </button>
                                <div style={{textAlign: "right"}}>
                                    <br/>
                                    <Link to="/books" className="btn btn-danger"
                                          style={{marginRight: 2}}>Отмена</Link>
                                    <button className="btn btn-success"
                                            onClick={e => {
                                                if (validData()) saveOrUpdate(e)
                                            }}>
                                        {operation}
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>)
}
export default AddBookComponent;