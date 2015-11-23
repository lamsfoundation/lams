<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>
<c:set var="tool"><lams:WebAppURL /></c:set>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript">
	function showMessage(url) {
		var area=document.getElementById("marksInputArea");
		if(area != null){
			area.style.width="95%";
			area.src=url;
			area.style.display="block";
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
	}
	
	function editMark(userUid){
		var url = "<c:url value="/monitoring/editMark.do?userUid="/>" + userUid +"&toolContentID=" + ${param.toolContentID} + "&sessionMapID=" + "${sessionMapID}";
		showMessage(url);
	}
	
	function updateMarkAfterSaving(userUid, mark) {
		var span = document.getElementById('mark'+userUid);
		while( span.firstChild ) {
		    span.removeChild( span.firstChild );
		}
		span.appendChild( document.createTextNode(mark) );
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

<h1>
	<c:out value="${spreadsheet.title}" escapeXml="true"/>
</h1>

<div class="instructions space-top">
	<c:out value="${spreadsheet.instructions}" escapeXml="false"/>
</div>

<%-- Summary list  --%>

<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
<span id="messageArea"></span>

<%@ include file="/common/messages.jsp"%>

<div id="summariesArea">
	<%@ include file="/pages/monitoring/parts/summarylist.jsp"%>
</div>

<c:if test="${spreadsheet.markingEnabled}">	
	<p>
		<iframe
			onload="javascript:this.style.height=eval(this.contentWindow.document.body.scrollHeight+220)+'px'"
			id="marksInputArea" name="marksInputArea"
			style="width:0px;height:0px;border:0px;display:none" frameborder="no"
			scrolling="no">
		</iframe>
	</p>
</c:if>

<%@ include file="parts/advanceoptions.jsp"%>
