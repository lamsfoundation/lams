<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the frame for the LAMS Community
	 */
	function showCommunity(url) {
		var area=document.getElementById("communityArea");
		if(area != null){
			area.style.width="640px";
			area.style.height="500px";
			area.src=url;
			area.style.display="block";
		}
	}
</script>
<iframe
	onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'; showCommunity('http://lamscommunity.org/lams/welcome');"
	id="communityArea" name="communityArea"
	style="width:0px;height:0px;border:0px;display:none" frameborder="no"
	scrolling="yes">
</iframe>
