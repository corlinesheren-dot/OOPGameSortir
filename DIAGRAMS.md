# 📊 DIAGRAMS UNTUK LAPORAN

## CLASS DIAGRAM - Sorting Algorithm Hierarchy

```
┌──────────────────────────────────────────────┐
│         <<interface>>                        │
│         SortAlgorithm                        │
├──────────────────────────────────────────────┤
│ + generateSteps(int[]): List<SortStep>       │
│ + getAlgorithmName(): String                 │
│ + getDescription(): String                   │
└──────────────────────────────────────────────┘
           △                △
           │                │
    ┌──────┴──────┐  ┌──────┴──────┐
    │ BaseSort    │  │ SortFactory  │
    │ (abstract)  │  │ (Factory)    │
    └──────┬──────┘  └──────────────┘
           △
    ┌──────┴──────┐
    │             │
┌───┴──────┐  ┌──┴────────┐
│BubbleSort│  │SelectionSort│
├──────────┤  ├────────────┤
│-steps[]  │  │-steps[]    │
└──────────┘  └────────────┘

KEY:
- SortAlgorithm: Interface (Strategy Pattern)
- BaseSort: Abstract Class (Inheritance)
- BubbleSort, SelectionSort: Concrete Classes (Polymorphism)
- SortFactory: Factory untuk membuat objek algoritma
```

## CLASS DIAGRAM - Facade Pattern Architecture

```
┌─────────────────────────────────────────────────┐
│              GameFacade                         │
│         (Facade Pattern + Singleton)            │
├─────────────────────────────────────────────────┤
│ - databaseManager: DatabaseManager              │
│ - currentPlayer: PlayerState                    │
│ - currentAlgorithm: SortAlgorithm               │
│ - currentSteps: List<SortStep>                  │
├─────────────────────────────────────────────────┤
│ + getInstance(): GameFacade                     │
│ + setCurrentPlayer(String): void                │
│ + setAlgorithm(String): void (Strategy!)        │
│ + startSimulation(int[]): void                  │
│ + validateSwap(int,int,int[]): boolean          │
│ + buyItem(...): boolean                         │
│ + getPlayerInventory(): List<InventoryItem>     │
│ + savePlayerProgress(): void                    │
└──────────┬──────────────────────────────────────┘
           │
           ├──────────────────┬──────────────────┐
           │                  │                  │
      ┌────▼──────┐  ┌───────▼────┐  ┌──────────▼────────┐
      │DatabaseMgr│  │SortAlgorithm│  │ColorSkinManager  │
      │(Singleton)│  │(Interface)  │  │                  │
      └───────────┘  └─────────────┘  └──────────────────┘
```

## SEQUENCE DIAGRAM - Simulasi Sorting

```
User          UI              GameFacade          Algorithm        DB
 │             │                   │                   │            │
 ├─Input────────>│                   │                   │            │
 │ Data         │                   │                   │            │
 │             │──setCurrentPlayer──>│                   │            │
 │             │                   ├──────Check Player──────────────>│
 │             │                   <──Player/Create─────────────────┤
 │             │<──PlayerSet────────│                   │            │
 │             │                   │                   │            │
 ├─Pilih Algo──>│                   │                   │            │
 │             │──setAlgorithm────>│──Create via        │            │
 │             │                   │  Factory Pattern   │            │
 │             │                   │<──SortAlgorithm───│            │
 │             │<──AlgoSet─────────│                   │            │
 │             │                   │                   │            │
 ├─Input Array─>│                   │                   │            │
 ├─Click Start─>│                   │                   │            │
 │             │──startSimulation──>│──generateSteps──>│            │
 │             │                   │<──List<SortStep>─│            │
 │             │<──StepsData───────│                   │            │
 │             │                   │                   │            │
 │ [Draw Chart]                    │                   │            │
 │             │                   │                   │            │
 ├─Click Next──>│                   │                   │            │
 │ Step        │──getNextStep─────>│                   │            │
 │             │<──SortStep Data───│                   │            │
 │             │                   │                   │            │
 │ [Update UI] │                   │                   │            │
 │ [Highlight] │                   │                   │            │
 │             │                   │                   │            │
 └─... repeat for all steps                            │            │
```

## SEQUENCE DIAGRAM - Game Mode (Manual Swap)

```
User         UI              GameFacade          DB
 │            │                   │               │
 ├─Click Bar1─>│                   │               │
 │            │ [Highlight Bar1]  │               │
 │            │                   │               │
 ├─Click Bar2─>│                   │               │
 │            │──validateSwap(1,2)─>               │
 │            │                   │               │
 │            │   [Check rules:   │               │
 │            │    - Neighbor?    │               │
 │            │    - Expected?]   │               │
 │            │                   │               │
 │            │<──true/false──────│               │
 │            │                   │               │
 │   IF VALID: │                  │               │
 │            │──handleCorrectSwap─>              │
 │            │                   ├──addScore────>│
 │            │                   ├──updatePlayer>│
 │            │<──Success─────────│               │
 │            │ [+10 poin]        │               │
 │            │                   │               │
 │   IF INVALID:                   │               │
 │            │──handleWrongSwap──>               │
 │            │                   ├──subtractLife >│
 │            │                   ├──subtractScore>│
 │            │<──Failure─────────│               │
 │            │ [-1 life, -5 poin]│               │
 │            │                   │               │
 └─... repeat until sorted                        │
```

