<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<style type="text/css">
	iframe {
	overflow-x: hidden;
	overflow-y: scroll;
	}
</style> 
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jqgrid/js/jquery-1.2.6.pack.js"></script>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the frame for the LAMS Community
	 */
	function showCommunity(url) {
		var area=document.getElementById("communityArea");
		if(area != null){
			area.style.width="100%";
			area.style.height="450px";
			area.src=url;
			area.style.display="block";
		}
	}
	
	jQuery(document).ready(function(){
		showCommunity('${comLoginUrl}');
	});
</script>
<iframe
	id="communityArea" name="communityArea"
	style="width:0px;height:0px;border:0px;display:none" frameborder="no"
>
</iframe>
