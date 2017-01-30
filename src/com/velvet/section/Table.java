package com.velvet.section;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vduseev on 26/01/2017.
 */
public class Table implements Section {

    private Table(int rowCount, int columnCount, boolean hasHeader, String[][] table, String sourceText) {

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.hasHeader = hasHeader;
        this.table = table;
        this.sourceText = sourceText;
    }

    public static Table fromVelvetText(String text) {

        // This is to check that every row has the same amount
        // of columns.
        int maxColumnCount = 0;

        // We will find out if table has header later, when we
        // copy collected cell to the fixed size array.
        boolean hasHeader = false;

        // Prior to moving this data to 2-dimensional fixed array
        // we will check if actual row.length equals to size of
        // rowsOfCells list below.
        List<List<String>> rowsOfCells = new ArrayList<List<String>>();

        // Precompile reusable regex for each row.
        String rowRegEx = "\\|([^\\|\\n]+)";
        Pattern pattern = Pattern.compile(rowRegEx);

        // Split the whole table into rows by newline
        // and collect into rowsOfCells.
        String[] rows = text.split("\\n");
        for (int r = 0; r < rows.length; r++) {

            List<String> cells = new ArrayList<String>();

            // Collect cells from a row.
            Matcher matcher = pattern.matcher(rows[r]);
            while (matcher.find()) {

                String cell = matcher.group(1);
                cells.add(cell);
            }

            // See if this row contains more cells than any other row.
            // Spoiler alert: it shouldn't.
            if (cells.size() > maxColumnCount) {

                maxColumnCount = cells.size();
            }

            rowsOfCells.add(cells);
        }

        // Check that rows.length is the same as List size.
        if (rows.length != rowsOfCells.size()) {

            // bail
            return null;
        }

        // Check that each row contains maxColumnCount of cells.
        // If not, bail.
        for (List<String> row:
             rowsOfCells) {

            if (row.size() != maxColumnCount) {

                // bail
                return null;
            }
        }

        // See if table has a header by checking if the second row
        // consists of cells with '-' dashes symbolising the header divider.
        if (rowsOfCells.size() > 2) {
            hasHeader = isRowHeaderDivider(rowsOfCells.get(1));
        }

        // Allocate fixed array for the table, since we know
        // for sure that table is satisfying our requirements
        // and if it has a header.
        int rowCount = (hasHeader) ? rowsOfCells.size() - 1 : rowsOfCells.size();
        int columnCount = maxColumnCount;
        String[][] table = new String[rowCount][columnCount];

        // Copy first row separately.
        copyRowToTable(rowsOfCells.get(0), table, 0);

        // Copy the rest of the rows to the fixed size table.
        for (int r = 2; r < rowsOfCells.size(); r++) {

            List<String> row = rowsOfCells.get(r);
            copyRowToTable(row, table, r - 1);
        }

        return new Table(rowCount, columnCount, hasHeader, table, text);
    }

    public static boolean isRowHeaderDivider(List<String> row) {

        // By default we assume the given row is not a header divider,
        // but loop below will have to refute (disprove) that assumption.
        boolean isNotHeaderDivider = true;

        for (String cell:
             row) {

            if (!cell.matches("\\-+")) {

                isNotHeaderDivider = false;
                break;
            }
        }

        return isNotHeaderDivider;
    }

    public static void copyRowToTable(List<String> row, String[][] table, int rowIndex) {

        for (int c = 0; c < row.size(); c++) {

            table[rowIndex][c] = row.get(c);
        }
    }

    public int getRowCount() { return rowCount; }

    public int getColumnCount() { return columnCount; }

    public boolean hasHeader() { return hasHeader; }

    public String get(int row, int column) { return table[row][column]; }

    public String getSourceText() { return sourceText; }

    public SectionType getType() { return SectionType.Table; }

    private String sourceText;
    private String[][] table;
    private int rowCount;
    private int columnCount;
    private boolean hasHeader;
}
