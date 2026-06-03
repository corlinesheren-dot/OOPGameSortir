package com.gamesortir.gamification;

import javafx.scene.paint.Color;

/**
 * KONSEP OOP: Class untuk mengelola semua color skins
 * Menyediakan skin default dan custom yang dapat dibeli
 */
public class ColorSkinManager {
    
    // ===== Default Skins (Tersedia gratis) =====
    public static final BarColorSkin SKIN_RED = new BarColorSkin(
        1, "Red (Default)", Color.web("#FF6B6B"), Color.web("#FF3333"), 0
    );
    
    public static final BarColorSkin SKIN_GREEN = new BarColorSkin(
        2, "Green", Color.web("#51CF66"), Color.web("#2F9E44"), 150
    );
    
    public static final BarColorSkin SKIN_BLACK = new BarColorSkin(
        3, "Black", Color.web("#1a1a1a"), Color.web("#595959"), 150
    );
    
    public static final BarColorSkin SKIN_PINK = new BarColorSkin(
        4, "Pink", Color.web("#FF69B4"), Color.web("#FF1493"), 200
    );
    
    public static final BarColorSkin SKIN_ORANGE = new BarColorSkin(
        5, "Orange", Color.web("#FFA500"), Color.web("#FF8C00"), 200
    );
    
    /**
     * Mendapatkan skin berdasarkan ID
     */
    public static BarColorSkin getSkinById(int skinId) {
        switch (skinId) {
            case 1:
                return SKIN_RED;
            case 2:
                return SKIN_GREEN;
            case 3:
                return SKIN_BLACK;
            case 4:
                return SKIN_PINK;
            case 5:
                return SKIN_ORANGE;
            default:
                return SKIN_RED;  // Default ke RED
        }
    }
    
    /**
     * Mendapatkan semua available skins untuk ditampilkan di shop
     */
    public static BarColorSkin[] getAllSkins() {
        return new BarColorSkin[]{
            SKIN_RED,
            SKIN_GREEN,
            SKIN_BLACK,
            SKIN_PINK,
            SKIN_ORANGE
        };
    }
}
