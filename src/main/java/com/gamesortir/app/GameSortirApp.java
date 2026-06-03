package com.gamesortir.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * KONSEP OOP: Class utama untuk aplikasi JavaFX
 * Menjalankan aplikasi desktop GameSortir dengan JavaFX framework
 */
public class GameSortirApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(GameSortirApp.class);
    
    /**
     * Start method untuk menginisialisasi stage JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load main scene dari FXML
            FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/MainMenu.fxml")
            );
            
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            
            // Set aplikasi style
            String cssResource = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
            
            primaryStage.setTitle("GameSortir - Simulasi & Game Sorting Algorithm");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            
            // Set icon (opsional)
            try {
                Image icon = new Image(
                    getClass().getResourceAsStream("/images/icon.png")
                );
                if (icon != null) {
                    primaryStage.getIcons().add(icon);
                }
            } catch (Exception e) {
                logger.warn("Icon tidak ditemukan, menggunakan default");
            }
            
            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                logger.info("Aplikasi ditutup");
            });
            
            primaryStage.show();
            logger.info("Aplikasi GameSortir berhasil dimulai");
            
        } catch (IOException e) {
            logger.error("Error memuat FXML", e);
            e.printStackTrace();
        }
    }
    
    /**
     * Main entry point untuk aplikasi
     */
    public static void main(String[] args) {
        launch(args);
    }
}
