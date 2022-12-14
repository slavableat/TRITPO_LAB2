import React, {useEffect, useState} from 'react';
import bookService from "../services/BookService";
import BookService from "../services/BookService";

const FilterComponent = ({returnValue, reload}) => {
    const [genres, setGenres] = useState([]);
    const [genre, setGenre] = useState('');
    const [author, setAuthor] = useState('');

    const getGenres = () => {
        BookService.getGenres().then(res => {
            setGenres(res.data)
        }).catch(error => {
            console.log(error);
        })
    }

    useEffect(() => {
        getGenres()
    },[])

    const submit = (e) => {
        if (genre.toLowerCase().includes("Выберите жанр".toLowerCase())) setGenre("")
        e.preventDefault()
        returnValue(genre, author);
    }

    return (<div className="container col-lg-auto">
        <h2 className="text-center">Фильтр:</h2>
        <div className="form-control" style={{backgroundColor: "#dfe1f3", borderRadius: 10, width: 320}}>
            <form>
                <label className="form-label">
                    Автор:
                    <br/>
                    <input
                        type="text"
                        className="form-control-sm"
                        value={author}
                        onChange={e => setAuthor(e.target.value)}
                    />
                </label>
                <label className="form-label">
                    Жанр:
                    <br/>
                    <select
                        className="form-select-sm"
                        value={genre}
                        onChange={(e) => setGenre(e.target.value)}
                    >
                        <option selected>Выберите жанр</option>
                        {genres.map(genre => <option>{genre.name}</option>)}
                    </select>
                </label>
                <br/>
                <div className="grid">
                    <input
                        type="submit"
                        value="Отменить"
                        onClick={e => {
                            e.preventDefault();
                            setGenre('')
                            setAuthor('')
                            reload()
                        }}
                    />
                    <input
                        type="submit"
                        value="Сортировать"
                        onClick={e => submit(e)}
                    />
                </div>

            </form>
            <br/>
            <p>Примененные фильтры:</p>
            <ul>
                {genre !== '' &&genre!=="Выберите жанр"&& <li>{genre}</li>}
                {author !== "" && <li>{author}</li>}
            </ul>
        </div>
    </div>);
};

export default FilterComponent;