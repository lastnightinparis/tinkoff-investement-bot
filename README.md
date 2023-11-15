# tinkoff-investement-bot

для работы контроллера необходимо проставить личный токен в качестве "api.token" в пропертях
http://localhost:9999/currentCandles/SBER - простейшая ручка, возвращает часовые свечи за день
http://localhost:9999/customCandles?ticker=SBER&startDate=2023-11-12T07:00:00Z&endDate=2023-11-13T23:00:00Z&intervalMin=30 - кастомизированная ручка 