import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BookListComponent } from './book-list/book-list.component';
import {BookService} from "./book.service";
import { HttpClientModule} from "@angular/common/http";
import { AddBookComponent } from './add-book/add-book.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { UpdateBookComponent } from './update-book/update-book.component';
import { GenreListComponent } from './genre-list/genre-list.component';
import { AuthorListComponent } from './author-list/author-list.component';


@NgModule({
  declarations: [
    AppComponent,
    BookListComponent,
    AddBookComponent,
    UpdateBookComponent,
    GenreListComponent,
    AuthorListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [BookService],
  bootstrap: [AppComponent]
})
export class AppModule { }
