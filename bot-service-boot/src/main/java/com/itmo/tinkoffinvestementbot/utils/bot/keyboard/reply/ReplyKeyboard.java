package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyKeyboard {

    private String type;
    private Boolean selective;
    private Boolean resized;
    private Boolean oneTime;

    private List<List<ReplyButton>> keyboard;

    public ReplyKeyboardMarkup generateStaticKeyboard() {
        if (!type.equals("static")) {
            throw new IllegalArgumentException("This method generate only static keyboard!");
        }
        return ReplyKeyboardMarkup.builder()
                .keyboard(
                        keyboard.stream()
                                .map(buttonRow -> {
                                    KeyboardRow keyboardRow = new KeyboardRow();
                                    buttonRow.forEach(replyButton -> {
                                        keyboardRow.add(replyButton.renderStaticButton());
                                    });
                                    return keyboardRow;
                                }).collect(Collectors.toList())
                )
                .selective(selective)
                .resizeKeyboard(resized)
                .oneTimeKeyboard(oneTime)
                .build();
    }

    public ReplyKeyboardMarkup generateDynamicKeyboard(Map<String, Boolean> conditions) {
        if (!type.equals("dynamic")) {
            throw new IllegalArgumentException("This method generate only static keyboard!");
        }
        return ReplyKeyboardMarkup.builder()
                .keyboard(
                        keyboard.stream()
                                .map(buttonRow -> {
                                    KeyboardRow keyboardRow = new KeyboardRow();
                                    buttonRow.forEach(replyButton -> {
                                        if (replyButton.getType().equals("static")) {
                                            keyboardRow.add(replyButton.renderStaticButton());
                                        } else if (replyButton.getType().equals("dynamic-condition")) {
                                            replyButton.renderConditionalButton(conditions).ifPresent(keyboardRow::add);
                                        }
                                    });
                                    return keyboardRow;
                                }).collect(Collectors.toList())
                )
                .selective(selective)
                .resizeKeyboard(resized)
                .oneTimeKeyboard(oneTime)
                .build();
    }
}
