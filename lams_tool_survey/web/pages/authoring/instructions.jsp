<%@ include file="/common/taglibs.jsp"%>
<%@ page
	import="org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	var offlineArea = "offlinefileArea";
	var onlineArea = "onlinefileArea";
	function deleteOfflineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOfflineFile.do"/>";
	    var reqIDVar = new Date();
		var param = "sessionMapID=${formBean.sessionMapID}&fileUuid=" + fileUuid + "&fileVersionId="+ fileVersionId+"&reqID="+reqIDVar.getTime();
		offlineLoading();
	    var myAjax = new Ajax.Updater(
		    	offlineArea,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:offlineComplete,
		    		evalScripts:false
		    	}
	    );
	}
	function deleteOnlineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOnlineFile.do"/>";
	    var reqIDVar = new Date();
		var param = "sessionMapID=${formBean.sessionMapID}&fileUuid=" + fileUuid + "&fileVersionId="+ fileVersionId+"&reqID="+reqIDVar.getTime();
		onlineLoading();
	    var myAjax = new Ajax.Updater(
		    	onlineArea,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:onlineComplete,
		    		evalScripts:false
		    	}
	    );
	}

	function offlineLoading(){
		showBusy(offlineArea);
	}
	function offlineComplete(){
		hideBusy(offlineArea);
	}
	function onlineLoading(){
		showBusy(onlineArea);
	}
	function onlineComplete(){
		hideBusy(onlineArea);
	}
</script>
<!-- Instruction Tab Content -->
<table class="forms">
	<!-- Instructions Row -->
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.online.instruction"></fmt:message>
			</div>
			<html:textarea property="survey.onlineInstructions" rows="3"
				cols="75"></html:textarea>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="fileTypeFlag"
				value="<%=IToolContentHandler.TYPE_ONLINE%>" />
			<div id="onlinefileArea">
				<%@ include file="parts/instructionfilelist.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.online.file" />
			</div>
			<html:file property="onlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<a href="#" onclick="javascript:doUploadOnline();" class="button">
				<fmt:message key="label.authoring.upload.online.button" /> </a>
		</td>
	</tr>

	<tr>
		<td>
			<hr />
		</td>
	</tr>

	<!-- Offline Instructions -->
	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="label.authoring.offline.instruction"></fmt:message>
			</div>
			<html:textarea property="survey.offlineInstructions" rows="3"
				cols="75"></html:textarea>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="fileTypeFlag"
				value="<%=IToolContentHandler.TYPE_OFFLINE%>" />
			<div id="offlinefileArea">
				<%@ include file="parts/instructionfilelist.jsp"%>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="label.authoring.offline.file" />
			</div>
			<html:file property="offlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<a href="#" onclick="javascript:doUploadOffline();" class="button">
				<fmt:message key="label.authoring.upload.offline.button" /> </a>
		</td>
	</tr>
</table>

