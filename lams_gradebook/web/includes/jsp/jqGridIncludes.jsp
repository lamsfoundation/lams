<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c"%>

<!-- 
Include this jsp in your jqGrid page head to get some jqGrid functionaility
 -->
 
<!-- Do not include these if used within tabs such as monitoring as redeclaring the jquery and jqgrid breaks the tabs.  -->
<!-- Need both stylesheets or we lose the grey headers on the grids. -->
<c:if test="${!isInTabs}"> 
<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
<link type="text/css" href="<lams:LAMSURL />css/jquery.jqGrid.css" rel="stylesheet" />
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.js"></script>
</c:if>

<style>
	.tooltip{
	    position:absolute;
	    z-index:999;
	    left:-9999px;
	    background-color:#dedede;
	    padding:5px;
	    border:1px solid #fff;
	    width:250px;
	    font-size: 1.1em;
	}
</style>

<script type="text/javascript">
		
	// JQGRID LANGUAGE ENTRIES ---------------------------------------------
	
	// editing entries
	$.jgrid.edit = {
	    addCaption: "Add Record",
	    editCaption: "Edit Record",
	    bSubmit: "Submit",
	    bCancel: "Cancel",
		bClose: "Close",
	    processData: "Processing...",
	    msg: {
	        required:"Field is required",
	        number:"<fmt:message key="gradebook.function.error.enterNumber"/>",
	        minValue:"value must be greater than or equal to ",
	        maxValue:"value must be less than or equal to",
	        email: "is not a valid e-mail",
	        integer: "Please, enter valid integer value",
			date: "Please, enter valid date value"
	    }
	};
	
	// search entries
	$.jgrid.search = {
		    caption: "<fmt:message key="gradebook.function.search.title"/>",
		    Find: "<fmt:message key="label.find"/>",
		    Reset: "<fmt:message key="label.reset"/>",
		    odata : [
		    	"<fmt:message key="gradebook.function.search.equalTo"/>", 
		    	"<fmt:message key="gradebook.function.search.notEqualTo"/>", 
		    	'less', 
		    	'less or equal',
		    	'greater',
		    	'greater or equal', 
		    	"<fmt:message key="gradebook.function.search.startsWith"/>",
		    	"<fmt:message key="gradebook.function.search.endsWith"/>",
		    	"<fmt:message key="gradebook.function.search.contains"/>" 
		    ]
	};
	
	// setcolumns module
	$.jgrid.col = {
	    caption: "<fmt:message key="gradebook.function.window.showColumns"/>",
	    bSubmit: "<fmt:message key="label.ok"/>",
	    bCancel: "<fmt:message key="label.cancel"/>"
	};
	
	// ---------------------------------------------------------------------
	
	
	// Applies tooltips to a jqgrid
	function toolTip(gRowObject) {
		var my_tooltip = $('#tooltip'); // Div created for tooltip
		gRowObject.css({ 
			cursor: 'pointer' 
        }).mouseover(function(kmouse){ 
               if (checkCell(kmouse)) {
               	showToolTip(my_tooltip, kmouse);
               	//setTimeout(function(){showToolTip(my_tooltip, kmouse);}, 1000);
               }
        }).mousemove(function(kmouse){ 
               if (checkCell(kmouse)) {
               	moveToolTipBox(my_tooltip, kmouse);
               	//setTimeout(function(){moveToolTipBox(my_tooltip, kmouse);}, 1000);
               }
           }).mouseout(function(){ 
               my_tooltip.stop().fadeOut(400); 
       	}).css({cursor:'pointer'}).click(function(e){
               my_tooltip.stop().fadeOut(400); 
       	});
	}
	
	// Check a cell before opening tooltip to make sure empty or invalid cells do not display
	function checkCell(kmouse) {
		var cell = $(kmouse.target).html();
		if (cell != null && cell !="" && cell !="&nbsp;" && cell != "-" && cell.charAt(0) != '<') {
			return true;
		}
		return false;
	}
	
	// Shows a tootip and applies the cell value
	function showToolTip(my_tooltip, kmouse) {
		
		var cell = $(kmouse.target).html();
		my_tooltip.html(cell);
              my_tooltip.css({ 
              	opacity: 0.85, 
              	display: "none" 
          	}).stop().fadeIn(400);
	}
	
	// Moves the tooltip box so it is not in the way of the mouse
	function moveToolTipBox(my_tooltip, kmouse) {
		var border_top = $(window).scrollTop(); 
        var border_right = $(window).width(); 
        var left_pos; 
        var top_pos; 
        var offset = 15;  
        if (border_right - (offset * 2) >= my_tooltip.width() + kmouse.pageX)  
        { 
        	left_pos = kmouse.pageX + offset;  
        }  
        else  
        { 
       		left_pos = border_right - my_tooltip.width() - offset; 
        }  
        if (border_top + (offset * 2) >= kmouse.pageY - my_tooltip.height())  
        { 
        	top_pos = border_top + offset;  
        }  
        else  
        { 
        	top_pos = kmouse.pageY - my_tooltip.height() - offset; 
        } 
        my_tooltip.css({ 
	        left: left_pos, 
	        top: top_pos 
        }); 
	}
	
	// launches a popup from the page
	function launchPopup(url,title,width,height) {
		var wd = null;
		if(wd && wd.open && !wd.closed){
			wd.close();
		}
		//wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
		wd = window.open(url,title,'resizable,width='+width+',height='+height+',scrollbars');
		wd.window.focus();
	}
	
	// Removes html tags from a string
	function removeHTMLTags(string) {
	 	var strTagStrippedText = string.replace(/<\/?[^>]+(>|$)/g, "");
	 	return strTagStrippedText;
	}
	
	function trim(str) {
		str = str.replace(/^\s+|\s+$/g, '');
		return str;
	}
		
</script>
