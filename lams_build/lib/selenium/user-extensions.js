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
 
/**
 * The following is the ID for the Flex object on the html page.
 * Should be different depending on the application under test.
 * Use the command flexSetFlexObjID to set the command during runtime
 */

Selenium.prototype.getFlexObject =  function(){
	var obj = (this.browserbot.locateElementByXPath('//embed', this.browserbot.getDocument())) ? this.browserbot.locateElementByXPath('//embed', this.browserbot.getDocument()) : this.browserbot.locateElementByXPath('//object', this.browserbot.getDocument());
	return obj.id;
}

Selenium.prototype.flashObjectLocator = null;

Selenium.prototype.callFlexMethod = function (method, id, args) {
	
	if (this.flashObjectLocator === null){
		this.flashObjectLocator = "CloudWizard";
	}
	
	// the object that contains the exposed Flex functions
	var funcObj = null;
	// get the flash object
	var flashObj = selenium.browserbot.findElement(this.flashObjectLocator);

	// find object holding functions
	if((typeof(flashObj[method]) == 'function') || (typeof(flashObj[method]) == 'object'))
//		// for IE (will be the flash object itself)
		funcObj = flashObj;
//	else {
//		// Firefox (will be a child of the flash object)
//		for(var i = 0; i < flashObj.childNodes.length; i++) {
//			var tmpFuncObj = flashObj.childNodes[i];
//			if(typeof(tmpFuncObj) == 'function') {
//				funcObj = tmpFuncObj;
//				break;
//			}
//		}
//	}
	
	// throw a error to Selenium if the exposed function could not be found
	if(funcObj == null)
		throw new SeleniumError('Function ' + method + ' not found on the External Interface for the flash object ' + this.flashObjectLocator);

	return funcObj[method](id, args);
}

Selenium.prototype.doFlexSetFlexObjID = function(flasObjID) {
    if(null == flasObjID) throw new SeleniumError(flasObjID);
    this.flashObjectLocator = flasObjID;
};

