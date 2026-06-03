# 🎮 GameSortir - Simulasi Interaktif & Game Pengurutan Struktur Data

Aplikasi desktop berbasis Java dengan JavaFX untuk simulasi dan permainan algoritma sorting (Bubble Sort & Selection Sort) dengan gamifikasi lengkap.

## ✨ Fitur Utama

### 1. **Mode Belajar (Simulasi)**
- Input array angka manual atau generate random
- Visualisasi grafik batang interaktif yang bergerak real-time
- Tombol "Langkah Berikutnya" untuk menjalankan algoritma step-by-step
- Live commentary penjelasan logika setiap langkah
- Pilih dan ganti skin warna batang secara real-time

### 2. **Mode Permainan**
- Level tantangan: user harus menukar batang secara manual
- Klik 2 batang untuk menukarnya sesuai aturan algoritma
- Validasi penukaran:
  - Bubble Sort: hanya boleh tukar elemen bertetangga
  - Selection Sort: sesuai logika algoritma
- Sistem poin & nyawa:
  - Penukaran benar: +10 poin
  - Penukaran salah: -1 nyawa, -5 poin
  - Simulasi selesai: +100 poin bonus

### 3. **Toko Hadiah**
- Beli skin warna batang: Hijau, Hitam, Pink, Oranye (150-200 poin)
- Beli ramuan nyawa (100 poin)
- Beli bocoran langkah (150 poin)
- Item masuk ke tas inventory setelah dibeli
- Validasi skor sebelum pembelian

### 4. **Inventory Tas**
- Lihat semua item yang dimiliki
- Tombol "Equip" untuk skin (ubah warna batang real-time)
- Tombol "Gunakan" untuk consumable items
- Kategori item yang terorganisir

## 🏗️ Arsitektur & Design Patterns

### **Layered Architecture**
```
┌─────────────────────────────────────────┐
│  PRESENTATION LAYER (JavaFX UI)         │
├─────────────────────────────────────────┤
│  FACADE LAYER (Business Logic)          │
│  - GameFacade                           │
├─────────────────────────────────────────┤
│  SERVICE LAYER                          │
│  - Algorithm, Database, Gamification    │
├─────────────────────────────────────────┤
│  DATA LAYER (JPA Entities)              │
│  - PlayerState, SimulationLog           │
├─────────────────────────────────────────┤
│  DATABASE LAYER (SQLite + Hibernate)    │
└─────────────────────────────────────────┘
```

### **Design Patterns Implementasi (3 Pattern Wajib)**

1. **Factory Pattern** (Creational)
   - `SortFactory.createSortAlgorithm(String type)`
   - Membuat objek algoritma berdasarkan input user
   - File: `algorithm/SortFactory.java`

2. **Facade Pattern** (Structural)
   - `GameFacade` menyatukan Database, Algorithm, Gamification
   - GUI hanya interaksi dengan GameFacade
   - File: `gamification/GameFacade.java`

3. **Strategy Pattern** (Behavioral)
   - `SortAlgorithm` interface dengan multiple implementations
   - GUI bisa switch strategi (Bubble ↔ Selection) saat runtime
   - File: `algorithm/SortAlgorithm.java`, `BubbleSort.java`, `SelectionSort.java`

### **SOLID Principles**

- **Single Responsibility**: Setiap class punya 1 tanggung jawab
  - DatabaseManager: hanya database ops
  - SortAlgorithm: hanya sorting logic
  - GameFacade: hanya game orchestration
  
- **Open/Closed**: Terbuka untuk extension, tertutup untuk modification
  - BaseSort terbuka untuk extend dengan algoritma baru
  - SortAlgorithm interface tidak perlu berubah

- **Dependency Inversion**: Depend pada abstractions, bukan concrete
  - GameFacade depend pada SortAlgorithm interface
  - GUI depend pada GameFacade abstraction

## 💻 Teknologi

| Komponen | Teknologi |
|----------|-----------|
| Frontend | JavaFX 21, FXML, CSS |
| Backend | Java 17+ |
| Database | SQLite + Hibernate ORM + JPA |
| Build Tool | Gradle 8+ |
| Logging | SLF4J |

