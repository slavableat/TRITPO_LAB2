**GET /books**\
Описание\
Получение списка всех книг.

Параметры\
Отсутствуют.

Пример запроса\
GET /books HTTP/1.1\
Host: example.com\
Accept: application/json\
\
Пример ответа\
HTTP/1.1 200 OK\
Content-Type: application/json

`[
    {
    "id": 1,
    "name": "1984",
    "author": "George Orwell",
    "authors": "Secker and Warburg",
    "publishedDate": "1949-06-08"
    },
    {
    "id": 2,
    "title": "To Kill a Mockingbird",
    "author": "Harper Lee",
    "publisher": "J. B. Lippincott & Co.",
    "publishedDate": "1960-07-11"
    }
]
`