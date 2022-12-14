import './App.css';

import BookListComponent from "./components/BookListComponent";
import HeaderComponent from "./components/HeaderComponent";
import {BrowserRouter, Route, Router, Routes} from "react-router-dom";
import AddBookComponent from "./components/AddBookComponent";
import GenreListComponent from "./components/GenreListComponent";
import AuthorListComponent from "./components/AuthorListComponent";


function App() {
    return (
        <div>
            <BrowserRouter>
                <HeaderComponent/>
                <div className="container">
                    <Routes>
                        <Route path="/" exact element={<BookListComponent/>}></Route>
                        <Route path="/books" element={<BookListComponent/>}></Route>
                        <Route path="/add-book" element={<AddBookComponent/>}></Route>
                        <Route path="/edit-book/:id" element={<AddBookComponent/>}></Route>
                        <Route path="/genres" element={<GenreListComponent/>}></Route>
                        <Route path="/authors" element={<AuthorListComponent/>}></Route>
                    </Routes>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
