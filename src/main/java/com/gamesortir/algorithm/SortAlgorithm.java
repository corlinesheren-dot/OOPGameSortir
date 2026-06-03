package com.gamesortir.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Interface
 * Interface SortAlgorithm mendefinisikan kontrak untuk semua algoritma sorting.
 * 
 * DESIGN PATTERN: Strategy Pattern
 * Interface ini memungkinkan GUI JavaFX untuk mengganti strategi sorting (Bubble atau Selection)
 * secara dinamis saat runtime tanpa mengubah kode GUI.
 */
public interface SortAlgorithm {
    
    /**
     * Melakukan sorting pada array dan mengembalikan list of steps
     * @param array Array yang akan di-sort
     * @return List dari SortStep yang merepresentasikan setiap langkah sorting
     */
    List<SortStep> generateSteps(int[] array);
    
    /**
     * @return Nama algoritma sorting
     */
    String getAlgorithmName();
    
    /**
     * @return Deskripsi singkat algoritma
     */
    String getDescription();
}
