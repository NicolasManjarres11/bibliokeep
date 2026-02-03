package com.devsenior.nmanja.bibliokeep.controller;

import com.devsenior.nmanja.bibliokeep.model.dto.DashboardStatsDTO;
import com.devsenior.nmanja.bibliokeep.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dashboard")
    public DashboardStatsDTO getDashboard(@RequestHeader("user-id") String userId) {
        return statsService.getDashboardStats(UUID.fromString(userId));
    }
}
