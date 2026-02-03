package com.devsenior.nmanja.bibliokeep.model.dto;

public record DashboardStatsDTO(
        Integer totalBooks,
        Integer booksRead,
        Integer booksReading,
        Integer activeLoans,
        Integer overdueLoans,
        Integer annualGoal,
        Integer booksReadThisYear,
        Double progressPercentage
) {
}
