package com.gamesortir.gamification;

import com.gamesortir.algorithm.*;
import com.gamesortir.database.DatabaseManager;
import com.gamesortir.model.PlayerState;
import com.gamesortir.model.SimulationLog;
import com.gamesortir.model.InventoryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DESIGN PATTERN: Facade Pattern (Structural Pattern)
 * GameFacade adalah facade utama yang menyatukan:
 * 1. Database operations (DatabaseManager)
 * 2. Game logic dan scoring
 * 3. Sorting algorithm processing
 * 
 * KEUNTUNGAN:
 * - GUI JavaFX hanya perlu berinteraksi dengan GameFacade
 * - Kompleksitas subsistem tersembunyi
 * - Easy maintenance dan testing
 * 
 * KONSEP OOP: Instansiasi objek-objek dari berbagai subsistem
 */
public class GameFacade {
    private static final Logger logger = LoggerFactory.getLogger(GameFacade.class);
    private static GameFacade instance;
    
    private DatabaseManager databaseManager;
    private PlayerState currentPlayer;
    private SortAlgorithm currentAlgorithm;
    private List<SortStep> currentSteps;
    private int currentStepIndex;
    
    // ===== GAMIFICATION CONSTANTS =====
    private static final int POINTS_PER_CORRECT_SWAP = 10;
    private static final int POINTS_FOR_COMPLETION = 100;
    private static final int LIVES_PENALTY_FOR_WRONG_SWAP = 1;
    private static final int POINTS_PENALTY_FOR_WRONG_SWAP = 5;
    
    // ===== Singleton Pattern =====
    private GameFacade() {
        this.databaseManager = DatabaseManager.getInstance();
        this.currentStepIndex = 0;
    }
    
    public static synchronized GameFacade getInstance() {
        if (instance == null) {
            instance = new GameFacade();
        }
        return instance;
    }
    
    // ===== PLAYER MANAGEMENT =====
    
    /**
     * Membuat atau mengambil pemain
     */
    public void setCurrentPlayer(String playerName) {
        PlayerState existingPlayer = databaseManager.getPlayerStateByName(playerName);
        
        if (existingPlayer != null) {
            this.currentPlayer = existingPlayer;
            logger.info("Pemain diambil dari database: " + playerName);
        } else {
            // Buat pemain baru
            this.currentPlayer = new PlayerState(playerName);
            databaseManager.saveOrUpdatePlayerState(currentPlayer);
            logger.info("Pemain baru dibuat: " + playerName);
        }
    }
    
    public PlayerState getCurrentPlayer() {
        return currentPlayer;
    }
    
    // ===== ALGORITHM & SIMULATION MANAGEMENT =====
    
    /**
     * Mengatur algoritma sorting yang akan digunakan
     * DESIGN PATTERN: Strategy Pattern - GUI bisa mengganti strategi saat runtime
     */
    public void setAlgorithm(String algorithmType) {
        // FACTORY PATTERN: Menggunakan SortFactory untuk membuat algoritma
        this.currentAlgorithm = SortFactory.createSortAlgorithm(algorithmType);
        this.currentStepIndex = 0;
        logger.info("Algoritma diubah menjadi: " + currentAlgorithm.getAlgorithmName());
    }
    
    /**
     * Memulai simulasi dengan array angka
     */
    public void startSimulation(int[] array) {
        if (currentAlgorithm == null) {
            throw new IllegalStateException("Algoritma belum diatur!");
        }
        
        // Generate steps menggunakan algoritma yang dipilih
        this.currentSteps = currentAlgorithm.generateSteps(array);
        this.currentStepIndex = 0;
        
        logger.info("Simulasi dimulai dengan algoritma: " + currentAlgorithm.getAlgorithmName() +
                   " dan " + currentSteps.size() + " steps");
    }
    
