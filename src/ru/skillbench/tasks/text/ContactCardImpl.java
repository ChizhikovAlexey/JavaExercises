package ru.skillbench.tasks.text;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactCardImpl implements ContactCard {

    public ContactCardImpl() {
        this.fullName = null;
        this.organisation = null;
        this.gender = false;
        this.birthday = null;
        this.phoneNumbers = null;
    }

    private String fullName;
    private String organisation;
    private boolean gender;
    private LocalDate birthday;
    private HashMap<String, String> phoneNumbers;

    /**
     * Основной метод парсинга: создает экземпляр карточки из источника данных (Scanner),
     * содержащего следующие текстовые данные о человеке (5 полей):<br/>
     * 1) FN - Полное имя - обязательное поле<br/>
     * 2) ORG - Организация - обязательное поле<br/>
     * 3) GENDER - Пол - один символ: F или M. Это поле в данных может отсутствовать.<br/>
     * 4) BDAY - Дата рождения - в следующем формате: "DD-MM-YYYY", где DD - 2 цифры, обозначающие день,
     * MM - 2 цифры, обозначающие месяц, YYYY - 4 цифры, обозначающие год.
     * Это поле в данных может отсутствовать.<br/>
     * 5) TEL - Номер телефона - ровно 10 цифр, не включающие код страны. Полей TEL может быть 0 или несколько,
     * и разные поля TEL различаются значением обязательного атрибута TYPE, например:
     * TEL;TYPE=HOME,VOICE:4991112233<br/>
     * <p/>
     * Каждое из этих полей в источнике данных расположено на отдельной строке;
     * строки в стандарте vCard отделяются символом CRLF (\r\n).<br/>
     * Имя поля отделяется от его значения двоеточием, например: GENDER:F<br/>
     * Если нужно, можно предположить, что порядок полей будет именно такой, как выше.<br/>
     * Но первой строкой всегда идет BEGIN:VCARD, последней - END:VCARD.<br/>
     * Пример содержимого Scanner:<br/>
     * <code>
     * BEGIN:VCARD
     * FN:Forrest Gump
     * ORG:Bubba Gump Shrimp Co.
     * BDAY:06-06-1944
     * TEL;TYPE=WORK,VOICE:4951234567
     * TEL;TYPE=CELL,VOICE:9150123456
     * END:VCARD
     * </code>
     * <p/>
     * ПРИМЕЧАНИЕ: Такой метод в реальных приложениях был бы static, однако
     * система проверки учебных задач проверяет только не-статические методы.
     *
     * @param scanner Источник данных
     * @return {@link ContactCard}, созданный из этих данных
     * @throws InputMismatchException Возникает, если структура или значения данных не соответствуют формату,
     *                                описанному выше; например, если после названия поля нет двоеточия или дата указана в ином формате
     *                                или номер телефона содержит неверное число цифр.
     * @throws NoSuchElementException Возникает, если данные не содержат обязательных полей
     *                                (FN, ORG, BEGIN:VCARD, END:VCARD)
     */
    @Override
    public ContactCard getInstance(Scanner scanner) {
        StringBuilder string = new StringBuilder("");
        while (scanner.hasNextLine()) {
            string.append(scanner.nextLine()).append('\n');
        }
        return getInstance(string.toString());
    }


    /**
     * Метод создает {@link Scanner} и вызывает {@link #getInstance(Scanner)}
     *
     * @param data Данные для разбора, имеющие формат, описанный в {@link #getInstance(Scanner)}
     * @return {@link ContactCard}, созданный из этих данных
     */
    @Override
    public ContactCard getInstance(String data) {
        fullName = null;
        organisation = null;
        gender = false;
        birthday = null;
        boolean begin = false;
        boolean end = false;

        Pattern pattern = Pattern.compile("(?<type>\\w{2,6})" +
                "(?:[:;])" +
                "(?<data>\\V*)" +
                "(?:\\v*)");
        Pattern phonePattern = Pattern.compile("(?:TYPE=)" +
                "(?<phoneType>\\w+)" +
                "(?:\\D*)" +
                "(?<phoneNumber>\\d{10})" +
                "(?:\\v*)");
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            if (end) {
                throw new InputMismatchException();
            }
            switch (matcher.group("type")) {
                case "BEGIN":
                    if (matcher.group("data").equals("VCARD")) {
                        begin = true;
                    }
                    break;
                case "FN":
                    fullName = matcher.group("data");
                    break;
                case "ORG":
                    organisation = matcher.group("data");
                    break;
                case "GENDER":
                    if (matcher.group("data").equals("F")) {
                        gender = true;
                    } else if (matcher.group("data").equals("M")) {
                        gender = false;
                    } else {
                        throw new InputMismatchException();
                    }
                    break;
                case "TEL":
                    Matcher phoneMatcher = phonePattern.matcher(matcher.group("data"));
                    if (phoneMatcher.matches()) {
                        if (phoneNumbers == null) {
                            phoneNumbers = new HashMap<>();
                        }
                        phoneNumbers.put(phoneMatcher.group("phoneType"),
                                phoneMatcher.group("phoneNumber"));
                    } else {
                        throw new InputMismatchException();
                    }
                    break;
                case "BDAY":
                    try{
                        birthday = LocalDate.parse(matcher.group("data"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    } catch (DateTimeException exception) {
                        throw new InputMismatchException();
                    }

                    break;
                case "END":
                    if (matcher.group("data").equals("VCARD")) {
                        end = true;
                    }
                    break;
                default:
                    throw new InputMismatchException();
            }
            if (!begin) {
                throw new InputMismatchException();
            }
        }

        if (!begin || !end || fullName == null || organisation == null) {
            throw (new NoSuchElementException());
        }
        return this;
    }

    /**
     * @return Полное имя - значение vCard-поля FN: например, "Forrest Gump"
     */
    @Override
    public String getFullName() {
        return fullName;
    }

    /**
     * @return Организация - значение vCard-поля ORG: например, "Bubba Gump Shrimp Co."
     */
    @Override
    public String getOrganization() {
        return organisation;
    }

    /**
     * Если поле GENDER отсутствует в данных или равно "M", этот метод возвращает false
     *
     * @return true если этот человек женского пола (GENDER:F)
     */
    @Override
    public boolean isWoman() {
        return gender;
    }

    /**
     * ПРИМЕЧАНИЕ: в современных приложениях рекомендуется для работы с датой применять java.time.LocalDate,
     * однако такие классы как java.util.Calendar или java.util.Date необходимо знать.
     *
     * @return День рождения человека в виде {@link Calendar}
     * @throws NoSuchElementException Если поле BDAY отсутствует в данных
     */
    @Override
    public Calendar getBirthday() {
        if (birthday == null) {
            throw new NoSuchElementException();
        }
        Calendar result = Calendar.getInstance();
        //result.set(birthday.getYear(), birthday.getMonthValue() - 1, birthday.getDayOfMonth(), 0, 0, 0);
        result.setTimeInMillis(birthday.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return result;
    }

    /**
     * ПРИМЕЧАНИЕ: В реализации этого метода рекомендуется использовать {@link DateTimeFormatter}
     *
     * @return Возраст человека на данный момент в виде {@link Period}
     * @throws NoSuchElementException Если поле BDAY отсутствует в данных
     */
    @Override
    public Period getAge() {
        if (birthday == null) {
            throw new NoSuchElementException();
        }
        return birthday.until(LocalDate.now());
    }

    /**
     * @return Возраст человека в годах: например, 74
     * @throws NoSuchElementException Если поле BDAY отсутствует в данных
     */
    @Override
    public int getAgeYears() {
        if (birthday == null) {
            throw new NoSuchElementException();
        }
        return birthday.until(LocalDate.now()).getYears();
    }

    /**
     * Возвращает номер телефона в зависимости от типа.
     *
     * @param type Тип телефона, который содержится в данных между строкой "TEL;TYPE=" и двоеточием
     * @return Номер телефона - значение vCard-поля TEL, приведенное к следующему виду: "(123) 456-7890"
     * @throws NoSuchElementException если в данных нет телефона указанного типа
     */
    @Override
    public String getPhone(String type) {
        if (phoneNumbers == null || phoneNumbers.get(type) == null) {
            throw new NoSuchElementException();
        }
        StringBuilder result = new StringBuilder(phoneNumbers.get(type));
        result.insert(0, '(').insert(4, ") ").insert(9, '-');
        return result.toString();
    }
}
