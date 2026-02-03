import { BookStatus } from './book-status.enum';

export interface Book {
  id: number; // Long (Auto-increment)
  ownerId: string; // UUID
  isbn: string; // 10 o 13 dígitos
  title: string;
  authors: string[]; // List<String>
  description?: string;
  thumbnail?: string; // URL de imagen
  status: BookStatus; // Enum
  rating?: number; // 1-5
  isLent: boolean; // Default: false
}
