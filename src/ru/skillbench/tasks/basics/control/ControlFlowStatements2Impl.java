package ru.skillbench.tasks.basics.control;

public class ControlFlowStatements2Impl implements ControlFlowStatements2 {
    /**
     * Для данного целого x найти значение следующей функции f, принимающей значения целого типа:<br/>
     * <pre>
     *        | 2*x, если x<-2 или x>2,
     *  f(x)= |
     *        | -3*x, в противном случае.
     * </pre>
     *
     * @param x
     * @return значение f.
     */
    @Override
    public int getFunctionValue(int x) {
        return (x < -2 || x > 2) ? 2 * x : -3 * x;
    }

    /**
     * Дано целое число mark.
     * Вернуть строку - описание оценки, соответствующей числу mark:<br/>
     * 1 — Fail, 2 — Poor, 3 — Satisfactory, 4 — Good, 5 — Excellent.<br/>
     * Если mark не лежит в диапазоне 1–5, то вывести строку "Error".
     *
     * @param mark
     * @return строковое представление mark
     */
    @Override
    public String decodeMark(int mark) {
        switch (mark) {
            case 1:
                return "Fail";
            case 2:
                return "Poor";
            case 3:
                return "Satisfactory";
            case 4:
                return "Good";
            case 5:
                return "Excellent";
            default:
                return "Error";
        }
    }

    /**
     * Создать двумерный массив, содержащий 5x8 вещественных элементов,
     * и присвоить каждому элементу следующее значение, зависящее от его индексов:
     * array[i][j] = (i в степени 4) минус корень квадратный из j.<br/>
     * Для возведения в степень и взятия корня рекомендуется использовать
     * статические методы класса {@link Math}.
     *
     * @return массив
     */
    @Override
    public double[][] initArray() {
        double[][] result = new double[5][8];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = Math.pow(i, 4) - Math.sqrt(j);
            }
        }
        return result;
    }

    /**
     * Найти максимальный элемент заданного двумерного массива.
     *
     * @param array массив, содержащий как минимум один элемент
     * @return максимальный элемент массива array.
     */
    @Override
    public double getMaxValue(double[][] array) {
        double maxValue = array[0][0];
        for (double[] doubles : array) {
            for (double element : doubles) {
                if (element > maxValue) {
                    maxValue = element;
                }
            }
        }
        return maxValue;
    }

    /**
     * Спортсмен начал тренировки, пробежав в первый день 10 км.
     * Каждый следующий день он увеличивал длину пробега на P процентов от
     * пробега предыдущего дня.<br/>
     * По заданному P определить, через сколько дней суммарный пробег спортсмена за все дни
     * превысит 200 км (целое число дней), а также сам суммарный пробег (вещественное число).
     *
     * @param P процент увеличения пробега в день
     * @return информация о пробеге (в виде экземпляра класса {@link Sportsman}) после наступления вышеуказанного условия
     */
    @Override
    public Sportsman calculateSportsman(float P) {
        Sportsman sportsman = new Sportsman();
        float currentDistance = 10;
        float multiplicator = 1 + P / 100f;
        while (sportsman.getTotalDistance() < 200) {
            sportsman.addDay(currentDistance);
            currentDistance *= multiplicator;
        }
        return sportsman;
    }
}
