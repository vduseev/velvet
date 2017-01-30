package com.velvet.parser;

import com.velvet.Document;
import com.velvet.section.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vduseev on 22/01/2017.
 */
public class Parser {
    public static Document parseDocument(String text) {

        Document doc = new Document();

        Map<Integer, Section> sectionMap = new HashMap<Integer, Section>();

        // Match tags

        // Match headers
        Map<Integer, Header> headerMap = getHeaders(text);
        sectionMap.putAll(headerMap);

        // Match lists
        Map<Integer, com.velvet.section.List> listMap = getLists(text);
        sectionMap.putAll(listMap);

        // Match paragraphs
        Map<Integer, Paragraph> paragraphMap = getParagraphs(text);
        sectionMap.putAll(paragraphMap);

        // Match tables
        Map<Integer, Table> tableMap = getTables(text);
        sectionMap.putAll(tableMap);

        // Match diagrams
        Map<Integer, Diagram> diagramMap = getDiagrams(text);
        sectionMap.putAll(diagramMap);

        // Sort keys
        Set<Integer> sectionMapKeys = sectionMap.keySet();
        java.util.List<Integer> sortedSectionMapKeys = new ArrayList<Integer>(sectionMapKeys);
        java.util.Collections.sort(sortedSectionMapKeys);

        // Add sections to doc in the correct order
        for (Integer key:
             sortedSectionMapKeys) {

            doc.addSection(sectionMap.get(key));
        }

        return doc;
    }

    public static Map<Integer, Header> getHeaders(String text) {

        String lineSplitterRegEx = "\\s*(.+)(\\r?\\n|$)";
        Pattern pattern = Pattern.compile(lineSplitterRegEx);
        Matcher matcher = pattern.matcher(text);

        Map<Integer, Header> headers = new HashMap<Integer, Header>();
        while (matcher.find()) {

            String line = matcher.group(1);
            Integer key = matcher.start();

            String headerRegEx = "^(#+\\s*.+)$";
            if (line.matches(headerRegEx)) {

                Header header = Header.fromVelvetLine(line);
                headers.put(key, header);
            }
        }

        return headers;
    }

    public static Map<Integer, Paragraph> getParagraphs(String text) {

        String lineSplitterRegEx = "\\s*(.+)\\r?\\n";
        Pattern pattern = Pattern.compile(lineSplitterRegEx);
        Matcher matcher = pattern.matcher(text);

        Map<Integer, Paragraph> paragraphs = new HashMap<Integer, Paragraph>();
        while (matcher.find()) {

            String line = matcher.group(1);
            Integer key = matcher.start();

            String paragraphRegEx = "^(?![#*]\\s*.+).+";
            if (line.matches(paragraphRegEx)) {

                Paragraph paragraph = Paragraph.fromVelvetText(line);
                paragraphs.put(key, paragraph);
            }
        }

        return paragraphs;
    }

    public static Map<Integer, Diagram> getDiagrams(String text) {

        String diagramRegEx = "[\\^\\n]\\s*(@startuml\\n)(.*?)(@enduml)";
        Pattern pattern = Pattern.compile(diagramRegEx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        Map<Integer, Diagram> diagrams = new HashMap<Integer, Diagram>();
        while (matcher.find()) {

            String diagramSource = matcher.group(1) + matcher.group(2) + matcher.group(3);
            Integer key = matcher.start();

            Diagram diagram = Diagram.fromVelvetText(diagramSource);
            diagrams.put(key, diagram);
        }

        return diagrams;
    }

    public static Map<Integer, Table> getTables(String text) {

        String tableRegEx = "(?:\\G|\\n{1,2})\\s*(\\|.+?\\|)(\\n[^\\|]|$|\\n$)";
        Pattern pattern = Pattern.compile(tableRegEx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        Map<Integer, Table> tables = new HashMap<Integer, Table>();
        while (matcher.find()) {

            String tableSource = matcher.group(1);
            Integer key = matcher.start();

            Table table = Table.fromVelvetText(tableSource);

            if (table != null) {
                tables.put(key, table);
            }
        }

        return tables;
    }

    public static Map<Integer, com.velvet.section.List> getLists(String text) {

        String listRegEx = "(?:\\G|\\n{1,2})(\\s*(\\*|n.|N.)\\s.+?)(\\n\\n|$|\\n$)";
        Pattern pattern = Pattern.compile(listRegEx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        Map<Integer, com.velvet.section.List> lists = new HashMap<Integer, com.velvet.section.List>();
        while (matcher.find()) {

            String listSource = matcher.group(1);// + "%%%" + matcher.group(2);// + "%%%" + matcher.group(3);;
            Integer key = matcher.start();

            com.velvet.section.List list = com.velvet.section.List.fromVelvetText(listSource);
            lists.put(key, list);
        }

        return lists;
    }

    public static boolean isHeader(String line) {

        return line.matches("(\\s*)(#+)(\\s*)(.+)");
    }

    public static java.util.List splitFileIntoCommentSections(String text) {

        String reg = "/\\*\\s*(.*?)\\s*\\*/";
        Pattern pattern = Pattern.compile(reg, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        java.util.List sections = new ArrayList<String>();
        while (matcher.find()) {
            sections.add(matcher.group(1));
        }

        return sections;
    }

    public static String readFile(String path)
            throws IOException    {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
}
