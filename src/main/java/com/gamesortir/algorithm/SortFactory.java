package com.gamesortir.algorithm;

/**
 * DESIGN PATTERN: Factory Pattern (Creational Pattern)
 * SortFactory adalah factory class yang bertanggung jawab membuat objek algoritma sorting.
 * 
 * KEUNTUNGAN:
 * - Memisahkan logika pembuatan objek dari GUI
 * - GUI hanya perlu memanggil factory, tidak perlu tahu detail pembuatan
 * - Mudah menambah algoritma baru tanpa mengubah GUI
 * - Centralized object creation logic
 */
public class SortFactory {
    
    /**
     * Factory method untuk membuat objek SortAlgorithm berdasarkan tipe
     * 
     * @param algorithmType Tipe algoritma: "BUBBLE_SORT" atau "SELECTION_SORT"
     * @return Objek SortAlgorithm yang sesuai
     * @throws IllegalArgumentException jika tipe algoritma tidak dikenal
     */
    public static SortAlgorithm createSortAlgorithm(String algorithmType) {
        if (algorithmType == null) {
            throw new IllegalArgumentException("Algorithm type tidak boleh null");
        }
        
        // FACTORY LOGIC: Buat objek berdasarkan tipe
        switch (algorithmType.toUpperCase()) {
            case "BUBBLE_SORT":
                // Instansiasi BubbleSort (KONSEP OOP: Instansiasi Objek)
                return new BubbleSort();
                
            case "SELECTION_SORT":
                // Instansiasi SelectionSort (KONSEP OOP: Instansiasi Objek)
                return new SelectionSort();
                
            default:
                throw new IllegalArgumentException(
                    "Tipe algoritma '" + algorithmType + "' tidak dikenal. " +
                    "Gunakan 'BUBBLE_SORT' atau 'SELECTION_SORT'"
                );
        }
    }
    
    /**
     * Factory method overload untuk membuat algoritma dengan enum
     */
    public static SortAlgorithm createSortAlgorithm(AlgorithmType type) {
        return createSortAlgorithm(type.getValue());
    }
}

/**
 * Enum untuk tipe-tipe algoritma yang tersedia
 */
enum AlgorithmType {
    BUBBLE_SORT("BUBBLE_SORT", "Bubble Sort"),
    SELECTION_SORT("SELECTION_SORT", "Selection Sort");
    
    private final String value;
    private final String displayName;
    
    AlgorithmType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
