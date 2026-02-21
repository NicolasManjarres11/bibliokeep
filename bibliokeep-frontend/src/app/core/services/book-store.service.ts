import { Injectable, signal, computed, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Book, BookStatus } from '../models';
import { BookService } from './book.service';

@Injectable({
  providedIn: 'root'
})
export class BookStoreService {
  private readonly bookService = inject(BookService);
  
  // Signals
  readonly books = signal<Book[]>([]);
  readonly isLoading = signal<boolean>(false);
  readonly filterStatus = signal<BookStatus | null>(null);
  readonly searchQuery = signal<string>('');

  // Computed signal para libros filtrados
  readonly filteredBooks = computed(() => {
    const allBooks = this.books();
    const status = this.filterStatus();
    const query = this.searchQuery().toLowerCase().trim();

    let filtered = allBooks;

    // Filtrar por status
    if (status) {
      filtered = filtered.filter(book => book.status === status);
    }

    // Filtrar por búsqueda (título, autores, ISBN)
    if (query) {
      filtered = filtered.filter(book => 
        book.title.toLowerCase().includes(query) ||
        book.authors.some(author => author.toLowerCase().includes(query)) ||
        book.isbn.includes(query)
      );
    }

    return filtered;
  });

  // Métodos para actualizar el estado
  setBooks(books: Book[]): void {
    this.books.set(books);
  }

  addBook(book: Book): void {
    this.books.update(books => [...books, book]);
  }

  updateBook(updatedBook: Book): void {
    this.books.update(books =>
      books.map(book => book.id === updatedBook.id ? updatedBook : book)
    );
  }

  removeBook(bookId: number): void {
    this.books.update(books => books.filter(book => book.id !== bookId));
  }

  // Optimistic update para status
  optimisticUpdateStatus(bookId: number, status: BookStatus): void {
    this.books.update(books =>
      books.map(book =>
        book.id === bookId ? { ...book, status } : book
      )
    );
  }

  // Optimistic update para rating
  optimisticUpdateRating(bookId: number, rating: number | undefined): void {
    this.books.update(books =>
      books.map(book =>
        book.id === bookId ? { ...book, rating } : book
      )
    );
  }

  setFilterStatus(status: BookStatus | null): void {
    this.filterStatus.set(status);
  }

  setSearchQuery(query: string): void {
    this.searchQuery.set(query);
  }

  setLoading(loading: boolean): void {
    this.isLoading.set(loading);
  }

  // Métodos para cargar datos
  async loadBooks(userId: string): Promise<void> {
    this.setLoading(true);
    try {
      console.log('Loading books for userId:', userId);
      const books = await firstValueFrom(this.bookService.findAll());
      console.log('Books loaded:', books);
      console.log('Number of books:', books.length);
      if (books.length === 0) {
        console.warn('No books found for userId:', userId);
      }
      this.setBooks(books);
    } catch (error: any) {
      console.error('Error loading books:', error);
      // Mostrar más detalles del error
      if (error?.error) {
        console.error('Error response:', error.error);
      }
      if (error?.message) {
        console.error('Error message:', error.message);
      }
      // Limpiar libros en caso de error
      this.setBooks([]);
    } finally {
      this.setLoading(false);
    }
  }

  async searchBooks(query: string, userId: string): Promise<void> {
    if (!query.trim()) {
      await this.loadBooks(userId);
      return;
    }

    this.setLoading(true);
    try {
      const books = await firstValueFrom(this.bookService.search(query));
      this.setBooks(books);
    } catch (error) {
      console.error('Error searching books:', error);
    } finally {
      this.setLoading(false);
    }
  }
}
