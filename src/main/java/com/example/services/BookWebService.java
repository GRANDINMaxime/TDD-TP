package com.example.services;

import com.example.models.*;
import com.example.repositories.*;

public class BookWebService extends Book {
        private final String isbn;
        private final String title;
        private final String author;
        private final String publisher;
        private final Format format;
        private final boolean inStock;

        private BookRepository repository;
        public BookWebService(String isbn, String title, String author, String publisher, Format format, boolean inStock) {
            super(isbn, title, author, publisher, Format.BROCHE,false);
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.format = format;
            this.inStock = inStock;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getPublisher() {
            return publisher;
        }

        public Format getFormat() {
            return format;
        }

        public boolean isInStock() {
            return inStock;
        }

        @Override
        public String toString() {
            return "BookWebServiceResponse{" +
                    "isbn='" + isbn + '\'' +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", publisher='" + publisher + '\'' +
                    ", format=" + format +
                    ", inStock=" + inStock +
                    '}';
        }

    public Book getBookByISBN(String number) {
            return repository.findByIsbn(number);
    }
}

