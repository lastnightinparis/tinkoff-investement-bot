package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mvel2.MVEL;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineButton {

    private String type;
    private String name;
    private String text;
    private String baseCallback;
    private String callback;
    private String condition;
    private String link;

    public InlineKeyboardButton renderStaticButton() {
        if (type.equals("static") || type.equals("dynamic-condition")) {
            return renderCallbackButton(text, callback);
        } else if (type.equals("static-link")) {
            return renderLinkButton(text, link);
        } else {
            throw new IllegalArgumentException("This method only for render static buttons!");
        }
    }

    public List<InlineKeyboardButton> renderYesNoButtons() {
        return List.of(
                renderCallbackButton("Да", baseCallback + "_yes"),
                renderCallbackButton("Нет", baseCallback + "_no")
        );
    }

    public List<InlineKeyboardButton> renderYesNoButtons(DynamicFieldValue dynamicFieldValue) {
        String postfix = dynamicFieldValue.getPostfix();
        return List.of(
                renderCallbackButton("Да", baseCallback + "_yes_" + postfix),
                renderCallbackButton("Нет", baseCallback + "_no_" + postfix)
        );
    }

    public InlineKeyboardButton renderDynamicCallbackButton(DynamicFieldValue dynamicFieldValue) {
        if (type.equals("dynamic-callback")) {
            if (dynamicFieldValue.getBaseCallback() != null) {
                return renderCallbackButton(dynamicFieldValue.getText(),
                        dynamicFieldValue.getBaseCallback() + "_" + dynamicFieldValue.getPostfix());
            } else {
                return renderCallbackButton(dynamicFieldValue.getText(),
                        baseCallback + "_" + dynamicFieldValue.getPostfix());
            }
        } else {
            throw new IllegalArgumentException("This method only for render dynamic callback buttons!");
        }
    }

    public InlineKeyboardButton renderDynamicLinkButton(String link) {
        if (type.equals("dynamic-link")) {
            return renderLinkButton(text, link);
        } else {
            throw new IllegalArgumentException("This method only for render dynamic link buttons!");
        }
    }

    public static InlineKeyboardButton renderCallbackButton(String text, String callback) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback)
                .build();
    }

    public static InlineKeyboardButton renderLinkButton(String text, String link) {
        return InlineKeyboardButton.builder()
                .text(text)
                .url(link)
                .build();
    }

    public Optional<InlineKeyboardButton> renderConditionalButton(Map<String, Boolean> res) {
        if (type.equals("dynamic-condition")) {
            Boolean result = (Boolean) MVEL.eval(condition, res);
            if (Boolean.TRUE.equals(result)) {
                return Optional.of(renderStaticButton());
            } else {
                return Optional.empty();
            }
        } else {
            throw new IllegalArgumentException("This method only for render dynamic condition buttons!");
        }
    }
}
