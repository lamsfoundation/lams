<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<script lang="javascript">
<!-- Common Javascript functions for LAMS -->
	var offlineArea = "offlinefileArea";
	var onlineArea = "onlinefileArea";
	function deleteOfflineFile(fileUuid,fileVersionId){
		var url = "<c:url value="/authoring/deleteOffline.do"/>";
	    var reqIDVar = new Date();
		var param = "sessionMapID=${formBean.sessionMapID}&uuID=" + fileUuid + "&versionID="+ fileVersionId+"&reqID="+reqIDVar.getTime();
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
		var url = "<c:url value="/authoring/deleteOnline.do"/>";
	    var reqIDVar = new Date();
		var param = "sessionMapID=${formBean.sessionMapID}&uuID=" + fileUuid + "&versionID="+ fileVersionId+"&reqID="+reqIDVar.getTime();
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
				<lams:SetEditor id="forum.onlineInstructions" small="true" text="${formBean.forum.onlineInstructions}" key="label.authoring.online.instruction" />
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
			<td nowrap="nowrap" class="field-name">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td>
				<html:file property="onlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<a href="#" onclick="javascript:doSubmit('uploadOnline');" class="button">
					<fmt:message key="label.authoring.upload.online.button" />
				</a>
			</td>
		</tr>	
		<!-- Offline Instructions -->
		<tr>
			<td colspan="2">
			<lams:SetEditor id="forum.offlineInstructions" small="true"  text="${formBean.forum.offlineInstructions}" key="label.authoring.offline.instruction"/>
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
			<td nowrap="nowrap" valign="top" class="field-name">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td valign="top">
				<html:file property="offlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<a href="#" onclick="javascript:doSubmit('uploadOffline');" class="button">
					<fmt:message key="label.authoring.upload.offline.button" />
				</a>
				<p>&nbsp;</p>
			</td>
		</tr>			
		</table>
		