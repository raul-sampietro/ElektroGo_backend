package elektroGo.back;

import elektroGo.back.data.finders.FinderAchievements;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderUserAchievement;
import elektroGo.back.data.gateways.GatewayAchieviements;
import elektroGo.back.data.gateways.GatewayUser;
import elektroGo.back.data.gateways.GatewayUserAchievements;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserAchievementsTest {
    private FinderAchievements fA;
    private GatewayAchieviements gA1;
    private GatewayAchieviements gA2;
    private FinderUser fU;
    private GatewayUser gU;

    private FinderUserAchievement fUA;
    private GatewayUserAchievements gUA;
    private GatewayUserAchievements gUA2;

    @BeforeEach
    public void initialize() throws SQLException {
        fA = FinderAchievements.getInstance();
        fUA = FinderUserAchievement.getInstance();
        fU = FinderUser.getInstance();
        gA1 = insertTestAchivement();
        gA2 = insertTestAchivement2();
        gU = insertTestUser();
        gUA = insertTestUserAchievement1();
        gUA2 = insertTestUserAchievement2();

    }

    private GatewayUserAchievements insertTestUserAchievement2() throws SQLException {
        GatewayUserAchievements gUAAux = new GatewayUserAchievements(gU.getUsername(), gA1.getName());
        gUAAux.insert();
        return gUAAux;
    }

    private GatewayUserAchievements insertTestUserAchievement1() throws SQLException {
        GatewayUserAchievements gUAAux = new GatewayUserAchievements(gU.getUsername(), gA2.getName());
        gUAAux.insert();
        return gUAAux;
    }


    private GatewayUser insertTestUser() throws SQLException {
        gU = new GatewayUser("0","p","UserTestClass","t@gmail.com","T","T", "f", "/t");
        gU.insert();
        return gU;
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
    public void removeAll() throws SQLException {
        if (gA1 != null) gA1.remove();
        if (gA2 != null) gA2.remove();
        if (gU != null) gU.remove();
        if (gUA != null) gUA.remove();
        if (gUA2 != null) gUA2.remove();
    }

    @Test
    public void create() throws SQLException {
        GatewayUserAchievements gUATest = fUA.findByPK(gUA.getUsername(), gUA.getAchievement());
        assertEquals(gUATest.json(), gUA.json());
    }

    @Test
    public void update() throws SQLException {
        gUA.setPoints(2);
        gUA.update();
        GatewayUserAchievements gUA1Test = fUA.findByPK(gUA.getUsername(), gUA.getAchievement());
        GatewayUserAchievements gUA2Test = fUA.findByPK(gUA2.getUsername(), gUA2.getAchievement());
        assertEquals(gUA.json(), gUA1Test.json());
        assertEquals(gUA2.json(), gUA2Test.json());
    }

    //Remove is tested in removeAll

    @Test
    public void testPrimaryKey() {
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> {
            GatewayUserAchievements gUATest = new GatewayUserAchievements(gUA.getUsername(), gUA2.getAchievement());
            gUATest.insert();
        });
    }
}
