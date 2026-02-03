import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { LucideAngularModule, Menu, X, BookOpen, LayoutDashboard, Handshake, User } from 'lucide-angular';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, LucideAngularModule],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  protected readonly isSidebarOpen = signal(false);
  protected readonly Menu = Menu;
  protected readonly X = X;
  protected readonly BookOpen = BookOpen;
  protected readonly LayoutDashboard = LayoutDashboard;
  protected readonly Handshake = Handshake;
  protected readonly User = User;

  protected toggleSidebar(): void {
    this.isSidebarOpen.update(value => !value);
  }

  protected closeSidebar(): void {
    this.isSidebarOpen.set(false);
  }
}
