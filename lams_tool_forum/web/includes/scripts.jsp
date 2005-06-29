<%@ page language="java" contentType="application/x-javascript" %>
<%@ include file="/includes/taglibs.jsp" %>
<!--
/**
 ********************************************************************
 * Javascript functions required by various modules on the site.
 ********************************************************************
 */

	// determine the screen width and height
	var screenW = 640, screenH = 480;
	if (parseInt(navigator.appVersion)>3) {
		screenW = screen.width;
		screenH = screen.height;
	} else if (navigator.appName == "Netscape" 
			&& parseInt(navigator.appVersion)==3 && navigator.javaEnabled()) {
		var jToolkit = java.awt.Toolkit.getDefaultToolkit();
		var jScreenSize = jToolkit.getScreenSize();
		screenW = jScreenSize.width;
		screenH = jScreenSize.height;
	}
	
	var popupCfg = new Object();
	popupCfg["product_type"] = new Array(300, 180, 100);
	popupCfg["measurement_unit"] = new Array(200, 100, 100);
	popupCfg["factory"] = new Array(300, 180, 100);
	popupCfg["product_group"] = new Array(300, 200, 100);
	popupCfg["tax_code"] = new Array(200, 200, 100);



	/**
	 * Launches the popup for code lookup.
	 */
	function launchPopup(formName, fieldName, type) {
		WIDTH = 0;
		HEIGHT = 1;
		CODE_FRAME_SIZE = 2;
		var url = '<html:rewrite page="/reference/index.do" />';
		url += '?type=' + type
			+ '&formName=' + formName
			+ '&fieldName=' + fieldName
			+ '&titleKey=' + ''
			+ '&leftFrameSize=' + popupCfg[type][CODE_FRAME_SIZE];
		width = popupCfg[type][WIDTH];
		height = popupCfg[type][HEIGHT];
		screenX = screenW - width - 10;
		screenPos = 'screenX=' + screenX + ',screenY=0,top=0,left=' + screenX;
		win = window.open(url, 'popup', 
			'width=' + width + ',height=' + height + ',scrollbars,toolbars=no,resizable,'
			+ screenPos);
		win.window.focus();
	}

	/* 
	 * added this onSubmit function makes sure all the 
	 * values of targetList are selected when the form is submitted otherwise 
	 * only the selected values are taken 
	 */ 
	function selectAllItems(form, listName) {
		var theList = form[listName];
		total = theList.options.length; 	
		for (count = 0; count < total; ++count) {
			theList.options[count].selected = true;
		}
	}

	function moveItems(formname, sourceList, targetList) {
		var theSourceList = document.forms[formname][sourceList];
        var theTargetList = document.forms[formname][targetList];
		// Run through this list twice, once to add, once to delete
        total = theSourceList.options.length; 
        for (count = 0; count < total; ++count) {
			current_target = theSourceList.options[count]; 
			if (current_target.selected) {
				// remove the object from the current selectedaccountlist
				target_name = current_target.text;
				target_id   = current_target.value; 

				// Create a new option
				option = new Option(target_name, target_id, false, false);

				// add it to the possible selectedaccountlist
				theTargetList.options[theTargetList.options.length] = option;
			}
		}

		for (count = theSourceList.options.length -1;  count >= 0; --count) {
			if (theSourceList.options[count].selected) {
				theSourceList.options[count] = null;
			}
		}
	}

	function moveAllItems(formname, sourceList, targetList) {
		var theSourceList = document.forms[formname][sourceList];
        var theTargetList = document.forms[formname][targetList];
        // Run through the options and remove all selections
        total = theSourceList.options.length;
        for (count = 0; count < total; ++count) {
			current_target = theSourceList.options[count];
                    
            // remove the object from the current selectedaccountlist
            target_name = current_target.text;
            target_id   = current_target.value; 
            
			// Create a new option
            option = new Option(target_name, target_id, false, false); 
            
            // add it to the possible selectedaccountlist
            theTargetList.options[theTargetList.options.length] = option;
        }

		// Remove old objects
		theSourceList.options.length = 0; 
	}
//-->
