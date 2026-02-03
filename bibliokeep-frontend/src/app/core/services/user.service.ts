import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/users';

  getFirstUserId(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/first`);
  }
}
