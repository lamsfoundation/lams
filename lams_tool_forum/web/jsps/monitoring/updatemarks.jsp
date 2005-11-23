  <b>Please assign a mark and a comment for the report by 
  		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
  </b></p>
 <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="MIDDLE" width="48%">
			<c:set var="viewtopic">
				<html:rewrite page="/learning/viewTopic.do?topicId=${topic.message.uid}&create=${topic.message.created.time}" />
			</c:set> 
			<html:link href="${viewtopic}">
				<c:out value="${topic.message.subject}" />
			</html:link>
		</td>
		<td width="2%">
			<c:if test="${topic.hasAttachment}">
				<img src="<html:rewrite page="/images/paperclip.gif"/>">
			</c:if>
		</td>
		<td>
			<c:out value="${topic.author}"/>
		</td>
		<td>
			<c:out value="${topic.message.replyNumber}"/>
		</td>
		<td>
			<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
			<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
		</td>
	</tr>
</table>
<table class="forms">
			<tr><td colspan="2"><html:errors/></td></tr>
			<html:form action="/monitoring/updateMark"  method="post">	
				<c_rt:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
				 <input type="hidden" name="toolSessionID" value="<c:out value='${toolSessionID}'/>" />						
				 <input type="hidden" name="messageID" value="<c:out value='${topic.message.uid}'/>" />						
				 <input type="hidden" name="userID" value="<c:out value='${user.userID}'/>" />
				 <tr>						 
			            <td class="formlabel">Marks:</td>
			            <td class="formcontrol">
			        		<html:input property="mark"/>
			        	</td>
			    </tr>
			    <tr>
			    		<td class="formlabel">Comments:</td>
			        <td class="formcontrol">
			            <FCK:editor id="comments" 
			    			basePath="/lams/fckeditor/"
			    			height="150"    
			    			width="85%">
									<c:out value="${formBean.comment}" escapeXml="false"/>
						</FCK:editor>
			            </td>
	        </tr>
			<tr>
				  <td class="formcontrol" colspan="2">
					<input type="submit" value="Update Marks" />
				  </td>
			</tr>
			</html:form>
		</span>
</table>
