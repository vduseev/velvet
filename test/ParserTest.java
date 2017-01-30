import com.velvet.parser.Parser;
import com.velvet.section.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.format;
import static junit.framework.TestCase.fail;

/**
 * Created by vduseev on 22/01/2017.
 */
public class ParserTest {
    @Test
    public void testReadFile() {
        try {
            String text = Parser.readFile("example/example_01.vel");
            assertTrue(text.contains("This is a Velvet spec."));
        } catch (IOException ioEx) {
            fail("IOException: " + ioEx.getMessage());
        }
    }

    @Test
    public void testGetSections() {
        String INPUT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor \n" +
                "incididunt ut labore et dolore magna aliqua. " + "/* Inline comment */ " +
                "Ut enim ad minim veniam, quis nostrud exercitation \n" +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
                "\n" +
                "/* This is a one line comment. */ \n" +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat \n" +
                "nulla pariatur. \n" +
                "/*\n" +
                "This is a multiline\n" +
                "comment.\n" +
                "*/\n" +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                "deserunt mollit anim id est laborum.\n" +
                "\n" +
                "/* Another\n" +
                "multiline\n" +
                "comment. */\n" +
                "\n" +
                "The End.\n";
        java.util.List sections = Parser.splitFileIntoCommentSections(INPUT);
        assertTrue(sections.size() == 4);
    }

    @Test
    public void testCountOfGetHeaders() {
        String TEXT = "# Header 1\n" +
                "    ## Header 1.1\n" +
                "# Header 2\n" +
                "\n" +
                " This is a paragraph.\n" +
                "This is also a paragraph ### Smth.\n" +
                " Not a header # at all.\n" +
                "\n" +
                "You think I'm a header, no, I'm not #.\n" +
                "\n" +
                "    ### Header 2.1.1\n" +
                "\n" +
                "    # Header 3";

        Map<Integer, Header> headers = Parser.getHeaders(TEXT);
        for (Integer key:
                headers.keySet()) {
            Header header = headers.get(key);
            System.out.println("(" + key + ") " + header.getText());
        }
        assertTrue(headers.size() == 5);
    }

    @Test
    public void testCountOfGetParagraphs() {
        String TEXT = "This is a Velvet spec.\n" +
                "\n" +
                "       This page describes all the things you can do with Velvet.\n" +
                "\n" +
                "    It is written to be rendered as HTML using Velvet itself.\n" +
                "\n" +
                "       # This is not a paragraph, this is a header.\n" +
                "\n" +
                "    This is a paragraph sample.\n" +
                "    Paragraph is text succeeded by one line break.\n" +
                "\n";

        //System.out.println(TEXT);

        Map<Integer, Paragraph> paragraphs = Parser.getParagraphs(TEXT);
        for (Integer key:
             paragraphs.keySet()) {
            Paragraph paragraph = paragraphs.get(key);
            System.out.println("(" + key + ") " + paragraph.getText());
        }

        assertTrue(paragraphs.size() == 5);
    }

    @Test
    public void testCountOfGetDiagrams() {
        String TEXT = "@startuml Wrong diagram right from the beggining @enduml\n" +
                "\n" +
                "   @startuml\n" +
                "       [Actor] -> (Use case) : comment\n" +
                "   @enduml\n" +
                "   @startuml\n" +
                "@enduml\n" +
                "\n" +
                "@startuml\n" +
                "@enduml";

        Map<Integer, Diagram> diagrams = Parser.getDiagrams(TEXT);
        for (Integer key:
             diagrams.keySet()) {
            Diagram diagram = diagrams.get(key);
            System.out.println("(" + key + "):\n" + diagram.getSourceText());
        }

        assertTrue(diagrams.size() == 3);
    }

    @Test
    public void testCountOfGetTables() {
        String TEXT = "# Table Examples\n" +
                "|Header 1|Header 2|\n" +
                "|--------|--------|\n" +
                "|col 1|col 2|\n" +
                "\n" +
                "|H1|\n" +
                "|--|\n" +
                "|R1|\n" +
                "|R2|\n" +
                "\n" +
                "|That's a table with wrong number of columns|\n" +
                "|See, this won't be counted|Two columns in a second row here|\n" +
                "\n" +
                "|Just value| Another value|More values with no header |\n" +
                "|Damn that's it | I can put it here all day | Here it is |\n";

        Map<Integer, Table> tables = Parser.getTables(TEXT);
        for (Integer key:
             tables.keySet()) {
            Table table = tables.get(key);
            System.out.println("(" + key + "):\n" + table.getSourceText());

            System.out.println("Actual table (" + (table.hasHeader() ? "with header" : "no header") +"):");
            for (int r = 0; r < table.getRowCount(); r++) {
                for (int c = 0; c < table.getColumnCount(); c++) {
                    System.out.print("|" + table.get(r, c) + "|");
                }
                System.out.print("\n");
            }
        }

        assertTrue(tables.size() == 3);
    }

    @Test
    public void testCountOfGetLists() {
        String TEXT = "# List Examples\n" +
                "\n" +
                "* Simple list\n" +
                "* Contains some items.\n" +
                "\n" +
                "* Lists with multiline items\n" +
                "  are possible.\n" +
                "* Yep, no problem with that.\n" +
                "\n" +
                "  The fuck you think is happening here?\n" +
                "   * A nice list right here.\n" +
                "   * With more items\n" +
                "   * And double lines\n" +
                "     just for you.";

        Map<Integer, List> lists = Parser.getLists(TEXT);
        for (Integer key:
             lists.keySet()) {
            List list = lists.get(key);
            System.out.println("(" + key + "):\n" + list.getSourceText());
            System.out.println("(content):");
            for (int i = 0; i < list.getItems().size(); i++) {

                Paragraph par = list.getItems().get(i);
                System.out.println("[" + i + "]:" + par.getText());
            }
        }

        assertTrue(lists.size() == 3);
    }

    static String readFile(String path) {
        try {
            return Parser.readFile(path);
        } catch (IOException ioEx) {
            System.out.println(ioEx.getMessage());
            return null;
        }
    }
}
