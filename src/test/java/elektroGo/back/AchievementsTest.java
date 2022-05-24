package elektroGo.back;

import elektroGo.back.data.finders.FinderAchievements;
import elektroGo.back.data.gateways.GatewayAchieviements;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class AchievementsTest {
    FinderAchievements fA;
    GatewayAchieviements gA1;
    GatewayAchieviements gA2;

    @BeforeEach
    public void initialize() throws SQLException {
        fA = FinderAchievements.getInstance();
        gA1 = insertTestAchivement();
        gA2 = insertTestAchivement2();
    }

    public GatewayAchieviements insertTestAchivement() throws SQLException {
        GatewayAchieviements gA = new GatewayAchieviements("achievementUnitTest1","example1");
        gA.insert();
        return gA;
    }

    public GatewayAchieviements insertTestAchivement2() throws SQLException {
        GatewayAchieviements gA = new GatewayAchieviements("achievementUnitTest2","example2");
        gA.insert();
        return gA;
    }

    @AfterEach
    public void removeAchivements() throws SQLException {
        if (gA1 != null) gA1.remove();
        if (gA2 != null) gA2.remove();
    }

    @Test
    public void create() throws SQLException {
        GatewayAchieviements gATest = fA.findByName(gA1.getName());
        assertEquals(gATest.json(), gA1.json());
    }

    @Test
    public void update() throws SQLException {
        gA1.setDescription("example of update1");
        gA1.update();
        GatewayAchieviements gA1Test = fA.findByName(gA1.getName());
        GatewayAchieviements gA2Test = fA.findByName(gA2.getName());
        assertEquals(gA1.json(), gA1Test.json());
        assertEquals(gA2.json(), gA2Test.json());
    }

    @Test
    public void remove() throws SQLException {
        gA1.remove();
        GatewayAchieviements gA1Test = fA.findByName(gA1.getName());
        assertNull(gA1Test);
        gA1 = null;
    }

    @Test
    public void testPrimaryKey() {
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> {
            GatewayAchieviements gA = new GatewayAchieviements(gA1.getName(), "example description testing PK");
            gA.insert();
        });
    }


}
