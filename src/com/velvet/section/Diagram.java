package com.velvet.section;

/**
 * Created by vduseev on 26/01/2017.
 */
public class Diagram implements Section {

    public Diagram(String sourceText) {

        this.sourceText = sourceText;
    }

    public static Diagram fromVelvetText(String text) {

        return new Diagram(text);
    }

    public String getSourceText() { return sourceText; }

    public SectionType getType() { return SectionType.Diagram; }

    private String sourceText;
}
