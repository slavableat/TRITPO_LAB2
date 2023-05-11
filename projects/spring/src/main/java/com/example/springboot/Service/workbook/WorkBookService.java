package com.example.springboot.Service.workbook;

import com.example.springboot.Model.Book;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface WorkBookService {
    Workbook createWorkbook(List<Book> books);
    void writeToServletResponse(HttpServletResponse response, Workbook workbook, String workBookName) throws IOException;
}
