package me.cerdax.cerkour.utils;

public class PointsUtil {

    public static int getPointsForDifficulty(int difficulty) {
        switch(difficulty) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 6;
            case 5:
                return 9;
            case 6:
                return 12;
            case 7:
                return 15;
            case 8:
                return 19;
            case 9:
                return 24;
            case 10:
                return 30;
            case 11:
                return 40;
            case 12:
                return 100;
            default:
                return 0;
        }
    }
}
