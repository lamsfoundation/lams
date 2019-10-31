package org.lamsfoundation.lams.util.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel sheet, containing Excel rows.
 * 
 * @author Andrey
 */
public class ExcelSheet {
    private String sheetName = "";
    private List<ExcelRow> rows = new ArrayList<>();
    
    public ExcelSheet() {}
    
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
     * 
     * @param index
     * @return
     */
    public ExcelRow getRow(int index) {
	return rows.get(index);
    }
    
    public ExcelRow initRow() {
	ExcelRow row = new ExcelRow();
	rows.add(row);
	return row;
    }
    
    public void addRow(ExcelRow row) {
	rows.add(row);
    }
    
    public List<ExcelRow> getRows() {
	return rows;
    }
    public void setRows(List<ExcelRow> rows) {
	this.rows = rows;;
    }
    
    public String getSheetName() {
	return sheetName;
    }
    public void setSheetName(String sheetName) {
	this.sheetName = sheetName;;
    }
}
