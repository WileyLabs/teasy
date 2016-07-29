package com.wiley.autotest.utils;

/**
 * @author ptsarev
 */
public final class ScoreFormatUtils {

    private ScoreFormatUtils() {
    }

    public static String getFormattedGradebookScoreForInstructor(double score) {
        String scoreString = String.valueOf(score);
        try {
            if(scoreString.split("\\.")[1].length() < 2) {
                scoreString += "0";
            }
            return scoreString;
        } catch (IndexOutOfBoundsException ex) {
            return scoreString + ".00";
        }
    }
}
