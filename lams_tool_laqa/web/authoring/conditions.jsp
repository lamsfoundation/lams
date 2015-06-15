<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showConditionMessage(url) {
		var area=document.getElementById("conditionInputArea");
		if(area != null){
			area.style.width="650px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "conditionInputArea";
	}
	function hideConditionMessage(){
		var area=document.getElementById("conditionInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}

	function editCondition(orderId,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editCondition.do?orderId="/>" + orderId +"&sessionMapID="+sessionMapID;;
		showConditionMessage(url);
	}
	//The panel of taskList list panel
	var conditionListTargetDiv = "#conditionsArea";
	function deleteCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/removeCondition.do"/>";
	    var reqIDVar = new Date();
		$(conditionListTargetDiv).load(
			url,
			{
				orderId: orderId,
				sessionMapID: sessionMapID
			}
		);
	}
	
	function upCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/upCondition.do"/>";
	    var reqIDVar = new Date();
	    $(conditionListTargetDiv).load(
			url,
			{
				orderId: orderId,
				sessionMapID: sessionMapID
			}
		);
	}
	function downCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/downCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "orderId=" + orderId + "&sessionMapID="+sessionMapID;;
		$(conditionListTargetDiv).load(
			url,
			{
				orderId: orderId,
				sessionMapID: sessionMapID
			}
		);
	}

</script>

<!-- Conditions Tab Content -->

<div id="conditionsArea">
	<c:set var="sessionMapID" value="${formBean.httpSessionID}" />
	<%@ include file="/authoring/conditionList.jsp"%>
</div>

<p class="small-space-bottom">
	<a href="javascript:showConditionMessage('<html:rewrite page="/authoring/newConditionInit.do?sessionMapID=${formBean.httpSessionID}"/>');" class="button-add-item">
	<fmt:message key="label.authoring.conditions.add.condition" /></a> 
</p>


<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="conditionInputArea" name="conditionInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
