<%@ include file="/common/taglibs.jsp"%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<table>
	<tr>
		<td width="10%" nowrap valign="top" class="field-name">
			<fmt:message key="label.authoring.title"/>:
		</td>
		<td>
			<c:out value="${content.title}" escapeXml="true"/>
		</td>
	</tr>
	<tr>
		<td width="10%" nowrap valign="top" class="field-name">
			<fmt:message key="label.authoring.instructions"/>:
		</td>
		<td>
			<c:out value="${content.instructions}" escapeXml="false"/>
		</td>
	</tr>
	
	<tr>
		<td colspan=2 class="align-right">
			<c:url  var="authoringUrl" value="/authoringStarter.do">
				<c:param name="toolContentID" value="${formBean.toolContentID}" />
				<c:param name="contentFolderID" value="${formBean.contentFolderID}" />
				<c:param name="mode" value="teacher" />
			</c:url>
			<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="button">
				<fmt:message key="label.edit"/>
			</html:link>
		</td>
	</tr>		 
</table>																								
		