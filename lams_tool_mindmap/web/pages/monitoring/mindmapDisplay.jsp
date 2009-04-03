<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="includes/javascript/swfobject.js"></script>

<script type="text/javascript">
//<![CDATA[
	flashvars = { xml: "${mindmapContentPath}", user: "${currentMindmapUser}", dictionary: "${localizationPath}" }
	
	embedFlashObject();
	
	function getFlashMovie(movieName) {
		var isIE = navigator.appName.indexOf("Microsoft") != -1;
		return (isIE) ? window[movieName] : document[movieName];
	}
	
	function embedFlashObject()
	{
		swfobject.embedSWF("${mindmapType}", "flashContent", "500", "375", "9.0.0", false, flashvars);
	}
	
	function setToolContentID()
	{
		var toolContentID = document.getElementById("toolContentID");
		toolContentID.value = "${toolContentID}";
	}
	
	function setUserId()
	{
		var userId = document.getElementById("userId");
		userId.value = "${userDTO.uid}";
	}
	
	function setMindmapContent()
	{
		var mindmapContent = document.getElementById("mindmapContent");
		if(mindmapContent != null) {
			mindmapContent.value = getFlashMovie('flashContent').getMindmap();
		}

		setUserId();
		setToolContentID();
	}
//]]>
</script>

<html:form action="/monitoring" method="post" onsubmit="setMindmapContent();">
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="mindmapContent" styleId="mindmapContent" />
	<html:hidden property="userId" styleId="userId" />
	<html:hidden property="toolContentID" styleId="toolContentID" />
	
	<table>
		<tr>
			<td colspan="2">
				<h2>
					${userDTO.firstName} ${userDTO.lastName}
				</h2>
			</td>
		</tr>
		<tr>
			<td class="field-name">
				<fmt:message key="label.mindmapEntry" />
			</td>
			<td align="center">
				<div id="flashContent">
					<fmt:message>message.enableJavaScript</fmt:message>
				</div>
			</td>
		</tr>
	</table>
	
	<div class="space-bottom-top align-right">
		<html:button styleClass="button" property="backButton" onclick="history.go(-1)">
			<fmt:message>button.back</fmt:message>
		</html:button>
		
		<html:submit styleClass="button" styleId="saveButton">
			<fmt:message>button.save</fmt:message>
		</html:submit>
	</div>

</html:form>
