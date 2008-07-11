<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script lang="javascript">
	function showMessage(url) {
		var area=document.getElementById("marksInputArea");
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
		location.hash = "marksInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("marksInputArea");
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
	
	function editMark(userUid){
		var url = "<c:url value="/monitoring/editMark.do?userUid="/>" + userUid +"&toolContentID=" + ${param.toolContentID} + "&sessionMapID=" + "${sessionMapID}";
		showMessage(url);
	}
	
	function downloadMarks(sessionId){
		var url = "<c:url value="/monitoring/downloadMarks.do"/>";
	    var reqIDVar = new Date();
		var param = "?toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href = url;
	}	
	
	function viewAllMarks(sessionId){
		var wd = null;
		if(wd && wd.open && !wd.closed){
			wd.close();
		}
		wd = window.open("<c:url value='/monitoring/viewAllMarks.do?toolSessionID='/>" + sessionId, "mark", 'resizable, width=796, height=570, scrollbars');
		wd.window.focus();
	}
	
	var messageTargetDiv = "messageArea";
	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring/releaseMarks.do"/>";
	    var reqIDVar = new Date();
		var param = "toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		messageLoading();
	    var myAjax = new Ajax.Updater(
		    	messageTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:messageComplete,
		    		evalScripts:true
		    	}
	    );
		
	}	
	function messageLoading(){
		if($(messageTargetDiv+"_Busy") != null){
			Element.show(messageTargetDiv+"_Busy");
		}		
	}
	function messageComplete(){
		if($(messageTargetDiv+"_Busy") != null){
			Element.hide(messageTargetDiv+"_Busy");
		}
	}	
</script>


<%-- Overall TaskList information  --%>

<div class="info space-bottom" align="right" style="position:relative; right:40px; top:15px;">
	<fmt:message key="label.monitoring.summary.lock.when.finished" />
	<c:choose>
		<c:when test="${spreadsheet.lockWhenFinished}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>
	 						
	<fmt:message key="label.monitoring.summary.individual.spreadsheets" /> 
	<c:choose>
		<c:when test="${spreadsheet.learnerAllowedToSave}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>	
						
	<fmt:message key="label.monitoring.summary.marking.enabled" />
	<c:choose>
		<c:when test="${spreadsheet.markingEnabled}"> 
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>	
						
	<fmt:message key="label.monitoring.summary.notebook.reflection" />
	<c:choose>
		<c:when test="${spreadsheet.reflectOnActivity}">
			On
		</c:when>
		<c:otherwise>
			Off
		</c:otherwise>
	</c:choose>	
	<br>
</div>

<%-- Summary list  --%>

<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
<span id="messageArea"></span>

<%@ include file="/common/messages.jsp"%>

<div id="summariesArea">
	<%@ include file="/pages/monitoring/summarylist.jsp"%>
</div>

<c:if test="${spreadsheet.markingEnabled}">	
	<p>
		<iframe
			onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
			id="marksInputArea" name="marksInputArea"
			style="width:0px;height:0px;border:0px;display:none" frameborder="no"
			scrolling="no">
		</iframe>
	</p>
</c:if>