## SEQUENCE DIAGRAM - Shop & Inventory

```
User          UI              GameFacade          DB
 │             │                   │               │
 ├─Click Shop─>│                   │               │
 │            │──getPlayerInventory──>             │
 │            │                   ├──Query Inv.──>│
 │            │<──InventoryList───────────────────┤
 │            │                   │               │
 │ [Display   │                   │               │
 │  Available]│                   │               │
 │            │                   │               │
 ├─Click Buy──>│                   │               │
 │    Item    │──buyItem(name,cost)─>             │
 │            │                   │ Check score?  │
 │            │                   ├──Save Item───>│
 │            │                   ├──Update Score>│
 │            │<──Purchase Success │               │
 │            │ [Item Added]      │               │
 │            │                   │               │
 ├─Click Inv──>│                   │               │
 │            │──getPlayerInventory─>             │
 │            │                   ├──Query Inv.──>│
 │            │<──InventoryList───────────────────┤
 │            │                   │               │
 ├─Click      │                   │               │
 │ Equip Skin─>│──equipSkin(skinId)─>             │
 │            │                   ├──Update Skin─>│
 │            │<──SkinEquipped────│               │
 │            │ [Real-time        │               │
 │            │  Color Change]    │               │
 │            │                   │               │
 └─... continue using items                       │
```

## ENTITY-RELATIONSHIP DIAGRAM (ERD)

```
┌──────────────────┐
│  PlayerState     │
├──────────────────┤
│ id (PK)          │
│ playerName       │
│ score            │
│ lives            │
│ level            │
│ equippedSkinId   │
│ lastPlayedTime   │
└────┬──────────┬──┘
     │          │
     │1        1│
     │N        N│
     │          │
┌────▼──────┐┌──▼──────────────┐
│  Simul.   ││  InventoryItem  │
│ LogTable  ││                 │
├───────────┤├─────────────────┤
│ id (PK)   ││ id (PK)         │
│ player_id ││ player_id (FK)  │
│ algorithm ││ itemType        │
│ initialArr││ itemName        │
│ finalArray││ itemId          │
│ steps     ││ quantity        │
│ points    ││ costInPoints    │
│ gameMode  ││ acquiredTime    │
│ lives     ││                 │
│ timestamp ││                 │
└───────────┘└─────────────────┘

KEY:
- PK: Primary Key
- FK: Foreign Key
- 1:N: One to Many relationship
- Each PlayerState can have many SimulationLogs
- Each PlayerState can have many InventoryItems
```

## COMPONENT DIAGRAM

```
┌─────────────────────────────────────────────┐
│           PRESENTATION LAYER                │
│  ┌───────────────────────────────────────┐  │
│  │        JavaFX UI Components           │  │
│  │ ├─ MainMenu.fxml                      │  │
│  │ ├─ ModeSelection.fxml                 │  │
│  │ ├─ LearnMode.fxml                     │  │
│  │ ├─ GameMode.fxml                      │  │
│  │ ├─ Shop.fxml                          │  │
│  │ └─ Inventory.fxml                     │  │
│  └────────────────┬──────────────────────┘  │
└───────────────────┼──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│           BUSINESS LOGIC LAYER               │
│  ┌──────────────────────────────────────┐   │
│  │  GameFacade (Facade Pattern)         │   │
│  │  - Orchestrate subsystems            │   │
│  │  - Manage game state                 │   │
│  │  - Handle user interactions          │   │
│  └────┬──────────────┬──────────────┬───┘   │
└───────┼──────────────┼──────────────┼────────┘
        │              │              │
    ┌───▼────┐    ┌───▼─────┐   ┌──▼──────┐
    │Database │    │Algorithm │   │Gamif.   │
    │Manager  │    │Service   │   │Manager  │
    └─────────┘    └──────────┘   └─────────┘
        │              │              │
    ┌───▼──────────────▼──────────────▼──┐
    │  DATA ACCESS & SERVICE LAYER       │
    │  ├─ JPA/Hibernate ORM              │
    │  ├─ SortAlgorithm Interface        │
    │  ├─ BubbleSort Implementation      │
    │  ├─ SelectionSort Implementation   │
    │  ├─ SortFactory                    │
    │  ├─ ColorSkinManager               │
    │  └─ Entity Classes                 │
    └───┬──────────────────────────────┬─┘
        │                              │
    ┌───▼──────────────────────────────▼──┐
    │    PERSISTENCE LAYER (SQLite DB)    │
    │    ├─ player_state table            │
    │    ├─ simulation_log table          │
    │    └─ inventory_item table          │
    └────────────────────────────────────┘
```

## STATE DIAGRAM - Game State Machine

