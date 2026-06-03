package com.gamesortir.ui.controller;

import com.gamesortir.algorithm.SortStep;
import com.gamesortir.gamification.BarColorSkin;
import com.gamesortir.gamification.ColorSkinManager;
import com.gamesortir.gamification.GameFacade;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * KONSEP OOP: Controller untuk Learn Mode (Simulasi)
 * Menampilkan bar chart visual, steps, dan penjelasan algoritma
 * 
 * FITUR:
 * - Input array angka manual atau random
 * - Visualisasi bar chart
 * - Tombol "Next Step" untuk menjalankan per step
 * - Penjelasan live commentary
 * - Bisa ganti skin warna
 */
public class LearnModeController {
    private static final Logger logger = LoggerFactory.getLogger(LearnModeController.class);
    
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private TextArea arrayInputTextArea;
    @FXML private Button startButton;
    @FXML private Button nextStepButton;
    @FXML private Button resetButton;
    @FXML private HBox chartContainer;
    @FXML private TextArea explanationTextArea;
    @FXML private Label stepCounterLabel;
    @FXML private Label scoreLabel;
    @FXML private Label livesLabel;
    @FXML private ComboBox<String> skinComboBox;
    @FXML private Button applySkinButton;
    @FXML private Button backButton;
    
    private GameFacade gameFacade;
    private List<Integer> currentArray;
    private List<Rectangle> barRectangles;
    private int currentStepIndex = 0;
    private BarColorSkin currentSkin;
    
    @FXML
    public void initialize() {
        gameFacade = GameFacade.getInstance();
        barRectangles = new ArrayList<>();
        currentSkin = ColorSkinManager.SKIN_RED;
        
        // Setup algorithm combo box
        algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort");
        algorithmComboBox.setValue("Bubble Sort");
        
        // Setup skin combo box
        for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
            skinComboBox.getItems().add(skin.getSkinName());
        }
        skinComboBox.setValue("Red (Default)");
        
        nextStepButton.setDisable(true);
        updatePlayerStats();
        
