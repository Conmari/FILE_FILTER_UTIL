package scari.corp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scari.corp.enums.LineType;
import scari.corp.model.Stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Выполняет построчную обработку входных файлов.
 * <p>
 * Определяет тип каждой строки ({@link LineType}), записывает её
 * в соответствующий выходной файл и накапливает статистику в {@link Stats}.
 */
public class FileProcessor {

    private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);
    private final LineTypeDetector detector = new LineTypeDetector();

    /**
     * Пути к выходным файлам для целых чисел, вещественных чисел и строк.
     *
     * @param integers путь к файлу с целыми числами
     * @param floats   путь к файлу с вещественными числами
     * @param strings  путь к файлу со строками
     */
    public record OutputFiles(Path integers, Path floats, Path strings) {
    }

    /**
     * Формирует пути к выходным файлам на основе каталога и префикса.
     *
     * @param outputDir каталог, в котором будут созданы выходные файлы
     * @param prefix    префикс имён выходных файлов (может быть пустой)
     * @return объект с путями к файлам для целых чисел, вещественных чисел и строк
     */
    public OutputFiles buildOutputFiles(String outputDir, String prefix) {
        Path baseDir = Paths.get(outputDir);
        return new OutputFiles(
                baseDir.resolve(prefix + "integers.txt"),
                baseDir.resolve(prefix + "floats.txt"),
                baseDir.resolve(prefix + "strings.txt")
        );
    }

    /**
     * Обрабатывает список входных файлов и записывает строки по типам данных
     * в соответствующие выходные файлы.
     * <p>
     * Для каждой строки определяется {@link LineType}, выполняется запись
     * в нужный writer и обновление статистики.
     *
     * @param inputFiles пути к входным файлам
     * @param out        пути к выходным файлам для каждого типа данных
     * @param append     если {@code true}, данные добавляются в существующие файлы;
     *                   иначе файлы перезаписываются
     * @return объект {@link Stats} с итоговой статистикой по всем файлам
     */
    public Stats processFiles(String[] inputFiles, OutputFiles out, boolean append) {
        Stats stats = new Stats();

        StandardOpenOption[] options = append
                ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

        BufferedWriter intWriter = null;
        BufferedWriter floatWriter = null;
        BufferedWriter stringWriter = null;

        int processedFiles = 0;

        try {

            for (String filePath : inputFiles) {
                Path path = Paths.get(filePath);
                log.info("Обрабатываем файл: {}", filePath);
                if (!Files.exists(path)) {
                    log.warn("Файл не найден: {}", filePath);
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        LineType type = detector.detect(line);

                        switch (type) {
                            case INTEGER -> {
                                if (intWriter == null) {
                                    intWriter = Files.newBufferedWriter(out.integers(), StandardCharsets.UTF_8, options);
                                }
                                handleInteger(intWriter, line, stats);
                            }
                            case FLOAT -> {
                                if (floatWriter == null) {
                                    floatWriter = Files.newBufferedWriter(out.floats(), StandardCharsets.UTF_8, options);
                                }
                                handleFloat(floatWriter, line, stats);
                            }
                            case STRING -> {
                                if (stringWriter == null) {
                                    stringWriter = Files.newBufferedWriter(out.strings(), StandardCharsets.UTF_8, options);
                                }
                                handleString(stringWriter, line, stats);
                            }
                        }
                        processedFiles++;
                    }
                }
            }
        } catch (IOException e) {
            log.error("Ошибка записи: {}", e.getMessage(), e);
        } finally {
            try {
                if (intWriter != null) {
                    intWriter.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (floatWriter != null) {
                    floatWriter.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (stringWriter != null) {
                    stringWriter.close();
                }
            } catch (IOException ignored) {
            }
        }

        if (processedFiles == 0) {
            log.warn("Не удалось обработать ни одного входного файла");
        }
        return stats;
    }

    private void handleString(BufferedWriter stringWriter, String line, Stats stats) throws IOException {
        writeLine(stringWriter, line);
        stats.stringCount++;

        int len = line.length();

        if (stats.minStringLength == null || len < stats.minStringLength) {
            stats.minStringLength = len;
        }
        if (stats.maxStringLength == null || len > stats.maxStringLength) {
            stats.maxStringLength = len;
        }
    }

    private void handleFloat(BufferedWriter floatWriter, String line, Stats stats) throws IOException {
        writeLine(floatWriter, line);
        stats.floatCount++;

        double value = Double.parseDouble(line.trim());
        stats.floatSum += value;

        if (stats.floatMin == null || value < stats.floatMin) {
            stats.floatMin = value;
        }
        if (stats.floatMax == null || value > stats.floatMax) {
            stats.floatMax = value;
        }
    }

    private void handleInteger(BufferedWriter intWriter, String line, Stats stats) throws IOException {
        writeLine(intWriter, line);
        stats.intCount++;

        long value = Long.parseLong(line.trim());
        stats.intSum += value;
        if (stats.intMin == null || value < stats.intMin) {
            stats.intMin = value;
        }
        if (stats.intMax == null || value > stats.intMax) {
            stats.intMax = value;
        }
    }

    private void writeLine(BufferedWriter stringWriter, String line) throws IOException {
        stringWriter.write(line);
        stringWriter.newLine();
    }

}
