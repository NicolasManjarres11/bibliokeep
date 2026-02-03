import { Component, Input, output, inject } from '@angular/core';
import { Book, BookStatus } from '../../../core/models';
import { BookService } from '../../../core/services/book.service';
import { BookStoreService } from '../../../core/services/book-store.service';
import { UserHelperService } from '../../../core/services/user-helper.service';
import { LucideAngularModule, Star, StarOff, BookOpen } from 'lucide-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.css'
})
export class BookCardComponent {
  @Input({ required: true }) book!: Book;
  
  readonly statusChange = output<{ bookId: number; status: BookStatus }>();
  readonly ratingChange = output<{ bookId: number; rating: number | undefined }>();

  private readonly bookService = inject(BookService);
  private readonly bookStore = inject(BookStoreService);
  private readonly userHelper = inject(UserHelperService);

  private async getUserId(): Promise<string> {
    return await this.userHelper.getCurrentUserId();
  }

  readonly Star = Star;
  readonly StarOff = StarOff;
  readonly BookOpen = BookOpen;
  readonly BookStatus = BookStatus;

  async onStatusChange(status: BookStatus): Promise<void> {
    const previousStatus = this.book.status;
    const userId = await this.getUserId();
    
    // Optimistic UI: actualizar localmente primero
    this.bookStore.optimisticUpdateStatus(this.book.id, status);
    this.statusChange.emit({ bookId: this.book.id, status });

    // Actualizar en el servidor
    this.bookService.updateStatus(this.book.id, status, userId).subscribe({
      next: (updatedBook) => {
        this.bookStore.updateBook(updatedBook);
      },
      error: (error) => {
        console.error('Error updating status:', error);
        // Revertir el cambio optimista
        this.bookStore.optimisticUpdateStatus(this.book.id, previousStatus);
      }
    });
  }

  async onRatingChange(rating: number | undefined): Promise<void> {
    const previousRating = this.book.rating;
    const userId = await this.getUserId();
    
    // Optimistic UI: actualizar localmente primero
    this.bookStore.optimisticUpdateRating(this.book.id, rating);
    this.ratingChange.emit({ bookId: this.book.id, rating });

    // Actualizar en el servidor
    const updatedBook = { ...this.book, rating };
    this.bookService.update(this.book.id, updatedBook, userId).subscribe({
      next: (book) => {
        this.bookStore.updateBook(book);
      },
      error: (error) => {
        console.error('Error updating rating:', error);
        // Revertir el cambio optimista
        this.bookStore.optimisticUpdateRating(this.book.id, previousRating);
      }
    });
  }

  getStatusColor(status: BookStatus): string {
    const colors: Record<BookStatus, string> = {
      [BookStatus.DESEADO]: 'bg-purple-100 text-purple-700',
      [BookStatus.COMPRADO]: 'bg-blue-100 text-blue-700',
      [BookStatus.LEYENDO]: 'bg-yellow-100 text-yellow-700',
      [BookStatus.LEIDO]: 'bg-green-100 text-green-700',
      [BookStatus.ABANDONADO]: 'bg-red-100 text-red-700'
    };
    return colors[status] || 'bg-gray-100 text-gray-700';
  }

  getStatusLabel(status: BookStatus): string {
    const labels: Record<BookStatus, string> = {
      [BookStatus.DESEADO]: 'Deseado',
      [BookStatus.COMPRADO]: 'Comprado',
      [BookStatus.LEYENDO]: 'Leyendo',
      [BookStatus.LEIDO]: 'Leído',
      [BookStatus.ABANDONADO]: 'Abandonado'
    };
    return labels[status] || status;
  }
}
