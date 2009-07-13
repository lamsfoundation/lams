
/*	
 *	License
 *	
 *	This file is part of The SeleniumFlex-API.
 *	
 *	The SeleniumFlex-API is free software: you can redistribute it and/or
 *  modify it  under  the  terms  of  the  GNU  General Public License as 
 *  published  by  the  Free  Software Foundation,  either  version  3 of 
 *  the License, or any later version.
 *
 *  The SeleniumFlex-API is distributed in the hope that it will be useful,
 *  but  WITHOUT  ANY  WARRANTY;  without  even the  implied  warranty  of
 *  MERCHANTABILITY   or   FITNESS   FOR  A  PARTICULAR  PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with The SeleniumFlex-API.
 *	If not, see http://www.gnu.org/licenses/
 *
 */

package org.lamsfoundation.lams.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;
 

public class DefaultSeleniumFlex extends DefaultSelenium {
	
	public DefaultSeleniumFlex(HttpCommandProcessor proc) {
		super(proc);
	}
	
	protected void handleException(SeleniumException e, String command, String target, String args) {
		System.out.println(e.getMessage());
		System.out.println("Failed command : " + command + "(" + target + ", " + args +")");
	}
	
	protected String executeCommand(String command, String target, String args) throws Exception {
		String retval = "";
		try {
			retval = this.commandProcessor.doCommand(command, new String[] { target, args });
		} catch (SeleniumException e) {
			handleException(e, command, target, args);
			throw e;
		}
		return retval;
	}

