import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { LucideAngularModule, Menu, X, BookOpen, LayoutDashboard, Handshake, User } from 'lucide-angular';
import { AuthService } from '../services/auth.service';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, LucideAngularModule, CommonModule],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  protected readonly isSidebarOpen = signal(false);
  protected readonly isProfileMenuOpen = signal(false);


  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly storage = inject(StorageService);


  protected readonly Menu = Menu;
  protected readonly X = X;
  protected readonly BookOpen = BookOpen;
  protected readonly LayoutDashboard = LayoutDashboard;
  protected readonly Handshake = Handshake;
  protected readonly User = User;
  protected readonly userEmail = signal<string>(this.storage.getEmail());

  protected toggleSidebar(): void {
    this.isSidebarOpen.update(value => !value);
  }

  protected closeSidebar(): void {
    this.isSidebarOpen.set(false);
  }

  protected toggleProfileMenu(): void {
    this.isProfileMenuOpen.update(v => !v);
  }

  protected closeProfileMenu(): void {
    this.isProfileMenuOpen.set(false);
  }


  protected hasRoleAdmin(): boolean {
    return this.storage.hasRole("ROLE_ADMIN");
  }

  protected logout(): void {
    this.auth.logout();
    this.closeProfileMenu();
    this.router.navigate(['/login']);
  }
}
