export interface User {
  id: string; // UUID
  email: string;
  password?: string; // BCrypt encoded, optional in responses
  preferences: string[]; // Géneros favoritos
  annualGoal: number; // Default: 12
}
