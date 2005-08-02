<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/authoring" target="_self" enctype="multipart/form-data" >
<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center">
	<html:submit property="method" styleClass="button">
	
				<fmt:message key="button.basic" />
		</html:submit>
	
		<html:submit property="method" disabled="true" styleClass="button">
			<fmt:message key="button.advanced" />
		</html:submit>
	
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.instructions" />
		</html:submit>
	</td>
</tr>
</table>
</div>

						
<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center">
				<tr>
					<td class="formlabel"><fmt:message key="instructions.onlineInstructions" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckEditor/"
								height="200"
								width="85%">
								<c:out value="${NbAuthoringForm.onlineInstructions}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOnlineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="onlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
					</td>						
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="instructions.offlineInstructions" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckEditor/"
								width="85%"
								height="200">
							<c:out value="${NbAuthoringForm.offlineInstructions}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOfflineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="offlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
					</td>						
				</tr>
			
			</table>
		</td>
	</tr>
</table>
</div>

<h2><fmt:message key="label.attachments" /></h2>
<div id="datatablecontainer">
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<logic:present name="attachmentList">
		        	<bean:size id="count" name="attachmentList" />
		                  <logic:notEqual name="count" value="0">
		                  
		                        <table align="center">
		                        <tr>
		                          		<th scope="col"><fmt:message key="label.filename" /></th>
		                          		<th scope="col"><fmt:message key="label.type" /></th>
		                          		<th>&nbsp;</th>
		                                
		                        </tr>
		                        <logic:iterate name="attachmentList" id="attachment">
		                        <tr>
		                         	
		                         	<td><bean:write name="attachment" property="filename"/></td>
		                         	<td>
		                         		<c:choose>
				                         	<c:when test="${attachment.onlineFile}" >
				                         		<fmt:message key="instructions.type.online" />
				                         	</c:when>
				                         	<c:otherwise>
				                         		<fmt:message key="instructions.type.offline" />
				                         	</c:otherwise>
			                         	</c:choose>
			                         </td>
			                         <td>
			                         		<fmt:message key="label.view" />
			                         		<fmt:message key="label.download" />
			                         		<fmt:message key="label.delete" />
			                         </td>
		                        </tr>
		                        </logic:iterate>
		                          </table>
		                  
		                     </logic:notEqual>
		         </logic:present>
		      
			</td>
		</tr>
	</table>
</div>

<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center"><html:submit property="method"><fmt:message key="button.done"/></html:submit>
	</td>
</tr>
</table>
</div>
				<html:hidden property="onlineInstructions" />
				<html:hidden property="offlineInstructions" />
				
</html:form>
