package com.itmo.tinkoffinvestementbot.service.bot;

import com.itmo.tinkoffinvestementbot.model.bot.TradingBot;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.itmo.tinkoffinvestementbot.model.domain.User;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class BotSenderServiceImpl implements BotSenderService {

    @Value("#{'${app.mastersId}'.split(',')}")
    private List<String> mastersId;

    private final TradingBot tradingBot;
    private final UserService userService;
    private final ExecutorService executorService;

    @Override
    public CompletableFuture<Serializable> sendEditMessageTextAsync(EditMessageText message) {
        try {
            return tradingBot.executeAsync(message)
                    .handleAsync((res, ex) -> handleException(ex, message.getChatId(), res));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message sendMessage(SendMessage message) {
        try {
            return tradingBot.execute(message);
        } catch (TelegramApiException e) {
            handleException(e, message.getChatId(), null);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Message> sendMessageAsync(SendMessage message, long sleepTime) {
        CompletableFuture<Message> future;
        try {
            if (sleepTime == 0) {
                future = tradingBot.executeAsync(message)
                        .handleAsync((res1, ex) ->
                                (Message) handleException(ex, message.getChatId(), res1));
            } else {
                future = tradingBot.executeAsync(
                        SendChatAction.builder()
                                .chatId(message.getChatId())
                                .action(ActionType.TYPING.toString())
                                .build()
                ).thenCompose(res -> {
                    try {
                        Thread.sleep(sleepTime * 1000);
                        return tradingBot.executeAsync(message)
                                .handleAsync((res1, ex) ->
                                        (Message) handleException(ex, message.getChatId(), res1));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return future;
    }

    @Override
    public CompletableFuture<Message> sendDocumentAsync(SendDocument message) {
        return tradingBot.executeAsync(message);
    }

    @Override
    public Boolean sendAnswerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            return tradingBot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void notifyMasters(String message) {
        mastersId.forEach(masterChatId -> {
            sendMessageAsync(
                    SendMessage.builder()
                            .chatId(masterChatId)
                            .text(message)
                            .build(), 0
            );
        });
    }

    @Override
    public Future<Boolean> sendNewsLetter(List<Long> chatIds, String message,
                                          InlineKeyboardMarkup keyboardMarkup) {
        return executorService.submit(() -> {
            chatIds.forEach(chatId -> {
                try {
                    Thread.sleep(2050);
                    tradingBot.execute(
                            SendMessage.builder()
                                    .chatId(String.valueOf(chatId))
//                  .parseMode(ParseMode.MARKDOWN)
                                    .text(message)
                                    .replyMarkup(keyboardMarkup)
                                    .build()
                    );
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (TelegramApiException e) {
                    handleException(e, String.valueOf(chatId), null);
                }
            });
            return true;
        });
    }

    private Serializable handleException(Throwable ex, String chatId, Serializable res) {
        if (ex != null) {
            if (ex.getMessage().contains("bot was blocked by the user")) {
                User user = userService.getUserByChatId(Long.parseLong(chatId));
                user.setBlocked(true);
                userService.save(user);
            } else {
                ex.printStackTrace();
                return null;
            }
        }
        return res;
    }
}
