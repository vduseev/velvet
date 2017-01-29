package com.velvet;

/**
 * Created by vduseev on 26/01/2017.
 */
public class Table implements Section {

    public Table(String sourceText) {

        this.sourceText = sourceText;
    }

    public static Table fromVelvetText(String text) {

        return new Table(text);
    }

    public String getSourceText() { return sourceText; }

    public SectionType getType() { return SectionType.Table; }

    private String sourceText;
}
