import {Genre} from "./genre";
import {Author} from "./author";

export class Book {
  id: number;
  name: string;
  genre: Genre;
  authors: Author[];
}
