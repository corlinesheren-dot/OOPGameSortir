# ✅ CHECKLIST IMPLEMENTASI KRITERIA PENILAIAN DOSEN

Dokumen ini menunjukkan di mana setiap kriteria penilaian dari dosen telah diimplementasikan dalam aplikasi GameSortir.

---

## 📋 KRITERIA 1: BAHASA & GUI UTAMA

**Kriteria**: Wajib menggunakan Java (bukan JavaScript) dengan framework JavaFX untuk visualisasi grafik batang interaktif

### ✅ IMPLEMENTASI:
- **Bahasa**: Java 17+ (Bukan JavaScript)
  - Lokasi: Semua file `*.java` di `src/main/java/`
  
- **Framework JavaFX**:
  - File: `src/main/java/com/gamesortir/app/GameSortirApp.java`
  - Extends: `Application` (JavaFX Framework)
  - Method: `start(Stage primaryStage)` - Menginisialisasi JavaFX GUI
  
- **Visualisasi Grafik Batang Interaktif**:
  - File: `src/main/java/com/gamesortir/ui/controller/LearnModeController.java`
    - Method: `drawChart()` - Membuat bar chart dari array
    - Method: `updateChart(int idx1, int idx2, boolean isSwapped)` - Highlight bar yang dibandingkan/ditukar
  
  - File: `src/main/java/com/gamesortir/ui/controller/GameModeController.java`
    - Method: `drawChart()` - Bar chart dengan click handlers
    - Method: `handleBarClick(int barIndex)` - Handle klik bar untuk swap
  
  - File: `src/main/resources/fxml/LearnMode.fxml` - FXML UI
  - File: `src/main/resources/fxml/GameMode.fxml` - FXML UI
  - File: `src/main/resources/css/style.css` - Styling JavaFX
  
- **Bar Naik-Turun Berdasarkan Nilai**:
  - Code di LearnModeController.drawChart():
    ```java
    int maxValue = currentArray.stream().mapToInt(Integer::intValue).max().orElse(100);
    double chartHeight = 300;
    double barHeight = (double) value / maxValue * chartHeight;
    Rectangle bar = new Rectangle(barWidth - 5, barHeight);
    ```
    Bar height dihitung berdasarkan value relatif terhadap max value

---

## 📋 KRITERIA 2: DEPENDENCY & BUILD TOOL

**Kriteria**: Gunakan Gradle (build.gradle) untuk mengelola dependensi

### ✅ IMPLEMENTASI:
- File: `build.gradle` di root directory
  - Plugin: `java`, `application`, `org.openjfx.javafxplugin`
  - JavaFX Dependencies:
    ```gradle
    implementation 'org.openjfx:javafx-controls:21'
    implementation 'org.openjfx:javafx-fxml:21'
    implementation 'org.openjfx:javafx-graphics:21'
    ```
  
  - Hibernate & JPA Dependencies:
    ```gradle
    implementation 'org.hibernate.orm:hibernate-core:6.3.1.Final'
    implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    implementation 'jakarta.transaction:jakarta.transaction-api:2.0.1'
    ```
  
  - SQLite JDBC:
    ```gradle
    implementation 'org.xerial:sqlite-jdbc:3.44.0.0'
    ```
  
  - Logging:
    ```gradle
    implementation 'org.slf4j:slf4j-api:2.0.9'
    implementation 'org.slf4j:slf4j-simple:2.0.9'
    ```
  
- Build commands:
  ```bash
  gradle clean build      # Build project
  gradle run             # Run aplikasi
  gradle jar             # Create JAR
  ```

---

## 📋 KRITERIA 3: DATABASE & ORM

**Kriteria**: Wajib terintegrasi dengan database SQLite asli menggunakan Hibernate ORM / JPA

### ✅ IMPLEMENTASI:
- **SQLite Database**:
  - File: `gamesortir.db` (auto-created saat pertama run)
  - Driver: SQLite JDBC (di build.gradle)
  
