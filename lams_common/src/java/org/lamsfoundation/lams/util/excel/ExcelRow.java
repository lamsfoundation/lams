package org.lamsfoundation.lams.util.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Excel row, containing Excel cells.
 * 
 * @author Andrey Balan
 */
public class ExcelRow {
    private List<ExcelCell> cells = new ArrayList<>();
    
    public ExcelCell addCell(Object cellValue) {
	ExcelCell cell = new ExcelCell(cellValue);
	cells.add(cell);
	return cell;
    }

    public ExcelCell addCell(Object cellValue, Boolean isBold) {
	ExcelCell cell = new ExcelCell(cellValue, isBold);
	cells.add(cell);
	return cell;
    }

    public void addCell(Object cellValue, IndexedColors color) {
	ExcelCell cell = new ExcelCell(cellValue, color);
	cells.add(cell);
    }

    public void addCell(Object cellValue, int borderStyle) {
	ExcelCell cell = new ExcelCell(cellValue, borderStyle);
	cells.add(cell);
    }

    public void addCell(Object cellValue, Boolean isBold, int borderStyle) {
	ExcelCell cell = new ExcelCell(cellValue, isBold, borderStyle);
	cells.add(cell);
    }
    
    public void addEmptyCells(int numberEmptyCells) {
	for (int i = 0; i < numberEmptyCells; i++) {
	    ExcelCell cell = new ExcelCell("");
	    cells.add(cell);
	}
    }
    
    public List<ExcelCell> getCells() {
	return cells;
    }
    public void setCells(List<ExcelCell> cells) {
	this.cells = cells;;
    }
}
