package com.gamesortir.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Inheritance & Polymorphism
 * BubbleSort adalah implementasi konkret dari BaseSort yang meng-override method generateSteps.
 * Melakukan sorting dengan algoritma Bubble Sort.
 * 
 * ALGORITMA BUBBLE SORT:
 * - Membandingkan elemen bertetangga
 * - Menukar jika elemen kiri > elemen kanan
 * - Proses berulang sampai semua elemen terurut
 */
public class BubbleSort extends BaseSort {
    
    public BubbleSort() {
        this.algorithmName = "Bubble Sort";
        this.description = "Algoritma yang membandingkan dan menukar elemen bertetangga secara berulang";
    }
    
    /**
     * KONSEP OOP: Polymorphism (Override method dari base class)
     * Menghasilkan list of steps untuk Bubble Sort
     * Setiap step merepresentasikan satu perbandingan/penukaran
     * 
     * @param array Array yang akan di-sort
     * @return List dari SortStep yang merepresentasikan setiap langkah Bubble Sort
     */
    @Override
    public List<SortStep> generateSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int[] workingArray = cloneArray(array);
        int n = workingArray.length;
        
        // BUBBLE SORT ALGORITHM
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                // Perbandingan: jika elemen kiri lebih besar dari elemen kanan
                if (workingArray[j] > workingArray[j + 1]) {
                    // Tukar elemen
                    swap(workingArray, j, j + 1);
                    
                    // Buat SortStep untuk penukaran ini
                    String explanation = String.format(
                        "Bandingkan array[%d]=%d dengan array[%d]=%d. " +
                        "Karena %d > %d, tukar posisinya.",
                        j, array[j], j + 1, array[j + 1], array[j], array[j + 1]
                    );
                    
                    steps.add(new SortStep(
                        workingArray,
                        j,
                        j + 1,
                        explanation,
                        true,  // ada penukaran
                        false  // belum selesai
                    ));
                } else {
                    // Tidak ada penukaran, tapi tetap buat step untuk ditampilkan
                    String explanation = String.format(
                        "Bandingkan array[%d]=%d dengan array[%d]=%d. " +
                        "Karena %d <= %d, tidak perlu ditukar.",
                        j, workingArray[j], j + 1, workingArray[j + 1], workingArray[j], workingArray[j + 1]
                    );
                    
                    steps.add(new SortStep(
                        workingArray,
                        j,
                        j + 1,
                        explanation,
                        false,  // tidak ada penukaran
                        false   // belum selesai
                    ));
                }
            }
        }
        
        // Tambahkan step terakhir: array sudah selesai di-sort
        String finalExplanation = "Proses selesai! Array sudah terurut dari kecil ke besar.";
        SortStep finalStep = new SortStep(
            workingArray,
            -1,
            -1,
            finalExplanation,
            false,
            true  // SELESAI
        );
        steps.add(finalStep);
        
        return steps;
    }
}