- **Hibernate ORM Configuration**:
  - File: `src/main/java/com/gamesortir/database/DatabaseManager.java`
  - Konfigurasi Hibernate:
    ```java
    config.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
    config.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
    config.setProperty("hibernate.connection.url", "jdbc:sqlite:gamesortir.db");
    config.setProperty("hbm2ddl.auto", "update");  // Auto-create tables
    ```
  
- **JPA Entities**:
  - File: `src/main/java/com/gamesortir/model/PlayerState.java`
    - @Entity annotation
    - @Table name = "player_state"
    - Fields: playerName, score, lives, level, equippedSkinId, lastPlayedTime
  
  - File: `src/main/java/com/gamesortir/model/SimulationLog.java`
    - @Entity annotation
    - @Table name = "simulation_log"
    - Menyimpan data algorithmUsed, initialArray, finalArray, stepsCount, pointsEarned
  
  - File: `src/main/java/com/gamesortir/model/InventoryItem.java`
    - @Entity annotation
    - @Table name = "inventory_item"
    - Menyimpan data pembelian item
  
- **CRUD Operations via Hibernate**:
  - File: `src/main/java/com/gamesortir/database/DatabaseManager.java`
  - Methods:
    - `saveOrUpdatePlayerState(PlayerState)` - Save/update
    - `getPlayerStateById(Long)` - Read
    - `deletePlayerState(Long)` - Delete
    - `getSimulationLogsForPlayer(String)` - Query
    - `saveInventoryItem(InventoryItem)` - Save
    - `getInventoryItemsForPlayer(Long)` - Query
  
- **Database Schema** (auto-created):
  ```sql
  CREATE TABLE player_state (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      player_name VARCHAR NOT NULL,
      score INTEGER DEFAULT 0,
      lives INTEGER DEFAULT 3,
      level INTEGER DEFAULT 1,
      equipped_skin_id INTEGER DEFAULT 1,
      last_played_time BIGINT
  );
  
  CREATE TABLE simulation_log (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      player_name VARCHAR NOT NULL,
      algorithm_used VARCHAR NOT NULL,
      initial_array TEXT NOT NULL,
      final_array TEXT NOT NULL,
      steps_count INTEGER,
      points_earned INTEGER,
      game_mode VARCHAR,
      lives_remaining INTEGER,
      timestamp BIGINT
  );
  
  CREATE TABLE inventory_item (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      player_state_id BIGINT NOT NULL,
      item_type VARCHAR NOT NULL,
      item_name VARCHAR NOT NULL,
      item_id INTEGER NOT NULL,
      quantity INTEGER DEFAULT 1,
      cost_in_points INTEGER,
      acquired_time BIGINT
  );
  ```

---

## 📋 KRITERIA 4: KONSEP OOP WAJIB

**Kriteria**: Class & Object, Interface, Inheritance, Polymorphism, Attribute & Method (Enkapsulasi)

### ✅ A. CLASS & OBJECT (Instansiasi Objek)

- **Lokasi**: Semua file Java adalah definisi class
- **Contoh Instansiasi**:
  - File: `src/main/java/com/gamesortir/app/GameSortirApp.java`
    ```java
    // Instansiasi PlayerState
    PlayerState ps = new PlayerState("John");
    ```
  
  - File: `src/main/java/com/gamesortir/gamification/GameFacade.java`
    ```java
    // Instansiasi PlayerState & SortAlgorithm
    this.currentPlayer = new PlayerState(playerName);
    this.currentAlgorithm = SortFactory.createSortAlgorithm(algorithmType);
    ```
  
  - File: `src/main/java/com/gamesortir/algorithm/SortAlgorithm.java`
    ```java
    // Instansiasi SortStep
    SortStep step = new SortStep(array, idx1, idx2, explanation, isSwapped, isCompleted);
    ```

### ✅ B. INTERFACE

- **File**: `src/main/java/com/gamesortir/algorithm/SortAlgorithm.java`
  ```java
  public interface SortAlgorithm {
      List<SortStep> generateSteps(int[] array);
      String getAlgorithmName();
      String getDescription();
  }
  ```
  - Mendefinisikan kontrak untuk semua algoritma sorting
  - Implementasi: BubbleSort, SelectionSort

