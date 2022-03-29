/**
 * @file Gateway.java
 * @author Daniel Pulido
 * @date 13/03/2022
 * @brief Interficie dels Gateways.
 */

package elektroGo.back.data.Gateways;

import java.sql.SQLException;

/**
 * @brief La interficie Gateway implementa el gateway que fa insert/update/delete a la BD
 */
public interface Gateway {

    /**
     * @brief Insert de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un insert en la BD amb els atributs de l'objecte
     */
    void insert() throws SQLException;

    /**
     * @brief Update de l'objecte en la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post Es fa un update en la BD amb els atributs de l'objecte
     */
    void update() throws SQLException;

    /**
     * @brief Borra l'objecte de la BD
     * @pre Els atributs de l'objecte no son nuls
     * @post L'objecte es esborrat de la BD
     */
    void remove() throws SQLException;

    /**
     * @brief Passa l'objecte a JSON
     * @pre cert
     * @return Retorna l'objecte en format JSON amb un String
     */
    String json();
}
