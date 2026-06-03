package com.gamesortir.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Inheritance & Polymorphism
 * SelectionSort adalah implementasi konkret dari BaseSort yang meng-override method generateSteps.
 * Melakukan sorting dengan algoritma Selection Sort.
 * 
 * ALGORITMA SELECTION SORT:
 * - Menemukan elemen minimum
 * - Menukar dengan elemen di posisi pertama yang belum terurut
 * - Proses berulang sampai semua elemen terurut
 */
public class SelectionSort extends BaseSort {
    
    public SelectionSort() {
        this.algorithmName = "Selection Sort";
        this.description = "Algoritma yang mencari elemen minimum dan menempatkannya di posisi awal";
    }
    
    /**
     * KONSEP OOP: Polymorphism (Override method dari base class)
     * Menghasilkan list of steps untuk Selection Sort
     * Setiap step merepresentasikan satu pencarian minimum dan penukaran
     * 
     * @param array Array yang akan di-sort
     * @return List dari SortStep yang merepresentasikan setiap langkah Selection Sort
     */
    @Override
    public List<SortStep> generateSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int[] workingArray = cloneArray(array);
        int n = workingArray.length;
        
        // SELECTION SORT ALGORITHM
        for (int i = 0; i < n - 1; i++) {
            // Cari indeks elemen minimum dari i sampai n-1
            int minIndex = i;
            
            for (int j = i + 1; j < n; j++) {
                if (workingArray[j] < workingArray[minIndex]) {
                    minIndex = j;
                }
                
                // Buat step untuk setiap perbandingan
                String explanation = String.format(
                    "Cari minimum: Bandingkan array[%d]=%d dengan array[%d]=%d. " +
                    "Minimum saat ini ada di indeks %d.",
                    minIndex, workingArray[minIndex], j, workingArray[j], minIndex
                );
                
                steps.add(new SortStep(
                    workingArray,
                    minIndex,
                    j,
                    explanation,
                    false,  // tidak ada penukaran di fase pencarian
                    false   // belum selesai
                ));
            }
            
            // Setelah menemukan minimum, tukar dengan posisi i (jika berbeda)
            if (minIndex != i) {
                swap(workingArray, i, minIndex);
                
                String swapExplanation = String.format(
                    "Minimum ditemukan di indeks %d dengan nilai %d. " +
                    "Tukar dengan posisi %d.",
                    minIndex, array[minIndex], i
                );
                
                steps.add(new SortStep(
                    workingArray,
                    i,
                    minIndex,
                    swapExplanation,
                    true,  // ada penukaran
                    false  // belum selesai
                ));
            } else {
                String noSwapExplanation = String.format(
                    "Elemen di indeks %d sudah minimum, tidak perlu ditukar.",
                    i
                );
                
                steps.add(new SortStep(
                    workingArray,
                    i,
                    i,
                    noSwapExplanation,
                    false,  // tidak ada penukaran
                    false   // belum selesai
                ));
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
