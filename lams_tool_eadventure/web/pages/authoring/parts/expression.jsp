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
			var win = window.top ? window.top : window;
			win.document.location.hash = "expressionInputArea";
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
		
		function editExpression(position,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editExpression.do?positionExpression="/>" + position +"&sessionMapID="+sessionMapID;;
		showExpressionMessage(url);
	}
	
	var expressionListTargetDiv = "expressionArea";
	
	function deleteExpression(position,sessionMapID){
		var url = "<c:url value="/authoring/removeExpression.do"/>";
	    var reqIDVar = new Date();
		var param = "positionExpression=" + position  +"&sessionMapID="+sessionMapID;;
	    var myAjax = new Ajax.Request(
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete: function(resp){
		    			var obj = window.document.getElementById('conditionInputArea');
		    			if (!obj && window.parent) {
		    				 obj = window.parent.document.getElementById('conditionInputArea');
		    			}  
		    			if (!obj) {
		    				obj = window.top.document.getElementById('conditionInputArea');
		    			}
		    			obj.contentDocument.getElementById("expressionArea").innerHTML  = resp.responseText;
		    			obj.contentWindow.hideExpressionMessage();
					},
		    		evalJS:true
		    	}
	    );
		
		
	}
	
	function deleteExpressionLoading(){
		showBusy(expressionListTargetDiv);
	}
	function deleteExpressionComplete(){
		hideBusy(expressionListTargetDiv);
	}
	
	
		function upExpression(position,sessionMapID){
		var url = "<c:url value="/authoring/upExpression.do"/>";
	    var reqIDVar = new Date();
		var param = "positionExpression=" + position + "&sessionMapID="+sessionMapID;;
	     var myAjax = new Ajax.Request(
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete: function(resp){
		    			var obj = window.document.getElementById('conditionInputArea');
		    			if (!obj && window.parent) {
		    				 obj = window.parent.document.getElementById('conditionInputArea');
		    			}  
		    			if (!obj) {
		    				obj = window.top.document.getElementById('conditionInputArea');
		    			}
						obj.contentDocument.getElementById("expressionArea").innerHTML  = resp.responseText;
					},
		    		evalJS:true
		    	}
	    );
	}
	function downExpression(position,sessionMapID){
		var url = "<c:url value="/authoring/downExpression.do"/>";
	    var reqIDVar = new Date();
		var param = "positionExpression=" + position + "&sessionMapID="+sessionMapID;;
		deleteExpressionLoading();
	    var myAjax = new Ajax.Request(
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete: function(resp){
		    			var obj = window.document.getElementById('conditionInputArea');
		    			if (!obj && window.parent) {
		    				 obj = window.parent.document.getElementById('conditionInputArea');
		    			}  
		    			if (!obj) {
		    				obj = window.top.document.getElementById('conditionInputArea');
		    			}
						obj.contentDocument.getElementById("expressionArea").innerHTML  = resp.responseText;
					},
		    		evalJS:true
		    	}
	    );
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