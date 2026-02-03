import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/books';

  search(query: string, userId: string): Observable<Book[]> {
    const headers = new HttpHeaders().set('user-id', userId);
    const params = new HttpParams().set('query', query);
    
    return this.http.get<Book[]>(`${this.apiUrl}/search`, { headers, params });
  }

  findAll(userId: string): Observable<Book[]> {
    const headers = new HttpHeaders().set('user-id', userId);
    console.log('Fetching books with userId:', userId);
    return this.http.get<Book[]>(this.apiUrl, { headers });
  }

  findById(id: number, userId: string): Observable<Book> {
    const headers = new HttpHeaders().set('user-id', userId);
    return this.http.get<Book>(`${this.apiUrl}/${id}`, { headers });
  }

  create(book: Partial<Book>, userId: string): Observable<Book> {
    const headers = new HttpHeaders().set('user-id', userId);
    return this.http.post<Book>(this.apiUrl, book, { headers });
  }

  update(id: number, book: Partial<Book>, userId: string): Observable<Book> {
    const headers = new HttpHeaders().set('user-id', userId);
    return this.http.put<Book>(`${this.apiUrl}/${id}`, book, { headers });
  }

  updateStatus(id: number, status: string, userId: string): Observable<Book> {
    const headers = new HttpHeaders().set('user-id', userId);
    return this.http.patch<Book>(`${this.apiUrl}/${id}/status`, { status }, { headers });
  }

  delete(id: number, userId: string): Observable<void> {
    const headers = new HttpHeaders().set('user-id', userId);
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }
}
