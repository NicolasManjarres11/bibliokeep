package com.devsenior.nmanja.bibliokeep.service;

import com.devsenior.nmanja.bibliokeep.model.dto.DashboardStatsDTO;

import java.util.UUID;

public interface StatsService {

    DashboardStatsDTO getDashboardStats(UUID ownerId);
}
