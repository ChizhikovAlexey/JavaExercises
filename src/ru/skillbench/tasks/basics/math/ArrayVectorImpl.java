package ru.skillbench.tasks.basics.math;

import java.util.Arrays;

public class ArrayVectorImpl implements ArrayVector {

    private double[] coords;
    private int size;

    /**
     * Задает все элементы вектора (определяет длину вектора).
     * Передаваемый массив не клонируется.
     *
     * @param elements Не равен null
     */
    @Override
    public void set(double... elements) {
        coords = elements;
        size = coords.length;
    }

    /**
     * Изменяет размер массива, сохраняя первые min(size, newSize) элементов.
     *
     * @param newSize новый размер массива
     */
    private void resizeArray(int newSize) {
        double[] newCoords = new double[newSize];
        System.arraycopy(coords, 0, newCoords, 0, size);
        set(newCoords);
    }

    /**
     * Возвращает все элементы вектора. Массив не клонируется.
     */
    @Override
    public double[] get() {
        return coords;
    }

    /**
     * Возвращает копию вектора (такую, изменение элементов
     * в которой не приводит к изменению элементов данного вектора).<br/>
     * Рекомендуется вызвать метод clone() у самого массива или использовать
     * {@link System#arraycopy(Object, int, Object, int, int)}.
     */
    @Override
    public ArrayVector clone() {
        ArrayVector result = new ArrayVectorImpl();
        result.set(coords.clone());
        return result;
    }

    /**
     * Возвращает число элементов вектора.
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Изменяет элемент по индексу.
     *
     * @param index В случае выхода индекса за пределы массива:<br/>
     *              а) если index<0, ничего не происходит;<br/>
     *              б) если index>=0, размер массива увеличивается так, чтобы index стал последним.
     * @param value
     */
    @Override
    public void set(int index, double value) {
        if (index > 0) {
            if (index >= size) {
                resizeArray(index + 1);
            }
            coords[index] = value;
        }

    }

    /**
     * Возвращает элемент по индексу.
     *
     * @param index В случае выхода индекса за пределы массива
     *              должно генерироваться ArrayIndexOutOfBoundsException
     */
    @Override
    public double get(int index) throws ArrayIndexOutOfBoundsException {
        return coords[index];
    }

    /**
     * Возвращает максимальный элемент вектора.
     */
    @Override
    public double getMax() {
        double maxValue = coords[0];
        for (double value : coords) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    /**
     * Возвращает минимальный элемент вектора.
     */
    @Override
    public double getMin() {
        double minValue = coords[0];
        for (double value : coords) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    /**
     * Сортирует элементы вектора в порядке возрастания.
     */
    @Override
    public void sortAscending() {
        Arrays.sort(coords);
    }

    /**
     * Умножает вектор на число.<br/>
     * Замечание: не пытайтесь использовать безиндексный цикл foreach:
     * для изменения элемента массива нужно знать его индекс.
     *
     * @param factor
     */
    @Override
    public void mult(double factor) {
        for (int i = 0; i < size; i++) {
            coords[i] *= factor;
        }
    }

    /**
     * Складывает вектор с другим вектором, результат запоминает в элементах данного вектора.<br/>
     * Если векторы имеют разный размер, последние элементы большего вектора не учитываются<br/>
     * (если данный вектор - больший, его размер менять не надо, просто не меняйте последние элементы).
     *
     * @param anotherVector Не равен null
     * @return Ссылка на себя (результат сложения)
     */
    @Override
    public ArrayVector sum(ArrayVector anotherVector) {
        int minSize = Math.min(size, anotherVector.getSize());
        int maxSize = Math.max(size, anotherVector.getSize());
        if (maxSize > size) {
            resizeArray(maxSize);
        }
        for (int i = 0; i < minSize; i++) {
            coords[i] += anotherVector.get(i);
        }
        return this;
    }

    /**
     * Возвращает скалярное произведение двух векторов.<br/>
     * Если векторы имеют разный размер, последние элементы большего вектора не учитываются.
     *
     * @param anotherVector Не равен null
     */
    @Override
    public double scalarMult(ArrayVector anotherVector) {
        int minSize = Math.min(size, anotherVector.getSize());
        double result = 0;
        for (int i = 0; i < minSize; i++) {
            result += coords[i] * anotherVector.get(i);
        }
        return result;
    }

    /**
     * Возвращает евклидову норму вектора (длину вектора
     * в n-мерном евклидовом пространстве, n={@link #getSize()}).
     * Это можно подсчитать как корень квадратный от скалярного произведения вектора на себя.
     */
    @Override
    public double getNorm() {
        double result = 0;
        for (int i = 0; i < size; i++) {
            result += coords[i] * coords[i];
        }
        return Math.sqrt(result);
    }
}
