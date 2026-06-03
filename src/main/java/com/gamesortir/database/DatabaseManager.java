package com.gamesortir.database;

import com.gamesortir.model.PlayerState;
import com.gamesortir.model.SimulationLog;
import com.gamesortir.model.InventoryItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DESIGN PATTERN: Facade Pattern (Structural Pattern)
 * DatabaseManager adalah facade yang menyatukan semua operasi database Hibernate.
 * 
 * KEUNTUNGAN:
 * - Menyembunyikan kompleksitas Hibernate dari GUI JavaFX
 * - GUI hanya perlu memanggil method-method sederhana di class ini
 * - Semua CRUD operations terpusat di satu tempat
 * - Mudah melakukan perubahan implementasi database tanpa mengubah GUI
 * 
 * PRINSIP SOLID: Single Responsibility Principle
 * Class ini hanya bertanggung jawab untuk mengurus database operations.
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static SessionFactory sessionFactory;
    private static DatabaseManager instance;
    
    // ===== Singleton Pattern untuk DatabaseManager =====
    private DatabaseManager() {
        initializeSessionFactory();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize Hibernate SessionFactory dengan konfigurasi SQLite
     */
    private void initializeSessionFactory() {
        try {
            Configuration config = new Configuration();
            
            // Set Hibernate properties untuk SQLite
            config.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            config.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
            config.setProperty("hibernate.connection.url", "jdbc:sqlite:gamesortir.db");
            config.setProperty("hibernate.connection.username", "");
            config.setProperty("hibernate.connection.password", "");
            config.setProperty("hibernate.hbm2ddl.auto", "update");  // Auto-create tables
            config.setProperty("hibernate.show_sql", "false");
            config.setProperty("hibernate.format_sql", "true");
            
            // Register Entity classes
            config.addAnnotatedClass(PlayerState.class);
            config.addAnnotatedClass(SimulationLog.class);
            config.addAnnotatedClass(InventoryItem.class);
            
            // Build SessionFactory
            sessionFactory = config.buildSessionFactory();
            logger.info("SessionFactory berhasil diinisialisasi");
            
        } catch (Exception e) {
            logger.error("Error inisialisasi SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * Mendapatkan Session baru dari SessionFactory
     */
    private Session getSession() {
        return sessionFactory.openSession();
    }
    
    // ===== CRUD OPERATIONS =====
    
    /**
     * Menyimpan atau mengupdate PlayerState
     */
    public void saveOrUpdatePlayerState(PlayerState playerState) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(playerState);  // Hibernate JPA operation
            transaction.commit();
            logger.info("PlayerState berhasil disimpan: " + playerState.getPlayerName());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error menyimpan PlayerState", e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Mengambil PlayerState berdasarkan ID
     */
    public PlayerState getPlayerStateById(Long id) {
        Session session = getSession();
        try {
            return session.get(PlayerState.class, id);
        } catch (Exception e) {
            logger.error("Error mengambil PlayerState dengan ID " + id, e);
            return null;
        } finally {
            session.close();
        }
    }
    
    /**
     * Mengambil PlayerState berdasarkan nama pemain
     */
    public PlayerState getPlayerStateByName(String playerName) {
        Session session = getSession();
        try {
            List<PlayerState> results = session.createQuery(
                "FROM PlayerState WHERE playerName = :name",
                PlayerState.class
            )
            .setParameter("name", playerName)
            .getResultList();
            
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            logger.error("Error mengambil PlayerState dengan nama " + playerName, e);
            return null;
        } finally {
            session.close();
        }
    }
    
    /**
     * Mengambil semua PlayerState
     */
    public List<PlayerState> getAllPlayerStates() {
        Session session = getSession();
        try {
            return session.createQuery("FROM PlayerState", PlayerState.class).getResultList();
        } catch (Exception e) {
            logger.error("Error mengambil semua PlayerState", e);
            return List.of();
        } finally {
            session.close();
        }
    }
    
    /**
     * Menghapus PlayerState
     */
    public void deletePlayerState(Long id) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            PlayerState playerState = session.get(PlayerState.class, id);
            if (playerState != null) {
                session.delete(playerState);
            }
            transaction.commit();
            logger.info("PlayerState dengan ID " + id + " berhasil dihapus");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error menghapus PlayerState", e);
        } finally {
            session.close();
        }
    }
    
    // ===== SIMULATION LOG OPERATIONS =====
    
    /**
     * Menyimpan SimulationLog
     */
    public void saveSimulationLog(SimulationLog log) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(log);
            transaction.commit();
            logger.info("SimulationLog berhasil disimpan");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error menyimpan SimulationLog", e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Mengambil semua SimulationLog untuk pemain tertentu
     */
    public List<SimulationLog> getSimulationLogsForPlayer(String playerName) {
        Session session = getSession();
        try {
            return session.createQuery(
                "FROM SimulationLog WHERE playerName = :name ORDER BY timestamp DESC",
                SimulationLog.class
            )
            .setParameter("name", playerName)
            .getResultList();
        } catch (Exception e) {
            logger.error("Error mengambil SimulationLog untuk player " + playerName, e);
            return List.of();
        } finally {
            session.close();
        }
    }
    
    // ===== INVENTORY OPERATIONS =====
    
    /**
     * Menyimpan InventoryItem
     */
    public void saveInventoryItem(InventoryItem item) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
            logger.info("InventoryItem berhasil disimpan: " + item.getItemName());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error menyimpan InventoryItem", e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Mengambil semua InventoryItem untuk pemain tertentu
     */
    public List<InventoryItem> getInventoryItemsForPlayer(Long playerStateId) {
        Session session = getSession();
        try {
            return session.createQuery(
                "FROM InventoryItem WHERE playerStateId = :playerId",
                InventoryItem.class
            )
            .setParameter("playerId", playerStateId)
            .getResultList();
        } catch (Exception e) {
            logger.error("Error mengambil InventoryItem", e);
            return List.of();
        } finally {
            session.close();
        }
    }
    
    /**
     * Menghapus InventoryItem
     */
    public void deleteInventoryItem(Long id) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            InventoryItem item = session.get(InventoryItem.class, id);
            if (item != null) {
                session.delete(item);
            }
            transaction.commit();
            logger.info("InventoryItem dengan ID " + id + " berhasil dihapus");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error menghapus InventoryItem", e);
        } finally {
            session.close();
        }
    }
    
    /**
     * Close SessionFactory (dipanggil saat aplikasi ditutup)
     */
    public void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory ditutup");
        }
    }
}
