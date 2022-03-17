package elektroGo.back.data.Gateways;

import java.sql.SQLException;

public interface Gateway {
    void insert() throws SQLException;
    void update() throws SQLException;
    void remove() throws SQLException;
    String json();
}