    /**
     * Mendapatkan step berikutnya dalam simulasi
     */
    public SortStep getNextStep() {
        if (currentSteps == null || currentStepIndex >= currentSteps.size()) {
            logger.warn("Tidak ada step berikutnya");
            return null;
        }
        
        SortStep step = currentSteps.get(currentStepIndex);
        currentStepIndex++;
        return step;
    }
    
    /**
     * Mendapatkan step pada index tertentu
     */
    public SortStep getStepAt(int index) {
        if (currentSteps == null || index < 0 || index >= currentSteps.size()) {
            return null;
        }
        return currentSteps.get(index);
    }
    
    /**
     * Mendapatkan total jumlah steps
     */
    public int getTotalSteps() {
        return currentSteps != null ? currentSteps.size() : 0;
    }
    
    /**
     * Mendapatkan step index saat ini
     */
    public int getCurrentStepIndex() {
        return currentStepIndex;
    }
    
    public String getAlgorithmName() {
        return currentAlgorithm != null ? currentAlgorithm.getAlgorithmName() : "Unknown";
    }
    
    // ===== GAME MODE - MANUAL SWAP VALIDATION =====
    
    /**
     * Memvalidasi penukaran manual pemain dalam Mode Game
     * Mengecek apakah penukaran sesuai dengan aturan algoritma saat ini
     * 
     * @return true jika penukaran valid, false jika invalid
     */
    public boolean validateSwap(int index1, int index2, int[] currentArray) {
        if (currentAlgorithm == null) {
            return false;
        }
        
        SortStep expectedStep = getStepAt(currentStepIndex);
        if (expectedStep == null) {
            return false;
        }
        
        // Untuk Bubble Sort: hanya boleh tukar elemen bertetangga
        if (currentAlgorithm.getAlgorithmName().equals("Bubble Sort")) {
            if (Math.abs(index1 - index2) != 1) {
                logger.warn("Bubble Sort: Hanya boleh tukar elemen bertetangga!");
                return false;
            }
        }
        
        // Cek apakah penukaran sesuai dengan langkah yang diharapkan
        if (expectedStep.getComparingIndex1() == index1 && expectedStep.getComparingIndex2() == index2 ||
            expectedStep.getComparingIndex1() == index2 && expectedStep.getComparingIndex2() == index1) {
            logger.info("Penukaran valid!");
            return true;
        }
        
        logger.warn("Penukaran tidak sesuai dengan langkah yang diharapkan");
        return false;
    }
    
    // ===== SCORING & GAMIFICATION =====
    
    /**
     * Menambah skor pemain
     */
    public void addScore(int points) {
        if (currentPlayer != null) {
            int newScore = currentPlayer.getScore() + points;
            currentPlayer.setScore(newScore);
            logger.info("Skor ditambah: " + points + ". Total skor: " + newScore);
        }
    }
    
    /**
     * Mengurangi skor pemain
     */
    public void subtractScore(int points) {
        if (currentPlayer != null) {
            int newScore = Math.max(0, currentPlayer.getScore() - points);
            currentPlayer.setScore(newScore);
            logger.info("Skor dikurangi: " + points + ". Total skor: " + newScore);
        }
    }
    
    /**
     * Mengurangi nyawa pemain
     */
    public void subtractLife() {
        if (currentPlayer != null) {
            int newLives = Math.max(0, currentPlayer.getLives() - 1);
            currentPlayer.setLives(newLives);
            logger.info("Nyawa dikurangi. Sisa nyawa: " + newLives);
        }
    }
    
    /**
     * Menambah nyawa pemain (saat membeli ramuan)
     */
    public void addLife() {
        if (currentPlayer != null) {
            int newLives = currentPlayer.getLives() + 1;
            currentPlayer.setLives(newLives);
            logger.info("Nyawa ditambah. Total nyawa: " + newLives);
        }
    }
    
    /**
     * Menangani penukaran yang benar
     */
    public int handleCorrectSwap() {
        int points = POINTS_PER_CORRECT_SWAP;
        addScore(points);
        return points;
    }
    
