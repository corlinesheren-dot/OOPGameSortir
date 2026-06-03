package com.gamesortir.ui.controller;

import com.gamesortir.gamification.GameFacade;
import com.gamesortir.model.PlayerState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * KONSEP OOP: Controller class untuk Main Menu
 * Menangani interaksi user di layar utama aplikasi
 * Berinteraksi dengan GameFacade untuk business logic
 */
public class MainMenuController {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuController.class);
    
    @FXML private TextField playerNameField;
    @FXML private Button playButton;
    @FXML private Label welcomeLabel;
    
    private GameFacade gameFacade;
    
    /**
     * Method yang dipanggil saat FXML di-load
     */
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        logger.info("MainMenuController diinisialisasi");
    }
    
    /**
     * Handler untuk tombol "Play/Mulai"
     */
    @FXML
    private void handlePlayButtonClick() {
        String playerName = playerNameField.getText().trim();
        
        if (playerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Nama pemain tidak boleh kosong!");
            return;
        }
        
        try {
            // Set pemain di GameFacade
            gameFacade.setCurrentPlayer(playerName);
            PlayerState player = gameFacade.getCurrentPlayer();
            logger.info("Pemain dipilih: " + player.getPlayerName() + 
                       " (Skor: " + player.getScore() + ", Nyawa: " + player.getLives() + ")");
            
            // Buka Main Game Menu (pilih mode)
            openModeSelection();
            
        } catch (Exception e) {
            logger.error("Error saat memilih pemain", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error: " + e.getMessage());
        }
    }
    
    /**
     * Membuka layar pemilihan mode (Learn atau Game)
     */
    private void openModeSelection() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/ModeSelection.fxml")
            );
            
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            String cssResource = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
            
            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("GameSortir - Pilih Mode");
            
            logger.info("Mode Selection window dibuka");
        } catch (IOException e) {
            logger.error("Error membuka Mode Selection", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka Mode Selection");
        }
    }
    
    /**
     * Helper method untuk menampilkan alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
