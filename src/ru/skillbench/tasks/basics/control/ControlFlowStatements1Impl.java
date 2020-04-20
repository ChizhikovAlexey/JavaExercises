package ru.skillbench.tasks.basics.control;

import static java.lang.Math.sin;

public class ControlFlowStatements1Impl implements ControlFlowStatements1 {
    @Override
    public float getFunctionValue(float x) {
        return (float) (x > 0 ? 2 * sin(x) : 6 - x);
    }

    /**
     * Дано целое число в диапазоне 1–7.
     * Вернуть строку — название дня недели, соответствующее этому числу:<br/>
     * 1 — Monday, 2 — Tuesday, 3 - Wednesday, 4 - Thursday, 5 - Friday, 6 - Saturday, 7 - Sunday.
     *
     * @param weekday
     * @return строковое представление weekday
     */
    @Override
    public String decodeWeekday(int weekday) {
        switch (weekday) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
            default:
                return "DOOMSDAY";
        }
    }

    /**
     * Создать двумерный массив, содержащий 8x5 целочисленных элементов,
     * и присвоить каждому элементу произведение его индексов: array[i][j] = i*j.
     *
     * @return массив.
     */
    @Override
    public int[][] initArray() {
        int[][] result = new int[8][5];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                result[i][j] = i * j;
            }
        }
        return result;
    }

    /**
     * Найти минимальный элемент заданного двумерного массива.
     *
     * @param array массив, содержащий как минимум один элемент
     * @return минимальный элемент массива array.
     */
    @Override
    public int getMinValue(int[][] array) {
        int minValue = array[0][0];
        for (int[] ints : array) {
            for (int element : ints) {
                if (element < minValue) {
                    minValue = element;
                }
            }
        }
        return minValue;
    }

    /**
     * Начальный размер вклада в банке равен $1000.<br/>
     * По окончанию каждого года размер вклада увеличивается на P процентов (вклад с капитализацией процентов).<br/>
     * По заданному P определить, через сколько лет размер вклада превысит $5000, а также итоговый размер вклада.
     *
     * @param P процент по вкладу
     * @return информация о вкладе (в виде экземпляра класса {@link BankDeposit}) после наступления вышеуказанного условия
     */
    @Override
    public BankDeposit calculateBankDeposit(double P) {
        BankDeposit bankDeposit = new BankDeposit();
        bankDeposit.amount = 1000;
        bankDeposit.years = 0;
        double multiplicator = 1 + P / 100;

        while (bankDeposit.amount <= 5000) {
            bankDeposit.amount *= multiplicator;
            bankDeposit.years++;
        }

        return bankDeposit;
    }
}
