import React, {useEffect, useState} from 'react';
import bookService from "../services/BookService";

const GenreListComponent = () => {
    const [genres, setGenres] = useState([]);
    const title = "Жанры"
    const [status, setStatus] = useState('')

    useEffect(() => {
        setStatus("Загрузка...")
        getGenres();
    }, [])
    const getGenres = () => {
        bookService.getGenres().then((res) => {
            setGenres(res.data)
            setStatus("Пусто")
        }).catch(error => {
            console.log(error)
        })
    }
    return (<div>
            <h1 className="text-center" style={{paddingTop: 30}}> {title}</h1>
            <div className="card">
                <div className="card-body">
                    <table className="table table-bordered table-striped ">
                        <thead>
                        <tr>
                            <th className="text-center">
                                Жанр
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {genres.length !== 0 ? genres.map(genre => <tr>
                            <td>
                                {genre.name}
                            </td>
                        </tr>) : <tr>
                            <td className="text-center">
                                {status}
                            </td>
                        </tr>}

                        </tbody>
                    </table>
                </div>
            </div>
        </div>);
};

export default GenreListComponent;