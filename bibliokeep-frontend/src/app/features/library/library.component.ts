import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookStoreService } from '../../core/services/book-store.service';
import { UserHelperService } from '../../core/services/user-helper.service';
import { BookCardComponent } from '../../shared/components/book-card/book-card.component';
import { BookStatus } from '../../core/models';
import { LucideAngularModule, Search, Filter } from 'lucide-angular';

@Component({
  selector: 'app-library',
  standalone: true,
  imports: [CommonModule, FormsModule, BookCardComponent, LucideAngularModule],
  templateUrl: './library.component.html',
  styleUrl: './library.component.css'
})
export class LibraryComponent implements OnInit {
  private readonly bookStore = inject(BookStoreService);
  private readonly userHelper = inject(UserHelperService);

  private userId: string | null = null;

  readonly Search = Search;
  readonly Filter = Filter;
  readonly BookStatus = BookStatus;

  searchInput = signal<string>('');
  showFilters = signal<boolean>(false);

  // Exponer signals del store
  readonly books = this.bookStore.filteredBooks;
  readonly isLoading = this.bookStore.isLoading;

  async ngOnInit(): Promise<void> {
    try {
      // Obtener userId automáticamente desde el backend
      this.userId = await this.userHelper.getCurrentUserId();
      console.log('LibraryComponent initialized with userId:', this.userId);
      await this.bookStore.loadBooks(this.userId);
    } catch (error) {
      console.error('Error inicializando LibraryComponent:', error);
    }
  }

  async onSearch(query: string): Promise<void> {
    if (!this.userId) {
      this.userId = await this.userHelper.getCurrentUserId();
    }

    this.searchInput.set(query);
    this.bookStore.setSearchQuery(query);
    
    if (query.trim()) {
      await this.bookStore.searchBooks(query, this.userId);
    } else {
      await this.bookStore.loadBooks(this.userId);
    }
  }

  onFilterChange(status: BookStatus | 'ALL'): void {
    if (status === 'ALL') {
      this.bookStore.setFilterStatus(null);
    } else {
      this.bookStore.setFilterStatus(status);
    }
  }

  toggleFilters(): void {
    this.showFilters.update(value => !value);
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
