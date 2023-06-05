package com.example.assignspring.service;

import com.example.assignspring.dto.BookDto;
import com.example.assignspring.model.Book;
import com.example.assignspring.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
 class BookServiceTest {

        @Test
        void Test() {
            assertTrue(true);
        }

        @Test
        @DisplayName("Displays All Books")
        void testShowAllBooks() {
            //given
            BookRepository bookRepository = mock(BookRepository.class);
            Book b1 = new Book("The Greatest Secret", "515");
            Book b2 = new Book("The Silent Patient", "203");
            List<Book> books = Arrays.asList(b1,b2);
            given(bookRepository.findAll()).willReturn(books);
            BookService bookService=new BookService(bookRepository);
            //when
            List<BookDto> bookslist=bookService.showBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .name(book.getName())
                            .price(book.getPrice())
                            .id(book.getId())
                            .build())
                    .toList();
            //then
            verify(bookRepository, times(1)).findAll();
            Assertions.assertEquals(2,bookslist.size());
        }

        @Test
        @DisplayName("Displays Single Book")
        void testShowSingleBooks() {
            // given
            BookRepository bookRepository = mock(BookRepository.class);
            Book b1 = new  Book ("The Greatest Secret", "515");
            given(bookRepository.findById(1L)).willReturn(Optional.of(b1));
            BookService bookService = new BookService(bookRepository);

            // when
            List<BookDto> booksList = bookService.showBooks(1L)
                    .stream()
                    .map(book -> BookDto.builder()
                            .name(book.getName())
                            .price(book.getPrice())
                            .id(book.getId())
                            .build())
                    .toList();
            // then
            verify(bookRepository, times(1)).findById(1L);
            Assertions.assertEquals(1, booksList.size());
        }
        @Test
        @DisplayName("Successfully Updates a Book")
        void testUpdateBook() {
            // given
            BookRepository bookRepository = mock(BookRepository.class);
             Book existingBook = new Book("The Greatest Secret", "515");
            Book updatedBook = new Book("The Greatest Secret updated", "203");
            given(bookRepository.findById(1L)).willReturn(Optional.of(existingBook));
            given(bookRepository.save(any(Book.class))).willReturn(updatedBook);
           BookService bookService = new BookService(bookRepository);
            // when
            bookService.updateBook(1L, "The Greatest Secret updated", "700");
            //then
            verify(bookRepository, times(1)).findById(1L);
            verify(bookRepository, times(1)).save(any(Book.class));
        }
        @Test
        @DisplayName("Adds a Book")
        void testAddBook() {
            // given
            BookRepository bookRepository = mock(BookRepository.class);
           BookDto newBookDto = new BookDto("The Greatest Secret", "515",1L);
            BookService bookService = new BookService(bookRepository);
            // when
            bookService.addBook(newBookDto);
            // then
            verify(bookRepository, times(1)).save(any(Book.class));
        }
        @Test
        @DisplayName("Deletes a Book")
        void testDeleteBook() {
            // given
            BookRepository bookRepository = mock(BookRepository.class);
            BookService bookService = new BookService(bookRepository);
            // when
           bookService.deleteBook(1L);
            // then
            verify(bookRepository, times(1)).deleteById(1L);
        }
}
