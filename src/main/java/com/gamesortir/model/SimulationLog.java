package com.gamesortir.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * KONSEP OOP: Class & Object
 * Entity SimulationLog merepresentasikan log dari setiap simulasi/game yang dimainkan.
 * Menyimpan data seperti algorithm yang digunakan, nilai-nilai yang di-sort, dst.
 * 
 * PRINSIP SOLID: Single Responsibility Principle
 * Class ini hanya bertanggung jawab untuk merepresentasikan log simulasi.
 */
@Entity
@Table(name = "simulation_log")
public class SimulationLog implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String playerName;
    
    @Column(nullable = false)
    private String algorithmUsed;  // "BUBBLE_SORT" atau "SELECTION_SORT"
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String initialArray;  // Format: "5,3,8,1,9"
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String finalArray;
    
    @Column(nullable = false)
    private Integer stepsCount = 0;
    
    @Column(nullable = false)
    private Integer pointsEarned = 0;
    
    @Column(nullable = false)
    private String gameMode;  // "LEARN" atau "GAME"
    
    @Column(nullable = false)
    private Integer livesRemaining = 3;
    
    @Column(nullable = false)
    private Long timestamp = System.currentTimeMillis();
    
    // ===== KONSEP OOP: Attribute & Method (Encapsulation) =====
    
    public SimulationLog() {}
    
    public SimulationLog(String playerName, String algorithmUsed, String initialArray) {
        this.playerName = playerName;
        this.algorithmUsed = algorithmUsed;
        this.initialArray = initialArray;
        this.finalArray = "";
        this.gameMode = "LEARN";
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getter & Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public String getAlgorithmUsed() {
        return algorithmUsed;
    }
    
    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }
    
    public String getInitialArray() {
        return initialArray;
    }
    
    public void setInitialArray(String initialArray) {
        this.initialArray = initialArray;
    }
    
    public String getFinalArray() {
        return finalArray;
    }
    
    public void setFinalArray(String finalArray) {
        this.finalArray = finalArray;
    }
    
    public Integer getStepsCount() {
        return stepsCount;
    }
    
    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }
    
    public Integer getPointsEarned() {
        return pointsEarned;
    }
    
    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
    
    public String getGameMode() {
        return gameMode;
    }
    
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }
    
    public Integer getLivesRemaining() {
        return livesRemaining;
    }
    
    public void setLivesRemaining(Integer livesRemaining) {
        this.livesRemaining = livesRemaining;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "SimulationLog{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", algorithmUsed='" + algorithmUsed + '\'' +
                ", initialArray='" + initialArray + '\'' +
                ", finalArray='" + finalArray + '\'' +
                ", stepsCount=" + stepsCount +
                ", pointsEarned=" + pointsEarned +
                ", gameMode='" + gameMode + '\'' +
                '}';
    }
}
