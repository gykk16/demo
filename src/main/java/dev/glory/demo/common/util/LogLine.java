package dev.glory.demo.common.util;

public class LogLine {

    public static final  String LOG_LINE             = "# =================================================================================================";
    private static final String SUB_LINE             = "# ==================== ";
    private static final String START                = "# ============================== ";
    private static final String END                  = " ==============================";
    private static final int    TITLE_MAX_LEN        = 35;
    private static final String TITLE_FORMAT_PATTERN = START + "%-" + TITLE_MAX_LEN + "s" + END;

    private LogLine() {
    }

    /**
     * log title
     *
     * @param value title
     */
    public static String logTitle(String value) {
        return String.format(TITLE_FORMAT_PATTERN,
                value.length() <= TITLE_MAX_LEN ? value : value.substring(0, TITLE_MAX_LEN));
    }

    /**
     * log subtitle
     *
     * @param value subtitle
     */
    public static String logSubTitle(String value) {
        return SUB_LINE + value;
    }

}
