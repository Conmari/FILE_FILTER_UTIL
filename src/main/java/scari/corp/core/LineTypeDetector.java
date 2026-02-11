package scari.corp.core;

import scari.corp.enums.LineType;

/**
 * Определяет тип содержимого строки.
 * <p>
 * Строка сначала проверяется как целое число, затем как вещественное;
 * если оба преобразования неуспешны, считается строкой.
 */
public class LineTypeDetector {

    /**
     * Определяет тип данных, представленный в строке.
     *
     * @param line исходная строка
     * @return {@link LineType#INTEGER}, {@link LineType#FLOAT} или {@link LineType#STRING}
     */
    public LineType detect(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return LineType.STRING;
        }
        try {
            Long.parseLong(trimmed);
            return LineType.INTEGER;
        } catch (NumberFormatException ignored) {
        }

        try {
            Double.parseDouble(trimmed);
            return LineType.FLOAT;
        } catch (NumberFormatException ignored) {
        }
        return LineType.STRING;
    }
}
