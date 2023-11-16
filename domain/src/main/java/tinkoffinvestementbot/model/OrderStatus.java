package tinkoffinvestementbot.model;

import ru.tinkoff.piapi.contract.v1.OrderExecutionReportStatus;

import java.util.Map;

/**
 * Статус ордера
 */
public enum OrderStatus {
    CREATED, COMPLETED, PARTIALLY_COMPLETED, REJECTED, WONT_EXECUTE, UNKNOWN;

    private static final Map<OrderExecutionReportStatus, OrderStatus> STATUS_MAP = Map.of(
            OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_NEW, CREATED,
            OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_FILL, COMPLETED,
            OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_PARTIALLYFILL, PARTIALLY_COMPLETED,
            OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_REJECTED, REJECTED,
            OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_CANCELLED, WONT_EXECUTE
    );

    public static OrderStatus getByExecutionReportStatus(OrderExecutionReportStatus executionReportStatus) {
        if (!STATUS_MAP.containsKey(executionReportStatus)) {
            return UNKNOWN;
        }

        return STATUS_MAP.get(executionReportStatus);
    }
}