package com.itmo.tinkoffinvestementbot.service.bot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface BotSenderService {

    Message sendMessage(SendMessage message);

    CompletableFuture<Message> sendDocumentAsync(SendDocument message);

    Boolean sendAnswerCallbackQuery(AnswerCallbackQuery answerCallbackQuery);

    CompletableFuture<Serializable> sendEditMessageTextAsync(EditMessageText editMessageText);

    CompletableFuture<Message> sendMessageAsync(SendMessage message, long sleepTime);

    void notifyMasters(String message);

    Future<Boolean> sendNewsLetter(List<Long> chatIds, String message,
                                   InlineKeyboardMarkup keyboardMarkup);
}
