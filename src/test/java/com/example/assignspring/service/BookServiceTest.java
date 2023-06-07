package com.example.assignspring.service;

import com.example.assignspring.dto.BookDto;
import com.example.assignspring.model.Book;
import com.example.assignspring.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
     @ParameterizedTest
     @DisplayName("Update Book ")
     @CsvSource({
             "'The Greatest Secret', '515'"
     })
     void testUpdatePhones1(String name, String price) {
         // Given
         BookRepository bookRepository = mock(BookRepository.class);
         Book existingBook = new Book("The Greatest Secret", "515");
         given(bookRepository.findById(1L)).willReturn(Optional.of(existingBook));
         BookService bookService = new BookService(bookRepository);
        BookDto book = new BookDto(name, price, 1L);
         // When
         bookService.updateBook(1L, book);
         // Then
         verify(bookRepository, times(1)).findById(1L);
         verify(bookRepository, never()).save(any(Book.class));
     }

     @ParameterizedTest
     @DisplayName("Update Book Test Cases-2")
     @CsvSource({
             "null, null",                   // No changes
             "null, '1000'",                  // Only price changed
             "' The Greatest Secret', null",      // Only name changed
             "'The Greatest Secret', '1000'",     // Both name and price changed
             "'The Greatest Secret', null",              // Name same as existing, only price changed
             "null, '515'",                  // Price same as existing, only name changed
             "'The Greatest Secret updated', '25k'",     // Only name changed, price same as existing
             "'The Greatest Secret', '1000'",              // Only price changed, name same as existing
             "'', '1000'",                     // Empty name, only price changed
             "'The Greatest Secret updated', ''",      // Only name changed, empty price
             "'', 'null'",
             "null, ''",
             "'', ''",
             "'The Greatest Secret', ''",                 // Name same as existing, empty price
             "'', '1000'",                     // Empty name, price same as existing
     })
     void testUpdatePhones2(String name, String price) {
         // given
         BookRepository bookRepository = mock(BookRepository.class);
        Book existingBook = new Book("samsung", "25k");
         Book updatedBook = new Book();
         updatedBook.setName(name);
         updatedBook.setPrice(price);
         given(bookRepository.findById(1L)).willReturn(Optional.of(existingBook));
         given(bookRepository.save(any(Book.class))).willReturn(updatedBook);
         System.out.println(updatedBook.getName());
         System.out.println(existingBook.getName());
         System.out.println(updatedBook);
         System.out.println(existingBook);
         BookService bookService = new BookService(bookRepository);
        BookDto book = new BookDto(name, price, 1L);
         // when
         bookService.updateBook(1L, book);
         System.out.println();
         // then
         verify(bookRepository, times(1)).findById(1L);
         if (!existingBook.equals(updatedBook)) {
             verify(bookRepository, times(1)).save(any(Book.class));
         } else {
             verify(bookRepository, never()).save(any(Book.class));
         }
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
