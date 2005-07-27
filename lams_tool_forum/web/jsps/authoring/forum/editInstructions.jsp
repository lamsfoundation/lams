<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>


<html:errors property="error" />
<html:javascript formName="forumForm" dynamicJavascript="true" staticJavascript="false"/>
<div align="center">
<%-- <legend><bean:message key="title.forum.details" /></legend> --%>
<html:form action="/authoring/forum/editInstructions.do" focus="forum.title"
	styleId="forumForm" onsubmit="return validateForumForm(this);" enctype="multipart/form-data">
<fieldset>
<%@ include file="includes/instructions.jsp" %>
 </fieldset>
 </html:form>
            <%--
            <div align="center">
               <display:table name="attachmentList" id="attachment">
	                <display:column property="name" title="File Name" headerClass="displayHeaderCell" class="displayDataCell"/>
	                <display:column property="type" title="Online" headerClass="displayHeaderCell" class="displayDataCell"/>
                   <display:column headerClass="displayHeaderCell" class="displayDataCellCenter">
                   <html:link page="/authoring/forum/deleteAttachment.do" paramId="fileName" paramName="attachment" paramProperty="name">
                        <html:img page="/images/icon_edit.gif" hspace="0" vspace="0" border="0" altKey="forum.attachment.label.delete" titleKey="label.delete" />
                   </html:link></display:column>
                </display:table>
            </div>
            --%>
             <logic:present name="attachmentList">
                 <bean:size id="count" name="attachmentList" />
                  <logic:notEqual name="count" value="0">
                     <div align="center">
                        <table width="100%" cellspacing="8" align="CENTER"  style="background-color:#ffffff;"  cellspacing="3" cellpadding="3">
                        <tr>
                          		<td valign="MIDDLE"><b>FILE NAME</b></td>
                          		<td valign="MIDDLE"><b>TYPE</b></td>
                                <td colspan="3"/>
                        </tr>
                        <logic:iterate name="attachmentList" id="attachment">
                        <tr>
                         	<td valign="MIDDLE"><bean:write name="attachment" property="name"/></td>
                         	<td valign="MIDDLE"><bean:write name="attachment" property="type"/></td>
                            <bean:define id="type" name="attachment" property="type" />
                            <bean:define id="fileName" name="attachment" property="name" />
							<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
							<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
                            <td valign="MIDDLE">
								<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
								<b><bean:message key="label.view"/></b></a></td>
                            <td valign="MIDDLE">
								<html:link page="<%=download%>">
								<b><bean:message key="label.download"/></b></html:link></td>
                            <td valign="MIDDLE">
								<a href="/authoring/forum/deleteAttachment.do?type=<%= type %>&fileName=<%= fileName %>">
								<b><bean:message key="label.delete"/></b></a></td>  
                        </logic:iterate>
                          </table>
                     </div>
                     </logic:notEqual>
            </logic:present>
</div>
