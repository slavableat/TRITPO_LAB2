import {Component, OnInit} from '@angular/core';
import {Book} from "../book";
import {BookService} from "../book.service";
import {Router} from "@angular/router";
import {Genre} from "../genre";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Author} from "../author";

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {
  myForm: FormGroup;
  book: Book = new Book();

  constructor(private formBuilder: FormBuilder,
              private bookService: BookService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.book.genre = new Genre();
    this.book.authors = [];
    this.book.authors.push();
    this.myForm = this.formBuilder.group({
      "bookName": ["", [Validators.required]],
      "bookGenre": ["", [Validators.required]],
      "authors": this.formBuilder.array([
        ["", Validators.required]
      ])
    });
  }

  getFormsControls(): FormArray {
    return this.myForm.controls['authors'] as FormArray;
  }

  addAuthor() {
    this.getFormsControls().push(new FormControl("", Validators.required));
  }

  deleteAuthor(id: number) {
    this.getFormsControls().removeAt(id);
  }

  submit() {
    console.log(this.myForm.value);
    for (let formAuthor of this.myForm.value.authors) {
      let author: Author = new Author();
      author.name = formAuthor;
      this.book.authors.push(author);
    }
    this.book.name = this.myForm.value.bookName;
    this.book.genre.name = this.myForm.value.bookGenre;
    this.bookService.saveBook(this.book).subscribe(data => {
      this.router.navigate(['books']);
    });

  }
}
