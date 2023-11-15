package com.itmo.tinkoffinvestementbot.configuration.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.reply.ReplyKeyboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class ReplyKeyboardConfiguration {

    private Map<String, ReplyKeyboard> replyKeyboardMap;

    @Bean
    public Map<String, ReplyKeyboard> replyKeyboardMap() throws IOException {
        Gson gson = new Gson();

        Optional<File> keyboardFile = Arrays.stream(
                        Objects.requireNonNull(
                                new File(
                                        Objects.requireNonNull(
                                                this.getClass().getClassLoader()
                                                        .getResource("keyboards")
                                        ).getFile()
                                ).listFiles())
                ).filter(file -> file.getName().equals("reply-keyboards.json"))
                .findFirst();

        if (keyboardFile.isPresent()) {
            Reader reader = Files.newBufferedReader(
                    keyboardFile.get().toPath()
            );

            Type type = new TypeToken<LinkedHashMap<String, ReplyKeyboard>>() {
            }.getType();
            replyKeyboardMap = gson.fromJson(reader, type);
            reader.close();
            return replyKeyboardMap;
        }
        throw new IOException("Keyboard config not found");
    }

    @Bean
    @DependsOn({"replyKeyboardMap"})
    public Map<String, ReplyKeyboardMarkup> staticReplyKeyboardMarkupMap() {
        return replyKeyboardMap.entrySet().stream()
                .filter(entry -> entry.getValue().getType().contains("static"))
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> {
                                    ReplyKeyboard replyKeyboard = entry.getValue();
                                    if (replyKeyboard.getType().equals("static")) {
                                        return replyKeyboard.generateStaticKeyboard();
                                    } else {
                                        throw new IllegalTradingBotException(
                                                "Unknown reply keyboard type - " + replyKeyboard.getType());
                                    }
                                }
                        )
                );
    }

    @Bean
    @DependsOn({"replyKeyboardMap"})
    public Map<String, ReplyKeyboard> dynamicReplyKeyboard() {
        return replyKeyboardMap.entrySet().stream()
                .filter(entry -> entry.getValue().getType().contains("dynamic"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
