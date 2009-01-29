<%@ include file="/common/taglibs.jsp"%>
<%@ page
	import="org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	var offlineArea = "#offlinefileArea";
	var onlineArea = "#onlinefileArea";
	function deleteOfflineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOfflineFile.do"/>";
		$(offlineArea).load(
			url,
			{
				fileVersionId: fileVersionId,
				fileUuid: fileUuid, 
				sessionMapID: "${formBean.sessionMapID}"
			}
		);
	}
	function deleteOnlineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOnlineFile.do"/>";
		$(offlineArea).load(
				url,
				{
					fileVersionId: fileVersionId,
					fileUuid: fileUuid, 
					sessionMapID: "${formBean.sessionMapID}"
				}
		);
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
			<lams:STRUTS-textarea property="assessment.onlineInstructions" rows="3" cols="75" />
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
			<lams:STRUTS-textarea property="assessment.offlineInstructions" rows="3" cols="75" />
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

