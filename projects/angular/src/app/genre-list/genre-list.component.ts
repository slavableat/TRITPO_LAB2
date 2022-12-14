import {Component, OnInit} from '@angular/core';
import {BookService} from "../book.service";
import {Genre} from "../genre";
import {Book} from "../book";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-genre-list',
  templateUrl: './genre-list.component.html',
  styleUrls: ['./genre-list.component.css']
})
export class GenreListComponent implements OnInit {
  public genres: Genre[] = [];

  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
    this.getGenres();
  }

  public getGenres(): void {
    this.bookService.findAllGenres().subscribe(
      (response: Genre[]) => {
        this.genres = response;
        console.log(this.genres);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
