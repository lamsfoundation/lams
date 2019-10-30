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
