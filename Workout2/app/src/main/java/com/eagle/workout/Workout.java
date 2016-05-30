package com.eagle.workout;

/**
 * Created by 1 on 18.01.2016.
 */
public class Workout {

    private String mName;
    private String mDescription;

    public static final Workout[] workouts = {
        new Workout("The Pushup",
              "100 Pull-ups\n" +
              "100 Push-ups\n" +
              "100 Sit-ups\n" +
              "100 Squats"),
        new Workout("The Bodyweight",
                "100 Pull-ups\n" +
                "100 Push-ups\n" +
                "100 Sit-ups\n" +
                "100 Squats"),
        new Workout("The Lunge",
                "100 Pull-ups\n" +
                "100 Push-ups\n" +
                "100 Sit-ups\n" +
                "100 Squats")
    };

    public Workout(String name, String description) {
        mName = name;
        mDescription = description;
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return this.mName;
    }
}
