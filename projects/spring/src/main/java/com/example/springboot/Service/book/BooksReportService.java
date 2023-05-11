package com.example.springboot.Service.book;

import com.example.springboot.Model.Author;
import com.example.springboot.Model.Book;
import com.example.springboot.Service.workbook.WorkBookService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    /**
     * Заполнение отчета данными
     * @param books  книги для отчета
     * @return возвращает отчет
     */
    @Override
    public Workbook createWorkbook(List<Book> books) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        int rowNum = 0;
        configureHeaderRow(workbook, sheet, rowNum++);

        CellStyle dataStyle = createDataCellStyle(workbook);
        int ordinalNumber = 0;
        for (Book book : books) {
            Row row = sheet.createRow(rowNum++);
            createStyledCell(dataStyle, String.valueOf(++ordinalNumber), row, ORDINAL_NUMBER);
            createStyledCell(dataStyle, book.getName(), row, NAME);
            createStyledCell(dataStyle, book.getGenre().getName(), row, GENRE);
            createStyledCell(dataStyle,
                    book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(",")),
                    row, AUTHORS);
        }
        return workbook;
    }

    /**
     * Настройка стиля для основной части таблицы
     * @param workbook отчет
     */
    @NotNull
    private static CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        return dataStyle;
    }

    /**
     * Создает стилизованную ячейку
     * @param style стиль
     * @param value значение
     * @param row строка отчета
     * @param cellNumber порядковый номер в строке
     */
    private static void createStyledCell(CellStyle style, String value, Row row, Integer cellNumber) {
        row.createCell(cellNumber).setCellValue(value);
        row.getCell(cellNumber).setCellStyle(style);
    }

    /**
     * Назначает переданные шрифт и стиль
     * @param headerStyle исходный стиль
     * @param font исходный шрифт
     */
    private static void configureHeaderCellStyleAndHeaderFont(CellStyle headerStyle, Font font) {
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * Настройка длины ячеек и стиля заголовка
     * @param workbook отчет
     * @param sheet страница отчета
     * @param rowNumber номер строки
     * @return возвращает Row отчета
     */
    private Row configureHeaderRow(Workbook workbook, Sheet sheet, Integer rowNumber) {
        Row headerRow = sheet.createRow(rowNumber);

        sheet.setColumnWidth(ORDINAL_NUMBER, 256 * 4);
        headerRow.createCell(ORDINAL_NUMBER).setCellValue(ORDINAL_NUMBER_COLUMN_NAME);

        sheet.setColumnWidth(NAME, 256 * 40);
        headerRow.createCell(NAME).setCellValue(BOOK_COLUMN_NAME);

        sheet.setColumnWidth(GENRE, 256 * 40);
        headerRow.createCell(GENRE).setCellValue(GENRE_COLUMN_NAME);

        sheet.setColumnWidth(AUTHORS, 256 * 70);
        headerRow.createCell(AUTHORS).setCellValue(AUTHORS_COLUMN_NAME);

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        configureHeaderCellStyleAndHeaderFont(headerStyle, font);

        for (int i = 0; i < 4; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        return headerRow;
    }

    /**
     * Запись отчета в поток данных HTTP ответа
     *
     * @param response      ответ из полученного запроса
     * @param workbook      отчет
     * @param workBookName  имя отчета
     */
    @Override
    public void writeToServletResponse(HttpServletResponse response, Workbook workbook, String workBookName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + workBookName + ".xlsx\"");
        workbook.write(response.getOutputStream());
    }
}
