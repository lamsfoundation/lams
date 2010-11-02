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

	function editCondition(position,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editCondition.do?position="/>" + position +"&sessionMapID="+sessionMapID;;
		showConditionMessage(url);
	}
	
	var conditionListTargetDiv = "conditionsArea";
	function deleteCondition(position,sessionMapID){
		var url = "<c:url value="/authoring/removeCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "position=" + position  +"&sessionMapID="+sessionMapID;;
		deleteConditionLoading();
	    var myAjax = new Ajax.Updater(
		    	conditionListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteConditionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	
	
	function deleteConditionLoading(){
		showBusy(conditionListTargetDiv);
	}
	function deleteConditionComplete(){
		hideBusy(conditionListTargetDiv);
		
	}
	
		function upCondition(position,sessionMapID){
		var url = "<c:url value="/authoring/upCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "position=" + position + "&sessionMapID="+sessionMapID;;
		deleteConditionLoading();
	    var myAjax = new Ajax.Updater(
		    	conditionListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteConditionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function downCondition(position,sessionMapID){
		var url = "<c:url value="/authoring/downCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "position=" + position + "&sessionMapID="+sessionMapID;;
		deleteConditionLoading();
	    var myAjax = new Ajax.Updater(
		    	conditionListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteConditionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	

</script>

<!-- Conditions Tab Content -->




<div id="conditionsArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/conditionlist.jsp"%>
</div>





	<p class="small-space-bottom" id="addConditionButton">
		<a href="javascript:showConditionMessage('<html:rewrite page="/authoring/newConditionInit.do?sessionMapID=${formBean.sessionMapID}"/>');" class="button-add-item">
		<fmt:message key="label.authoring.conditions.add.condition" /></a> 
	</p>
	<p id="addConditionMessage" style="display:none">
		<fmt:message key="label.authoring.conditions.can.not.add" />
	<p>


<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${!sessionMap.hasFile}">
	<script lang="javascript">
		 document.getElementById("addConditionButton").style.display = "none";
		 document.getElementById("addConditionMessage").style.display = "block";
	</script>
</c:if>


<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="conditionInputArea" name="conditionInputArea"
		style="width:0px;height:0px;border:0px;display:none;overflow-x:hidden" frameborder="no"
		scrolling="yes">
	</iframe>
</p>