- **Konsep**: Strategy Pattern
  - GUI bisa menggunakan interface, tidak perlu tahu implementasi konkret
  - Memungkinkan dinamik switch algoritma saat runtime

### ✅ C. INHERITANCE (Abstract Class)

- **File**: `src/main/java/com/gamesortir/algorithm/BaseSort.java`
  ```java
  public abstract class BaseSort implements SortAlgorithm {
      protected String algorithmName;
      protected String description;
      
      public abstract List<SortStep> generateSteps(int[] array);
      
      protected void swap(int[] array, int i, int j) { ... }
      protected boolean isSorted(int[] array) { ... }
      protected int[] cloneArray(int[] array) { ... }
  }
  ```
  
- **Konsep**: Inheritance
  - BaseSort: abstract class dengan shared logic
  - BubbleSort extends BaseSort
  - SelectionSort extends BaseSort
  - Menghindari code duplication

- **Komentar di Kode**:
  ```java
  // KONSEP OOP: Inheritance / Abstract Class
  // BaseSort adalah abstract class yang mengimplementasikan logika umum untuk sorting.
  // Digunakan sebagai base class untuk BubbleSort dan SelectionSort.
  ```

### ✅ D. POLYMORPHISM (Method Override)

- **File**: `src/main/java/com/gamesortir/algorithm/BubbleSort.java`
  ```java
  @Override
  public List<SortStep> generateSteps(int[] array) {
      // Implementasi spesifik Bubble Sort
      for (int i = 0; i < n - 1; i++) {
          for (int j = 0; j < n - 1 - i; j++) {
              if (workingArray[j] > workingArray[j + 1]) {
                  swap(workingArray, j, j + 1);
                  // Create SortStep
              }
          }
      }
  }
  ```

- **File**: `src/main/java/com/gamesortir/algorithm/SelectionSort.java`
  ```java
  @Override
  public List<SortStep> generateSteps(int[] array) {
      // Implementasi spesifik Selection Sort
      for (int i = 0; i < n - 1; i++) {
          int minIndex = i;
          for (int j = i + 1; j < n; j++) {
              if (workingArray[j] < workingArray[minIndex]) {
                  minIndex = j;
              }
          }
          if (minIndex != i) {
              swap(workingArray, i, minIndex);
          }
      }
  }
  ```

- **Konsep**: Polymorphism (Method Override)
  - Kedua class override method `generateSteps()` dengan cara yang berbeda
  - Runtime polymorphism:
    ```java
    SortAlgorithm algo = SortFactory.createSortAlgorithm("BUBBLE_SORT");
    List<SortStep> steps = algo.generateSteps(array);  // Panggil BubbleSort version
    ```

- **Komentar di Kode**:
  ```java
  // KONSEP OOP: Polymorphism (Override method dari base class)
  // Menghasilkan list of steps untuk Bubble Sort
  ```

### ✅ E. ATTRIBUTE & METHOD (Enkapsulasi)

- **File**: `src/main/java/com/gamesortir/model/PlayerState.java`
  ```java
  // Private attributes (Encapsulation)
  private Long id;
  private String playerName;
  private Integer score = 0;
  private Integer lives = 3;
  private Integer level = 1;
  private Integer equippedSkinId = 1;
  private Long lastPlayedTime = System.currentTimeMillis();
  
  // Public Getter & Setter
  public String getPlayerName() { return playerName; }
  public void setPlayerName(String playerName) { this.playerName = playerName; }
  
  public Integer getScore() { return score; }
  public void setScore(Integer score) { this.score = score; }
  
  // ... other getters/setters
  ```

- **Konsep**: Enkapsulasi
  - Private variables melindungi state
  - Public getter/setter untuk controlled access
  - Validation bisa ditambahkan di setter jika perlu

- **Komentar di Kode**:
  ```java
  // KONSEP OOP: Attribute & Method (Encapsulation)
  // Getter & Setter untuk semua private variabel (Enkapsulasi)
  ```

