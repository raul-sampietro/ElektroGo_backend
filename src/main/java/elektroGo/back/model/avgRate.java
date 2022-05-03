package elektroGo.back.model;

public class avgRate {
    Double ratingValue  ;
    Integer numberOfRatings ;

    public avgRate() {
    }

    public avgRate(Double ratingValue, Integer numberOfRatings) {
        this.ratingValue = ratingValue;
        this.numberOfRatings = numberOfRatings;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }


}
