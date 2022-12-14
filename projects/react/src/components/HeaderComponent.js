import {Link} from "react-router-dom";

function HeaderComponent() {
    return <div>
        <nav className="navbar navbar-expand-md navbar-dark bg-dark justify-content-center">
            <Link to="/books" className="nav-link">Книги</Link>
            <Link to="/genres" className="nav-link ">Жанры</Link>
            <Link to="/authors" className="nav-link">Авторы</Link>
        </nav>
    </div>
}

export default HeaderComponent;
