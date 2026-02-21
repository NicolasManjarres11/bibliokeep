import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(StorageService)
  private readonly apiUrl = 'http://localhost:8080/api/books';



  search(query: string): Observable<Book[]> {
    const params = new HttpParams().set('query', query);
    
    return this.http.get<Book[]>(`${this.apiUrl}/search`, { params });
  }

  findAll(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
  }

  findById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  create(book: Partial<Book>): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, book);
  }

  update(id: number, book: Partial<Book>): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}`, book);
  }

  updateStatus(id: number, status: string): Observable<Book> {
    return this.http.patch<Book>(`${this.apiUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
