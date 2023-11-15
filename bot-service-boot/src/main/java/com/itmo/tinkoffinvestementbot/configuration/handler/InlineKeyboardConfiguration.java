package com.itmo.tinkoffinvestementbot.configuration.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.keyboard.InlineKeyboardHandler;
import com.itmo.tinkoffinvestementbot.utils.StrategyUtils;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineKeyboard;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.reply.ReplyKeyboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class InlineKeyboardConfiguration {

    private Map<String, InlineKeyboardHandler> inlineKeyboardHandlersMap;
    private Map<String, InlineKeyboard> inlineKeyboardMap;

    @Bean
    public Map<String, InlineKeyboardHandler> inlineKeyboardHandlersMap(
            List<InlineKeyboardHandler> inlineKeyboardHandlerList) {
        inlineKeyboardHandlersMap = StrategyUtils.stringHandlersMap(inlineKeyboardHandlerList);
        return inlineKeyboardHandlersMap;
    }

    @Bean
    public Map<String, InlineKeyboard> inlineKeyboardMap() throws IOException {
        Gson gson = new Gson();

        Optional<File> keyboardFile = Arrays.stream(
                        Objects.requireNonNull(
                                new File(
                                        Objects.requireNonNull(
                                                this.getClass().getClassLoader()
                                                        .getResource("keyboards")
                                        ).getFile()
                                ).listFiles())
                ).filter(file -> file.getName().equals("inline-keyboards.json"))
                .findFirst();

        if (keyboardFile.isPresent()) {
            Reader reader = Files.newBufferedReader(
                    keyboardFile.get().toPath()
            );

            Type type =
                    new TypeToken<LinkedHashMap<String, InlineKeyboard>>() {
                    }.getType();
            inlineKeyboardMap = gson.fromJson(reader, type);
            reader.close();
            return inlineKeyboardMap;
        }
        throw new IOException("Keyboard config not found");
    }

    @Bean
    @DependsOn({"inlineKeyboardMap", "inlineKeyboardHandlersMap"})
    public Map<String, InlineKeyboardMarkup> staticInlineKeyboardMarkupMap() {
        return inlineKeyboardMap.entrySet().stream()
                .filter(entry -> entry.getValue().getType().contains("static"))
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> {
                                    InlineKeyboard inlineKeyboard = entry.getValue();
                                    if (inlineKeyboard.getType().equals("static")) {
                                        return inlineKeyboard.generateStaticKeyboard();
                                    } else if (inlineKeyboard.getType().equals("static-from-resource")) {
                                        if (inlineKeyboardHandlersMap.containsKey(inlineKeyboard.getHandler())) {
                                            return inlineKeyboard.generateFromResourcesKeyboard(
                                                    inlineKeyboardHandlersMap.get(inlineKeyboard.getHandler())
                                                            .getData(inlineKeyboard.getBaseCallback())
                                            );
                                        } else {
                                            throw new IllegalTradingBotException(
                                                    "Unknown inline keyboard handler - " + inlineKeyboard.getHandler());
                                        }

                                    } else {
                                        throw new IllegalTradingBotException(
                                                "Unknown inline keyboard type - " + inlineKeyboard.getType());
                                    }
                                }
                        )
                );
    }

    @Bean
    @DependsOn({"inlineKeyboardMap"})
    public Map<String, InlineKeyboard> dynamicInlineKeyboard() {
        return inlineKeyboardMap.entrySet().stream()
                .filter(entry -> entry.getValue().getType().contains("dynamic"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
