package tinkoffinvestementbot.model;

import ru.tinkoff.piapi.contract.v1.OrderExecutionReportStatus;

import java.util.Map;

public record OrderResult(String id, Status status, Long fulfilledQuantity, Double commission, Double totalPrice) {
    public enum Status {
        CREATED, COMPLETED, PARTIALLY_COMPLETED, REJECTED, WONT_EXECUTE, UNKNOWN;

        private static final Map<OrderExecutionReportStatus, Status> STATUS_MAP = Map.of(
                OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_NEW, CREATED,
                OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_FILL, COMPLETED,
                OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_PARTIALLYFILL, PARTIALLY_COMPLETED,
                OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_REJECTED, REJECTED,
                OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_CANCELLED, WONT_EXECUTE
        );

        public static Status getByExecutionReportStatus(OrderExecutionReportStatus executionReportStatus) {
            if (!STATUS_MAP.containsKey(executionReportStatus)) {
                return UNKNOWN;
            }

            return STATUS_MAP.get(executionReportStatus);
        }
    }
}
