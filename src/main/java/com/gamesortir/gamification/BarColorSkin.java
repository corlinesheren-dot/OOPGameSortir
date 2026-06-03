package com.gamesortir.gamification;

import javafx.scene.paint.Color;

/**
 * KONSEP OOP: Class untuk merepresentasikan warna skin bar chart
 * DTO (Data Transfer Object) untuk konfigurasi warna
 */
public class BarColorSkin {
    private int skinId;                   // ID unik untuk skin
    private String skinName;              // Nama skin (e.g., "Merah", "Hijau")
    private Color barColor;               // Warna bar utama
    private Color highlightColor;         // Warna highlight saat dibandingkan/ditukar
    private int costInPoints;             // Biaya dalam points untuk membeli skin
    
    public BarColorSkin(int skinId, String skinName, Color barColor, Color highlightColor, int costInPoints) {
        this.skinId = skinId;
        this.skinName = skinName;
        this.barColor = barColor;
        this.highlightColor = highlightColor;
        this.costInPoints = costInPoints;
    }
    
    // Getter & Setter
    public int getSkinId() {
        return skinId;
    }
    
    public void setSkinId(int skinId) {
        this.skinId = skinId;
    }
    
    public String getSkinName() {
        return skinName;
    }
    
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }
    
    public Color getBarColor() {
        return barColor;
    }
    
    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }
    
    public Color getHighlightColor() {
        return highlightColor;
    }
    
    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }
    
    public int getCostInPoints() {
        return costInPoints;
    }
    
    public void setCostInPoints(int costInPoints) {
        this.costInPoints = costInPoints;
    }
    
    @Override
    public String toString() {
        return "BarColorSkin{" +
                "skinId=" + skinId +
                ", skinName='" + skinName + '\'' +
                ", costInPoints=" + costInPoints +
                '}';
    }
}
