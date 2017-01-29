package com.velvet;

import com.velvet.section.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vduseev on 22/01/2017.
 */
public class Document {
    public Document() {
        sections = new ArrayList<Section>();
    }

    public void addSection(Section section) { sections.add(section); }

    private List<Section> sections;
}
