package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mvel2.MVEL;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyButton {

    private String type;
    private String name;
    private String text;
    private String condition;
    private Boolean requestContact;
    private Boolean requestLocation;

    public KeyboardButton renderStaticButton() {
        if (type.equals("static")) {
            return renderButton();
        } else {
            throw new IllegalArgumentException("This method only for render static buttons!");
        }
    }

    public Optional<KeyboardButton> renderConditionalButton(Map<String, Boolean> res) {
        if (type.equals("dynamic-condition")) {
            Boolean result = (Boolean) MVEL.eval(condition, res);
            if (Boolean.TRUE.equals(result)) {
                return Optional.of(renderButton());
            } else {
                return Optional.empty();
            }
        } else {
            throw new IllegalArgumentException("This method only for render dynamic condition buttons!");
        }
    }

    private KeyboardButton renderButton() {
        return KeyboardButton.builder()
                .text(text)
                .requestContact(requestContact != null && requestContact)
                .requestLocation(requestLocation != null && requestLocation)
                .build();
    }
}
