package com.nullptr.utils.random;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数生成器
 *
 * @author nullptr
 * @version 1.0 2020-3-18
 * @since 1.0 2020-3-18
 *
 * @see Random
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {
    protected static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    /**
     * 生成指定范围内的字符
     *
     * @param min 最小取值
     * @param max 最大取值
     * @return 返回[min-max]之间的字符
     *
     * @since 1.0
     */
    public static char nextCharacter(char min, char max) {
        return (char) (nextInt(min, max));
    }

    /**
     * 生成随机大写字母
     * @since 1.0
     */
    public static char nextUppercase() {
        return nextCharacter('A', 'Z');
    }

    /**
     * 生成随机小写字母
     * @since 1.0
     */
    public static char nextLowerCase() {
        return nextCharacter('a', 'z');
    }

    /**
     * 生成随机中文字符
     * @since 1.0
     */
    public static char nextChinese() {
        return nextCharacter('\u4e00', '\u9fa5');
    }

    /**
     * 生成随机的UUID
     * @since 1.0
     */
    public static String nexUUID() {
        return UUID.randomUUID().toString();
    }
}
