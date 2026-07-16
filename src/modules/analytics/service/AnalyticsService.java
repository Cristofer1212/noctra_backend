package modules.analytics.service;

import config.exception.DatabaseConnectionException;
import modules.analytics.repository.AnalyticsRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService() {
        this.analyticsRepository = new AnalyticsRepository();
    }

    public List<Map<String, Object>> obtenerRankingReingresos() throws DatabaseConnectionException {
        return analyticsRepository.getTopReingresos();
    }
}