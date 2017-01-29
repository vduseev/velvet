package com.velvet.section;

/**
 * Created by vduseev on 26/01/2017.
 */
public class List implements Section {

    public List(java.util.List<Paragraph> items) {

        //this.sourceText = sourceText;
        this.items = items;
    }

    private List() {
        sourceText = "";
        items = new java.util.ArrayList<Paragraph>();
    }

    public static List fromVelvetText(String text) {

        List list = new List();
        list.sourceText = text;

        String[] splittedSource = text.split("\\s*\\*");
        for (int i = 1; i < splittedSource.length; i++) {

            String paragraphSource = splittedSource[i];

            Paragraph paragraph = Paragraph.fromVelvetText(paragraphSource);
            list.items.add(paragraph);
        }

        return list;
    }

    public java.util.List<Paragraph> getItems() { return items; }

    public String getSourceText() { return sourceText; }

    public SectionType getType() { return SectionType.List; }

    private String sourceText;
    private java.util.List<Paragraph> items;
}
