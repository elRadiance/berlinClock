package com.ubs.opsit.interviews;

/**
 * Created by Vladimir Aseev on 08.04.2017.
 */
public class BerlinClock implements TimeConverter {

    // digit "prices" in seconds
    private static final int[] DIGITS_COST;

    private static final String[] FULL_LIGHT;

    private static final String MANY_O = "OOOOOOOOOOO";

    static{
        DIGITS_COST = new int[5];

        DIGITS_COST[0] = 1;
        DIGITS_COST[1] = 5 * 60 * 60;
        DIGITS_COST[2] = 60 * 60;
        DIGITS_COST[3] = 5 * 60;
        DIGITS_COST[4] = 60;

        FULL_LIGHT = new String[5];

        FULL_LIGHT[0] = "Y";
        FULL_LIGHT[1] = "RRRR";
        FULL_LIGHT[2] = "RRRR";
        FULL_LIGHT[3] = "YYRYYRYYRYY";
        FULL_LIGHT[4] = "YYYY";

    }

    @Override
    public String convertTime(String aTime) {

        checkArguments(aTime);

        int timeInSeconds = timeInputToSeconds(aTime);

        byte[] digits = splitSecondsToDigits(timeInSeconds);

        return rasterizeToString(digits);
    }


    /**
     * Check arguments
     * @param time input candidate
     */
    private void checkArguments(String time){
        try{
            String[] tmp =
                    time.split(":");
            assert Integer.parseInt(tmp[0]) <= 24;
            assert Integer.parseInt(tmp[1]) < 60;
            assert Integer.parseInt(tmp[2]) < 60;
            assert Integer.parseInt(tmp[0]) >= 0;
            assert Integer.parseInt(tmp[1]) >= 0;
            assert Integer.parseInt(tmp[2]) >= 0;
            if(tmp[0].equals("24")){
                assert (tmp[1].equals("00") &&
                        tmp[2].equals("00"));
            }
        } catch (Exception e){
            throw new RuntimeException("Incorrect time input: " + time);
        }

    }

    /**
     * Method creates array with lights count
     * to be lit for each clock raw
     *
     * @param seconds time representation in seconds
     * @return "how many" lights necessary to light
     *              for each raw
     *
     */
    private byte[] splitSecondsToDigits(int seconds){
        byte[] result = new byte[5];
        int rest = seconds;
        for(int currentRawNumber = 1; currentRawNumber < 5; currentRawNumber++){
            result[currentRawNumber] = (byte) (rest / DIGITS_COST[currentRawNumber]);
            rest = rest % DIGITS_COST[currentRawNumber];
        }

        result[0] = (byte) (1 >>> (1 & rest));

        return result;
    }


    /**
     * Method renders full visible representation of
     * the Berlin clock.
     * @param digits "how many" lights necessary to light
     *              for each raw
     * @return rendered clock
     */
    private String rasterizeToString(byte[] digits){
        StringBuilder builder = new StringBuilder(32);
        for(int i = 0; i < digits.length - 1; i++){
            builder.append(renderRaw(digits, i));
            builder.append(System.lineSeparator());
        }

        builder.append(renderRaw(digits, digits.length - 1));

        return builder.toString();
    }

    /**
     * Renders visible representation of selected raw
     *
     * @param digits "how many" lights necessary to light
     *              for each raw
     * @param rawNumber which raw necessary to render
     * @return rendered raw (visible representation without line separator)
     */
    private String renderRaw(byte[] digits, int rawNumber){
        String significantPart = FULL_LIGHT[rawNumber]
                .substring(0, digits[rawNumber]);

        // Let it be in String pool
        String zerosPart = String
                .valueOf(MANY_O
                        .substring(0,
                                FULL_LIGHT[rawNumber].length() -
                                        digits[rawNumber]));

        return significantPart + zerosPart;
    }

    /**
     * Converts time from String representation to
     * number of seconds
     * @param time time in format HH:MM:SS
     * @return number of seconds since 00:00:00
     */
    private int timeInputToSeconds(String time){
        String[] tmp =
                time.split(":");
        return Integer.parseInt(tmp[0]) * 3600 +
                Integer.parseInt(tmp[1]) * 60 +
                Integer.parseInt(tmp[2]);
    }

}
