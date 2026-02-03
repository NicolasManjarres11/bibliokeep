export interface Loan {
  id: number; // Long
  bookId: number; // Foreign Key
  contactName: string;
  loanDate: string; // LocalDate (ISO format: YYYY-MM-DD)
  dueDate: string; // LocalDate (ISO format: YYYY-MM-DD)
  returned: boolean; // Default: false
}
