package dev.glory.demo.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringUtil {

    private static final String EUC_KR = "EUC-KR";

    private StringUtil() {
    }

    /**
     * 문자열을 maxLength 만큼 자른다
     *
     * @param str       문자열
     * @param maxLength 최대 길이
     * @return maxLength 보다 길면 maxLength 만큼 자른 문자열
     */
    public static String substringN(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > maxLength) {
            return str.substring(0, maxLength);
        }
        return str;
    }

    /**
     * 문자열이 null 이면 "" 을 반환한다
     *
     * @param str 문자열
     * @return null 이면 "" 을 반환
     */
    public static String defaultIfBlank(String str) {
        return defaultIfBlank(str, "");
    }

    /**
     * 문자열이 null 이거나 공백(space 포함)이면 defaultStr 을 반환한다
     *
     * @param str        문자열
     * @param defaultStr null 이거나 공백이면 반환할 문자열
     * @return null 이거나 공백이면 defaultStr 을 반환
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        if (isBlank(str)) {
            return defaultStr;
        }
        return str;
    }

    /**
     * EUC-KR Url 인코딩
     *
     * @param str 인코딩할 문자열
     * @return 인코딩된 문자열
     */
    public static String urlEncodeEUCKR(String str) {
        if (isBlank(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str, EUC_KR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * EUC-KR Url 디코딩
     *
     * @param encodedStr 디코딩할 문자열
     * @return 디코딩된 문자열
     */
    public static String urlDecodeEUCKR(String encodedStr) {
        if (isBlank(encodedStr)) {
            return "";
        }
        try {
            return URLDecoder.decode(encodedStr, EUC_KR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 문자열이 null 이거나 공백(space 포함)인지 확인
     *
     * @param str 문자열
     * @return null 이거나 공백이면 true
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

}