	public String getFlexSelectedItemAtIndex(String target, String args) throws Exception { return executeCommand("getFlexSelectedItemAtIndex", target, args).replace("OK,", ""); }
	public String getFlexSelectedItemAtIndex(String target) throws Exception { return executeCommand("getFlexSelectedItemAtIndex", target,  "").replace("OK,", ""); }
	public String getFlexNumSelectedItems(String target, String args) throws Exception { return executeCommand("getFlexNumSelectedItems", target, args).replace("OK,", ""); }
	public String getFlexNumSelectedItems(String target) throws Exception { return executeCommand("getFlexNumSelectedItems", target,  "").replace("OK,", ""); }
	public String getFlexVisible(String target, String args) throws Exception { return executeCommand("getFlexVisible", target, args).replace("OK,", ""); }
	public String getFlexVisible(String target) throws Exception { return executeCommand("getFlexVisible", target,  "").replace("OK,", ""); }
	public String getFlexTextPresent(String target, String args) throws Exception { return executeCommand("getFlexTextPresent", target, args).replace("OK,", ""); }
	public String getFlexTextPresent(String target) throws Exception { return executeCommand("getFlexTextPresent", target,  "").replace("OK,", ""); }
	public String getFlexText(String target, String args) throws Exception { return executeCommand("getFlexText", target, args).replace("OK,", ""); }
	public String getFlexText(String target) throws Exception { return executeCommand("getFlexText", target,  "").replace("OK,", ""); }
	public String getFlexStepper(String target, String args) throws Exception { return executeCommand("getFlexStepper", target, args).replace("OK,", ""); }
	public String getFlexStepper(String target) throws Exception { return executeCommand("getFlexStepper", target,  "").replace("OK,", ""); }
	public String getFlexSelectionIndex(String target, String args) throws Exception { return executeCommand("getFlexSelectionIndex", target, args).replace("OK,", ""); }
	public String getFlexSelectionIndex(String target) throws Exception { return executeCommand("getFlexSelectionIndex", target,  "").replace("OK,", ""); }
	public String getFlexSelection(String target, String args) throws Exception { return executeCommand("getFlexSelection", target, args).replace("OK,", ""); }
	public String getFlexSelection(String target) throws Exception { return executeCommand("getFlexSelection", target,  "").replace("OK,", ""); }
	public String getFlexRadioButton(String target, String args) throws Exception { return executeCommand("getFlexRadioButton", target, args).replace("OK,", ""); }
	public String getFlexRadioButton(String target) throws Exception { return executeCommand("getFlexRadioButton", target,  "").replace("OK,", ""); }
	public String getFlexProperty(String target, String args) throws Exception { return executeCommand("getFlexProperty", target, args).replace("OK,", ""); }
	public String getFlexProperty(String target) throws Exception { return executeCommand("getFlexProperty", target,  "").replace("OK,", ""); }
	public String getFlexParseInt(String target, String args) throws Exception { return executeCommand("getFlexParseInt", target, args).replace("OK,", ""); }
	public String getFlexParseInt(String target) throws Exception { return executeCommand("getFlexParseInt", target,  "").replace("OK,", ""); }
	public String getFlexNumeric(String target, String args) throws Exception { return executeCommand("getFlexNumeric", target, args).replace("OK,", ""); }
	public String getFlexNumeric(String target) throws Exception { return executeCommand("getFlexNumeric", target,  "").replace("OK,", ""); }
	public String getFlexGlobalPosition(String target, String args) throws Exception { return executeCommand("getFlexGlobalPosition", target, args).replace("OK,", ""); }
	public String getFlexGlobalPosition(String target) throws Exception { return executeCommand("getFlexGlobalPosition", target,  "").replace("OK,", ""); }
	public String getFlexExists(String target, String args) throws Exception { return executeCommand("getFlexExists", target, args).replace("OK,", ""); }
	public String getFlexExists(String target) throws Exception { return this.commandProcessor.doCommand("getFlexExists", new String[] { target, }); }
	public String getFlexErrorString(String target, String args) throws Exception { return executeCommand("getFlexErrorString", target, args).replace("OK,", ""); }
	public String getFlexErrorString(String target) throws Exception { return executeCommand("getFlexErrorString", target,  "").replace("OK,", ""); }
	public String getFlexEnabled(String target, String args) throws Exception { return executeCommand("getFlexEnabled", target, args).replace("OK,", ""); }
	public String getFlexEnabled(String target) throws Exception { return executeCommand("getFlexEnabled", target,  "").replace("OK,", ""); }
	public String getFlexDate(String target, String args) throws Exception { return executeCommand("getFlexDate", target, args).replace("OK,", ""); }
	public String getFlexDate(String target) throws Exception { return executeCommand("getFlexDate", target,  "").replace("OK,", ""); }
	public String getFlexDataGridUIComponentLabel(String target, String args) throws Exception { return executeCommand("getFlexDataGridUIComponentLabel", target, args).replace("OK,", ""); }
	public String getFlexDataGridUIComponentLabel(String target) throws Exception { return executeCommand("getFlexDataGridUIComponentLabel", target,  "").replace("OK,", ""); }
	public String getFlexDataGridRowIndexForFieldValue(String target, String args) throws Exception { return executeCommand("getFlexDataGridRowIndexForFieldValue", target, args).replace("OK,", ""); }
	public String getFlexDataGridRowIndexForFieldValue(String target) throws Exception { return executeCommand("getFlexDataGridRowIndexForFieldValue", target,  "").replace("OK,", ""); }
	public String getFlexDataGridRowCount(String target, String args) throws Exception { return executeCommand("getFlexDataGridRowCount", target, args).replace("OK,", ""); }
	public String getFlexDataGridRowCount(String target) throws Exception { return executeCommand("getFlexDataGridRowCount", target,  "").replace("OK,", ""); }
	public String getFlexDataGridFieldValueForGridRow(String target, String args) throws Exception { return executeCommand("getFlexDataGridFieldValueForGridRow", target, args).replace("OK,", ""); }
	public String getFlexDataGridFieldValueForGridRow(String target) throws Exception { return executeCommand("getFlexDataGridFieldValueForGridRow", target,  "").replace("OK,", ""); }
	public String getFlexDataGridCellText(String target, String args) throws Exception { return executeCommand("getFlexDataGridCellText", target, args).replace("OK,", ""); }
	public String getFlexDataGridCellText(String target) throws Exception { return executeCommand("getFlexDataGridCellText", target,  "").replace("OK,", ""); }
	public String getFlexDataGridCell(String target, String args) throws Exception { return executeCommand("getFlexDataGridCell", target, args).replace("OK,", ""); }
	public String getFlexDataGridCell(String target) throws Exception { return executeCommand("getFlexDataGridCell", target,  "").replace("OK,", ""); }
	public String getFlexComponentInfo(String target, String args) throws Exception { return executeCommand("getFlexComponentInfo", target, args).replace("OK,", ""); }
	public String getFlexComponentInfo(String target) throws Exception { return executeCommand("getFlexComponentInfo", target,  "").replace("OK,", ""); }
	public String getFlexComboContainsLabel(String target, String args) throws Exception { return executeCommand("getFlexComboContainsLabel", target, args).replace("OK,", ""); }
	public String getFlexComboContainsLabel(String target) throws Exception { return executeCommand("getFlexComboContainsLabel", target,  "").replace("OK,", ""); }
	public String getFlexCheckBoxChecked(String target, String args) throws Exception { return executeCommand("getFlexCheckBoxChecked", target, args).replace("OK,", ""); }
	public String getFlexCheckBoxChecked(String target) throws Exception { return executeCommand("getFlexCheckBoxChecked", target,  "").replace("OK,", ""); }
	public String getFlexAlertTextPresent(String target, String args) throws Exception { return executeCommand("getFlexAlertTextPresent", target, args).replace("OK,", ""); }
	public String getFlexAlertTextPresent(String target) throws Exception { return executeCommand("getFlexAlertTextPresent", target,  "").replace("OK,", ""); }
	public String getFlexAlertText(String target, String args) throws Exception { return executeCommand("getFlexAlertText", target, args).replace("OK,", ""); }
	public String getFlexAlertText(String target) throws Exception { return executeCommand("getFlexAlertText", target,  "").replace("OK,", ""); }
	public String getFlexAlertPresent(String target, String args) throws Exception { return executeCommand("getFlexAlertPresent", target, args).replace("OK,", ""); }
	public String getFlexAlertPresent(String target) throws Exception { return executeCommand("getFlexAlertPresent", target,  "").replace("OK,", ""); }
	public String getFlexASProperty(String target, String args) throws Exception { return executeCommand("getFlexASProperty", target, args).replace("OK,", ""); }
	public String getFlexASProperty(String target) throws Exception { return executeCommand("getFlexASProperty", target,  "").replace("OK,", ""); }
	public String getDataGridUIComponentLabel(String target, String args) throws Exception { return executeCommand("getDataGridUIComponentLabel", target, args).replace("OK,", ""); }
	public String getDataGridUIComponentLabel(String target) throws Exception { return executeCommand("getDataGridUIComponentLabel", target,  "").replace("OK,", ""); }
	public String getDataGridCellText(String target, String args) throws Exception { return executeCommand("getDataGridCellText", target, args).replace("OK,", ""); }
	public String getDataGridCellText(String target) throws Exception { return executeCommand("getDataGridCellText", target,  "").replace("OK,", ""); }
	public void doRefreshIDToolTips(String target, String args) throws Exception { executeCommand("doRefreshIDToolTips", target, args); }
	public void doRefreshIDToolTips(String target) throws Exception { executeCommand("doRefreshIDToolTips", target, ""); }
	public void flexWaitForElementVisible(String target, String args) throws Exception { executeCommand("flexWaitForElementVisible", target, args); }
	public void flexWaitForElementVisible(String target) throws Exception { executeCommand("flexWaitForElementVisible", target, ""); }
	public void flexWaitForElement(String target, String args) throws Exception { executeCommand("flexWaitForElement", target, args); }
	public void flexWaitForElement(String target) throws Exception { executeCommand("flexWaitForElement", target, ""); }
	public void flexTypeAppend(String target, String args) throws Exception { executeCommand("flexTypeAppend", target, args); }
	public void flexTypeAppend(String target) throws Exception { executeCommand("flexTypeAppend", target, ""); }
	public void flexType(String target, String args) throws Exception { executeCommand("flexType", target, args); }
	public void flexType(String target) throws Exception { executeCommand("flexType", target, ""); }
	public void flexStepper(String target, String args) throws Exception { executeCommand("flexStepper", target, args); }
	public void flexStepper(String target) throws Exception { executeCommand("flexStepper", target, ""); }
	public void flexSetFocus(String target, String args) throws Exception { executeCommand("flexSetFocus", target, args); }
	public void flexSetFocus(String target) throws Exception { executeCommand("flexSetFocus", target, ""); }
	public void flexSetDataGridCell(String target, String args) throws Exception { executeCommand("flexSetDataGridCell", target, args); }
	public void flexSetDataGridCell(String target) throws Exception { executeCommand("flexSetDataGridCell", target, ""); }
	public void flexSelectMatchingOnField(String target, String args) throws Exception { executeCommand("flexSelectMatchingOnField", target, args); }
	public void flexSelectMatchingOnField(String target) throws Exception { executeCommand("flexSelectMatchingOnField", target, ""); }
	public void flexSelectIndex(String target, String args) throws Exception { executeCommand("flexSelectIndex", target, args); }
	public void flexSelectIndex(String target) throws Exception { executeCommand("flexSelectIndex", target, ""); }
	public void flexSelectComboByLabel(String target, String args) throws Exception { executeCommand("flexSelectComboByLabel", target, args); }
	public void flexSelectComboByLabel(String target) throws Exception { executeCommand("flexSelectComboByLabel", target, ""); }
	public void flexSelect(String target, String args) throws Exception { executeCommand("flexSelect", target, args); }
	public void flexSelect(String target) throws Exception { executeCommand("flexSelect", target, ""); }
	public void flexRefreshIDToolTips(String target, String args) throws Exception { executeCommand("flexRefreshIDToolTips", target, args); }
	public void flexRefreshIDToolTips(String target) throws Exception { executeCommand("flexRefreshIDToolTips", target, ""); }
	public void flexRadioButton(String target, String args) throws Exception { executeCommand("flexRadioButton", target, args); }
	public void flexRadioButton(String target) throws Exception { executeCommand("flexRadioButton", target, ""); }
	public void flexProperty(String target, String args) throws Exception { executeCommand("flexProperty", target, args); }
	public void flexProperty(String target) throws Exception { executeCommand("flexProperty", target, ""); }
	public void flexMouseUp(String target, String args) throws Exception { executeCommand("flexMouseUp", target, args); }
	public void flexMouseUp(String target) throws Exception { executeCommand("flexMouseUp", target, ""); }
	public void flexMouseRollOver(String target, String args) throws Exception { executeCommand("flexMouseRollOver", target, args); }
	public void flexMouseRollOver(String target) throws Exception { executeCommand("flexMouseRollOver", target, ""); }
	public void flexMouseRollOut(String target, String args) throws Exception { executeCommand("flexMouseRollOut", target, args); }
	public void flexMouseRollOut(String target) throws Exception { executeCommand("flexMouseRollOut", target, ""); }
	public void flexMouseOver(String target, String args) throws Exception { executeCommand("flexMouseOver", target, args); }
	public void flexMouseOver(String target) throws Exception { executeCommand("flexMouseOver", target, ""); }
	public void flexMouseMove(String target, String args) throws Exception { executeCommand("flexMouseMove", target, args); }
	public void flexMouseMove(String target) throws Exception { executeCommand("flexMouseMove", target, ""); }
	public void flexMouseDown(String target, String args) throws Exception { executeCommand("flexMouseDown", target, args); }
	public void flexMouseDown(String target) throws Exception { executeCommand("flexMouseDown", target, ""); }
	public void flexDragTo(String target, String args) throws Exception { executeCommand("flexDragTo", target, args); }
	public void flexDragTo(String target) throws Exception { executeCommand("flexDragTo", target, ""); }
	public void flexDoubleClick(String target, String args) throws Exception { executeCommand("flexDoubleClick", target, args); }
	public void flexDoubleClick(String target) throws Exception { executeCommand("flexDoubleClick", target, ""); }
	public void flexDate(String target, String args) throws Exception { executeCommand("flexDate", target, args); }
	public void flexDate(String target) throws Exception { executeCommand("flexDate", target, ""); }
	public void flexClickMenuBarUIComponent(String target, String args) throws Exception { executeCommand("flexClickMenuBarUIComponent", target, args); }
	public void flexClickMenuBarUIComponent(String target) throws Exception { executeCommand("flexClickMenuBarUIComponent", target, ""); }
	public void flexClickDataGridUIComponent(String target, String args) throws Exception { executeCommand("flexClickDataGridUIComponent", target, args); }
	public void flexClickDataGridUIComponent(String target) throws Exception { executeCommand("flexClickDataGridUIComponent", target, ""); }
	public void flexClickDataGridItem(String target, String args) throws Exception { executeCommand("flexClickDataGridItem", target, args); }
	public void flexClickDataGridItem(String target) throws Exception { executeCommand("flexClickDataGridItem", target, ""); }
	public void flexClick(String target, String args) throws Exception { executeCommand("flexClick", target, args); }
	public void flexClick(String target) throws Exception { executeCommand("flexClick", target, ""); }
	public void flexCheckBox(String target, String args) throws Exception { executeCommand("flexCheckBox", target, args); }
	public void flexCheckBox(String target) throws Exception { executeCommand("flexCheckBox", target, ""); }
	public void flexAlertResponse(String target, String args) throws Exception { executeCommand("flexAlertResponse", target, args); }
	public void flexAlertResponse(String target) throws Exception { executeCommand("flexAlertResponse", target, ""); }
	public void flexAddSelectMatchingOnField(String target, String args) throws Exception { executeCommand("flexAddSelectMatchingOnField", target, args); }
	public void flexAddSelectMatchingOnField(String target) throws Exception { executeCommand("flexAddSelectMatchingOnField", target, ""); }
	public void flexAddSelectIndex(String target, String args) throws Exception { executeCommand("flexAddSelectIndex", target, args); }
	public void flexAddSelectIndex(String target) throws Exception { executeCommand("flexAddSelectIndex", target, ""); }
}

 