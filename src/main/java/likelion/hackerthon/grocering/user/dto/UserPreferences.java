package likelion.hackerthon.grocering.user.dto;

public record UserPreferences(String hotFoodPreference,
                              String sweetFoodPreference,
                              String saltyFoodPreference,
                              String cookingMethodFoodPreference,
                              String maxCookingTimePreference,
                              String tryingNewFoodPreference,
                              String allergicFoods,
                              String religionBannedFoods) {
}
