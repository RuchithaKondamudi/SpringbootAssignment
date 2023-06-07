package com.example.assignspring.service;


import com.example.assignspring.dto.BookDto;
import com.example.assignspring.model.Book;
import com.example.assignspring.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;

    public Optional<BookDto>  showBooks(Long id){
        return bookRepository.findById(id).stream().map(books->BookDto.builder()
                .name(books.getName())
                .price(books.getPrice())
                .id(books.getId())
                .build()
        ).findFirst();
    }
    public List<BookDto> showBooks() {
        return bookRepository.findAll().stream().map(books -> BookDto.builder()
                .name(books.getName())
                .price(books.getPrice())
                .id(books.getId())
                .build()
        ).toList();
    }
    @Transactional
    public void addBook(BookDto newBook) {
        Book book = new Book();
        book.setName(newBook.getName());
        book.setPrice(newBook.getPrice());
        log.info("Saving Book Data..");
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void updateBook(Long bookId, BookDto bookDto) {
        String name= bookDto.getName();
        String price= bookDto.getPrice();
        Book book =bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("No such phone found"));

        boolean isNameChanged =   !Objects.equals(name,book.getName());
        boolean isPriceChanged =  !Objects.equals(price, book.getPrice());

        if (isNameChanged) {
            book.setName(name);
        }

        if (isPriceChanged) {
            book.setPrice(price);
        }

        if (isNameChanged || isPriceChanged) {
            log.info("Updating...........");
            bookRepository.save(book);
            log.info("Updated successfully.........");
        }
    }

}