---

## 📋 KRITERIA 5: PRINSIP SOLID

**Kriteria**: Terapkan minimal 1 prinsip SOLID (S, O, L, I, D)

### ✅ IMPLEMENTASI: Single Responsibility Principle (SRP)

- **Deskripsi**: Setiap class punya 1 tanggung jawab
  
- **Contoh 1: DatabaseManager**
  - File: `src/main/java/com/gamesortir/database/DatabaseManager.java`
  - Tanggung jawab: Hanya database operations
  - Tidak mengurus UI, tidak mengurus business logic
  - Komentar:
    ```java
    // PRINSIP SOLID: Single Responsibility Principle
    // Class ini hanya bertanggung jawab untuk mengurus database operations.
    ```

- **Contoh 2: PlayerState (JPA Entity)**
  - File: `src/main/java/com/gamesortir/model/PlayerState.java`
  - Tanggung jawab: Hanya merepresentasikan data pemain
  - Komentar:
    ```java
    // PRINSIP SOLID: Single Responsibility Principle
    // Class ini hanya bertanggung jawab untuk merepresentasikan state pemain.
    ```

- **Contoh 3: BubbleSort**
  - File: `src/main/java/com/gamesortir/algorithm/BubbleSort.java`
  - Tanggung jawab: Hanya implementasi algoritma Bubble Sort
  - Tidak mengurus database, tidak mengurus UI

- **Benefit**:
  - Mudah ditest
  - Mudah di-maintain
  - Mudah di-extend
  - Reusable components

### ✅ BONUS: Open/Closed Principle (OCP)

- **File**: `src/main/java/com/gamesortir/algorithm/BaseSort.java`
- **Deskripsi**: Class terbuka untuk extension (bisa bikin algoritma baru), tertutup untuk modification
- **Contoh**:
  - Bisa add algoritma baru (QuickSort, MergeSort) tanpa modify BaseSort
  - Subclass override `generateSteps()` sesuai kebutuhan
  - Komentar:
    ```java
    // PRINSIP SOLID: Open-Closed Principle
    // Class ini terbuka untuk extension (BubbleSort, SelectionSort bisa extends-nya)
    // tapi tertutup untuk modifikasi (logika umum tidak perlu diubah).
    ```

---

## 📋 KRITERIA 6: DESIGN PATTERNS (3 PATTERN WAJIB)

### ✅ A. FACTORY PATTERN (Creational Pattern)

**Lokasi**: `src/main/java/com/gamesortir/algorithm/SortFactory.java`

```java
public class SortFactory {
    public static SortAlgorithm createSortAlgorithm(String algorithmType) {
        switch (algorithmType.toUpperCase()) {
            case "BUBBLE_SORT":
                return new BubbleSort();  // Instansiasi objek
            case "SELECTION_SORT":
                return new SelectionSort();  // Instansiasi objek
            default:
                throw new IllegalArgumentException("...");
        }
    }
}
```

- **Problem**: GUI perlu membuat berbagai jenis algoritma
- **Solution**: Factory method menghandle object creation
- **Benefit**:
  - Centralized creation logic
  - GUI tidak perlu tahu detail pembuatan
  - Mudah add algoritma baru
  
- **Komentar**:
  ```java
  // DESIGN PATTERN: Factory Pattern (Creational Pattern)
  // SortFactory adalah factory class yang bertanggung jawab membuat objek algoritma sorting.
  ```

- **Penggunaan di GameFacade**:
  ```java
  // FACTORY PATTERN: Menggunakan SortFactory untuk membuat algoritma
  this.currentAlgorithm = SortFactory.createSortAlgorithm(algorithmType);
  ```

### ✅ B. FACADE PATTERN (Structural Pattern)

**Lokasi**: `src/main/java/com/gamesortir/gamification/GameFacade.java`

