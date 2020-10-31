package com.nullptr.utils.random;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 验证码图像生成器
 *
 * @author nullptr
 * @version 1.0 2020-3-18
 * @since 1.0 2020-3-18
 */
public final class VerificationImageGeneratorUtils {
    /** 图片宽 */
    private static final int WIDTH = 95;
    /** 图片高 */
    private static final int HEIGHT = 25;

    /**
     * 生成验证码图像
     *
     * @param text 验证码文本
     *
     * @since 1.0
     */
    public BufferedImage generate(String text) {
        // 初始化缓存区图像为8位图像
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
        // 获取图形上下文
        Graphics graphics = image.getGraphics();
        // 填充画布
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        // 绘制干扰线
        drawLine(graphics);
        // 绘制文字
        drawText(text, graphics);
        // 关闭画布
        graphics.dispose();
        return image;
    }

    /**
     * 获取字体
     *
     * @since 1.0
     */
    private Font getFont() {
        // 字体属性为Tahoma字体, 样式为斜体, 字体尺寸为18
        return new Font("Tahoma", Font.ITALIC, 18);
    }

    /**
     * 获取颜色
     *
     * @since 1.0
     */
    private Color getColor() {
        // 随机生成RGB三色通道值
        int rColor = RandomGeneratorUtils.randomInt(0, 255);
        int gColor = RandomGeneratorUtils.randomInt(0, 255);
        int bColor = RandomGeneratorUtils.randomInt(0, 255);
        return new Color(rColor, gColor, bColor);
    }

    /**
     * 绘制文字
     *
     * @param text 待绘制文字
     *
     * @since 1.0
     */
    private void drawText(String text, Graphics graphics) {
        // 获取字体
        graphics.setFont(getFont());
        // 逐个绘制文字
        for (int i = 0; i < text.length(); i++) {
            // 随机获取文字颜色
            graphics.setColor(getColor());
            // 随机生成X轴偏移量，取值范围0-9
            int translateX = RandomGeneratorUtils.randomInt(10);
            // 随机生成Y轴偏移量，取值范围0-3
            int translateY = RandomGeneratorUtils.randomInt(3);
            // 执行偏移操作
            graphics.translate(translateX, translateY);
            // 绘制文字，文字间距离相差15
            graphics.drawString(String.valueOf(text.charAt(i)), i * 15, 16);
        }
    }

    /**
     * 绘制干扰线
     *
     * @since 1.0
     */
    private void drawLine(Graphics graphics) {
        // 随机获取干扰线数量，取值范围为20-40
         int lineSize = RandomGeneratorUtils.randomInt(20, 40);
         // 绘制干扰线
         for (int i = 0; i <= lineSize; i++) {
             // 计算起始x轴坐标
             int x1 = RandomGeneratorUtils.randomInt(WIDTH);
             // 计算起始y轴坐标
             int y1 = RandomGeneratorUtils.randomInt(HEIGHT);
             // 计算终止x轴坐标
             int x2 = x1 + RandomGeneratorUtils.randomInt(13);
             // 计算终止y轴坐标
             int y2 = y1 + RandomGeneratorUtils.randomInt(15);
             // 设置干扰线颜色
             graphics.setColor(getColor());
             // 绘制干扰线
             graphics.drawLine(x1, y1, x2, y2);
        }
    }
}