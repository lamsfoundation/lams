<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>
<c:set var="tool"><lams:WebAppURL /></c:set>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/monitorToolSummaryAdvanced.js"></script>
<lams:JSImport src="includes/javascript/portrait.js" />
<script type="text/javascript">

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
		wd = window.open("<c:url value='/monitoring/viewAllMarks.do?toolSessionID='/>" + sessionId + "&sessionMapID=${sessionMapID}", 
				"mark", 'resizable, width=1152, height=648, scrollbars');
		wd.window.focus();
	}
	
	var messageTargetDiv = "messageArea";
	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring/releaseMarks.do"/>";
	    var reqIDVar = new Date();
		var param = "toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		messageLoading();
		$("#"+messageTargetDiv).load(
				url,
				param,
				function() {
					messageComplete();
				}
		);
	}	
	function messageLoading(){
		if($(messageTargetDiv+"_Busy") != null){
			$(messageTargetDiv+"_Busy").show();
		}		
	}
	function messageComplete(){
		if($(messageTargetDiv+"_Busy") != null){
			$(messageTargetDiv+"_Busy").hide();
		}
	}	
</script>

<div class="panel">
	<h4>
	    <c:out value="${spreadsheet.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${spreadsheet.instructions}" escapeXml="false"/>
	</div>
	
	<!--For release marks feature-->
	<i class="fa fa-refresh fa-spin fa-fw" style="display:none" id="message-area-busy"></i>
	<div id="message-area"></div> 

</div>

<%-- Summary list  --%>

<i class="fa fa-refresh fa-spin fa-fw" style="display:none" id="messageArea_Busy"></i>
<span id="messageArea"></span>

<lams:errors/>

<div id="summariesArea">
	<%@ include file="/pages/monitoring/parts/summarylist.jsp"%>
</div>

<%@ include file="parts/advanceoptions.jsp"%>