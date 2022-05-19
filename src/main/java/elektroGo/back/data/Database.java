/**
 * @file Database.java
 * @author Daniel Pulido
 * @date 11/03/2022
 * @brief Implementacio de la classe Database
 */

package elektroGo.back.data;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;

import java.sql.*;

/**
 * @brief La classe Database estableix connexio amb la BD, executa sentencies SQL donades i retorna l'objecte connection
 */
public class Database {
    /**
     * @brief Objecte Database per aplicar el patro singleton
     */
    private static Database singletonObject;
    /**
     * @brief Objecte de connexio amb la BD
     */
    private Connection conn;

    /**
     * @brief Constructora de Database
     * @post Crea un objecte Database i estableix connexio amb la BD
     */
    private Database() {
        CustomLogger logger = CustomLogger.getInstance();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.4.41.58/elektrogo",
                    "test", "test");
            boolean valid = conn.isValid(50000);
            if (valid) logger.log("Connection established" , logType.TRACE);
            else logger.log("Connection established", logType.ERROR);
        } catch (Exception ex) {
            logger.log("Error: " + ex, logType.ERROR);
        }
    }
    /**
     * @brief Dona la instancia de Database
     * @return Retorna la unica instancia de Database
     */
    public static Database getInstance() {
        if (singletonObject == null) {
            singletonObject = new Database();
        }
        return singletonObject;
    }

    /**
     * @brief Executa una sentencia SQL amb Statement
     * @param sql String amb la sentencia SQL
     * @post Executa la sentencia SQL continguda en l'string "sql" com a update
     */
    public void executeSQLUpdate(String sql) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate(sql);

    }

    /**
     * @brief Executa una sentencia SQL amb Statement
     * @param sql String amb la sentencia SQL
     * @post Executa la sentencia SQL continguda en l'string "sql" com a query
     */
    public ResultSet executeSQLQuery(String sql) throws SQLException {
        Statement s = conn.createStatement();
        return s.executeQuery(sql);
    }

    public Connection getConnection() {
        return conn;
    }


}