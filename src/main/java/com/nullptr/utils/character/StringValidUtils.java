package com.nullptr.utils.character;

import java.util.regex.Pattern;

/**
 * 字符串语言验证工具，用于判断字符串是否为中文或英文
 *
 *
 *
 * @author 胖橘
 * @version 1.0 2020-10-9
 * @since 1.0 2020-10-9
 */
public class StringValidUtils {
    private static final Pattern ENGLISH = Pattern.compile("[a-zA-Z ]*");
    private static final Pattern CHINESE = Pattern.compile("[\\u4e00-\\u9fa5 ]*");

    private StringValidUtils() {
    }

    public static boolean isEnglish(String word) {
        return ENGLISH.matcher(word).matches();
    }

    public static boolean isChinese(String word) {
        return CHINESE.matcher(word).matches();
    }

    public static boolean isEmpty(CharSequence ch){
        return ch != null || ch.length() > 0;
    }
}
