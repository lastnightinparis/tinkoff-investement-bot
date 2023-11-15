package com.itmo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileConverterService {

    /**
     * Converts a CSV file to a list of doubles representing the closing prices.
     *
     * @param csvFilePath The path to the CSV file.
     * @return List of closing prices.
     * @throws IOException If an I/O error occurs opening the file.
     */
    public List<Double> convertCsvToPriceList(String csvFilePath) throws IOException {
        // Чтение всех строк файла
        List<String> lines = Files.readAllLines(Path.of(csvFilePath));

        // Пропускаем заголовок и конвертируем каждую строку в цену закрытия
        return lines.stream()
                .skip(1) // Пропускаем заголовок CSV
                .map(line -> line.split(",")) // Разделяем строки по запятой
                .map(parts -> Double.parseDouble(parts[1])) // Извлекаем цену закрытия, предполагая, что она находится во втором столбце
                .collect(Collectors.toList());
    }
}