## 📦 Project Structure

```
GameSortirJava/
├── build.gradle                          # Gradle configuration
├── src/main/java/com/gamesortir/
│   ├── app/
│   │   └── GameSortirApp.java
│   ├── model/                            # JPA Entities
│   │   ├── PlayerState.java
│   │   ├── SimulationLog.java
│   │   └── InventoryItem.java
│   ├── algorithm/                        # Sorting Algorithms
│   │   ├── SortAlgorithm.java           # Interface + Strategy
│   │   ├── BaseSort.java                # Abstract class
│   │   ├── BubbleSort.java
│   │   ├── SelectionSort.java
│   │   └── SortFactory.java             # Factory Pattern
│   ├── database/
│   │   └── DatabaseManager.java         # Hibernate Facade
│   ├── gamification/
│   │   ├── GameFacade.java              # Main Facade
│   │   ├── ColorSkinManager.java
│   │   └── BarColorSkin.java
│   └── ui/controller/
│       ├── MainMenuController.java
│       ├── ModeSelectionController.java
│       ├── LearnModeController.java
│       ├── GameModeController.java
│       ├── ShopController.java
│       └── InventoryController.java
├── src/main/resources/
│   ├── fxml/                             # FXML UI files
│   │   ├── MainMenu.fxml
│   │   ├── ModeSelection.fxml
│   │   ├── LearnMode.fxml
│   │   ├── GameMode.fxml
│   │   ├── Shop.fxml
│   │   └── Inventory.fxml
│   └── css/
│       └── style.css
├── module-info.java                      # Java modules
├── gamesortir.db                         # SQLite database (auto-created)
└── ARCHITECTURE.md                       # Dokumentasi
```

## 🚀 Cara Menjalankan

### Prerequisites
- Java 17 atau lebih tinggi
- Gradle 8 atau lebih tinggi

### Build & Run
```bash
# Build project
gradle clean build

# Run aplikasi
gradle run

# Atau create JAR
gradle jar
java -jar build/libs/gamesortir-1.0-SNAPSHOT.jar
```

## 📊 OOP Concepts Implemented

| Konsep | Lokasi | Deskripsi |
|--------|--------|-----------|
| Class & Object | Semua file Java | Instansiasi objek dari berbagai class |
| Interface | `algorithm/SortAlgorithm.java` | Mendefinisikan kontrak sorting algorithm |
| Abstract Class | `algorithm/BaseSort.java` | Base class dengan shared logic |
| Inheritance | `BubbleSort`, `SelectionSort` | extends BaseSort |
| Polymorphism | Method override `generateSteps()` | Berbeda implementasi per algoritma |
| Encapsulation | Semua Entity classes | Private fields + public getter/setter |

## 📈 Database Schema

### PlayerState
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
```

### SimulationLog
```sql
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
```

### InventoryItem
```sql
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

## 🎓 Kriteria Penilaian (Semua Tercakup)

✅ Bahasa Java + JavaFX GUI
✅ Gradle Build Tool
✅ SQLite Database + Hibernate ORM + JPA
✅ OOP Concepts: Class, Interface, Inheritance, Polymorphism, Encapsulation
✅ SOLID Principles (Single Responsibility, Open/Closed, etc)
✅ 3 Design Patterns: Factory, Facade, Strategy
✅ Mode Belajar dengan visualisasi step-by-step
✅ Mode Permainan dengan manual swap & validasi
✅ Toko Hadiah & Inventory dengan sistem poin
✅ Real-time skin change untuk warna batang
✅ Bubble Sort & Selection Sort implementation
✅ Live commentary penjelasan langkah
✅ Sistem nyawa, level, poin

## 📝 Catatan Developer

- Semua komentar kode menjelaskan implementasi kriteria dosen
- Setiap class dilengkapi dokumentasi JavaDoc
- Struktur project mengikuti Java convention
- Database auto-create table saat pertama kali run
- Logging menggunakan SLF4J untuk debug

## 👨‍💻 Author

Senior Java Developer - Tugas Akhir Struktur Data
