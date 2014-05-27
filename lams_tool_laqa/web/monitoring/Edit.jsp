<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<%@ include file="/common/messages.jsp"%>

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
		</table>																								
		