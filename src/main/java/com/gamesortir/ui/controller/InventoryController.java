package com.gamesortir.ui.controller;

import com.gamesortir.gamification.ColorSkinManager;
import com.gamesortir.gamification.GameFacade;
import com.gamesortir.model.InventoryItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * KONSEP OOP: Controller untuk Inventory
 * Menampilkan semua item yang dimiliki pemain
 * User bisa equip skin dan menggunakan consumable items
 */
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    
    @FXML private VBox inventoryItemsContainer;
    @FXML private Label playerNameLabel;
    @FXML private Label scoreLabel;
    @FXML private Button backButton;
    
    private GameFacade gameFacade;
    
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        updatePlayerInfo();
        loadInventoryItems();
        logger.info("InventoryController diinisialisasi");
    }
    
    /**
     * Load dan display semua inventory items
     */
    private void loadInventoryItems() {
        inventoryItemsContainer.getChildren().clear();
        
        List<InventoryItem> inventoryItems = gameFacade.getPlayerInventory();
        
        if (inventoryItems.isEmpty()) {
            Label emptyLabel = new Label("Tas Anda kosong. Kunjungi toko untuk membeli item!");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d; -fx-padding: 20px;");
            inventoryItemsContainer.getChildren().add(emptyLabel);
            return;
        }
        
        // Separate items by type
        List<InventoryItem> skins = inventoryItems.stream()
            .filter(i -> i.getItemType().equals("SKIN_COLOR"))
            .toList();
        List<InventoryItem> consumables = inventoryItems.stream()
            .filter(i -> i.getItemType().equals("LIFE_POTION") || i.getItemType().equals("HINT"))
            .toList();
        
        // Display SKINS
        if (!skins.isEmpty()) {
            addCategoryLabel("🎨 SKIN WARNA BATANG");
            for (InventoryItem item : skins) {
                addInventorySkinItem(item);
            }
        }
        
        // Display CONSUMABLES
        if (!consumables.isEmpty()) {
            addCategoryLabel("🧪 ITEM KONSUMSI");
            for (InventoryItem item : consumables) {
                addInventoryConsumableItem(item);
            }
        }
        
        logger.info("Inventory items dimuat: " + inventoryItems.size() + " items");
    }
    
    /**
     * Add category label
     */
    private void addCategoryLabel(String categoryName) {
        Label categoryLabel = new Label(categoryName);
        categoryLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 15px 0px 10px 0px; " +
            "-fx-text-fill: #2c3e50;"
        );
        inventoryItemsContainer.getChildren().add(categoryLabel);
    }
    
    /**
     * Add skin item dengan tombol Equip
     */
    private void addInventorySkinItem(InventoryItem item) {
        HBox itemBox = new HBox();
        itemBox.setStyle(
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-radius: 5; " +
            "-fx-padding: 10px; " +
            "-fx-spacing: 15px; " +
            "-fx-background-color: #ecf0f1;"
        );
        itemBox.setPrefHeight(70);
        itemBox.setMinHeight(70);
        
        // Item info
        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-spacing: 5px;");
        
        Label nameLabel = new Label(item.getItemName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label typeLabel = new Label("Tipe: Skin Warna");
        typeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        
        infoBox.getChildren().addAll(nameLabel, typeLabel);
        
        // Equip button
        Button equipButton = new Button("EQUIP");
        equipButton.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-padding: 8px 20px; " +
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-cursor: hand;"
        );
        
        // Check if already equipped
        if (gameFacade.getCurrentPlayer().getEquippedSkinId().equals(item.getItemId())) {
            equipButton.setText("SEDANG DIPAKAI");
            equipButton.setDisable(true);
            equipButton.setStyle(
                "-fx-font-size: 12px; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white;"
            );
        }
        
        // Equip handler
        equipButton.setOnAction(event -> {
            gameFacade.equipSkin(item.getItemId());
            equipButton.setText("SEDANG DIPAKAI");
            equipButton.setDisable(true);
            equipButton.setStyle(
                "-fx-font-size: 12px; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white;"
            );
            showAlert(Alert.AlertType.INFORMATION, "Skin Diubah", 
                     "Skin " + item.getItemName() + " sekarang dipakai!");
            logger.info("Skin diequip: " + item.getItemName());
        });
        
        itemBox.getChildren().addAll(infoBox, equipButton);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        inventoryItemsContainer.getChildren().add(itemBox);
    }
    
    /**
     * Add consumable item dengan tombol Use
     */
    private void addInventoryConsumableItem(InventoryItem item) {
        HBox itemBox = new HBox();
        itemBox.setStyle(
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-radius: 5; " +
            "-fx-padding: 10px; " +
            "-fx-spacing: 15px; " +
            "-fx-background-color: #ecf0f1;"
        );
        itemBox.setPrefHeight(70);
        itemBox.setMinHeight(70);
        
        // Item info
        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-spacing: 5px;");
        
        Label nameLabel = new Label(item.getItemName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label quantityLabel = new Label("Jumlah: " + item.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        
        infoBox.getChildren().addAll(nameLabel, quantityLabel);
        
        // Use button
        Button useButton = new Button("GUNAKAN");
        useButton.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-padding: 8px 20px; " +
            "-fx-background-color: #9b59b6; " +
            "-fx-text-fill: white; " +
            "-fx-cursor: hand;"
        );
        
        useButton.setOnAction(event -> {
            if (item.getItemType().equals("LIFE_POTION")) {
                gameFacade.useLifePotion();
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() <= 0) {
                    // Remove from inventory
                    loadInventoryItems();
                } else {
                    quantityLabel.setText("Jumlah: " + item.getQuantity());
                }
                showAlert(Alert.AlertType.INFORMATION, "Item Digunakan", 
                         "Ramuan nyawa digunakan! Nyawa +1");
                logger.info("Life potion digunakan");
            } else if (item.getItemType().equals("HINT")) {
                showAlert(Alert.AlertType.INFORMATION, "Bocoran Langkah", 
                         "Lihat console atau log untuk bocoran langkah berikutnya");
                logger.info("Hint digunakan");
            }
        });
        
        itemBox.getChildren().addAll(infoBox, useButton);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        inventoryItemsContainer.getChildren().add(itemBox);
    }
    
    /**
     * Update player info
     */
    private void updatePlayerInfo() {
        playerNameLabel.setText("Pemain: " + gameFacade.getCurrentPlayer().getPlayerName());
        scoreLabel.setText("Skor: " + gameFacade.getCurrentPlayer().getScore());
    }
    
    /**
     * Handler untuk tombol Back
     */
    @FXML
    private void handleBackButton() {
        gameFacade.savePlayerProgress();
        openScene("ModeSelection.fxml", "GameSortir - Mode Selection");
    }
    
    /**
     * Show alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Open scene
     */
    private void openScene(String fxmlFileName, String windowTitle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/" + fxmlFileName)
            );
            
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            String cssResource = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(windowTitle);
        } catch (IOException e) {
            logger.error("Error membuka scene", e);
        }
    }
}
