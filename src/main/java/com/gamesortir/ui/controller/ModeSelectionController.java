package com.gamesortir.ui.controller;

import com.gamesortir.gamification.GameFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * KONSEP OOP: Controller untuk pemilihan mode (Learn atau Game)
 */
public class ModeSelectionController {
    private static final Logger logger = LoggerFactory.getLogger(ModeSelectionController.class);
    
    @FXML private Button learnModeButton;
    @FXML private Button gameModeButton;
    @FXML private Button shopButton;
    @FXML private Button inventoryButton;
    @FXML private Button backButton;
    
    private GameFacade gameFacade;
    
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        logger.info("ModeSelectionController diinisialisasi");
    }
    
    /**
     * Handler untuk tombol Learn Mode (Simulasi)
     */
    @FXML
    private void handleLearnModeClick() {
        logger.info("Learn Mode dipilih");
        openScene("LearnMode.fxml", "GameSortir - Learn Mode (Simulasi)");
    }
    
    /**
     * Handler untuk tombol Game Mode
     */
    @FXML
    private void handleGameModeClick() {
        logger.info("Game Mode dipilih");
        openScene("GameMode.fxml", "GameSortir - Game Mode");
    }
    
    /**
     * Handler untuk tombol Shop
     */
    @FXML
    private void handleShopClick() {
        logger.info("Shop dibuka");
        openScene("Shop.fxml", "GameSortir - Shop");
    }
    
    /**
     * Handler untuk tombol Inventory
     */
    @FXML
    private void handleInventoryClick() {
        logger.info("Inventory dibuka");
        openScene("Inventory.fxml", "GameSortir - Inventory");
    }
    
    /**
     * Handler untuk tombol Back (kembali ke Main Menu)
     */
    @FXML
    private void handleBackClick() {
        logger.info("Kembali ke Main Menu");
        openScene("MainMenu.fxml", "GameSortir - Main Menu");
    }
    
    /**
     * Helper method untuk membuka scene FXML
     */
    private void openScene(String fxmlFileName, String windowTitle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/" + fxmlFileName)
            );
            
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            String cssResource = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
            
            Stage stage = (Stage) learnModeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(windowTitle);
            
            logger.info("Scene " + fxmlFileName + " dibuka");
        } catch (IOException e) {
            logger.error("Error membuka " + fxmlFileName, e);
        }
    }
}