```java
public class GameFacade {
    private DatabaseManager databaseManager;
    private SortAlgorithm currentAlgorithm;
    private PlayerState currentPlayer;
    private List<SortStep> currentSteps;
    
    // Menyatukan semua subsistem dalam interface yang simple
    public void setCurrentPlayer(String playerName) { ... }
    public void setAlgorithm(String algorithmType) { ... }  // Strategy Pattern
    public void startSimulation(int[] array) { ... }
    public boolean validateSwap(int i, int j, int[] array) { ... }
    public void buyItem(String itemType, String itemName, ...) { ... }
    public void equipSkin(Integer skinId) { ... }
    public void savePlayerProgress() { ... }
}
```

- **Problem**: Kompleksitas subsistem (Database, Algorithm, Gamification)
- **Solution**: Facade menyatukan semua dalam 1 interface
- **Benefit**:
  - GUI hanya interaksi dengan GameFacade
  - Kompleksitas tersembunyi
  - Mudah di-maintain
  - Decoupled dari implementation
  
- **Komentar**:
  ```java
  // DESIGN PATTERN: Facade Pattern (Structural Pattern)
  // GameFacade adalah facade utama yang menyatukan:
  // 1. Database operations (DatabaseManager)
  // 2. Game logic dan scoring
  // 3. Sorting algorithm processing
  ```

- **Juga di DatabaseManager**:
  ```java
  // DESIGN PATTERN: Facade Pattern (Structural Pattern)
  // DatabaseManager adalah facade yang menyatukan semua operasi database Hibernate.
  // KEUNTUNGAN:
  // - Menyembunyikan kompleksitas Hibernate dari GUI JavaFX
  // - GUI hanya perlu memanggil method-method sederhana di class ini
  ```

### ✅ C. STRATEGY PATTERN (Behavioral Pattern)

**Lokasi**: 
- `src/main/java/com/gamesortir/algorithm/SortAlgorithm.java` (Interface)
- `src/main/java/com/gamesortir/algorithm/BubbleSort.java` (Strategy 1)
- `src/main/java/com/gamesortir/algorithm/SelectionSort.java` (Strategy 2)

```java
// Interface defines strategy
public interface SortAlgorithm {
    List<SortStep> generateSteps(int[] array);
    String getAlgorithmName();
}

// Concrete strategies
class BubbleSort implements SortAlgorithm { ... }
class SelectionSort implements SortAlgorithm { ... }

// Usage in GameFacade
public void setAlgorithm(String algorithmType) {
    // Switch strategy dynamically at runtime
    this.currentAlgorithm = SortFactory.createSortAlgorithm(algorithmType);
}
```

- **Problem**: GUI perlu support multiple sorting algorithms, bisa berubah saat runtime
- **Solution**: Encapsulate algorithms dalam Strategy objects
- **Benefit**:
  - GUI bisa switch strategy dinamis
  - Mudah add/remove strategi
  - Runtime flexibility
  
- **Komentar**:
  ```java
  // DESIGN PATTERN: Strategy Pattern
  // Interface SortAlgorithm memungkinkan GUI untuk mengganti strategi sorting 
  // (Bubble atau Selection) secara dinamis saat runtime tanpa mengubah kode GUI.
  ```

- **Penggunaan**:
  ```java
  // GUI Controller dapat switch algoritma saat runtime
  algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort");
  
  String selectedAlgorithm = algorithmComboBox.getValue();
  String algorithmType = selectedAlgorithm.replace(" ", "_").toUpperCase();
  gameFacade.setAlgorithm(algorithmType);  // Switch strategy!
  ```

---

## 📋 KRITERIA 7: MODE BELAJAR (SIMULASI)

**Kriteria**: User bisa input angka acak atau manual, menampilkan grafik batang visual, tombol "Langkah Selanjutnya", kotak teks penjelasan logika

### ✅ IMPLEMENTASI:

- **Controller**: `src/main/java/com/gamesortir/ui/controller/LearnModeController.java`

- **FXML UI**: `src/main/resources/fxml/LearnMode.fxml`

