package io.github.kloping.other;

import java.awt.*;

/**
 * @author github-kloping
 */
public class FormatColorUtils {
    public static enum FormatColor {
        RESET("\u001B[0m", Color.decode("#FF1C1D2C")),

        WHITE("\u001B[30m", Color.WHITE),
        RED("\u001B[31m", Color.RED),
        EMERALD_GREEN("\u001B[32m", Color.GREEN),
        GOLD("\u001B[33m", Color.YELLOW),
        BLUE("\u001B[34m", Color.BLUE),
        PURPLE("\u001B[35m", Color.BLACK),
        GREEN("\u001B[36m", Color.GREEN),

        GRAY("\u001B[90m", Color.GRAY),
        LIGHT_RED("\u001B[91m", Color.RED),
        LIGHT_GREEN("\u001B[92m", Color.decode("#FF009732")),
        LIGHT_YELLOW("\u001B[93m", Color.YELLOW),
        LIGHT_BLUE("\u001B[94m", Color.BLUE),
        LIGHT_PURPLE("\u001B[95m", Color.BLACK),
        LIGHT_CYAN("\u001B[96m", Color.CYAN);

        public String getM1() {
            return m1;
        }

        public Color getColor() {
            return color;
        }

        private String m1;
        private Color color;

        private FormatColor(String m1, Color color) {
            this.m1 = m1;
            this.color = color;
        }
    }

    public static class ColorString {
        private Color color;
        private String data;

        private ColorString() {
        }

        public Color getColor() {
            return color;
        }

        public String getData() {
            return data;
        }
    }

    /**
     * 过滤文字 到 颜色文字包装
     *
     * @param str
     * @return
     */
    public static ColorString filterString(String str) {
        ColorString colorString = new ColorString();
        colorString.data = str;
        colorString.color = null;
        if (str.startsWith("\u001B["))
            for (FormatColor c : FormatColor.values()) {
                if (str.startsWith(c.getM1())) {
                    int i = str.lastIndexOf(FormatColor.RESET.getM1());
                    String data = str.substring(c.getM1().length(), i);
                    colorString.data = data;
                    colorString.color = c.getColor();
                    return colorString;
                }
            }
        return colorString;
    }
}
