package com.velvet;

/**
 * Created by vduseev on 26/01/2017.
 */
public class Paragraph implements Section {

    public Paragraph(String text) {

        this.text = text;
    }

    public static Paragraph fromVelvetText(String text) {

        return new Paragraph(text);
    }

    public String getText() { return text; }

    public SectionType getType() { return SectionType.Paragraph; }

    private String text;
}
