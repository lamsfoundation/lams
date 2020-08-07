<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c"%>

<%-- 
Include this jsp in your jqGrid page head to get some jqGrid functionality
 --%>
 
<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>

<script type="text/javascript">
	<%-- Add jqGrid internationalisation snippet --%>
	<c:import url="/jqGrid.i18n.jsp" context="/lams" />
		
	var popupWidth = 1280,
		popupHeight = 720;
		
	// launches a popup from the page
	function launchPopup(url,title) {
		var wd = null;
		if(wd && wd.open && !wd.closed){
			wd.close();
		}

		var left = ((screen.width / 2) - (popupWidth / 2));
		var top = ((screen.height / 2) - (popupHeight / 2));

		wd = window.open(url,title,'resizable,width='+popupWidth+',height='+popupHeight
				+',scrollbars'
				+ ",top=" + top + ",left=" + left);
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
		
	function fixPagerInCenter(pagername, numcolshift) {
		$('#'+pagername+'_right').css('display','inline');
		if ( numcolshift > 0 ) {
			var marginshift = - numcolshift * 12;
			$('#'+pagername+'_center table').css('margin-left', marginshift+'px');
		}
	}

	<%--  gradebook dialog windows	on the ipad do not update the grid width properly using setGridWidth. Calling this is 
	-- setting the grid to parentWidth-1 and the width of the parent to parentWidth+1, leading to growing width window
	-- that overflows the dialog window. Keep the main grids slightly smaller than their containers and all is well. 
	--%>
    function resizeJqgrid(jqgrids) {
        jqgrids.each(function(index) {
            var gridId = $(this).attr('id');
            var parent = jQuery('#gbox_' + gridId).parent();
            var gridParentWidth = parent.width();
            if ( parent.hasClass('grid-holder') ) {
                	gridParentWidth = gridParentWidth - 2;
            }
            jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
        });
    };


    <%-- Based on jqgrid internal functions --%>
    function displayCellErrorMessage(table, iRow, iCol, errorLabel, errorMessage, buttonText ) {
    		setTimeout(function () {
			try {
			    	var frozenRows = table.grid.fbRows,
					tr = table.rows[iRow];
			    	tr = frozenRows != null && frozenRows[0].cells.length > iCol ? frozenRows[tr.rowIndex] : tr;
			    var td = tr != null && tr.cells != null ? $(tr.cells[iCol]) : $(),
			    		rect = td[0].getBoundingClientRect();
			    $.jgrid.info_dialog.call(table, errorLabel, errorMessage, buttonText, {left:rect.left-200, top:rect.top});
			} catch (e) {
				alert(errorMessage);
			}
		}, 50);
	}
	
</script>