```
                    ┌─────────────┐
                    │  Main Menu  │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │Mode Selection│
                    └──┬───────┬───┘
                       │       │
            ┌──────────┘       └──────────┐
            │                             │
      ┌─────▼────┐                 ┌────▼────┐
      │ Learn    │                 │  Game   │
      │ Mode     │                 │  Mode   │
      └─────┬────┘                 └────┬────┘
            │                           │
      ┌─────▼──────────────────────────▼─┐
      │  Shop / Inventory (accessible   │
      │  from any mode)                 │
      └─────┬──────────────────────────┬─┘
            │                          │
      ┌─────▼──────────────────────────▼─┐
      │   Return to Mode Selection       │
      │   or Continue Playing            │
      └────────────────────────────────┘

TRANSITIONS:
- Main Menu → Mode Selection: Input player name
- Mode Selection → Learn/Game: Click mode button
- Learn/Game ↔ Shop/Inventory: Accessible menus
- Learn/Game → Mode Selection: Click back button
- Mode Selection → Main Menu: Logout / Exit
```

## ALGORITHM COMPARISON - Bubble vs Selection

```
BUBBLE SORT STEPS:
┌─────────────────────────────────────────────────────┐
│ Array: [5, 3, 8, 1, 9]                              │
│                                                     │
│ Step 1: Compare 5 vs 3 → Swap → [3, 5, 8, 1, 9]   │
│ Step 2: Compare 5 vs 8 → No Swap → [3, 5, 8, 1, 9]│
│ Step 3: Compare 8 vs 1 → Swap → [3, 5, 1, 8, 9]   │
│ Step 4: Compare 8 vs 9 → No Swap → [3, 5, 1, 8, 9]│
│         (Pass 1 complete, 9 sudah di posisi)      │
│                                                     │
│ [Continue for remaining passes...]                  │
│                                                     │
│ Total steps: ~10-12 (tergantung array size)        │
└─────────────────────────────────────────────────────┘

SELECTION SORT STEPS:
┌─────────────────────────────────────────────────────┐
│ Array: [5, 3, 8, 1, 9]                              │
│                                                     │
│ Find min(0-4)=1 at idx 3 → Swap idx 0 → [1, 3, 8, 5, 9]
│ Find min(1-4)=3 at idx 1 → No Swap → [1, 3, 8, 5, 9]
│ Find min(2-4)=5 at idx 3 → Swap idx 2 → [1, 3, 5, 8, 9]
│ Find min(3-4)=8 at idx 3 → No Swap → [1, 3, 5, 8, 9]
│                                                     │
│ Total steps: ~7-8 (fewer swaps than Bubble)       │
└─────────────────────────────────────────────────────┘
```

## GAMIFICATION SYSTEM - Points & Rewards

```
PLAYER PROGRESSION:
┌──────────────────────────────────────────────────┐
│ Start: Level 1, 0 Points, 3 Lives                │
└──────────────────┬───────────────────────────────┘
                   │
         ┌─────────┴──────────┐
         │                    │
    ┌────▼─────┐         ┌───▼────┐
    │ Learn Mode│        │Game Mode│
    └────┬─────┘         └────┬────┘
         │                    │
    ┌────▼─────────────────────▼────┐
    │  Each Correct Swap: +10 pts   │
    │  Each Wrong Swap: -5 pts      │
    │  Complete Simulasi: +50 pts   │
    │  Complete Game: +100 pts      │
    └────┬──────────────────────┬───┘
         │                      │
    ┌────▼─────────────────────▼────┐
    │ Score Milestones:             │
    │ - 100 pts → Level Up!         │
    │ - 200 pts → Level Up!         │
    │ - ...                         │
    └────┬──────────────────────────┘
         │
    ┌────▼──────────────────────────┐
    │ SHOP - Beli dengan poin:      │
    │ - Skin Warna: 150-200 pts     │
    │ - Ramuan Nyawa: 100 pts       │
    │ - Bocoran: 150 pts            │
    └───────────────────────────────┘

LIVES SYSTEM:
    Start with 3 Lives
         │
         ├─ Each Wrong Move: -1 Life
         ├─ Buy Life Potion: +1 Life
         │
    0 Lives → Game Over
    
INVENTORY:
    Buy Item → Goes to Inventory
         │
         ├─ Skin: Click Equip → Real-time Color Change
         │
         ├─ Life Potion: Click Use → +1 Life
         │
         └─ Hint: Click Use → Show Next Step Info
```

---

## 📝 CARA MENGGUNAKAN DIAGRAM INI UNTUK LAPORAN

1. **Class Diagram**: Jelaskan relationship antara class, interface, dan inheritance
2. **Sequence Diagram**: Tunjukkan alur komunikasi antar komponen
3. **ERD**: Jelaskan struktur database dan relationships
4. **Component Diagram**: Tunjukkan layer arsitektur aplikasi
5. **State Diagram**: Jelaskan flow/state dari user perspective
6. **Algorithm Comparison**: Bandingkan efisiensi kedua algoritma

Gunakan tools seperti:
- Draw.io (https://draw.io) - untuk membuat diagram
- Lucidchart
- Visual Studio Code dengan Mermaid extension
- Atau simple ASCII art seperti di atas

---

**Semua diagram sudah menggunakan konsep OOP dan design patterns yang diterapkan!**
