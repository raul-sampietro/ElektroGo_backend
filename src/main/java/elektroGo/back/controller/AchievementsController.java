package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderAchievements;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.finders.FinderUserAchievement;
import elektroGo.back.data.gateways.GatewayUserAchievements;
import elektroGo.back.exceptions.AchievementNotFound;
import elektroGo.back.exceptions.UserNotFound;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/achievements/")
public class AchievementsController {
    private final CustomLogger logger = CustomLogger.getInstance();

    @GetMapping("{achievement}/users/{username}")
    public GatewayUserAchievements getUserAchievement(@PathVariable String achievement, @PathVariable String username) throws SQLException {
        logger.log("Starting getUserAchievement method with achievement '" + achievement + "' and user '" + username + "'...", logType.TRACE);
        FinderUserAchievement fUA = FinderUserAchievement.getInstance();
        GatewayUserAchievements gUA =  fUA.findByPK(username, achievement);
        if (gUA == null) {
            FinderUser fU = FinderUser.getInstance();
            if (fU.findByUsername(username) == null) throw new UserNotFound(username);
            FinderAchievements fA = FinderAchievements.getInstance();
            if (fA.findByName(achievement) == null) throw new AchievementNotFound(achievement);
            logger.log("User with this achievement hasn't been created, creating it...", logType.TRACE);
            gUA = new GatewayUserAchievements(username, achievement);
            gUA.insert();
            if (fUA.findByPK(username, achievement) != null) logger.log("UserAchievement successfully created", logType.TRACE);
            else logger.log("UserAchievement couldn't been created" ,logType.ERROR);
        }
        else {
            logger.log("UserAchievement exists, returning it...", logType.TRACE);

        }
        logger.log("Returning this UserAchievement: " + gUA.json(), logType.TRACE);
        return gUA;
    }

}
