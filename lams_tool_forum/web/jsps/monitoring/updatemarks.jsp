<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>

<html:form action="/monitoring/updateMark"  method="post">	
<c_rt:set var="formBean" value="<%= session.getAttribute("markForm") %>" />
  <b><fmt:message key="message.assign.mark"/>
  		 <c:out value="${formBean.user.loginName}" /> , <c:out value="${formBean.user.firstName}" />  <c:out value="${formBean.user.lastName}" /> 	
  </b></p>
 <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="MIDDLE" width="48%">
			<c:set var="viewtopic">
				<html:rewrite page="/monitoring/viewTopic.do?messageID=${formBean.messageDto.message.uid}&create=${formBean.messageDto.message.created.time}" />
			</c:set> 
			<html:link href="javascript:launchPopup('${viewtopic}')">
				<c:out value="${formBean.messageDto.message.subject}" />
			</html:link>
		</td>
		<td width="2%">
			<c:if test="${formBean.messageDto.hasAttachment}">
				<img src="<html:rewrite page="/images/paperclip.gif"/>">
			</c:if>
		</td>
		<td>
			<c:out value="${formBean.messageDto.author}"/>
		</td>
		<td>
			<c:out value="${formBean.messageDto.message.replyNumber}"/>
		</td>
		<td>
			<fmt:formatDate value="${formBean.messageDto.message.updated}" type="time" timeStyle="short" />
			<fmt:formatDate value="${formBean.messageDto.message.updated}" type="date" dateStyle="full" />
		</td>
	</tr>
</table>
<table class="forms">
				 <input type="hidden" name="toolSessionID" value="<c:out value='${formBean.sessionId}'/>" />						
				 <input type="hidden" name="messageID" value="<c:out value='${formBean.messageDto.message.uid}'/>" />						
				 <input type="hidden" name="userID" value="<c:out value='${formBean.user.uid}'/>" />
				 <tr>						 
			            <td class="formlabel"><fmt:message key="lable.topic.title.mark"/>:</td>
			            <td class="formcontrol">
			        		<input type="text"  name="mark" value="<c:out value='${formBean.mark}'/>"/>
			        	</td>
			        	<td><html:errors property="report.mark" /></td>
			    </tr>
			    <tr>
		    		<td class="formlabel"><fmt:message key="lable.topic.title.comment"/>:</td>
			        <td class="formcontrol">
			            <FCK:editor id="comment" 
			    			basePath="/lams/fckeditor/"
			    			height="150"    
			    			width="85%">
									<c:out value="${formBean.comment}" escapeXml="false"/>
						</FCK:editor>
			            </td>
			            <td><html:errors property="report.comment" /></td>
	        </tr>
			<tr>
				  <td class="formlabel" colspan="3">
				  	<html:errors property="report.globel"/>
				  </td>
			</tr>
			<tr>
				  <td class="formcontrol" colspan="3">
					<input type="submit" value="<fmt:message key="lable.update.mark"/>" />
				  </td>
			</tr>
		</span>
</table>
</html:form>
