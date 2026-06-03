package com.gamesortir.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * KONSEP OOP: Class & Object
 * Entity PlayerState merepresentasikan state pemain dalam database.
 * Menggunakan @Entity untuk JPA/Hibernate mapping ke database SQLite.
 * 
 * PRINSIP SOLID: Single Responsibility Principle
 * Class ini hanya bertanggung jawab untuk merepresentasikan state pemain.
 */
@Entity
@Table(name = "player_state")
public class PlayerState implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String playerName;
    
    @Column(nullable = false)
    private Integer score = 0;
    
    @Column(nullable = false)
    private Integer lives = 3;
    
    @Column(nullable = false)
    private Integer level = 1;
    
    // Skin ID: 1=Default(Red), 2=Green, 3=Black, 4=Pink, 5=Orange
    @Column(nullable = false)
    private Integer equippedSkinId = 1;
    
    @Column(nullable = false)
    private Long lastPlayedTime = System.currentTimeMillis();
    
    // ===== KONSEP OOP: Attribute & Method (Encapsulation) =====
    // Getter & Setter untuk semua private variabel (Enkapsulasi)
    
    public PlayerState() {}
    
    public PlayerState(String playerName) {
        this.playerName = playerName;
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.equippedSkinId = 1;
        this.lastPlayedTime = System.currentTimeMillis();
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
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Integer getLives() {
        return lives;
    }
    
    public void setLives(Integer lives) {
        this.lives = lives;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getEquippedSkinId() {
        return equippedSkinId;
    }
    
    public void setEquippedSkinId(Integer equippedSkinId) {
        this.equippedSkinId = equippedSkinId;
    }
    
    public Long getLastPlayedTime() {
        return lastPlayedTime;
    }
    
    public void setLastPlayedTime(Long lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }
    
    @Override
    public String toString() {
        return "PlayerState{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                ", lives=" + lives +
                ", level=" + level +
                ", equippedSkinId=" + equippedSkinId +
                '}';
    }
}
