import {Component, OnInit} from '@angular/core';
import {Genre} from "../genre";
import {BookService} from "../book.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Author} from "../author";

@Component({
  selector: 'app-author-list',
  templateUrl: './author-list.component.html',
  styleUrls: ['./author-list.component.css']
})
export class AuthorListComponent implements OnInit {

  public authors: Author[] = [];

  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
    this.getAuthors();
  }

  public getAuthors(): void {
    this.bookService.findAllAuthors().subscribe(
      (response: Genre[]) => {
        this.authors = response;
        console.log(this.authors);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}
