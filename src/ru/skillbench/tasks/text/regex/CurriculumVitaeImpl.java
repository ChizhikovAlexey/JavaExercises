package ru.skillbench.tasks.text.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurriculumVitaeImpl implements CurriculumVitae {
    private StringBuilder text;
    private ArrayList<Phone> phoneNumbers;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private HashMap<String, String> hiddenParts;

    public CurriculumVitaeImpl() {
        text = null;
        phoneNumbers = new ArrayList<>();
        fullName = null;
        firstName = null;
        middleName = null;
        lastName = null;
        hiddenParts = new HashMap<>();
    }

    /**
     * Выбрасывает исключение {@link IllegalStateException}, если не задан текст резюме.
     *
     * @throws IllegalStateException Если не задан текст резюме
     */
    void checkStateException() throws IllegalStateException {
        if (text == null) {
            throw new IllegalStateException();
        }
    }

    /**
     * Задает текст резюме.<br/>
     * О реализации: текст НЕ должен анализировать в этом методе.
     *
     * @param text Текст резюме
     */
    @Override
    public void setText(String text) {
        this.text = new StringBuilder(text);
        fullName = null;
        firstName = null;
        middleName = null;
        lastName = null;
        phoneNumbers = new ArrayList<>();
    }

    /**
     * Рекомендуется вызывать этот метод во всех остальных методах вашего класса.
     *
     * @return Текущий текст резюме (который мог измениться не только методом setText, но и методами update*).
     * @throws IllegalStateException Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public String getText() {
        checkStateException();
        return text.toString();
    }

    /**
     * Возвращает список телефонов в том же порядке, в котором они расположены в самом резюме.<br/>
     * О реализации: используйте {@link #PHONE_PATTERN} для поиска телефонов;
     * используйте группы этого регулярного выражения, чтобы извлечь код региона и extension из найденных номеров;
     * если код региона или extension не присутствует в номере, объект {@link Phone} должен хранить отрицательное значение.
     *
     * @return Список, который не может быть <code>null</code>, но может быть пустым (если ни одного телефона не найдено).
     * @throws IllegalStateException Если текст резюме не был задан путем вызова {@link #setText(String)}.
     * @see Phone
     */
    @Override
    public List<Phone> getPhones() {
        checkStateException();
        if (phoneNumbers.isEmpty()) {
            Pattern pattern = Pattern.compile(PHONE_PATTERN);
            Matcher matcher = pattern.matcher(text);
            System.out.println("Parsing phones in: " + text);
            while (matcher.find()) {
                System.out.println("found: " + matcher.group());
                phoneNumbers.add(new Phone(matcher.group()));
            }
        }
        System.out.println(phoneNumbers.toString());
        return phoneNumbers;
    }

    /**
     * Возвращает полное имя, т.е. ПЕРВУЮ часть текста резюме, которая удовлетворяет такие критериям:
     * <ol>
     * <li>полное имя содержит 2 или 3 слова, разделенных пробелом (' ');</li>
     * <li>каждое слово содержит не меньше двух символов;</li>
     * <li>первый символ слова - это заглавная латинская буква (буква английского алфавита в upper case);</li>
     * <li>последний символ слова - это либо точка ('.'), либо строчная(lower case) латинская буква;</li>
     * <li>не первые и не последние символы слова - это только строчные (lower case) латинские буквы.</li>
     * </ol>
     *
     * @return Полное имя (в точности равно значению в тексте резюме)
     * @throws NoSuchElementException Если резюме не содержит полного имени, которое удовлетворяет критериям.
     * @throws IllegalStateException  Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public String getFullName() {
        checkStateException();
        if (fullName == null) {
            Pattern pattern = Pattern.compile("[A-Z][a-z]*[a-z.]( [A-Z][a-z]*[a-z.]){1,2}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                fullName = matcher.group();
            } else {
                throw new NoSuchElementException();
            }
        }
        return fullName;
    }

    /**
     * Возвращает имя (первое слово из полного имени {@link #getFullName()}).
     *
     * @throws NoSuchElementException Если резюме не содержит полного имени.
     * @throws IllegalStateException  Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public String getFirstName() {
        checkStateException();
        if (firstName == null) {
            if (fullName == null) {
                getFullName();
            }
            Pattern pattern = Pattern.compile("^(?<firstName>[A-Z][a-z]*[a-z.])" +
                    "((?: )(?<middleName>[A-Z][a-z]*[a-z.]))?" +
                    "(?: )(?<lastName>[A-Z][a-z]*[a-z.])$");
            Matcher matcher = pattern.matcher(fullName);
            if (matcher.find()) {
                firstName = matcher.group("firstName");
                middleName = matcher.group("firstName");
                lastName = matcher.group("firstName");
            } else {
                firstName = null;
            }
        }
        return firstName;
    }

    /**
     * Возвращает отчество (второе слово из полного имени {@link #getFullName()})
     * или <code>null</null>, если полное имя состоит только из двух слов.
     *
     * @throws NoSuchElementException Если резюме не содержит полного имени.
     * @throws IllegalStateException  Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public String getMiddleName() {
        checkStateException();
        if (middleName == null) {
            if (fullName == null) {
                getFullName();
            }
            Pattern pattern = Pattern.compile("(?<firstName>[A-Z][a-z]*[a-z.])" +
                    "(?: )(?<middleName>[A-Z][a-z]*[a-z.])" +
                    "(?: )(?<lastName>[A-Z][a-z]*[a-z.])$");
            Matcher matcher = pattern.matcher(fullName);
            if (matcher.find()) {
                firstName = matcher.group("firstName");
                middleName = matcher.group("middleName");
                lastName = matcher.group("lastName");
            } else {
                middleName = null;
            }
        }
        return middleName;
    }

    /**
     * Возвращает фамилию (последнее слово из полного имени {@link #getFullName()}).
     *
     * @throws NoSuchElementException Если резюме не содержит полного имени.
     * @throws IllegalStateException  Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public String getLastName() {
        checkStateException();
        if (lastName == null) {
            if (fullName == null) {
                getFullName();
            }
            Pattern pattern = Pattern.compile("(?<firstName>[A-Z][a-z]*[a-z.])" +
                    "((?: )(?<middleName>[A-Z][a-z]*[a-z.]))?" +
                    "(?: )(?<lastName>[A-Z][a-z]*[a-z.])$");
            Matcher matcher = pattern.matcher(fullName);
            if (matcher.find()) {
                firstName = matcher.group("firstName");
                middleName = matcher.group("middleName");
                lastName = matcher.group("lastName");
            } else {
                lastName = null;
            }
        }
        return lastName;
    }

    /**
     * Заменяет фамилию на <code>newLastName</code> в тексте резюме.
     *
     * @param newLastName Не может быть null
     * @throws NoSuchElementException Если резюме не содержит полного имени.
     * @throws IllegalStateException  Если текст резюме не был задан путем вызова {@link #setText(String)}.
     * @see #getLastName()
     */
    @Override
    public void updateLastName(String newLastName) {
        checkStateException();
        Pattern pattern = Pattern.compile(getLastName());
        Matcher matcher = pattern.matcher(text);
        text = new StringBuilder(matcher.replaceFirst(newLastName));
        lastName = newLastName;
    }

    /**
     * Заменяет <code>oldPhone.getNumber()</code> на <code>newPhone.getNumber()</code> в тексте резюме.<br/>
     * О реализации: использование regex здесь ведет к большему объему кода, чем вызов не связанных с
     * регулярными выражениями методов {@link String} (или метода {@link String} и метода {@link StringBuilder}).
     *
     * @param oldPhone Не может быть null
     * @param newPhone Не может быть null
     * @throws IllegalArgumentException Если резюме не содержит текста, равного <code>oldPhone.getNumber()</code>.
     * @throws IllegalStateException    Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public void updatePhone(Phone oldPhone, Phone newPhone) {
        checkStateException();
        Pattern pattern = Pattern.compile(oldPhone.getNumber());
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }
        setText(matcher.replaceAll(newPhone.getNumber()));
    }

    String removeParenthesises(String oldString) {
        return oldString.replaceAll("[(]", "[(]").replaceAll("[)]", "[)]");
    }

    /**
     * Ищет строку <code>piece</code> в тексте резюме и скрывает ее, то есть заменяет каждый символ из
     * <code>piece</code> на символ 'X', за исключениеми следующих разделительных символов: ' ', '.' и '@'.
     * Число символов 'X' равно числу замененных символов.<br/>
     * Например: "John A. Smith" заменяется на "XXXX X. XXXXX", "john@hp.com" - на "XXXX@XX.XXX".<br/>
     * Эта замена может быть отменена путем вызова {@link #unhideAll()}.
     *
     * @param piece Не может быть null
     * @throws IllegalArgumentException Если резюме не содержит текста, равного <code>piece</code>.
     * @throws IllegalStateException    Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public void hide(String piece) {
        checkStateException();
        Pattern pattern = Pattern.compile(removeParenthesises(piece));
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }
        String hider = piece.replaceAll("[^ ,.@]", "X");
        setText(matcher.replaceAll(hider));
        hiddenParts.put(hider, piece);
    }

    /**
     * Ищет строку <code>phone</code> в тексте резюме и скрывает ее, то есть, заменяет все ЦИФРЫ из
     * <code>phone</code> на символ 'X'.<br/>
     * Например: "(123)456 7890" заменяется на "(XXX)XXX XXXX".<br/>
     * Эта замена может быть отменена путем вызова {@link #unhideAll()}.
     *
     * @param phone Не может быть null
     * @throws IllegalArgumentException Если резюме не содержит текста, равного <code>phone</code>.
     * @throws IllegalStateException    Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public void hidePhone(String phone) {
        checkStateException();
        Pattern pattern = Pattern.compile(removeParenthesises(phone));
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }
        String hider = phone.replaceAll("\\d", "X");
        setText(matcher.replaceAll(hider));
        hiddenParts.put(hider, phone);
    }

    /**
     * Отменяет все изменения, сделанные методами {@link #hide(String)} и {@link #hidePhone(String)},
     * т.е. заменяет куски текста с символами 'X' в текущем тексте резюме (скрытые куски, вставленные ранее)
     * на соответствующие куски из исходного текста резюме.<br/>
     * Примечание: в резюме не может быть двух (или более) одинаковых скрытых кусков (одинаковых куско с 'X').<br/>
     * О реализации: исходные и скрытые куски следует хранить в некой коллекции.
     * Кроме того, эта коллекция должна очищаться при вызове {@link #setText(String)}.
     *
     * @return Число кусков, замененных в тексте резюме при выполнении метода
     * @throws IllegalStateException Если текст резюме не был задан путем вызова {@link #setText(String)}.
     */
    @Override
    public int unhideAll() {
        checkStateException();
        System.out.println(hiddenParts);
        AtomicInteger replacements = new AtomicInteger();
        hiddenParts.keySet().forEach(hider -> {
            try {
                System.out.println("Seek " + removeParenthesises(hider) + " in " + text);
                setText(Pattern.compile(removeParenthesises(hider)).matcher(text).replaceFirst(hiddenParts.get(hider)));
                replacements.getAndIncrement();
            } catch (Exception ignored) {
            }

        });
        return replacements.intValue();
    }
}
