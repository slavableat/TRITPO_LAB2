package com.example.springboot.Service.book;

import com.example.springboot.Model.Author;
import com.example.springboot.Model.Book;
import com.example.springboot.Service.workbook.WorkBookService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksReportService implements WorkBookService {

    public static final int ORDINAL_NUMBER = 0;
    public static final int NAME = 1;
    public static final int GENRE = 2;
    public static final int AUTHORS = 3;
    public static final String SHEET_NAME = "Книги";
    public static final String ORDINAL_NUMBER_COLUMN_NAME = "№";
    public static final String BOOK_NAME = "Название";
    public static final String BOOK_COLUMN_NAME = BOOK_NAME;
    public static final String GENRE_COLUMN_NAME = "Жанр";
    public static final String AUTHORS_COLUMN_NAME = "Авторы";

    @Override
    public Workbook createWorkbook(List<Book> books) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);

        sheet.setColumnWidth(ORDINAL_NUMBER,256 * 4);
        headerRow.createCell(ORDINAL_NUMBER).setCellValue(ORDINAL_NUMBER_COLUMN_NAME);

        sheet.setColumnWidth(NAME,256 * 40);
        headerRow.createCell(NAME).setCellValue(BOOK_COLUMN_NAME);

        sheet.setColumnWidth(GENRE,256 * 40);
        headerRow.createCell(GENRE).setCellValue(GENRE_COLUMN_NAME);

        sheet.setColumnWidth(AUTHORS,256 * 70);
        headerRow.createCell(AUTHORS).setCellValue(AUTHORS_COLUMN_NAME);


        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < 4; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        int ordinalNumber = 0;
        for (Book book : books) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(ORDINAL_NUMBER).setCellValue(++ordinalNumber);
            row.createCell(NAME).setCellValue(book.getName());
            row.createCell(GENRE).setCellValue(book.getGenre().getName());

            row.createCell(AUTHORS).setCellValue(book.getAuthors().stream().map(
                    Author::getName).collect(Collectors.joining(",")
            ));
            for (int i = 0; i < 4; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        return workbook;
    }
}
