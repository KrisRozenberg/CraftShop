package org.rozenberg.craftshop.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.craftshop.exception.CustomPoolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class CustomConnectionPool {
    private static final Logger logger = LogManager.getLogger();

    private static final ResourceBundle bundle;
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final int POOL_SIZE;

    private static final ReentrantLock instanceLocker = new ReentrantLock(true);
    private static final AtomicBoolean instanceIsCreated = new AtomicBoolean();
    private static CustomConnectionPool instance;
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final BlockingQueue<ProxyConnection> givenAwayConnections;

    static {
        bundle = ResourceBundle.getBundle("database");
        DRIVER = bundle.getString("driver");
        URL = bundle.getString("url");
        USERNAME = bundle.getString("username");
        PASSWORD = bundle.getString("password");
        POOL_SIZE = Integer.parseInt(bundle.getString("poolsize"));
    }

    private CustomConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>();
        try {
            Class.forName(DRIVER);
            for (int i = 0; i < POOL_SIZE; i++) {
                ProxyConnection connection = new ProxyConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
                freeConnections.offer(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.FATAL, "Failed to initialize connection pool: ", e);
            throw new ExceptionInInitializerError("Failed to initialize connection pool");
        }
    }

    public static CustomConnectionPool getInstance() {
        if (!instanceIsCreated.get()) {
            instanceLocker.lock();
            try {
                if (instance == null) {
                    instance = new CustomConnectionPool();
                    instanceIsCreated.set(true);
                }
            } finally {
                instanceLocker.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.put(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Error while taking connection: {}", e);
            Thread.currentThread().interrupt();
        }
        logger.log(Level.DEBUG, "Taken connection");
        return connection;
    }

    public void releaseConnection(Connection connection) throws CustomPoolException {
        if (connection.getClass() != ProxyConnection.class) {
            throw new CustomPoolException("Wrong type of connection");
        }
        givenAwayConnections.remove(connection);
        try {
            freeConnections.put((ProxyConnection) connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Error while releasing connection: {}", e);
            Thread.currentThread().interrupt();
        }
        logger.log(Level.DEBUG, "Released connection");
    }

    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                freeConnections.take().realClose();
                logger.log(Level.INFO,"Connection is closed");
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Destroying pool is failed: ", e);
                Thread.currentThread().interrupt();
            }
        }
        deregisterDrivers();
    }

    public void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Error while deregistration driver: {}", e);
            }
        });
    }
}
