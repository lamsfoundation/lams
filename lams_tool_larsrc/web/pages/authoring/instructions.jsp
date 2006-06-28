<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<script lang="javascript">
<!-- Common Javascript functions for LAMS -->
	var offlineArea = "offlinefileArea";
	var onlineArea = "onlinefileArea";
	function deleteOfflineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOfflineFile.do"/>";
	    var reqIDVar = new Date();
		var param = "fileUuid=" + fileUuid + "&fileVersionId="+ fileVersionId+"&reqID="+reqIDVar.getTime();
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
		var param = "fileUuid=" + fileUuid + "&fileVersionId="+ fileVersionId+"&reqID="+reqIDVar.getTime();
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
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td colspan="2">
				<lams:SetEditor id="Resource.onlineInstructions" text="${formBean.resource.onlineInstructions}" key="label.authoring.online.instruction" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="onlineFileList" value="${formBean.onlineFileList}"/>
				<div id="onlinefileArea">
					<%@ include file="parts/onlinefilelist.jsp" %>
				</div>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="onlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<a href="#" onclick="javascript:doUploadOnline();" class="button">
					<fmt:message key="label.authoring.upload.online.button" />
				</a>
			</td>
		</tr>	
		<!-- Offline Instructions -->
		<tr>
			<td colspan="2">
			<lams:SetEditor id="Resource.offlineInstructions" text="${formBean.resource.offlineInstructions}" key="label.authoring.offline.instruction"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="offlineFileList" value="${formBean.offlineFileList}"/>
				<div id="offlinefileArea">
					<%@ include file="parts/offlinefilelist.jsp" %>
				</div>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td valign="top">
				<html:file property="offlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<a href="#" onclick="javascript:doUploadOffline();" class="button">
					<fmt:message key="label.authoring.upload.offline.button" />
				</a>
				<p>&nbsp;</p>
			</td>
		</tr>			
		</table>