Selenium.prototype.doAssertFlexAlertTextPresent = function(searchStr) {
	var retval = this.callFlexMethod('getFlexAlertTextPresent', searchStr);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doAssertFlexAlertTextNotPresent = function(searchStr) {
	var retval = this.callFlexMethod('getFlexAlertTextPresent', searchStr);
	if(retval == 'true') throw new SeleniumError('The string ' + searchStr + ' was found');
};

Selenium.prototype.doVerifyFlexAlertTextPresent = function(searchStr) {
	var retval = this.callFlexMethod('getFlexAlertTextPresent', searchStr);
	if(retval != 'true') LOG.error(retval);
};

Selenium.prototype.doVerifyFlexAlertTextNotPresent = function(searchStr) {
	var retval = this.callFlexMethod('getFlexAlertTextPresent', searchStr);
	if(retval == 'true') LOG.error('The string ' + searchStr + ' was found');
};

Selenium.prototype.getFlexASProperty = function(idProperty) {
	var targets = idProperty.split('.');
	return this.callFlexMethod('getFlexASProperty', targets[0], targets[1]);
};

Selenium.prototype.getFlexProperty = function(idProperty) {
	var ID = idProperty.split('.')[0];
	var property = idProperty.substring(idProperty.indexOf('.') + 1);
	return this.callFlexMethod('getFlexProperty', ID, property);
};

Selenium.prototype.doFlexAlertResponse = function(id, args) {
	var retval = this.callFlexMethod('doFlexAlertResponse', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexCheckBox = function(id, args) {
	var retval = this.callFlexMethod('doFlexCheckBox', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexClick = function(id, args) {
	var retval = this.callFlexMethod('doFlexClick', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexClickDataGridItem = function(id, args) {
	var retval = this.callFlexMethod('doFlexClickDataGridItem', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexClickDataGridUIComponent = function(id, args) {
	var retval = this.callFlexMethod('doFlexClickDataGridUIComponent', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexClickMenuBarUIComponent = function(id, args) {
	var retval = this.callFlexMethod('doFlexClickMenuBarUIComponent', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexDate = function(id, args) {
	var retval = this.callFlexMethod('doFlexDate', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexDoubleClick = function(id, args) {
	var retval = this.callFlexMethod('doFlexDoubleClick', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexDragTo = function(id, args) {
	var retval = this.callFlexMethod('doFlexDragTo', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseDown = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseDown', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseMove = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseMove', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseOver = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseOver', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseRollOut = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseRollOut', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseRollOver = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseRollOver', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexMouseUp = function(id, args) {
	var retval = this.callFlexMethod('doFlexMouseUp', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexProperty = function(id, args) {
	var retval = this.callFlexMethod('doFlexProperty', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexRadioButton = function(id, args) {
	var retval = this.callFlexMethod('doFlexRadioButton', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexSelect = function(id, args) {
	var retval = this.callFlexMethod('doFlexSelect', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexSelectIndex = function(id, args) {
	var retval = this.callFlexMethod('doFlexSelectIndex', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexStepper = function(id, args) {
	var retval = this.callFlexMethod('doFlexStepper', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexType = function(id, args) {
	var retval = this.callFlexMethod('doFlexType', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexTypeAppend = function(id, args) {
	var retval = this.callFlexMethod('doFlexTypeAppend', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexWaitForElement = function(id, args) {
	var retval = this.callFlexMethod('doFlexWaitForElement', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexWaitForElementVisible = function(id, args) {
	var retval = this.callFlexMethod('doFlexWaitForElementVisible', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.doFlexRefreshIDToolTips = function(id, args) {
	var retval = this.callFlexMethod('doFlexRefreshIDToolTips', id, args);
	if(retval != 'true') throw new SeleniumError(retval);
};

Selenium.prototype.getFlexDataGridUIComponentLabel = function(id) {
	return this.callFlexMethod('getFlexDataGridUIComponentLabel', id);
};

Selenium.prototype.getFlexAlertPresent = function(id) {
	return this.callFlexMethod('getFlexAlertPresent', id);
};

Selenium.prototype.getFlexAlertText = function(id) {
	return this.callFlexMethod('getFlexAlertText', id);
};

Selenium.prototype.getFlexAlertTextPresent = function(id) {
	return this.callFlexMethod('getFlexAlertTextPresent', id);
};

Selenium.prototype.getFlexCheckBoxChecked = function(id) {
	return this.callFlexMethod('getFlexCheckBoxChecked', id);
};

Selenium.prototype.getFlexComponentInfo = function(id) {
	return this.callFlexMethod('getFlexComponentInfo', id);
};

Selenium.prototype.getFlexDataGridCellText = function(id) {
	return this.callFlexMethod('getFlexDataGridCellText', id);
};

Selenium.prototype.getFlexDate = function(id) {
	return this.callFlexMethod('getFlexDate', id);
};

Selenium.prototype.getFlexEnabled = function(id) {
	return this.callFlexMethod('getFlexEnabled', id);
};

Selenium.prototype.getFlexErrorString = function(id) {
	return this.callFlexMethod('getFlexErrorString', id);
};

Selenium.prototype.getFlexExists = function(id) {
	return this.callFlexMethod('getFlexExists', id);
};

Selenium.prototype.getFlexGlobalPosition = function(id) {
	return this.callFlexMethod('getFlexGlobalPosition', id);
};

Selenium.prototype.getFlexNumeric = function(id) {
	return this.callFlexMethod('getFlexNumeric', id);
};

Selenium.prototype.getFlexParseInt = function(id) {
	return this.callFlexMethod('getFlexParseInt', id);
};

Selenium.prototype.getFlexRadioButton = function(id) {
	return this.callFlexMethod('getFlexRadioButton', id);
};

Selenium.prototype.getFlexSelection = function(id) {
	return this.callFlexMethod('getFlexSelection', id);
};

Selenium.prototype.getFlexSelectionIndex = function(id) {
	return this.callFlexMethod('getFlexSelectionIndex', id);
};

Selenium.prototype.getFlexStepper = function(id) {
	return this.callFlexMethod('getFlexStepper', id);
};

Selenium.prototype.getFlexText = function(id) {
	return this.callFlexMethod('getFlexText', id);
};

Selenium.prototype.getFlexTextPresent = function(id) {
	return this.callFlexMethod('getFlexTextPresent', id);
};

Selenium.prototype.getFlexVisible = function(id) {
	return this.callFlexMethod('getFlexVisible', id);
};