- **Input Array (Manual atau Random)**:
  ```java
  // Manual input
  String[] parts = input.split("[,\\s]+");
  int[] array = new int[parts.length];
  for (int i = 0; i < parts.length; i++) {
      array[i] = Integer.parseInt(parts[i].trim());
  }
  
  // Random generate
  private void generateRandomArray() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 8; i++) {
          int randomNum = (int) (Math.random() * 100) + 1;
          sb.append(randomNum);
      }
      arrayInputTextArea.setText(sb.toString());
  }
  ```

- **Visualisasi Grafik Batang**:
  ```java
  private void drawChart() {
      int maxValue = currentArray.stream().mapToInt(Integer::intValue).max().orElse(100);
      double chartHeight = 300;
      double barWidth = 500.0 / currentArray.size();
      
      for (int i = 0; i < currentArray.size(); i++) {
          int value = currentArray.get(i);
          double barHeight = (double) value / maxValue * chartHeight;
          
          Rectangle bar = new Rectangle(barWidth - 5, barHeight);
          bar.setFill(currentSkin.getBarColor());
          // ... add to chartContainer
      }
  }
  ```

- **Tombol "Langkah Berikutnya"**:
  ```java
  @FXML
  private void handleNextStepButton() {
      SortStep step = gameFacade.getNextStep();
      
      if (step == null) {
          nextStepButton.setDisable(true);
          // Simulasi selesai
          return;
      }
      
      // Update array visualization
      currentArray.clear();
      for (int val : step.getCurrentArray()) {
          currentArray.add(val);
      }
      
      // Highlight comparing indices
      updateChart(step.getComparingIndex1(), step.getComparingIndex2(), step.isSwapped());
      
      // ... show explanation
      explanationTextArea.setText(step.getExplanation());
  }
  ```

- **Live Commentary Penjelasan**:
  ```java
  // Dari SortStep.getExplanation()
  String explanation = String.format(
      "Bandingkan array[%d]=%d dengan array[%d]=%d. " +
      "Karena %d > %d, tukar posisinya.",
      j, array[j], j + 1, array[j + 1], array[j], array[j + 1]
  );
  ```

---

## 📋 KRITERIA 8: MODE GAME

**Kriteria**: User harus menukar batang secara manual, validasi aturan struktur data, salah swap = nyawa berkurang + poin minus, benar = bonus poin

### ✅ IMPLEMENTASI:

- **Controller**: `src/main/java/com/gamesortir/ui/controller/GameModeController.java`

- **FXML UI**: `src/main/resources/fxml/GameMode.fxml`

- **Click Handler untuk Bar**:
  ```java
  private void handleBarClick(int barIndex) {
      if (selectedIndex == null) {
          selectedIndex = barIndex;  // First selection
      } else if (selectedIndex == barIndex) {
          selectedIndex = null;  // Deselect
      } else {
          // Attempt swap
          if (gameFacade.validateSwap(selectedIndex, barIndex, originalArray)) {
              // VALID SWAP
              swap(currentArray, selectedIndex, barIndex);
              gameFacade.handleCorrectSwap();
          } else {
              // INVALID SWAP
              gameFacade.handleWrongSwap();
          }
          selectedIndex = null;
      }
  }
  ```

- **Validasi Penukaran**:
  ```java
  public boolean validateSwap(int index1, int index2, int[] currentArray) {
      if (currentAlgorithm == null) return false;
      
      // Untuk Bubble Sort: hanya boleh tukar elemen bertetangga
      if (currentAlgorithm.getAlgorithmName().equals("Bubble Sort")) {
          if (Math.abs(index1 - index2) != 1) {
              return false;  // Invalid: not neighbors
          }
      }
      
      // Check if swap matches expected step
      SortStep expectedStep = getStepAt(currentStepIndex);
      if (expectedStep == null) return false;
      
      return (expectedStep.getComparingIndex1() == index1 && 
              expectedStep.getComparingIndex2() == index2) ||
             (expectedStep.getComparingIndex1() == index2 && 
              expectedStep.getComparingIndex2() == index1);
  }
  ```

