package com.gamesortir.algorithm;

/**
 * KONSEP OOP: Class untuk merepresentasikan setiap step dalam sorting
 * Menyimpan informasi tentang perubahan array di setiap step
 */
public class SortStep {
    private int[] currentArray;           // State array pada step ini
    private int comparingIndex1;          // Index pertama yang dibandingkan/ditukar
    private int comparingIndex2;          // Index kedua yang dibandingkan/ditukar
    private String explanation;           // Penjelasan text untuk step ini
    private boolean isSwapped;            // Apakah ada penukaran di step ini
    private boolean isCompleted;          // Apakah sudah selesai sorting
    
    // ===== KONSEP OOP: Attribute & Method (Encapsulation) =====
    
    public SortStep() {}
    
    public SortStep(int[] currentArray, int idx1, int idx2, String explanation, boolean isSwapped, boolean isCompleted) {
        this.currentArray = currentArray.clone();
        this.comparingIndex1 = idx1;
        this.comparingIndex2 = idx2;
        this.explanation = explanation;
        this.isSwapped = isSwapped;
        this.isCompleted = isCompleted;
    }
    
    // Getter & Setter
    public int[] getCurrentArray() {
        return currentArray;
    }
    
    public void setCurrentArray(int[] currentArray) {
        this.currentArray = currentArray;
    }
    
    public int getComparingIndex1() {
        return comparingIndex1;
    }
    
    public void setComparingIndex1(int comparingIndex1) {
        this.comparingIndex1 = comparingIndex1;
    }
    
    public int getComparingIndex2() {
        return comparingIndex2;
    }
    
    public void setComparingIndex2(int comparingIndex2) {
        this.comparingIndex2 = comparingIndex2;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public boolean isSwapped() {
        return isSwapped;
    }
    
    public void setSwapped(boolean swapped) {
        isSwapped = swapped;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    @Override
    public String toString() {
        return "SortStep{" +
                "comparingIndex1=" + comparingIndex1 +
                ", comparingIndex2=" + comparingIndex2 +
                ", explanation='" + explanation + '\'' +
                ", isSwapped=" + isSwapped +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
