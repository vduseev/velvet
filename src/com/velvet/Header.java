package com.velvet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vduseev on 26/01/2017.
 */
public class Header implements Section {
    public Header(int level, String text) {

        this.level = level;
        this.text = text;
    }

    public static Header fromVelvetLine(String line) {

        Pattern pattern = Pattern.compile("\\s*\\#+\\s*(.+)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            int level = line.length() - line.replace("#", "").length();
            String text = matcher.group(1);
            return new Header(level, text);
        } else {
            return null;
        }
    }

    public String getText() { return text; }

    public int getLevel() { return level; }

    public SectionType getType() { return SectionType.Header; }

    private int level;
    private String text;
}
