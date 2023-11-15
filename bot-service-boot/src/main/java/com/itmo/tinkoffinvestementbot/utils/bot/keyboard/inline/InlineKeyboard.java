package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.LinkedList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineKeyboard {

    private String type;
    private String handler;

    private String baseCallback;

    @SerializedName("max-column-size")
    private int maxColumnSize;

    @SerializedName("dynamic-fields")
    private List<String> dynamicFields;

    private List<List<InlineButton>> keyboard;

    public InlineKeyboardMarkup generateStaticKeyboard() {
        if (!type.equals("static")) {
            throw new IllegalArgumentException("This method generate only static keyboard!");
        }
        return new InlineKeyboardMarkup(
                keyboard.stream().map(row -> row.stream().flatMap(button -> {
                    switch (button.getType()) {
                        case "static":
                            return Stream.of(button.renderStaticButton());
                        case "yes-no":
                            return button.renderYesNoButtons().stream();
                        default:
                            return Stream.empty();
                    }
                }).collect(Collectors.toList())).collect(Collectors.toList())
        );
    }

    public InlineKeyboardMarkup generateFromResourcesKeyboard(
            List<? extends InlineButtonWrappedObject> objectList
    ) {
        return generateFromResourcesKeyboard(objectList, this.baseCallback);
    }

    public InlineKeyboardMarkup generateFromResourcesKeyboard(
            List<? extends InlineButtonWrappedObject> objectList,
            String baseCallback
    ) {
        if (!type.contains("from-resource")) {
            throw new IllegalArgumentException(
                    "This method generate only static from resources keyboard!");
        }
        int m = objectList.size() / maxColumnSize;
        int rowSize = objectList.size() % maxColumnSize == 0 ? m : m + 1;
        return new InlineKeyboardMarkup(
                IntStream.range(0, rowSize)
                        .mapToObj(i -> {
                            int fromIndex = i * maxColumnSize;
                            int toIndex = Math.min(i * maxColumnSize + maxColumnSize, objectList.size());
                            return IntStream.range(fromIndex, toIndex)
                                    .mapToObj(index -> {
                                        InlineButtonWrappedObject wrappedObject = objectList.get(index);
                                        return InlineButton.renderCallbackButton(
                                                wrappedObject.getName(),
                                                baseCallback + "_" + wrappedObject.getKey()
                                        );
                                    }).collect(Collectors.toCollection(LinkedList::new));
                        }).collect(Collectors.toCollection(LinkedList::new))
        );
    }

    public InlineKeyboardMarkup generateDynamicFieldsKeyboard(
            Map<String, DynamicFieldValue> dynamicValuesMap
    ) {
        if (!type.equals("dynamic-fields")) {
            throw new IllegalArgumentException("This method generate only dynamic fields keyboard!");
        }
        return new InlineKeyboardMarkup(
                keyboard.stream().map(row -> row.stream().flatMap(button -> {
                    switch (button.getType()) {
                        case "static":
                            return Stream.of(button.renderStaticButton());
                        case "yes-no":
                            return button.renderYesNoButtons().stream();
                        case "dynamic-yes-no":
                            return button.renderYesNoButtons(
                                    dynamicValuesMap.get(button.getName())
                            ).stream();
                        case "dynamic-callback":
                            return Stream.of(button.renderDynamicCallbackButton(
                                    dynamicValuesMap.get(button.getName())
                            ));
                        case "dynamic-link":
                            return Stream.of(
                                    button.renderDynamicLinkButton(
                                            dynamicValuesMap.get(button.getName()).getLink()
                                    )
                            );
                        default:
                            return Stream.empty();
                    }
                }).collect(Collectors.toList())).collect(Collectors.toList())
        );
    }

    public InlineKeyboardMarkup generateDynamicConditionsKeyboard(
            Map<String, Boolean> conditions
    ) {
        if (!type.equals("dynamic-conditions")) {
            throw new IllegalArgumentException("This method generate only dynamic conditions keyboard!");
        }
        return new InlineKeyboardMarkup(
                keyboard.stream().map(row -> row.stream().flatMap(button -> {
                    switch (button.getType()) {
                        case "static":
                            return Stream.of(button.renderStaticButton());
                        case "yes-no":
                            return button.renderYesNoButtons().stream();
                        case "dynamic-condition":
                            return button.renderConditionalButton(conditions).stream();
                        default:
                            return Stream.empty();
                    }
                }).collect(Collectors.toList())).collect(Collectors.toList())
        );
    }
}
