package com.example.springboot.Service.workbook;

import com.example.springboot.Model.Book;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface WorkBookService {
    Workbook createWorkbook(List<Book> books);
}
