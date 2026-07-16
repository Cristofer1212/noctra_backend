package modules.analytics.controller;

import modules.analytics.service.AnalyticsService;
import java.util.List;
import java.util.Map;

public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController() {
        this.analyticsService = new AnalyticsService();
    }

    // Este método lo llamarías desde tu vista o API para mostrar el reporte
    public void imprimirReporteReingresos() {
        try {
            List<Map<String, Object>> ranking = analyticsService.obtenerRankingReingresos();
            System.out.println("--- Reporte de Reingresos ---");
            ranking.forEach(fila ->
                    System.out.println("ID Invitación: " + fila.get("invitation_id") +
                            " | Entradas: " + fila.get("total_movimientos"))
            );
        } catch (Exception e) {
            System.err.println("Error al cargar el reporte: " + e.getMessage());
        }
    }
}