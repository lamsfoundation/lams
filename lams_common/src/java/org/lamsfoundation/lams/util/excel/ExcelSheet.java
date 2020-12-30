package org.lamsfoundation.lams.util.excel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Excel sheet, containing Excel rows.
 *
 * @author Andrey
 */
public class ExcelSheet {
    private String sheetName = "";
    private List<ExcelRow> rows = new ArrayList<>();
    Set<CellRangeAddress> mergedCells = new HashSet<>();

    public ExcelSheet() {
    }

    public ExcelSheet(String sheetName) {
	this.sheetName = sheetName;
    }

    /**
     * Add empty row.
     */
    public void addEmptyRow() {
	rows.add(new ExcelRow());
    }

    /**
     * Return row at the specified position in rows list.
     */
    public ExcelRow getRow(int index) {
	return rows.get(index);
    }

    public ExcelRow initRow(boolean isBold) {
	ExcelRow row = new ExcelRow();
	rows.add(row);
	row.setBold(isBold);
	return row;
    }

    public ExcelRow initRow() {
	return initRow(false);
    }

    public void addRow(ExcelRow row) {
	rows.add(row);
    }

    public List<ExcelRow> getRows() {
	return rows;
    }

    public void setRows(List<ExcelRow> rows) {
	this.rows = rows;
    }

    public String getSheetName() {
	return sheetName;
    }

    public void setSheetName(String sheetName) {
	this.sheetName = sheetName;
    }

    public void addMergedCells(int row, int fromColumn, int toColumn) {
	mergedCells.add(new CellRangeAddress(row, row, fromColumn, toColumn));
    }
}