- **Handle Correct Swap**:
  ```java
  public int handleCorrectSwap() {
      int points = POINTS_PER_CORRECT_SWAP;  // 10 poin
      addScore(points);
      return points;
  }
  ```

- **Handle Wrong Swap**:
  ```java
  public void handleWrongSwap() {
      subtractLife();  // -1 nyawa
      subtractScore(POINTS_PENALTY_FOR_WRONG_SWAP);  // -5 poin
  }
  ```

- **Handle Completion**:
  ```java
  public int handleSimulationComplete() {
      int bonusPoints = POINTS_FOR_COMPLETION;  // 100 poin
      addScore(bonusPoints);
      
      if (currentPlayer.getScore() >= 100) {
          currentPlayer.setLevel(currentPlayer.getLevel() + 1);
      }
      
      return bonusPoints;
  }
  ```

---

## 📋 KRITERIA 9: TOKO HADIAH & INVENTORY

**Kriteria**: User bisa membeli skin, ramuan, bocoran dengan poin. Skin masuk ke inventory. Ada tombol "Pakai/Equip" yang mengubah warna batang real-time.

### ✅ A. TOKO HADIAH

- **Controller**: `src/main/java/com/gamesortir/ui/controller/ShopController.java`

- **FXML UI**: `src/main/resources/fxml/Shop.fxml`

- **Load Shop Items**:
  ```java
  private void loadShopItems() {
      // SKIN WARNA
      for (BarColorSkin skin : ColorSkinManager.getAllSkins()) {
          if (skin.getCostInPoints() > 0) {
              addShopItem(skin.getSkinName(), skin.getCostInPoints(), ...);
          }
      }
      
      // CONSUMABLES
      addShopItem("Ramuan Nyawa", 100, ...);
      addShopItem("Bocoran Langkah", 150, ...);
  }
  ```

- **Beli Item**:
  ```java
  private void handleBuyClick(String itemName, int cost, ...) {
      if (gameFacade.getCurrentPlayer().getScore() < cost) {
          showAlert("Skor Tidak Cukup");
          return;
      }
      
      if (gameFacade.buyItem(dbItemType, itemName, itemId, cost)) {
          showAlert("Pembelian Berhasil");
          updatePlayerInfo();
      }
  }
  ```

- **Buy Item Logic di GameFacade**:
  ```java
  public boolean buyItem(String itemType, String itemName, Integer itemId, Integer costInPoints) {
      if (currentPlayer == null) return false;
      
      if (currentPlayer.getScore() < costInPoints) {
          return false;  // Skor tidak cukup
      }
      
      subtractScore(costInPoints);  // Kurangi skor
      
      InventoryItem newItem = new InventoryItem(
          currentPlayer.getId(),
          itemType,
          itemName,
          itemId,
          costInPoints
      );
      databaseManager.saveInventoryItem(newItem);  // Simpan ke DB
      
      return true;
  }
  ```

### ✅ B. INVENTORY TAS

- **Controller**: `src/main/java/com/gamesortir/ui/controller/InventoryController.java`

- **FXML UI**: `src/main/resources/fxml/Inventory.fxml`

- **Load Inventory Items**:
  ```java
  private void loadInventoryItems() {
      List<InventoryItem> inventoryItems = gameFacade.getPlayerInventory();
      
      // Separate by type
      List<InventoryItem> skins = inventoryItems.stream()
          .filter(i -> i.getItemType().equals("SKIN_COLOR"))
          .toList();
      
      // Display skins
      for (InventoryItem item : skins) {
          addInventorySkinItem(item);
      }
  }
  ```

- **Equip Skin (Real-time Color Change)**:
  ```java
  private void addInventorySkinItem(InventoryItem item) {
      Button equipButton = new Button("EQUIP");
      
      equipButton.setOnAction(event -> {
          gameFacade.equipSkin(item.getItemId());  // Set di player
          equipButton.setText("SEDANG DIPAKAI");
          equipButton.setDisable(true);
          
          // Real-time color change jika di Learn/Game mode
          // Akan otomatis update saat user kembali ke mode tersebut
      });
  }
  ```

