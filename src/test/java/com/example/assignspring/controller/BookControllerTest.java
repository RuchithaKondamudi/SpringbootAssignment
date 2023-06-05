package com.example.assignspring.controller;


import com.example.assignspring.dto.BookDto;
import com.example.assignspring.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @DisplayName("Test GET /api/v1/books/{id}")
    void testShowBooksById() throws Exception {

        Long id = 1L;
        BookDto bookDto = new BookDto("The Greatest Secret", "515",1L);


        when(bookService.showBooks(id)).thenReturn(Optional.of(bookDto));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("The Greatest Secret"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("515"));



        verify(bookService, times(1)).showBooks(id);
    }


    @Test
    @DisplayName("Verify GET Method for retrieving all books")
    void shouldRetrieveAllBooks() throws Exception {

        List<BookDto> expectedBooks = Arrays.asList(
                new BookDto("The Greatest Secret", "515",1L),
                new BookDto("The Silent Patient", "203",2L)
        );



        when(bookService.showBooks()).thenReturn(expectedBooks);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{ \"name\": \"The Greatest Secret\", \"price\": \"515\" }, { \"name\": \"The Silent Patient\", \"price\": \"203\" }]"));


        verify(bookService, times(1)).showBooks();
    }


    @Test
    @DisplayName("Verify POST Method for adding a book")
    void testAddBook() throws Exception {

        BookDto expectedBook = new BookDto("The Greatest Secret", "515",1L);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/")

                .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBook)))
                .andExpect(status().isCreated());


        ArgumentCaptor<BookDto> bookDtoCaptor = ArgumentCaptor.forClass(BookDto.class);


        verify(bookService, times(1)).addBook(bookDtoCaptor.capture());
        BookDto capturedBookDto = bookDtoCaptor.getValue();


        assertEquals(expectedBook.getName(), capturedBookDto.getName());
        assertEquals(expectedBook.getPrice(), capturedBookDto.getPrice());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test DELETE /api/v1/books/{id}")
    void testDeleteBook() throws Exception {

        Long id = 1L;


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(bookService, times(1)).deleteBook(id);
    }

    @Test
    @DisplayName("Test PUT /api/v1/books/{id}")
    void testUpdateBook() throws Exception {

        Long id = 1L;
        String name = "Updated The Greatest Secret";
        String price = "700";


        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", id)
                        .param("name", name)
                        .param("price", price)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(bookService, times(1)).updateBook(id, name, price);
    }


}
