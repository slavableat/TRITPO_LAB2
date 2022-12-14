import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Book} from "./book";
import {Genre} from "./genre";
import {Author} from "./author";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private booksUrl='http://localhost:8080';

  constructor(private httpClient: HttpClient) {}

  public findAllBooks(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.booksUrl}/books`);
  }

  public saveBook(book: Book) : Observable<Object>{
    return this.httpClient.post<Book>(`${this.booksUrl}/book-create`, book);
  }

  public deleteBook(id:number): Observable<Object>{
    return this.httpClient.delete(`${this.booksUrl}/delete/${id}`);
  }
  public getById(id:number):Observable<Book>{
    return this.httpClient.get<Book>(`${this.booksUrl}/book-edit/${id}`);
  }
  public updateBook(book:Book):Observable<Object>{
    return this.httpClient.put<Book>(`${this.booksUrl}/book-edit/${book.id}`,book);
  }
  public findAllGenres(): Observable<Genre[]> {
    return this.httpClient.get<Genre[]>(`${this.booksUrl}/genres`);
  }
  public findAllAuthors(): Observable<Author[]> {
    return this.httpClient.get<Author[]>(`${this.booksUrl}/authors`);
  }

}
