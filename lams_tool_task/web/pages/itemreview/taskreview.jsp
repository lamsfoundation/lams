<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	var onlineArea = "onlinefileArea";

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

	function onlineLoading(){
		showBusy(onlineArea);
	}
	function onlineComplete(){
		hideBusy(onlineArea);
	}
</script>



<lams:html>
	<lams:head>
		<title>${instructions.title}</title>

		<%@ include file="/common/header.jsp"%>

	</lams:head>
	<body>

	<div id="content">
		<h1>
			${taskListItem.title}
		</h1>

		<p>
			${taskListItem.instructions}
		</p>
	</div>


<table class="forms">


	<tr>
		<td>

			<hr />
		</td>
	</tr>


	<!-- Online Instructions -->
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.online.instruction"></fmt:message>
			</div>
			<html:textarea property="taskList.onlineInstructions" rows="3"
				cols="75"></html:textarea>
		</td>
	</tr>
	<tr>
		<td>
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

</table>




	</body>
</lams:html>
