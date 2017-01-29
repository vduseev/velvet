package com.velvet;

/**
 * Created by vduseev on 26/01/2017.
 */
public class VelvetList implements Section {

    public VelvetList(String sourceText) {

        this.sourceText = sourceText;
    }

    public static VelvetList fromVelvetText(String text) {

        return new VelvetList(text);
    }

    public String getSourceText() { return sourceText; }

    public SectionType getType() { return SectionType.List; }

    private String sourceText;
}
