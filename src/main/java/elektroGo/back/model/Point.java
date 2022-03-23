/**
 * @file Point.java
 * @author Adria Abad
 * @date 22/03/2022
 * @brief Implementacio de la classe Point.
 */

package elektroGo.back.model;

/**
 * @brief La classe Point representa un punt en el mapa identificat per les seves coordenades i el seu nom
 */
public class Point {
    private String name;
    private Double latitude;
    private Double longitude;

    /**
     * @brief Creadora de la classe Point
     * @param latitude Valor de la coordenada de latitud
     * @param longitude Valor de la coordenada de longitud
     * @return Retorna la instancia de la classe Point que s'acaba de crear
     */
    public Point(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @brief Creadora de la classe Point
     * @param name Nom del punt
     * @param latitude Valor de la coordenada de latitud
     * @param longitude Valor de la coordenada de longitud
     * @return Retorna la instancia de la classe Point que s'acaba de crear
     */
    public Point(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @brief Funcio per obtenir el nom del punt
     * @return Retrona el nom del punt
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Funcio per modificar el nom del punt
     * @param name Nom del punt
     * @post Es modifica el nom del paramtre implicit pel valor del parametre name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Funcio per obtenir el valor de la latitud
     * @return Retorna el valor de la latitud
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @brief Funcio per modificar el valor de la latitud
     * @param latitude Valor de la coordenada de latitud
     * @post Es modifica el valor de la latitud del paramtre implicit pel valor del parametre latitud
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @brief Funcio per obtenir el valor de la longitud
     * @return Retorna el valor de la longitud
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @brief Funcio per modificar el valor de la longitud
     * @param longitude Valor de la coordenada de longitud
     * @post Es modifica el valor de la longitud del paramtre implicit pel valor del parametre longitud
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
