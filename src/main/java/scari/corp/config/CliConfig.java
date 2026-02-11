package scari.corp.config;

import java.util.Arrays;

/**
 * Параметры запуска утилиты
 *
 * @param outputDir  каталог для выходных файлов
 * @param prefix     префикс имен выходных файлов
 * @param shortStats признак краткой статистики
 * @param append     признак добавления в существующие файлы
 * @param inputFiles список входных файлов
 */
public record CliConfig(String outputDir, String prefix, boolean shortStats, boolean append, String[] inputFiles) {

    @Override
    public String toString() {
        return """
                CliConfig{
                  outputDir='%s',
                  prefix='%s',
                  shortStats=%s,
                  append=%s,
                  inputFiles=%s
                }""".formatted(
                outputDir,
                prefix,
                shortStats,
                append,
                Arrays.toString(inputFiles)
        );
    }
}
