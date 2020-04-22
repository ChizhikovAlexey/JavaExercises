package ru.skillbench.tasks.text;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordCounterImpl implements WordCounter {
    private String text;
    private HashMap<String, Long> words;

    public WordCounterImpl() {
        words = new HashMap<>();
    }

    public WordCounterImpl(String text) {
        this.text = text;
    }

    private void checkState() {
        if (text == null) {
            throw new IllegalStateException();
        }
    }

    private void countWords() {
        Pattern pattern = Pattern.compile("\\b(?!<)(?<word>[a-zA-Zа-яА-Яё0-9]+)(?!>)\\b");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String word = matcher.group("word").toLowerCase();
            words.put(word, words.getOrDefault(word, 0L) + 1);
        }
    }

    /**
     * Принимает текст для анализа
     *
     * @param text текст для анализа
     */
    @Override
    public void setText(String text) {
        this.text = text;
        if (this.text != null) {
            countWords();
        }
    }

    /**
     * @return текст, переданный для анализа при последнем вызове метода
     * {@link #setText(String) setText}, или <code>null</code>,
     * если указанный метод еще не вызывался или последний раз вызывался
     * с параметром <code>null</code>
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Возвращает {@link Map}&lt;{@link String}, {@link Long}&gt;, сопоставляющую каждому
     * слову (длиной не менее 1 символа) количество его вхождений в анализируемый текст.<br/>
     * Все возвращаемые слова должны быть приведены к нижнему регистру.<br/>
     * Дополнительно оценивается, если из рассмотрения исключены слова, начинающиеся с &lt;
     * и заканчивающиеся на &gt; (то есть, расположенные в угловых скобках).<br/>
     *
     * @return результат подсчета количеств вхождений слов
     * @throws IllegalStateException если не задан текст для анализа
     *                               (если метод {@link #setText(String)} еще не вызывался
     *                               или последний раз вызывался с параметром <code>null</code>)
     */
    @Override
    public Map<String, Long> getWordCounts() {
        checkState();
        return words;
    }

    private static int compare(Map.Entry<String, Long> first, Map.Entry<String, Long> second) {
        long difference = second.getValue() - first.getValue();
        int intDifference;
        if (difference > 0L) {
            return 1;
        } else if (difference < 0L) {
            return -1;
        } else {
            return first.getKey().charAt(0) - second.getKey().charAt(0);
        }


    }

    /**
     * Возвращает список из {@link Map.Entry Map.Entry}&lt;{@link String}, {@link Long}&gt;,
     * сопоставляющий каждому слову количество его вхождений в анализируемый текст
     * и упорядоченный в прядке убывания количества вхождений слова.<br/>
     * Слова с одинаковым количеством вхождений упорядочиваются в алфавитном порядке (без учета регистра!).<br/>
     * Все возвращаемые слова должны быть приведены к нижнему регистру.<br/>
     * <p>
     * ПРИМЕЧАНИЕ: при реализации рекомендуется использовать {@link #sort(Map, Comparator)}
     *
     * @return упорядоченный результат подсчета количеств вхождений слов
     * @throws IllegalStateException если не задан текст для анализа
     *                               (если метод {@link #setText(String)} еще не вызывался
     *                               или последний раз вызывался с параметром <code>null</code>)
     */
    @Override
    public List<Map.Entry<String, Long>> getWordCountsSorted() {
        checkState();
        return words.entrySet().stream().sorted(WordCounterImpl::compare).collect(Collectors.toList());
    }

    /**
     * Упорядочивает содержимое <code>map</code> (это слова и количество их вхождений)
     * в соответствии с <code>comparator</code>.<br/>
     * <br/>
     * ПРИМЕЧАНИЕ:Этот метод работает только со своими параметрами, но не с полями объекта {@link WordCounter}.
     *
     * @param map        Например, неупорядоченный результат подсчета числа слов
     * @param comparator
     * @return Содержимое <code>map</code> в виде списка, упорядоченного в соответствии с <code>comparator</code>
     */
    @Override
    public <K extends Comparable<K>, V extends Comparable<V>> List<Map.Entry<K, V>> sort(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return map.entrySet().stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Распечатывает <code>entryList</code> (это слова и количество их вхождений)
     * в поток вывода <code>ps</code>.<br/>
     * Формат вывода следующий:
     * <ul>
     * 	<li>Каждое слово вместе с количеством вхождений выводится на отдельной строке</li>
     * 	<li>На каждой строке слово и количество вхождений разделены одним(!) пробелом,
     * никаких других символов на строке быть не должно</li>
     * </ul>
     * Все выводимые слова должны быть приведены к нижнему регистру.<br/>
     * <br/>
     * ПРИМЕЧАНИЕ: Этот метод работает только со своими параметрами, но не с полями объекта {@link WordCounter}.
     *
     * @param entries Список пар - например, результат подсчета числа слов
     * @param ps      Поток вывода - например, System.out.
     */
    @Override
    public <K, V> void print(List<Map.Entry<K, V>> entries, PrintStream ps) {
        for (Map.Entry<K, V> entry : entries) {
            ps.print((entry.getKey().toString() + " " + entry.getValue().toString()).toLowerCase() + "\n");
        }
    }
}
