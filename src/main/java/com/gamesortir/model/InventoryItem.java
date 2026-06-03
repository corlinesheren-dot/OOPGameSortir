package com.gamesortir.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * KONSEP OOP: Class & Object
 * Entity InventoryItem merepresentasikan item di inventory pemain.
 * Menyimpan informasi tentang skin, ramuan, atau bocoran yang dimiliki pemain.
 */
@Entity
@Table(name = "inventory_item")
public class InventoryItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private Long playerStateId;
    
    @Column(nullable = false)
    private String itemType;  // "SKIN_COLOR", "LIFE_POTION", "HINT"
    
    @Column(nullable = false)
    private String itemName;  // "Green Skin", "Life Potion", dll
    
    @Column(nullable = false)
    private Integer itemId;  // ID unik untuk item
    
    @Column(nullable = false)
    private Integer quantity = 1;
    
    @Column(nullable = false)
    private Integer costInPoints = 0;
    
    @Column(nullable = false)
    private Long acquiredTime = System.currentTimeMillis();
    
    // ===== KONSEP OOP: Attribute & Method (Encapsulation) =====
    
    public InventoryItem() {}
    
    public InventoryItem(Long playerStateId, String itemType, String itemName, Integer itemId, Integer costInPoints) {
        this.playerStateId = playerStateId;
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemId = itemId;
        this.costInPoints = costInPoints;
        this.acquiredTime = System.currentTimeMillis();
    }
    
    // Getter & Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPlayerStateId() {
        return playerStateId;
    }
    
    public void setPlayerStateId(Long playerStateId) {
        this.playerStateId = playerStateId;
    }
    
    public String getItemType() {
        return itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getCostInPoints() {
        return costInPoints;
    }
    
    public void setCostInPoints(Integer costInPoints) {
        this.costInPoints = costInPoints;
    }
    
    public Long getAcquiredTime() {
        return acquiredTime;
    }
    
    public void setAcquiredTime(Long acquiredTime) {
        this.acquiredTime = acquiredTime;
    }
    
    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", itemType='" + itemType + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
