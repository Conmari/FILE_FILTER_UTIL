package scari.corp.model;

/**
 * Хранит статистику с результатами фильтрации данных
 * <p>
 * Хранит количество элементов каждого типа, а также минимальные,
 * максимальные значения, суммы и длины строк для формирования
 * краткой и полной статистики.
 */
public class Stats {

    public long intCount;
    public long floatCount;
    public long stringCount;

    public Long intMin;
    public Long intMax;
    public long intSum;

    public Double floatMin;
    public Double floatMax;
    public double floatSum;

    public Integer minStringLength;
    public Integer maxStringLength;
}
