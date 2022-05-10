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
    private boolean isBold = false;
    private List<ExcelCell> cells = new ArrayList<>();

    /**
     * Return cell value at the specified position in cells list.
     *
     * @param index
     * @return
     */
    public Object getCell(int index) {
	return cells.get(index).getCellValue();
    }

    public ExcelCell addCell(Object cellValue) {
	ExcelCell cell = new ExcelCell(cellValue);
	cells.add(cell);
	return cell;
    }

    public ExcelCell addPercentageCell(Double cellValue) {
	return addPercentageCell(cellValue, false, 0);
    }

    public ExcelCell addPercentageCell(Double cellValue, Boolean isBold, int borderStyle) {
	ExcelCell cell = new ExcelCell(cellValue);
	cell.setDataFormat(ExcelCell.CELL_FORMAT_PERCENTAGE);
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

    public void addCell(Object cellValue, int... borderStyle) {
	ExcelCell cell = new ExcelCell(cellValue, borderStyle);
	cells.add(cell);
    }

    public ExcelCell addCell(Object cellValue, Boolean isBold, int... borderStyle) {
	ExcelCell cell = new ExcelCell(cellValue, isBold, borderStyle);
	cells.add(cell);
	return cell;
    }

    /**
     * Add one empty cell
     */
    public void addEmptyCell() {
	ExcelCell cell = new ExcelCell("");
	cells.add(cell);
    }

    /**
     * Add specified number of empty cell.
     *
     * @param numberEmptyCells
     */
    public void addEmptyCells(int numberEmptyCells) {
	if (numberEmptyCells < 1) {
	    return;
	}

	for (int i = 0; i < numberEmptyCells; i++) {
	    ExcelCell cell = new ExcelCell("");
	    cells.add(cell);
	}
    }

    public boolean isBold() {
	return isBold;
    }

    public void setBold(boolean isBold) {
	this.isBold = isBold;
    }

    public List<ExcelCell> getCells() {
	return cells;
    }

    public void setCells(List<ExcelCell> cells) {
	this.cells = cells;
	;
    }
}
