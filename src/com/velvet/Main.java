package com.velvet;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String file = "";
        try {
            file = Parser.readFile("example_01.vel");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> sections = Parser.splitFileIntoCommentSections(file);

        for (String sectionText : sections) {
            //Node node = Parser.parseDocument(sectionText);
        }
    }
}