    /**
     * Menangani penukaran yang salah
     */
    public void handleWrongSwap() {
        subtractLife();
        subtractScore(POINTS_PENALTY_FOR_WRONG_SWAP);
        logger.warn("Penukaran salah! Nyawa dan poin berkurang");
    }
    
    /**
     * Menangani penyelesaian simulasi dengan benar
     */
    public int handleSimulationComplete() {
        int bonusPoints = POINTS_FOR_COMPLETION;
        addScore(bonusPoints);
        
        // Naik level jika skor cukup
        if (currentPlayer.getScore() >= 100) {
            currentPlayer.setLevel(currentPlayer.getLevel() + 1);
            logger.info("Level naik!");
        }
        
        return bonusPoints;
    }
    
    // ===== SHOP & INVENTORY MANAGEMENT =====
    
    /**
     * Membeli item dari toko
     */
    public boolean buyItem(String itemType, String itemName, Integer itemId, Integer costInPoints) {
        if (currentPlayer == null) {
            logger.error("Pemain tidak ditemukan");
            return false;
        }
        
        // Cek apakah skor cukup
        if (currentPlayer.getScore() < costInPoints) {
            logger.warn("Skor tidak cukup untuk membeli item ini");
            return false;
        }
        
        // Kurangi skor
        subtractScore(costInPoints);
        
        // Tambahkan ke inventory
        InventoryItem newItem = new InventoryItem(
            currentPlayer.getId(),
            itemType,
            itemName,
            itemId,
            costInPoints
        );
        databaseManager.saveInventoryItem(newItem);
        
        logger.info("Item berhasil dibeli: " + itemName);
        return true;
    }
    
    /**
     * Mendapatkan semua item inventory pemain
     */
    public List<InventoryItem> getPlayerInventory() {
        if (currentPlayer == null) {
            return List.of();
        }
        return databaseManager.getInventoryItemsForPlayer(currentPlayer.getId());
    }
    
    /**
     * Meng-equip/menggunakan skin baru
     */
    public void equipSkin(Integer skinId) {
        if (currentPlayer != null) {
            currentPlayer.setEquippedSkinId(skinId);
            databaseManager.saveOrUpdatePlayerState(currentPlayer);
            logger.info("Skin diubah menjadi ID: " + skinId);
        }
    }
    
    /**
     * Menggunakan ramuan nyawa
     */
    public void useLifePotion() {
        if (currentPlayer != null) {
            addLife();
            logger.info("Ramuan nyawa digunakan");
        }
    }
    
    // ===== SAVE & LOAD OPERATIONS =====
    
    /**
     * Menyimpan state pemain ke database
     */
    public void savePlayerProgress() {
        if (currentPlayer != null) {
            currentPlayer.setLastPlayedTime(System.currentTimeMillis());
            databaseManager.saveOrUpdatePlayerState(currentPlayer);
            logger.info("Progress pemain disimpan");
        }
    }
    
    /**
     * Menyimpan log simulasi ke database
     */
    public void saveSimulationLog(String initialArray, String finalArray, 
                                  Integer stepsCount, Integer pointsEarned, String gameMode) {
        if (currentPlayer != null && currentAlgorithm != null) {
            SimulationLog log = new SimulationLog(
                currentPlayer.getPlayerName(),
                currentAlgorithm.getAlgorithmName(),
                initialArray
            );
            log.setFinalArray(finalArray);
            log.setStepsCount(stepsCount);
            log.setPointsEarned(pointsEarned);
            log.setGameMode(gameMode);
            log.setLivesRemaining(currentPlayer.getLives());
            
            databaseManager.saveSimulationLog(log);
            logger.info("Simulasi log disimpan");
        }
    }
    
    /**
     * Mendapatkan history simulasi pemain
     */
    public List<SimulationLog> getPlayerSimulationHistory() {
        if (currentPlayer != null) {
            return databaseManager.getSimulationLogsForPlayer(currentPlayer.getPlayerName());
        }
        return List.of();
    }
}
