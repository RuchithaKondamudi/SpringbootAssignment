package com.example.assignspring.controller;


import com.example.assignspring.dto.BookDto;
import com.example.assignspring.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {


    @Autowired
    private final BookService bookService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<BookDto> showBooks(@PathVariable Long id){

        return bookService.showBooks(id);
    }
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> showBooks() {

        return  bookService.showBooks();
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String addBook(@RequestBody BookDto newBook) {
        bookService.addBook(newBook);
        return "Book Added";
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return "Book Deleted";
    }
    @PutMapping("/{id}")
    public String updateStudent(@PathVariable Long id,@RequestBody BookDto book){

        bookService.updateBook(id,book);
        return "Phone Data Updated";
    }
}

