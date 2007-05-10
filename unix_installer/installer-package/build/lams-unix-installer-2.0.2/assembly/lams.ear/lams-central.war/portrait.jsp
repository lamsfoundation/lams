<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<html:form action="/saveportrait.do" method="post"
	enctype="multipart/form-data">
	<html:hidden name="PortraitActionForm" property="portraitUuid" />


	<div style="clear:both"></div>

	<h2 class="small-space-top"><fmt:message
		key="title.portrait.change.screen" /></h2>

	<div class="shading-bg"><logic:messagesPresent>
		<p class="warning"><html:errors /></p>
	</logic:messagesPresent>

	<table>
		<tr>
			<td class="align-right" valign="top"><fmt:message
				key="label.portrait.current" />:</td>
			<td><logic:notEqual name="PortraitActionForm"
				property="portraitUuid" value="0">
				<img class="img-border"
					src="/lams/download/?uuid=<bean:write name="PortraitActionForm" property="portraitUuid" />&preferDownload=false" />
			</logic:notEqual> <logic:equal name="PortraitActionForm" property="portraitUuid"
				value="0">
				<c:set var="lams">
					<lams:LAMSURL />
				</c:set>
				<em><fmt:message key="msg.portrait.none" /></em>
				
			</logic:equal></td>
		</tr>
		<tr>
			<td class="align-right"><fmt:message key="label.portrait.upload" />:</td>
			<td><html:file property="file" /></td>
		</tr>
		<tr>
		<td>&nbsp;</td>
			<td>
			<html:submit styleClass="button"><fmt:message key="button.save" />	
			</html:submit>  
			<html:cancel styleClass="button"><fmt:message key="button.cancel" />
			</html:cancel>
			
			</td>
		</tr>
	</table>

	</div>

	<p> <fmt:message key="msg.portrait.resized" />  </p>
</html:form>
