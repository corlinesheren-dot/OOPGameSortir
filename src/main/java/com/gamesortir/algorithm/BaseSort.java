package com.gamesortir.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Abstract Class (Inheritance)
 * BaseSort adalah abstract class yang mengimplementasikan logika umum untuk sorting.
 * Digunakan sebagai base class untuk BubbleSort dan SelectionSort.
 * 
 * PRINSIP SOLID: Open-Closed Principle
 * Class ini terbuka untuk extension (BubbleSort, SelectionSort bisa extends-nya)
 * tapi tertutup untuk modifikasi (logika umum tidak perlu diubah).
 */
public abstract class BaseSort implements SortAlgorithm {
    
    protected String algorithmName;
    protected String description;
    
    // ===== KONSEP OOP: Method abstrak yang harus di-override oleh subclass =====
    
    /**
     * Method abstrak yang harus diimplementasikan oleh subclass
     * untuk menghasilkan list of steps yang spesifik untuk algoritma tersebut
     */
    @Override
    public abstract List<SortStep> generateSteps(int[] array);
    
    @Override
    public String getAlgorithmName() {
        return algorithmName;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    /**
     * Utility method untuk menukar dua elemen dalam array
     * @param array Array yang akan ditukar elemennya
     * @param i Index pertama
     * @param j Index kedua
     */
    protected void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * Utility method untuk mengecek apakah array sudah terurut
     * @param array Array yang akan dicek
     * @return true jika array sudah terurut
     */
    protected boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Clone array untuk keperluan SortStep
     */
    protected int[] cloneArray(int[] array) {
        return array.clone();
    }
}
