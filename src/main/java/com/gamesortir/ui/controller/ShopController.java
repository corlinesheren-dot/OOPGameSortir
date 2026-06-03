package com.gamesortir.ui.controller;

import com.gamesortir.gamification.BarColorSkin;
import com.gamesortir.gamification.ColorSkinManager;
import com.gamesortir.gamification.GameFacade;
import com.gamesortir.model.InventoryItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * KONSEP OOP: Controller untuk Shop
 * User bisa membeli skin, ramuan nyawa, dan bocoran langkah
 * 
 * FITUR:
 * - Display semua available items
 * - Show harga dan deskripsi
 * - Tombol "Beli" untuk membeli item
 * - Sistem skor/poin
 */
public class ShopController {
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);
    
    @FXML private VBox shopItemsContainer;
    @FXML private Label scoreLabel;
    @FXML private Label playerNameLabel;
    @FXML private Button backButton;
    
    private GameFacade gameFacade;
    
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        updatePlayerInfo();
        loadShopItems();
        logger.info("ShopController diinisialisasi");
    }
    
    /**
     * Load dan display semua shop items
     */
    private void loadShopItems() {
        shopItemsContainer.getChildren().clear();
        
        // CATEGORY 1: SKINS
        addCategoryLabel("🎨 SKIN WARNA BATANG");
        for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
            if (skin.getCostInPoints() > 0) {  // Skip default red
                addShopItem(skin.getSkinName(), skin.getCostInPoints(), 
                           "Ubah warna batang menjadi " + skin.getSkinName(), 
                           "SKIN", skin.getSkinId());
            }
        }
        
        // CATEGORY 2: CONSUMABLES
        addCategoryLabel("🧪 ITEM KONSUMSI");
        addShopItem("Ramuan Nyawa", 100, 
                   "Tambah 1 nyawa", "LIFE_POTION", 1);
        addShopItem("Bocoran Langkah", 150, 
                   "Lihat langkah berikutnya dalam simulasi", "HINT", 2);
        
        logger.info("Shop items dimuat");
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
        shopItemsContainer.getChildren().add(categoryLabel);
    }
    
    /**
     * Add shop item UI
     */
    private void addShopItem(String itemName, int cost, String description, 
                            String itemType, Integer itemId) {
        // Item container
        HBox itemBox = new HBox();
        itemBox.setStyle(
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-radius: 5; " +
            "-fx-padding: 10px; " +
            "-fx-spacing: 15px; " +
            "-fx-background-color: #ecf0f1;"
        );
        itemBox.setPrefHeight(80);
        itemBox.setMinHeight(80);
        
        // Item info (left side)
        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-spacing: 5px;");
        
        Label nameLabel = new Label(itemName);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        
        Label costLabel = new Label("Harga: " + cost + " poin");
        costLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        
        infoBox.getChildren().addAll(nameLabel, descLabel, costLabel);
        
        // Buy button (right side)
        Button buyButton = new Button("BELI");
        buyButton.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-color: #27ae60; " +
            "-fx-text-fill: white; " +
            "-fx-cursor: hand;"
        );
        
        // Check if player already has item
        if (itemType.equals("SKIN")) {
            List<InventoryItem> inventory = gameFacade.getPlayerInventory();
            boolean hasItem = inventory.stream()
                .anyMatch(i -> i.getItemId().equals(itemId) && i.getItemType().equals("SKIN_COLOR"));
            if (hasItem) {
                buyButton.setText("SUDAH PUNYA");
                buyButton.setDisable(true);
                buyButton.setStyle(
                    "-fx-font-size: 12px; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-background-color: #95a5a6; " +
                    "-fx-text-fill: white;"
                );
            }
        }
        
        // Buy button handler
        final Integer finalItemId = itemId;
        final String finalItemType = itemType;
        buyButton.setOnAction(event -> handleBuyClick(itemName, cost, finalItemType, finalItemId, buyButton));
        
        itemBox.getChildren().addAll(infoBox, buyButton);
        HBox.setHgrow(infoBox, javafx.scene.layout.Priority.ALWAYS);
        
        shopItemsContainer.getChildren().add(itemBox);
    }
    
    /**
     * Handle tombol beli
     */
    private void handleBuyClick(String itemName, int cost, String itemType, 
                               Integer itemId, Button buyButton) {
        // Cek apakah skor cukup
        if (gameFacade.getCurrentPlayer().getScore() < cost) {
            showAlert(Alert.AlertType.WARNING, "Skor Tidak Cukup", 
                     "Anda membutuhkan " + cost + " poin untuk membeli " + itemName + 
                     "\nSkor Anda saat ini: " + gameFacade.getCurrentPlayer().getScore());
            return;
        }
        
        // Mapping item type
        String dbItemType = itemType.equals("SKIN") ? "SKIN_COLOR" : itemType;
        
        // Beli item
        if (gameFacade.buyItem(dbItemType, itemName, itemId, cost)) {
            showAlert(Alert.AlertType.INFORMATION, "Pembelian Berhasil", 
                     "Anda berhasil membeli " + itemName + "!");
            
            if (itemType.equals("SKIN")) {
                buyButton.setText("SUDAH PUNYA");
                buyButton.setDisable(true);
            }
            
            updatePlayerInfo();
            logger.info("Item dibeli: " + itemName);
        } else {
            showAlert(Alert.AlertType.ERROR, "Pembelian Gagal", 
                     "Gagal membeli " + itemName);
            logger.error("Gagal membeli item: " + itemName);
        }
    }
    
    /**
     * Update player info display
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
     * Show alert dialog
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
