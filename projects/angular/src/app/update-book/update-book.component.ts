import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {BookService} from "../book.service";
import {Book} from "../book";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Genre} from "../genre";
import {Author} from "../author";

@Component({
  selector: 'app-update-book',
  templateUrl: './update-book.component.html',
  styleUrls: ['./update-book.component.css']
})
export class UpdateBookComponent implements OnInit {
  oldBook: Book = new Book();
  id: number;
  myForm: FormGroup;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private bookService: BookService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.bookService.getById(this.id).subscribe(data => {
      this.oldBook = data;
      this.myForm = this.formBuilder.group({
        "bookName": [this.oldBook.name, [Validators.required]],
        "bookGenre": [this.oldBook.genre.name, [Validators.required]],
        "authors": this.formBuilder.array([])
      });
      for (let author of this.oldBook.authors) {
        if (author !== null && author !== undefined) {
          this.getFormsControls().push(new FormControl(author.name, Validators.required));
        }
      }
    }, error => console.log(error));

  }

  addAuthor() {
    this.getFormsControls().push(new FormControl("", Validators.required));
  }

  deleteAuthor(id: number) {
    this.getFormsControls().removeAt(id);
  }

  getFormsControls(): FormArray {
    return this.myForm.controls['authors'] as FormArray;
  }

  submit() {
    let book: Book = new Book();
    book.genre = new Genre();
    book.authors = [];
    book.id = this.id;
    for (let formAuthor of this.myForm.value.authors) {
      let author: Author = new Author();
      author.name = formAuthor;
      book.authors.push(author);
    }
    book.name = this.myForm.value.bookName;
    book.genre.name = this.myForm.value.bookGenre;
    this.bookService.updateBook(book).subscribe(data => {
      this.router.navigate(['books']);
      console.log(book);
    });
  }
}
