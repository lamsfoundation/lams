<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/swfobject.js"></script>
<script type="text/javascript" src="includes/javascript/mindmap.resize.js"></script>
<script type="text/javascript">
//<![CDATA[
           
    <c:set var="MindmapUser">
		<c:out value="${currentMindmapUser}" escapeXml="true"/>
	</c:set>
	
	flashvars = { xml: "${mindmapContentPath}", user: "${MindmapUser}", dictionary: "${localizationPath}" }

	embedFlashObject(700, 525);

	$(window).resize(makeNice);
	
	function embedFlashObject(x, y)	{
		swfobject.embedSWF("${mindmapType}", "flashContent", x, y, "9.0.0", false, flashvars);
	}
	
	function setToolContentID()	{
		var toolContentID = document.getElementById("toolContentID");
		toolContentID.value = "${toolContentID}";
	}
	
	function setUserId() {
		var userId = document.getElementById("userId");
		userId.value = "${userDTO.uid}";
	}
	
	function setMindmapContent() {
		var mindmapContent = document.getElementById("mindmapContent");
		if(mindmapContent != null) {
			mindmapContent.value = document['flashContent'].getMindmap();
		}

		setUserId();
		setToolContentID();
	}

	function updateContent() {
		$.post("${get}", { dispatch: "${dispatch}", mindmapId: "${mindmapId}", userId: "${userId}", 
			content: document['flashContent'].getMindmap() } );
	}
//]]>
</script>

<html:form action="/monitoring" method="get">
	<html:hidden property="toolContentID" styleId="toolContentID" value="${toolContentID}" />
	<html:hidden property="contentFolderID" styleId="contentFolderID" value="${contentFolderID}" />
	
	<table class="table">
		<tr>
			<td colspan="2">
				<c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/>
			</td>
		</tr>
		<tr>
			<td class="field-name">
				<fmt:message key="label.mindmapEntry" />
			</td>
		</tr>
	</table>
	
	<center id="center12">
		<div id="flashContent">
			<fmt:message>message.enableFlashAuthorMonitor</fmt:message>
		</div>
	</center>
		<c:choose>
			<c:when test="${isMultiUserMode}">
			</c:when>
			<c:otherwise>
				<html:submit styleClass="btn btn-primary voffset10 loffset5 pull-right" styleId="saveButton" onclick="updateContent()">
					<fmt:message>button.save</fmt:message>
				</html:submit>	
			</c:otherwise>
		</c:choose>
	
		<html:button styleClass="btn btn-primary voffset10 pull-right" property="backButton" onclick="history.go(-1)">
			<fmt:message>button.back</fmt:message>
		</html:button>

		

</html:form>
