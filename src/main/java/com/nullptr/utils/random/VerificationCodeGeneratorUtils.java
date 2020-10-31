package com.nullptr.utils.random;

/**
 * 验证码生成器
 *
 * @author nullptr
 * @version 1.0 2020-3-18
 * @since 1.0 2020-3-18
 */
public final class VerificationCodeGeneratorUtils {
    /**
     * 获取验证码
     *
     * @since 1.0
     */
    public String generate(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        // 生成指定位数的验证码
        for (int index = 0; index < size; index++) {
            // 判断下一位是否为字母
            if (nextIsLetter()) {
                stringBuilder.append(generateLetter());
                // 不是则生成数字
            } else {
                stringBuilder.append(generateNumber());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 判断下一位验证码是否为字母
     *
     * @return 结果为真则表示下一位是字母否则为数字
     *
     * @since 1.0
     */
    private boolean nextIsLetter() {
        return RandomGeneratorUtils.randomBoolean();
    }

    /**
     * 判断下一位验证码是否为大写字母
     *
     * @return 结果为真则表示下一位是大写字母否则为小写
     *
     * @since 1.0
     */
    private boolean nextIsUppercase() {
        return RandomGeneratorUtils.randomBoolean();
    }

    /**
     * 生成数字
     *
     * @since 1.0
     */
    private int generateNumber() {
        // 生成0-9内的随机数
        return RandomGeneratorUtils.randomInt(0, 9);
    }

    /**
     * 生成字母
     *
     * @since 1.0
     */
    private char generateLetter() {
        // 判断是否大写字母
        if (nextIsUppercase()) {
            // 生成大写字母ascii码范围内的随机数
            return RandomGeneratorUtils.randomChar('A', 'Z');
        } else {
            // 生成小写字母ascii码范围内的随机数
            return RandomGeneratorUtils.randomChar('a', 'z');
        }
    }
}
