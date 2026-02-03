import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  protected readonly annualGoal = signal(12);
  protected readonly readingCount = signal(0);
  protected readonly activeLoans = signal(0);
}
