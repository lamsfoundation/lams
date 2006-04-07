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
	<!---------------------------Instruction Tab Content ------------------------>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td NOWRAP width="700">
				<lams:SetEditor id="Resource.onlineInstructions" text="${formBean.resource.onlineInstructions}"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				<c:set var="onlineFileList" value="${formBean.onlineFileList}"/>
				<div id="onlinefileArea">
					<%@ include file="parts/onlinefilelist.jsp" %>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="onlineFile" styleClass="buttonStyle">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:button onclick="javascript:doUploadOnline();" property="uploadOnlineSubmit" styleClass="buttonStyle">
					<fmt:message key="label.authoring.upload.online.button" />
				</html:button>
			</td>
		</tr>	
		<!------------Offline Instructions ----------------------->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td NOWRAP width="700">
			<lams:SetEditor id="Resource.offlineInstructions" text="${formBean.resource.offlineInstructions}"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				<c:set var="offlineFileList" value="${formBean.offlineFileList}"/>
				<div id="offlinefileArea">
					<%@ include file="parts/offlinefilelist.jsp" %>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="offlineFile" styleClass="buttonStyle">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:button onclick="javascript:doUploadOffline();" property="uploadOfflineSubmit" styleClass="buttonStyle">
					<fmt:message key="label.authoring.upload.offline.button" />
				</html:button>
			</td>
		</tr>			
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>