package elektroGo.back.exceptions;

public class AchievementNotFound extends RuntimeException {

    public AchievementNotFound() {
        super();
    }
    public AchievementNotFound(String achievement) {
        super("The achievement '" + achievement + "' wasn't found");

    }
}
