/**
 * ============================================================================
 * DOKUMENTASI ARSITEKTUR APLIKASI GAMESORTIR
 * Simulasi Interaktif & Game Pengurutan Struktur Data (Bubble Sort & Selection Sort)
 * ============================================================================
 * 
 * DAFTAR ISI:
 * 1. Ringkasan Arsitektur
 * 2. Class Diagram
 * 3. Sequence Diagram
 * 4. Component Description
 * 5. Design Patterns & SOLID Principles
 * 
 * ============================================================================
 * 1. RINGKASAN ARSITEKTUR
 * ============================================================================
 * 
 * Aplikasi GameSortir menggunakan arsitektur layered (berlapis) dengan design pattern:
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │  PRESENTATION LAYER (JavaFX UI)                             │
 * │  - MainMenuController, ModeSelectionController              │
 * │  - LearnModeController, GameModeController                  │
 * │  - ShopController, InventoryController                      │
 * └──────────────────────┬──────────────────────────────────────┘
 *                        │
 * ┌──────────────────────▼──────────────────────────────────────┐
 * │  FACADE LAYER (Business Logic)                              │
 * │  - GameFacade (Menyatukan Database, Algorithm, Gamification)│
 * │  - Facade Pattern untuk menyederhanakan kompleksitas        │
 * └──────────────────────┬──────────────────────────────────────┘
 *                        │
 * ┌──────────────────────▼──────────────────────────────────────┐
 * │  SERVICE LAYER                                              │
 * │  ├─ DatabaseManager (Hibernate/JPA Operations)             │
 * │  ├─ SortAlgorithm Interface & Implementations               │
 * │  │  ├─ BubbleSort                                          │
 * │  │  └─ SelectionSort                                       │
 * │  ├─ SortFactory (Factory Pattern)                           │
 * │  └─ ColorSkinManager (Gamification)                         │
 * └──────────────────────┬──────────────────────────────────────┘
 *                        │
 * ┌──────────────────────▼──────────────────────────────────────┐
 * │  DATA LAYER (JPA/Hibernate Entities)                        │
 * │  - PlayerState                                              │
 * │  - SimulationLog                                            │
 * │  - InventoryItem                                            │
 * └──────────────────────┬──────────────────────────────────────┘
 *                        │
 * ┌──────────────────────▼──────────────────────────────────────┐
 * │  DATABASE LAYER (SQLite)                                    │
 * │  - gamesortir.db (SQLite database file)                     │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ============================================================================
 * 2. CLASS DIAGRAM (Notation UML Simplified)
 * ============================================================================
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              <<interface>> SortAlgorithm                    │
 * ├─────────────────────────────────────────────────────────────┤
 * │ - algorithmName: String                                     │
 * ├─────────────────────────────────────────────────────────────┤
 * │ + generateSteps(int[]): List<SortStep>                      │
 * │ + getAlgorithmName(): String                                │
 * │ + getDescription(): String                                  │
 * └──────────────────┬──────────────────────────────────────────┘
 *                    │
 *         ┌──────────┴──────────┐
 *         │                     │
 *    ┌────▼───────┐    ┌───────▼────┐
 *    │ BubbleSort │    │SelectionSort│
 *    ├────────────┤    ├────────────┤
 *    │ -steps[]   │    │ -steps[]   │
 *    ├────────────┤    ├────────────┤
 *    │+generateSteps│  │+generateSteps│
 *    │ (Override)  │    │(Override)  │
 *    └────────────┘    └────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              BaseSort (Abstract Class)                      │
 * ├─────────────────────────────────────────────────────────────┤
 * │ # algorithmName: String                                     │
 * │ # description: String                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ + generateSteps(int[]): List<SortStep> <<abstract>>         │
 * │ # swap(int[], int, int): void                               │
 * │ # isSorted(int[]): boolean                                  │
 * │ # cloneArray(int[]): int[]                                  │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              SortFactory (Factory Pattern)                  │
 * ├─────────────────────────────────────────────────────────────┤
 * │ - instance: SortFactory <<singleton>>                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ + createSortAlgorithm(String): SortAlgorithm                │
 * │ + createSortAlgorithm(AlgorithmType): SortAlgorithm         │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              GameFacade (Facade Pattern)                    │
 * ├─────────────────────────────────────────────────────────────┤
 * │ - databaseManager: DatabaseManager                          │
 * │ - currentPlayer: PlayerState                                │
 * │ - currentAlgorithm: SortAlgorithm                           │
 * │ - currentSteps: List<SortStep>                              │
 * ├─────────────────────────────────────────────────────────────┤
 * │ + getInstance(): GameFacade <<singleton>>                   │
 * │ + setCurrentPlayer(String): void                            │
 * │ + setAlgorithm(String): void   <<Strategy Pattern>>         │
 * │ + startSimulation(int[]): void                              │
 * │ + validateSwap(int, int, int[]): boolean                    │
 * │ + buyItem(...): boolean                                     │
 * │ + equipSkin(int): void                                      │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              DatabaseManager                                │
 * ├─────────────────────────────────────────────────────────────┤
 * │ - sessionFactory: SessionFactory                            │
 * │ - instance: DatabaseManager <<singleton>>                   │
 * ├─────────────────────────────────────────────────────────────┤
 * │ + getInstance(): DatabaseManager                            │
 * │ + saveOrUpdatePlayerState(PlayerState): void                │
 * │ + getPlayerStateById(Long): PlayerState                     │
 * │ + saveSimulationLog(SimulationLog): void                    │
 * │ + getSimulationLogsForPlayer(String): List<SimulationLog>   │
 * │ + saveInventoryItem(InventoryItem): void                    │
 * │ + getInventoryItemsForPlayer(Long): List<InventoryItem>     │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              JPA Entities (Data Model)                      │
 * ├─────────────────────────────────────────────────────────────┤
 * │ PlayerState       SimulationLog     InventoryItem           │
 * │ ├─ id             ├─ id             ├─ id                   │
 * │ ├─ playerName     ├─ playerName     ├─ playerStateId        │
 * │ ├─ score          ├─ algorithmUsed  ├─ itemType             │
 * │ ├─ lives          ├─ initialArray   ├─ itemName             │
 * │ ├─ level          ├─ finalArray     ├─ itemId               │
 * │ ├─ equippedSkinId ├─ stepsCount     ├─ quantity             │
 * │ └─ lastPlayedTime ├─ pointsEarned   └─ costInPoints         │
 * │                   ├─ gameMode       (1:N Relationship)      │
 * │                   ├─ livesRemaining │  PlayerState           │
 * │                   └─ timestamp      │  ├─ 1                  │
 * │                    (1:N Relationship├──┘                    │
 * │                     PlayerState     │  InventoryItem        │
 * │                     ├─ 1            ├─ N                    │
 * │                     └──N)           └──(1:N Relationship)   │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * ============================================================================
 * 3. SEQUENCE DIAGRAM - MAIN FLOW (Main Menu → Learn Mode → Simulasi)
 * ============================================================================
 * 
 * User          GUI             Controller         GameFacade        Database
 *  │             │                  │                   │              │
 *  │──Input Name──>│                  │                   │              │
 *  │             │──setCurrentPlayer─────────────────────>│              │
 *  │             │                  │  checkPlayerExist──────────────────>│
 *  │             │                  │                   │              │
 *  │             │                  │            create or load          │
 *  │             │<─Player Created───────────────────────│              │
 *  │             │                  │                   │              │
 *  │──Click Learn─>│                  │                   │              │
 *  │   Mode       │                  │                   │              │
 *  │             │──setAlgorithm───────────────────────>│              │
 *  │             │                  │    createSortAlgorithm           │
 *  │             │                  │    (via SortFactory)             │
 *  │             │                  │    Return: SortAlgorithm         │
 *  │             │<─Algorithm Set────────────────────────│              │
 *  │             │                  │                   │              │
 *  │──Input Array─>│                  │                   │              │
 *  │──Click Start─>│                  │                   │              │
 *  │             │──startSimulation────────────────────>│              │
 *  │             │                  │   algorithm.generateSteps()      │
 *  │             │                  │   Return: List<SortStep>         │
 *  │             │<─Steps Generated─────────────────────│              │
 *  │             │                  │                   │              │
 *  │   [Bar Chart Drawn]             │                   │              │
 *  │             │                  │                   │              │
 *  │──Click Next─>│                  │                   │              │
 *  │   Step      │──getNextStep────────────────────────>│              │
 *  │             │                  │   Return: SortStep              │
 *  │             │<─SortStep Data───────────────────────│              │
 *  │             │                  │                   │              │
 *  │   [Update Visualization]        │                   │              │
 *  │   [Show Explanation]            │                   │              │
 *  │             │                  │                   │              │
 *  │  ... (Repeat for all steps)     │                   │              │
 *  │             │                  │                   │              │
 *  │             │                  │                   │              │
 * 
 * ============================================================================
 * 4. SEQUENCE DIAGRAM - GAME MODE FLOW (Manual Swap Validation)
 * ============================================================================
 * 
 * User          GUI             Controller         GameFacade        Database
 *  │             │                  │                   │              │
 *  │──Click Bar1──>│                  │                   │              │
 *  │             │                   │                   │              │
 *  │             │  [Highlight Bar1]                     │              │
 *  │             │                   │                   │              │
 *  │──Click Bar2──>│                  │                   │              │
 *  │             │──validateSwap(1,2)──────────────────>│              │
 *  │             │                  │   Check:           │              │
 *  │             │                  │   - Neighbor?      │              │
 *  │             │                  │   - Expected step? │              │
 *  │             │<─true/false───────────────────────────│              │
 *  │             │                  │                   │              │
 *  │         IF VALID:               │                   │              │
 *  │             │  handleCorrectSwap()                 │              │
 *  │             │                  │   addScore(10)    │              │
 *  │             │                  │──saveProgress────────────────────>│
 *  │             │                  │                   │              │
 *  │         IF INVALID:             │                   │              │
 *  │             │  handleWrongSwap()                   │              │
 *  │             │                  │   subtractLife()  │              │
 *  │             │                  │   subtractScore() │              │
 *  │             │                  │──saveProgress────────────────────>│
 *  │             │                  │                   │              │
 *  │   [Update Score/Lives Display]  │                   │              │
 *  │             │                  │                   │              │
 * 
 * ============================================================================
 * 5. SEQUENCE DIAGRAM - SHOP & INVENTORY FLOW
 * ============================================================================
 * 
 * User          GUI             Controller         GameFacade        Database
 *  │             │                  │                   │              │
 *  │──Click Shop─>│                  │                   │              │
 *  │             │──getPlayerInventory()───────────────>│              │
 *  │             │                  │──queryInventory───────────────────>│
 *  │             │<─Inventory List──────────────────────│              │
 *  │             │                  │                   │              │
 *  │   [Display Available Items]     │                   │              │
 *  │             │                  │                   │              │
 *  │──Click Buy──>│                  │                   │              │
 *  │    Item     │──buyItem(name,cost)──────────────────>│              │
 *  │             │                  │   Check score >= cost? (YES)     │
 *  │             │                  │   subtractScore()                │
 *  │             │                  │──saveInventoryItem──────────────>│
 *  │             │                  │                   │              │
 *  │             │<─Purchase Success────────────────────│              │
 *  │   [Show Success Alert]          │                   │              │
 *  │   [Update Score Display]        │                   │              │
 *  │             │                  │                   │              │
 *  │──Click Inventory──>│            │                   │              │
 *  │             │──getPlayerInventory()───────────────>│              │
 *  │             │                  │──queryInventory───────────────────>│
 *  │             │<─InventoryItems──────────────────────│              │
 *  │             │                  │                   │              │
 *  │   [Display Inventory Items]     │                   │              │
 *  │             │                  │                   │              │
 *  │──Click Equip─>│                 │                   │              │
 *  │    Skin     │──equipSkin(skinId)───────────────────>│              │
 *  │             │                  │──updatePlayerState────────────────>│
 *  │             │                  │                   │              │
 *  │             │<─Skin Equipped───────────────────────│              │
 *  │   [Update Chart Color in Real-Time]                 │              │
 *  │             │                  │                   │              │
 * 
 * ============================================================================
 * 6. COMPONENT DESCRIPTION
 * ============================================================================
 * 
 * PRESENTATION LAYER (UI Controllers):
 * ├─ MainMenuController
 * │  └─ Menangani input nama pemain dan navigasi ke mode selection
 * │     └─ KONSEP OOP: Menggunakan JavaFX FXML loader
 * │     └─ DESAIN PATTERN: MVC (Model-View-Controller)
 * │
 * ├─ ModeSelectionController
 * │  └─ Menampilkan pilihan mode (Learn, Game, Shop, Inventory)
 * │     └─ Navigasi antar berbagai scene aplikasi
 * │
 * ├─ LearnModeController
 * │  └─ Mode simulasi step-by-step
 * │     ├─ Input array (manual atau random)
 * │     ├─ Pilih algoritma (Bubble Sort / Selection Sort)
 * │     ├─ Visualisasi bar chart real-time
 * │     ├─ Tombol "Next Step" untuk progres simulasi
 * │     ├─ Live commentary penjelasan setiap step
 * │     └─ Pilih dan apply skin warna batang real-time
 * │
 * ├─ GameModeController
 * │  └─ Mode permainan manual swap
 * │     ├─ User klik 2 batang untuk menukarnya
 * │     ├─ Validasi penukaran sesuai aturan algoritma
 * │     ├─ Sistem poin & nyawa
 * │     ├─ Highlight bar yang sedang dipilih
 * │     └─ Real-time skin change dan feedback
 * │
 * ├─ ShopController
 * │  └─ Toko untuk membeli item
 * │     ├─ Display semua available items dengan harga
 * │     ├─ Kategori: Skin Warna, Ramuan Nyawa, Bocoran Langkah
 * │     ├─ Tombol beli dengan validasi skor
 * │     ├─ Item masuk ke inventory setelah dibeli
 * │     └─ Tidak bisa beli jika skor kurang
 * │
 * └─ InventoryController
 *    └─ Tampilkan inventory tas pemain
 *       ├─ Kategorisasi item (Skin, Consumable)
 *       ├─ Tombol "Equip" untuk skin (ganti warna real-time)
 *       ├─ Tombol "Gunakan" untuk consumable items
 *       └─ Remove item setelah digunakan habis
 * 
 * FACADE LAYER (Business Logic Integration):
 * └─ GameFacade (Menyatukan semua subsistem)
 *    ├─ DESIGN PATTERN: Facade Pattern
 *    │  └─ Menyembunyikan kompleksitas database, algorithm, dan gamification
 *    │     dari GUI (hanya GUI yg perlu interact dengan GameFacade)
 *    │
 *    ├─ DESIGN PATTERN: Singleton Pattern
 *    │  └─ Hanya ada 1 instance GameFacade di whole application
 *    │
 *    ├─ DESIGN PATTERN: Strategy Pattern
 *    │  └─ Method setAlgorithm() memungkinkan GUI mengubah strategi sorting
 *    │     secara dinamis saat runtime (Bubble ↔ Selection)
 *    │
 *    ├─ Player Management: setCurrentPlayer, getCurrentPlayer
 *    ├─ Algorithm Management: setAlgorithm, startSimulation, getNextStep
 *    ├─ Game Logic: validateSwap, handleCorrectSwap, handleWrongSwap
 *    ├─ Scoring: addScore, subtractScore, handleSimulationComplete
 *    ├─ Shop & Inventory: buyItem, getPlayerInventory, equipSkin, useLifePotion
 *    └─ Persistence: savePlayerProgress, saveSimulationLog, getPlayerSimulationHistory
 * 
 * SERVICE LAYER (Algorithm & Data Access):
 * │
 * ├─ SORTING ALGORITHMS (KONSEP OOP + DESIGN PATTERNS)
 * │  ├─ <<interface>> SortAlgorithm
 * │  │  └─ DESIGN PATTERN: Strategy Pattern
 * │  │     └─ Mendefinisikan kontrak untuk berbagai strategi sorting
 * │  │     └─ GUI bisa mengganti strategi saat runtime
 * │  │
 * │  ├─ BaseSort (Abstract Class)
 * │  │  └─ KONSEP OOP: Inheritance / Abstract Class
 * │  │     ├─ Shared logic: swap(), isSorted(), cloneArray()
 * │  │     ├─ Abstract method: generateSteps() (harus di-implement subclass)
 * │  │     └─ Template Method Pattern untuk struktur algoritma
 * │  │
 * │  ├─ BubbleSort (extends BaseSort implements SortAlgorithm)
 * │  │  └─ KONSEP OOP: Polymorphism / Method Override
 * │  │     ├─ Membandingkan elemen bertetangga
 * │  │     ├─ Menukar jika elemen[i] > elemen[i+1]
 * │  │     ├─ Menghasilkan List<SortStep> untuk setiap perbandingan/penukaran
 * │  │     └─ Live explanation di setiap step
 * │  │
 * │  ├─ SelectionSort (extends BaseSort implements SortAlgorithm)
 * │  │  └─ KONSEP OOP: Polymorphism / Method Override
 * │  │     ├─ Mencari elemen minimum
 * │  │     ├─ Menukar dengan posisi pertama yang belum terurut
 * │  │     ├─ Menghasilkan List<SortStep> untuk setiap pencarian/penukaran
 * │  │     └─ Live explanation di setiap step
 * │  │
 * │  └─ SortFactory (Factory Pattern)
 * │     └─ DESIGN PATTERN: Factory Pattern (Creational Pattern)
 * │        ├─ Static method createSortAlgorithm(String algorithmType)
 * │        ├─ Membuat objek algoritma berdasarkan input user
 * │        ├─ GUI tidak perlu tahu detail pembuatan objek
 * │        └─ Mudah menambah algoritma baru tanpa mengubah GUI
 * │
 * ├─ DatabaseManager (Facade untuk Hibernate)
 * │  └─ DESIGN PATTERN: Singleton + Facade
 * │     ├─ PRINSIP SOLID: Single Responsibility Principle
 * │        └─ Hanya bertanggung jawab untuk database operations
 * │     ├─ Initialize Hibernate SessionFactory dengan SQLite
 * │     ├─ CRUD Operations: save, get, delete
 * │     ├─ JPA/Hibernate integration untuk 3 entity:
 * │     │  ├─ PlayerState (pemain & skor)
 * │     │  ├─ SimulationLog (riwayat simulasi/game)
 * │     │  └─ InventoryItem (item yang dimiliki pemain)
 * │     └─ Query methods: getPlayerStateByName, getSimulationLogsForPlayer, dll
 * │
 * └─ ColorSkinManager (Gamification)
 *    ├─ Predefined skins: RED (default), GREEN, BLACK, PINK, ORANGE
 *    ├─ Setiap skin punya: skinId, name, barColor, highlightColor, cost
 *    └─ getSkinById() untuk mengambil skin properties
 * 
 * DATA LAYER (JPA Entities):
 * ├─ PlayerState @Entity
 * │  ├─ KONSEP OOP: Class & Object, Encapsulation
 * │  ├─ Database table: player_state
 * │  ├─ Fields: playerName, score, lives, level, equippedSkinId, lastPlayedTime
 * │  └─ Getter & Setter untuk setiap field (Encapsulation)
 * │
 * ├─ SimulationLog @Entity
 * │  ├─ KONSEP OOP: Class & Object, Encapsulation
 * │  ├─ Database table: simulation_log
 * │  ├─ Menyimpan riwayat setiap simulasi/game yang dimainkan
 * │  ├─ Fields: playerName, algorithmUsed, initialArray, finalArray, stepsCount, pointsEarned, gameMode, livesRemaining, timestamp
 * │  └─ Relationship 1:N dengan PlayerState
 * │
 * └─ InventoryItem @Entity
 *    ├─ KONSEP OOP: Class & Object, Encapsulation
 *    ├─ Database table: inventory_item
 *    ├─ Menyimpan item yang dimiliki pemain
 *    ├─ Fields: playerStateId, itemType (SKIN_COLOR/LIFE_POTION/HINT), itemName, itemId, quantity, costInPoints, acquiredTime
 *    └─ Relationship 1:N dengan PlayerState
 * 
 * ============================================================================
 * 7. DESIGN PATTERNS YANG DIIMPLEMENTASIKAN
 * ============================================================================
 * 
 * A. CREATIONAL PATTERNS
 *    └─ Factory Pattern (SortFactory)
 *       └─ Problem: GUI perlu membuat berbagai jenis algoritma sorting
 *       └─ Solution: SortFactory.createSortAlgorithm(String type)
 *       └─ Benefit: Centralized object creation, mudah extend dengan algoritma baru
 * 
 * B. STRUCTURAL PATTERNS
 *    └─ Facade Pattern (GameFacade, DatabaseManager)
 *       └─ Problem: Kompleksitas subsistem (Database, Algorithm, Gamification)
 *       └─ Solution: GameFacade menyatukan semua subsistem dalam satu interface
 *       └─ Benefit: GUI hanya interaksi dengan GameFacade, mudah maintain
 * 
 * C. BEHAVIORAL PATTERNS
 *    ├─ Strategy Pattern (SortAlgorithm interface + implementations)
 *    │  └─ Problem: GUI perlu support multiple sorting algorithms
 *    │  └─ Solution: Encapsulate algorithms dalam Strategy objects
 *    │  └─ Benefit: GUI bisa switch strategy dynamically at runtime
 *    │
 *    └─ Singleton Pattern (GameFacade, DatabaseManager)
 *       └─ Problem: Perlu memastikan hanya 1 instance di whole application
 *       └─ Solution: getInstance() method dengan lazy initialization
 *       └─ Benefit: Single source of truth untuk game state & database
 * 
 * ============================================================================
 * 8. SOLID PRINCIPLES YANG DITERAPKAN
 * ============================================================================
 * 
 * S - Single Responsibility Principle
 *   └─ Setiap class punya 1 tanggung jawab:
 *      ├─ DatabaseManager: hanya database operations
 *      ├─ SortAlgorithm: hanya sorting logic
 *      ├─ GameFacade: hanya game logic orchestration
 *      ├─ Controllers: hanya UI logic
 *      └─ Entities: hanya data representation
 * 
 * O - Open/Closed Principle
 *   └─ Classes terbuka untuk extension, tertutup untuk modification
 *      ├─ BaseSort: terbuka untuk extend (BubbleSort, SelectionSort)
 *      │  tapi tertutup untuk modify (algorithm logic ada di subclass)
 *      ├─ SortAlgorithm interface: terbuka untuk extend dengan algoritma baru
 *      │  tapi GUI tidak perlu berubah
 *      └─ Gamification: bisa add item baru di ColorSkinManager
 * 
 * L - Liskov Substitution Principle
 *   └─ Subclass bisa menggantikan superclass tanpa break logic
 *      ├─ BubbleSort & SelectionSort bisa substitute SortAlgorithm interface
 *      └─ GameFacade bisa call generateSteps() tanpa tahu impl details
 * 
 * I - Interface Segregation Principle
 *   └─ Clients tidak harus depend pada interfaces yang tidak mereka gunakan
 *      ├─ SortAlgorithm interface hanya mendefinisikan methods yg relevan
 *      └─ GUI hanya depend pada methods yang benar-benar digunakan
 * 
 * D - Dependency Inversion Principle
 *   └─ Depend pada abstractions, bukan concrete implementations
 *      ├─ GameFacade depend pada SortAlgorithm interface, bukan BubbleSort concrete
 *      ├─ GUI depend pada GameFacade abstraction, bukan DatabaseManager concrete
 *      └─ Menggunakan Dependency Injection via getInstance() / constructor
 * 
 * ============================================================================
 * 9. KONSEP OOP YANG DITERAPKAN
 * ============================================================================
 * 
 * 1. Class & Object (Instansiasi)
 *    └─ Semua class di aplikasi adalah instantiation of concepts
 *    └─ PlayerState ps = new PlayerState("John");
 *    └─ Contoh: GameFacade, DatabaseManager, BubbleSort, SelectionSort, dll
 * 
 * 2. Interface
 *    └─ SortAlgorithm interface mendefinisikan kontrak untuk sorting algorithms
 *    └─ public interface SortAlgorithm { ... }
 *    └─ Memungkinkan multiple implementations dengan signature yang sama
 * 
 * 3. Abstract Class
 *    └─ BaseSort adalah abstract class dengan method-method umum
 *    └─ public abstract class BaseSort implements SortAlgorithm { ... }
 *    └─ Shared logic: swap(), isSorted(), cloneArray()
 *    └─ Abstract method: generateSteps() (harus di-implement oleh subclass)
 * 
 * 4. Inheritance
 *    └─ BubbleSort extends BaseSort
 *    └─ SelectionSort extends BaseSort
 *    └─ Mewarisi shared logic dari base class
 * 
 * 5. Polymorphism
 *    └─ Method Override: generateSteps() di-override di BubbleSort & SelectionSort
 *    └─ Runtime Polymorphism: 
 *       SortAlgorithm algo = SortFactory.createSortAlgorithm("BUBBLE_SORT");
 *       List<SortStep> steps = algo.generateSteps(array);  // Call BubbleSort implementation
 * 
 * 6. Encapsulation
 *    └─ Private variables dengan public Getter & Setter
 *    └─ PlayerState: private score, public getScore() / setScore()
 *    └─ Mengontrol akses ke internal state
 * 
 * 7. Association & Relationship
 *    └─ One-to-Many: PlayerState (1) ← SimulationLog (N)
 *    └─ One-to-Many: PlayerState (1) ← InventoryItem (N)
 *    └─ DatabaseManager maintain reference ke SessionFactory
 * 
 * ============================================================================
 * 10. TECHNOLOGY STACK
 * ============================================================================
 * 
 * FRONTEND:
 * ├─ JavaFX 21 (UI Framework)
 * ├─ FXML (XML-based UI markup)
 * ├─ CSS (Styling)
 * └─ Scene Builder Compatible
 * 
 * BACKEND:
 * ├─ Java 17+ (Programming Language)
 * ├─ Gradle 8+ (Build Tool)
 * └─ SLF4J (Logging)
 * 
 * DATABASE & ORM:
 * ├─ SQLite (Database Engine)
 * ├─ Hibernate 6.3+ (ORM Framework)
 * ├─ JPA/Jakarta Persistence (ORM Specification)
 * ├─ SQLite JDBC Driver (Database Driver)
 * └─ Automatic schema creation (hbm2ddl.auto = update)
 * 
 * ============================================================================
 * 11. PROJECT STRUCTURE
 * ============================================================================
 * 
 * GameSortirJava/
 * ├── build.gradle                              (Gradle build configuration)
 * ├── src/main/java/com/gamesortir/
 * │   ├── app/
 * │   │   └── GameSortirApp.java                (Main JavaFX Application)
 * │   ├── model/
 * │   │   ├── PlayerState.java                  (JPA Entity)
 * │   │   ├── SimulationLog.java                (JPA Entity)
 * │   │   └── InventoryItem.java                (JPA Entity)
 * │   ├── algorithm/
 * │   │   ├── SortAlgorithm.java                (Interface + SortStep class)
 * │   │   ├── BaseSort.java                     (Abstract class)
 * │   │   ├── BubbleSort.java                   (Implementation)
 * │   │   ├── SelectionSort.java                (Implementation)
 * │   │   └── SortFactory.java                  (Factory Pattern)
 * │   ├── database/
 * │   │   └── DatabaseManager.java              (Facade + Hibernate)
 * │   ├── gamification/
 * │   │   ├── GameFacade.java                   (Facade + Business Logic)
 * │   │   ├── ColorSkinManager.java             (Skin Management)
 * │   │   └── BarColorSkin.java                 (Color Skin DTO)
 * │   └── ui/
 * │       ├── controller/
 * │       │   ├── MainMenuController.java       (JavaFX Controller)
 * │       │   ├── ModeSelectionController.java  (JavaFX Controller)
 * │       │   ├── LearnModeController.java      (JavaFX Controller)
 * │       │   ├── GameModeController.java       (JavaFX Controller)
 * │       │   ├── ShopController.java           (JavaFX Controller)
 * │       │   └── InventoryController.java      (JavaFX Controller)
 * │       └── view/
 * │           └── (FXML files)
 * ├── src/main/resources/
 * │   ├── fxml/
 * │   │   ├── MainMenu.fxml
 * │   │   ├── ModeSelection.fxml
 * │   │   ├── LearnMode.fxml
 * │   │   ├── GameMode.fxml
 * │   │   ├── Shop.fxml
 * │   │   └── Inventory.fxml
 * │   ├── css/
 * │   │   └── style.css
 * │   └── META-INF/
 * │       └── persistence.xml                   (JPA configuration - optional)
 * ├── gamesortir.db                             (SQLite Database - auto-created)
 * ├── module-info.java                          (Java 9+ Module info)
 * └── README.md                                 (Documentation)
 * 
 * ============================================================================
 * 12. HOW TO RUN
 * ============================================================================
 * 
 * Prerequisites:
 * ├─ Java 17 atau lebih tinggi
 * ├─ Gradle 8 atau lebih tinggi
 * └─ Git (untuk clone repository)
 * 
 * Build:
 * └─ gradle clean build
 * 
 * Run:
 * └─ gradle run
 * 
 * Create JAR:
 * └─ gradle jar
 * └─ java -jar build/libs/gamesortir-1.0-SNAPSHOT.jar
 * 
 * ============================================================================
 */
