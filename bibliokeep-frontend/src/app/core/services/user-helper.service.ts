import { Injectable, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { UserService } from './user.service';

/**
 * Servicio para obtener el userId automáticamente desde el backend
 * TODO: Reemplazar con el servicio de autenticación real cuando se implemente
 */
@Injectable({
  providedIn: 'root'
})
export class UserHelperService {
  private readonly userService = inject(UserService);
  private readonly userIdSignal = signal<string | null>(null);

  /**
   * Obtiene el userId del usuario actual
   * Primero intenta obtenerlo del localStorage (cache)
   * Si no existe, lo obtiene del backend automáticamente
   */
  async getCurrentUserId(): Promise<string> {
    // Si ya tenemos el userId en el signal, devolverlo
    const cachedUserId = this.userIdSignal();
    if (cachedUserId) {
      return cachedUserId;
    }

    // Intentar obtener del localStorage (cache)
    const storedUserId = localStorage.getItem('bibliokeep-user-id');
    if (storedUserId) {
      this.userIdSignal.set(storedUserId);
      return storedUserId;
    }

    // Obtener del backend automáticamente
    try {
      console.log('Obteniendo userId desde el backend...');
      const userId = await firstValueFrom(this.userService.getFirstUserId());
      console.log('UserId obtenido del backend:', userId);
      
      // Guardar en cache y localStorage
      this.userIdSignal.set(userId);
      localStorage.setItem('bibliokeep-user-id', userId);
      
      return userId;
    } catch (error) {
      console.error('Error obteniendo userId del backend:', error);
      throw new Error('No se pudo obtener el userId. Asegúrate de que haya al menos un usuario registrado.');
    }
  }

  /**
   * Guarda el userId en localStorage y cache
   */
  setUserId(userId: string): void {
    this.userIdSignal.set(userId);
    localStorage.setItem('bibliokeep-user-id', userId);
    console.log('UserId guardado:', userId);
  }

  /**
   * Limpia el userId del cache (útil para logout)
   */
  clearUserId(): void {
    this.userIdSignal.set(null);
    localStorage.removeItem('bibliokeep-user-id');
  }
}