- **Equip Skin Logic di GameFacade**:
  ```java
  public void equipSkin(Integer skinId) {
      if (currentPlayer != null) {
          currentPlayer.setEquippedSkinId(skinId);  // Update player
          databaseManager.saveOrUpdatePlayerState(currentPlayer);  // Save ke DB
      }
  }
  ```

- **Real-time Update di LearnMode/GameMode**:
  ```java
  @FXML
  private void handleApplySkinButton() {
      String selectedSkin = skinComboBox.getValue();
      BarColorSkin skin = ColorSkinManager.getSkinById(...);
      
      currentSkin = skin;
      gameFacade.equipSkin(skin.getSkinId());  // Update di facade
      
      drawChart();  // Redraw chart dengan warna baru
  }
  ```

- **Color Skins Available**:
  - File: `src/main/java/com/gamesortir/gamification/ColorSkinManager.java`
  - SKIN_RED (Default, Free)
  - SKIN_GREEN (150 poin)
  - SKIN_BLACK (150 poin)
  - SKIN_PINK (200 poin)
  - SKIN_ORANGE (200 poin)

---

## 📋 KRITERIA TAMBAHAN: SEMUA FITUR TERINTEGRASI

- ✅ Bubble Sort & Selection Sort: Implementasi di `algorithm/` package
- ✅ Real-time Bar Chart: LearnModeController & GameModeController
- ✅ Sistem Poin & Nyawa: GameFacade + PlayerState
- ✅ Database Persistence: DatabaseManager + Hibernate
- ✅ Gamification: ColorSkinManager + InventoryItem
- ✅ Live Commentary: SortStep.getExplanation()
- ✅ Multi-mode Navigation: ModeSelectionController + FXML files

---

## 🎓 RINGKASAN PEMENUHAN KRITERIA

| Kriteria | Status | File/Lokasi |
|----------|--------|-------------|
| 1. Java + JavaFX GUI | ✅ | GameSortirApp.java, FXML files |
| 2. Gradle Build | ✅ | build.gradle |
| 3. SQLite + Hibernate | ✅ | DatabaseManager.java, Entity classes |
| 4. Class & Object | ✅ | Semua Java classes |
| 4. Interface | ✅ | SortAlgorithm.java |
| 4. Inheritance | ✅ | BaseSort.java |
| 4. Polymorphism | ✅ | BubbleSort.java, SelectionSort.java |
| 4. Encapsulation | ✅ | All Entity classes |
| 5. SOLID (SRP) | ✅ | DatabaseManager, SortAlgorithm, GameFacade |
| 6. Factory Pattern | ✅ | SortFactory.java |
| 6. Facade Pattern | ✅ | GameFacade.java, DatabaseManager.java |
| 6. Strategy Pattern | ✅ | SortAlgorithm interface + implementations |
| 7. Mode Belajar | ✅ | LearnModeController.java, LearnMode.fxml |
| 8. Mode Game | ✅ | GameModeController.java, GameMode.fxml |
| 9. Toko & Inventory | ✅ | ShopController.java, InventoryController.java |
| 9. Real-time Skin | ✅ | ColorSkinManager.java, updateChart() methods |

**SEMUA 13 KRITERIA TERCAKUP 100%** ✅

---

## 📝 CATATAN UNTUK LAPORAN

Saat menulis laporan, pastikan untuk:

1. **Sebutkan nama design pattern** di setiap pembahasan
2. **Tunjukkan code snippet** dari implementasi
3. **Jelaskan manfaat** dari setiap pattern/principle
4. **Gunakan diagram** dari DIAGRAMS.md
5. **Berikan komentar** tentang how OOP concepts meningkatkan maintainability
6. **Jelaskan flow** dari user perspective (main menu → mode → play)
7. **Dokumentasikan database schema** dan relationships
8. **Highlight SOLID principles** dan bagaimana menerapkannya

---

**Selamat Mengerjakan Laporan! Semua code sudah siap dan fully documented.** 🎉
