package com.gamesortir.ui.controller;

import com.gamesortir.algorithm.SortStep;
import com.gamesortir.gamification.BarColorSkin;
import com.gamesortir.gamification.ColorSkinManager;
import com.gamesortir.gamification.GameFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Controller untuk Game Mode
 * User harus menukar batang secara manual dengan aturan algoritma
 * 
 * FITUR:
 * - User input array atau random
 * - Visualisasi bar yang bisa diklik untuk ditukar
 * - Validasi penukaran sesuai aturan Bubble/Selection Sort
 * - Sistem poin dan nyawa
 * - Real-time skin change
 */
public class GameModeController {
    private static final Logger logger = LoggerFactory.getLogger(GameModeController.class);
    
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private TextArea arrayInputTextArea;
    @FXML private Button startButton;
    @FXML private Button submitButton;
    @FXML private Button resetButton;
    @FXML private HBox chartContainer;
    @FXML private Label statusLabel;
    @FXML private Label scoreLabel;
    @FXML private Label livesLabel;
    @FXML private Label levelLabel;
    @FXML private TextArea feedbackTextArea;
    @FXML private ComboBox<String> skinComboBox;
    @FXML private Button applySkinButton;
    @FXML private Button backButton;
    
    private GameFacade gameFacade;
    private List<Integer> currentArray;
    private List<Rectangle> barRectangles;
    private int[] originalArray;
    private Integer selectedIndex = null;
    private BarColorSkin currentSkin;
    private boolean gameStarted = false;
    
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        barRectangles = new ArrayList<>();
        currentSkin = ColorSkinManager.getSkinById(
            gameFacade.getCurrentPlayer().getEquippedSkinId()
        );
        
        algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort");
        algorithmComboBox.setValue("Bubble Sort");
        