        logger.info("LearnModeController diinisialisasi");
    }
    
    /**
     * Handler untuk tombol Start
     * Memulai simulasi dengan array yang di-input
     */
    @FXML
    private void handleStartButton() {
        String input = arrayInputTextArea.getText().trim();
        
        if (input.isEmpty()) {
            // Generate random array jika input kosong
            generateRandomArray();
            input = arrayInputTextArea.getText().trim();
        }
        
        try {
            // Parse array dari input
            String[] parts = input.split("[,\\s]+");
            int[] array = new int[parts.length];
            
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i].trim());
            }
            
            currentArray = new ArrayList<>();
            for (int val : array) {
                currentArray.add(val);
            }
            
            // Set algoritma
            String selectedAlgorithm = algorithmComboBox.getValue();
            String algorithmType = selectedAlgorithm.replace(" ", "_").toUpperCase();
            gameFacade.setAlgorithm(algorithmType);
            
            // Start simulasi
            gameFacade.startSimulation(array);
            
            // Draw chart
            drawChart();
            
            nextStepButton.setDisable(false);
            startButton.setDisable(true);
            currentStepIndex = 0;
            
            explanationTextArea.setText(
                "Simulasi dimulai dengan algoritma: " + selectedAlgorithm + "\n" +
                "Array: " + arrayInputTextArea.getText() + "\n" +
                "Klik 'Next Step' untuk menjalankan langkah berikutnya."
            );
            
            logger.info("Simulasi dimulai: " + selectedAlgorithm + " dengan array: " + arrayInputTextArea.getText());
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format Error", 
                     "Input harus berupa angka yang dipisah dengan koma atau spasi");
            logger.error("Error parsing array input", e);
        }
    }
    
    /**
     * Handler untuk tombol Next Step
     */
    @FXML
    private void handleNextStepButton() {
        SortStep step = gameFacade.getNextStep();
        
        if (step == null) {
            nextStepButton.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Selesai", 
                     "Simulasi selesai! Array sudah terurut.");
            resetButton.setDisable(false);
            
            int bonusPoints = 50;
            gameFacade.addScore(bonusPoints);
            explanationTextArea.appendText(
                "\n\n✓ SIMULASI SELESAI!\n" +
                "Anda mendapat bonus " + bonusPoints + " poin!"
            );
            updatePlayerStats();
            return;
        }
        
        // Update array visualization
        currentArray.clear();
        for (int val : step.getCurrentArray()) {
            currentArray.add(val);
        }
        
        // Redraw chart with the latest array state, then highlight comparing/swapping indices.
        drawChart();
        updateChart(step.getComparingIndex1(), step.getComparingIndex2(), step.isSwapped());
        
        // Update explanation
        explanationTextArea.setText(
            "Step " + currentStepIndex + " dari " + gameFacade.getTotalSteps() + ":\n\n" +
            step.getExplanation() + "\n" +
            (step.isSwapped() ? "\n✓ Penukaran dilakukan" : "\n✗ Tidak ada penukaran")
        );
        
        currentStepIndex++;
        stepCounterLabel.setText("Step: " + currentStepIndex + " / " + gameFacade.getTotalSteps());
        
        logger.info("Step " + currentStepIndex + " ditampilkan");
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
        explanationTextArea.clear();
        
        nextStepButton.setDisable(true);
        startButton.setDisable(false);
        currentStepIndex = 0;
        stepCounterLabel.setText("Step: 0");
        
        logger.info("Simulasi di-reset");
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
        
        // Redraw chart dengan skin baru
        drawChart();
        
        logger.info("Skin diubah menjadi: " + selectedSkin);
        showAlert(Alert.AlertType.INFORMATION, "Skin Changed", 
                 "Warna batang diubah menjadi " + selectedSkin);
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
     * Draw bar chart berdasarkan currentArray
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
            int value = currentArray.get(i);
            double barHeight = (double) value / maxValue * chartHeight;
            
            // Create rectangle bar
            Rectangle bar = new Rectangle(barWidth - 5, barHeight);
            bar.setFill(currentSkin.getBarColor());
            bar.setStroke(javafx.scene.paint.Color.BLACK);
            bar.setStrokeWidth(1);
            
            // Create label untuk nilai
            Label valueLabel = new Label(String.valueOf(value));
            valueLabel.setPrefWidth(barWidth - 5);
            valueLabel.setStyle("-fx-text-alignment: center; -fx-font-size: 10px;");
            
            // VBox untuk bar + label
            VBox barBox = new VBox(5, bar, valueLabel);
            barBox.setStyle("-fx-alignment: bottom-center; -fx-padding: 5px;");
            HBox.setHgrow(barBox, Priority.ALWAYS);
            
            chartContainer.getChildren().add(barBox);
            barRectangles.add(bar);
        }
    }
    
    /**
     * Update chart untuk menampilkan highlight pada comparing/swapped indices
     */
    private void updateChart(int idx1, int idx2, boolean isSwapped) {
        for (int i = 0; i < barRectangles.size(); i++) {
            Rectangle bar = barRectangles.get(i);
            
            if (i == idx1 || i == idx2) {
                bar.setFill(isSwapped ? javafx.scene.paint.Color.web("#FFD700") : 
                           javafx.scene.paint.Color.web("#87CEEB"));  // Gold jika swap, Sky blue jika comparing
                bar.setStrokeWidth(3);
                bar.setStroke(javafx.scene.paint.Color.BLACK);
            } else {
                bar.setFill(currentSkin.getBarColor());
                bar.setStroke(javafx.scene.paint.Color.BLACK);
                bar.setStrokeWidth(1);
            }
        }
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
     * Get skin ID berdasarkan nama
     */
    private int getColorSkinIdByName(String skinName) {
        for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
            if (skin.getSkinName().equals(skinName)) {
                return skin.getSkinId();
            }
        }
        return 1;  // Default RED
    }
    
    /**
     * Update player stats display
     */
    private void updatePlayerStats() {
        scoreLabel.setText("Skor: " + gameFacade.getCurrentPlayer().getScore());
        livesLabel.setText("Nyawa: " + gameFacade.getCurrentPlayer().getLives());
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
