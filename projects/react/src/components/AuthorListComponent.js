import React, {useEffect, useState} from 'react';
import bookService from "../services/BookService";

const AuthorListComponent = () => {
    const [authors, setAuthors] = useState([]);
    const [status, setStatus] = useState('')
    const title = "Авторы"

    useEffect(() => {
        setStatus("Загрузка...")
        getAuthors();
    }, [])
    const getAuthors = () => {
        bookService.getAuthors().then((res) => {
            setAuthors(res.data)
            setStatus("Пусто")
        }).catch(error => {
            console.log(error)
        })
    }
    return (
        <div>
            <h1 className="text-center" style={{paddingTop: 30}}> {title}</h1>
            <div className="card">
                <div className="card-body">
                    <table className="table table-bordered table-striped ">
                        <thead>
                        <tr>
                            <th className="text-center">
                                Автор
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            authors.length !== 0 ?
                                authors.map(author =>
                                    <tr>
                                        <td>
                                            {author.name}
                                        </td>
                                    </tr>
                                ) :
                                <tr>
                                    <td className="text-center">
                                        {status}
                                    </td>
                                </tr>
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default AuthorListComponent;