        for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
            skinComboBox.getItems().add(skin.getSkinName());
        }
        skinComboBox.setValue(currentSkin.getSkinName());
        
        submitButton.setDisable(true);
        
        updatePlayerStats();
        logger.info("GameModeController diinisialisasi");
    }
    
    /**
     * Handler untuk tombol Start Game
     */
    @FXML
    private void handleStartButton() {
        String input = arrayInputTextArea.getText().trim();
        
        if (input.isEmpty()) {
            generateRandomArray();
            input = arrayInputTextArea.getText();
        }
        
        try {
            String[] parts = input.split("[,\\s]+");
            originalArray = new int[parts.length];
            
            for (int i = 0; i < parts.length; i++) {
                originalArray[i] = Integer.parseInt(parts[i].trim());
            }
            
            currentArray = new ArrayList<>();
            for (int val : originalArray) {
                currentArray.add(val);
            }
            
            String selectedAlgorithm = algorithmComboBox.getValue();
            String algorithmType = selectedAlgorithm.replace(" ", "_").toUpperCase();
            gameFacade.setAlgorithm(algorithmType);
            
            gameFacade.startSimulation(originalArray);
            
            drawChart();
            
            gameStarted = true;
            startButton.setDisable(true);
            submitButton.setDisable(false);
            feedbackTextArea.setText(
                "Game dimulai dengan algoritma: " + selectedAlgorithm + "\n" +
                "Klik dua batang untuk menukarnya sesuai aturan " + selectedAlgorithm + "!\n" +
                "Jika trik penukaran salah, nyawa dan poin berkurang."
            );
            
            logger.info("Game mode dimulai dengan " + selectedAlgorithm);
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format Error", 
                     "Input harus berupa angka");
            logger.error("Error parsing input", e);
        }
    }
    
    /**
     * Handler untuk submit/finish game
     */
    @FXML
    private void handleSubmitButton() {
        // Cek apakah array sudah terurut
        boolean isSorted = isSorted(currentArray);
        
        if (!isSorted) {
            showAlert(Alert.AlertType.WARNING, "Belum Selesai", 
                     "Array belum terurut dengan benar! Lanjutkan permainan.");
            return;
        }
        
        // Array sudah terurut - pemain menang!
        int bonusPoints = gameFacade.handleSimulationComplete();
        
        feedbackTextArea.setText(
            "🎉 SELAMAT! Anda menyelesaikan permainan!\n" +
            "Bonus poin: " + bonusPoints + "\n" +
            "Total skor sekarang: " + gameFacade.getCurrentPlayer().getScore()
        );
        
        submitButton.setDisable(true);
        updatePlayerStats();
        
        logger.info("Game selesai - pemain menang!");
    }
    
    /**
     * Handler untuk klik bar (select/swap)
     */
    private void handleBarClick(int barIndex) {
        if (selectedIndex == null) {
            // First selection
            selectedIndex = barIndex;
            updateChartHighlight();
            feedbackTextArea.setText("Batang " + barIndex + " dipilih. Pilih batang kedua untuk ditukar.");
        } else if (selectedIndex == barIndex) {
            // Deselect
            selectedIndex = null;
            updateChartHighlight();
            feedbackTextArea.setText("Pilihan dibatalkan. Pilih batang pertama.");
        } else {
            // Attempt swap
            int idx1 = selectedIndex;
            int idx2 = barIndex;
            
            // Validate swap
            if (gameFacade.validateSwap(idx1, idx2, toIntArray(currentArray))) {
                // Valid swap
                swap(currentArray, idx1, idx2);
                drawChart();
                gameFacade.handleCorrectSwap();
                feedbackTextArea.setText(
                    "✓ Penukaran benar! Anda mendapat 10 poin.\n" +
                    "Total skor: " + gameFacade.getCurrentPlayer().getScore()
                );
                logger.info("Penukaran benar: " + idx1 + " <-> " + idx2);
            } else {
                // Invalid swap
                gameFacade.handleWrongSwap();
                feedbackTextArea.setText(
                    "✗ Penukaran salah!\n" +
                    "- Nyawa berkurang 1\n" +
                    "- Poin berkurang 5\n" +
                    "Sisa nyawa: " + gameFacade.getCurrentPlayer().getLives()
                );
                logger.warn("Penukaran salah: " + idx1 + " <-> " + idx2);
                
                if (gameFacade.getCurrentPlayer().getLives() <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Game Over", "Nyawa habis! Game selesai.");
                    submitButton.setDisable(true);
                    gameStarted = false;
                }
            }
            
            selectedIndex = null;
            updatePlayerStats();
        }
    }
    
    /**
     * Handler untuk tombol Apply Skin
     */
    @FXML
    private void handleApplySkinButton() {
        String selectedSkin = skinComboBox.getValue();
        BarColorSkin skin = ColorSkinManager.getSkinById(
            getColorSkinIdByName(selectedSkin)
        );
        
        currentSkin = skin;
        gameFacade.equipSkin(skin.getSkinId());
        
        if (gameStarted) {
            drawChart();
        }
        
        logger.info("Skin diubah menjadi: " + selectedSkin);
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
     * Handler untuk tombol Reset
     */
    @FXML
    private void handleResetButton() {
        if (currentArray != null) {
            currentArray.clear();
        }
        barRectangles.clear();
        chartContainer.getChildren().clear();
        arrayInputTextArea.clear();
        feedbackTextArea.clear();
        
        submitButton.setDisable(true);
        startButton.setDisable(false);
        gameStarted = false;
        selectedIndex = null;
        
        logger.info("Game di-reset");
    }
    
    /**
     * Draw bar chart dengan click handlers
     */
    private void drawChart() {
        chartContainer.getChildren().clear();
        barRectangles.clear();
        
        if (currentArray == null || currentArray.isEmpty()) {
            return;
        }
        
        int maxValue = currentArray.stream().mapToInt(Integer::intValue).max().orElse(100);
        double chartHeight = 300;
        double barWidth = 500.0 / currentArray.size();
        
        for (int i = 0; i < currentArray.size(); i++) {
            final int barIndex = i;
            int value = currentArray.get(i);
            double barHeight = (double) value / maxValue * chartHeight;
            
            Rectangle bar = new Rectangle(barWidth - 5, barHeight);
            bar.setFill(currentSkin.getBarColor());
            bar.setStroke(javafx.scene.paint.Color.BLACK);
            bar.setStrokeWidth(1);
            bar.setCursor(javafx.scene.Cursor.HAND);
            
            // Click handler
            bar.setOnMouseClicked(event -> handleBarClick(barIndex));
            bar.setOnMouseEntered(event -> bar.setOpacity(0.8));
            bar.setOnMouseExited(event -> bar.setOpacity(1.0));
            
            Label valueLabel = new Label(String.valueOf(value));
            valueLabel.setPrefWidth(barWidth - 5);
            valueLabel.setStyle("-fx-text-alignment: center; -fx-font-size: 10px;");
            
            VBox barBox = new VBox(5, bar, valueLabel);
            barBox.setStyle("-fx-alignment: bottom-center; -fx-padding: 5px;");
            HBox.setHgrow(barBox, Priority.ALWAYS);
            
            chartContainer.getChildren().add(barBox);
            barRectangles.add(bar);
        }
    }
    
    /**
     * Update chart highlight
     */
    private void updateChartHighlight() {
        for (int i = 0; i < barRectangles.size(); i++) {
            Rectangle bar = barRectangles.get(i);
            if (i == selectedIndex) {
                bar.setFill(javafx.scene.paint.Color.web("#FFD700"));  // Gold
                bar.setStrokeWidth(3);
            } else {
                bar.setFill(currentSkin.getBarColor());
                bar.setStrokeWidth(1);
            }
        }
    }
    
    /**
     * Swap array elements
     */
    private void swap(List<Integer> array, int i, int j) {
        Integer temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    private int[] toIntArray(List<Integer> values) {
        int[] array = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            array[i] = values.get(i);
        }
        return array;
    }
    
    /**
     * Check if array is sorted
     */
    private boolean isSorted(List<Integer> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (array.get(i) > array.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Generate random array
     */
    private void generateRandomArray() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomNum = (int) (Math.random() * 100) + 1;
            sb.append(randomNum);
            if (i < 7) sb.append(", ");
        }
        arrayInputTextArea.setText(sb.toString());
    }
    
    /**
     * Get skin ID by name
     */
    private int getColorSkinIdByName(String skinName) {
        for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
            if (skin.getSkinName().equals(skinName)) {
                return skin.getSkinId();
            }
        }
        return 1;
    }
    
    /**
     * Update player stats
     */
    private void updatePlayerStats() {
        scoreLabel.setText("Skor: " + gameFacade.getCurrentPlayer().getScore());
        livesLabel.setText("Nyawa: " + gameFacade.getCurrentPlayer().getLives());
        levelLabel.setText("Level: " + gameFacade.getCurrentPlayer().getLevel());
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
