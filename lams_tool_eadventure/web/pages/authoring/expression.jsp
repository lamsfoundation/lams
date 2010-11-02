<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
		function showExpressionMessage(url) {
			var area=document.getElementById("expressionInputArea");
			if(area != null){
				area.style.width="650px";
				area.style.height="100%";
				area.src=url;
				area.style.display="block";
			}
			var elem = document.getElementById("saveCancelButtonsExpression");
			if (elem != null) {
				elem.style.display="none";
			}
			
			location.hash = "expressionInputArea";
			
			
		}
		function hideExpressionMessage(){
			var area=document.getElementById("expressionInputArea");
			if(area != null){
				area.style.width="0px";
				area.style.height="0px";
				area.style.display="none";
			}
			var elem = document.getElementById("saveCancelButtonsExpression");
			if (elem != null) {
				elem.style.display="block";
			}
		}
		function changeFirsOp(url){
		var area=document.getElementById("editExpression");
			if(area != null){
				area.src=url;
			}
		}
		function changeSecondOp(){
		var check = document.getElementById("check");
		if (check.checked){
			document.getElementById("selectVar").style.display="";
			document.getElementById("introduceVar").style.display="none";
		}
		else {
			document.getElementById("selectVar").style.display="none";
			document.getElementById("introduceVar").style.display="";
		}
		}

	
		</script>	
<div id="expressionArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/expressionlist.jsp"%>
</div>

<p class="small-space-bottom">
	<a href="javascript:showExpressionMessage('<html:rewrite page="/authoring/newExpressionInit.do?sessionMapID=${formBean.sessionMapID}"/>');" class="button-add-item">
	<fmt:message key="label.authoring.conditions.add.expression" /></a> 
</p>

<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="expressionInputArea" name="expressionInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>