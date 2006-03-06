<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
  	<div id="datatablecontainer">
  		<c:set var="sessionUserMap" scope="request" value="${sessionUserMap}"/>
  		<logic:notEmpty name="sessionUserMap">
			<logic:iterate id="element" name="sessionUserMap">
				<bean:define id="sessionDto" name="element" property="key"/>
				<bean:define id="userlist" name="element" property="value"/>
		  		<table class="forms">
				    <tr>
				        <td style="border-bottom:1px #000 solid;" colspan="4"><b>SESSION NAME: <c:out value="${sessionDto.sessionName}" /></td>
				    </tr>
			  		<logic:notEmpty name="userlist">
						<logic:iterate id="user" name="userlist">
							<tr>
							<bean:define id="details" name="user" property="userID"/>
							<html:hidden property="userID" value="${details}"/>
							<html:hidden property="toolSessionID" value="${sessionDto.sessionID}"/>
							<td class="formlabel"><b><bean:write name="user" property="firstName"/> <bean:write name="user" property="lastName"/></b></td>
							<td class="formlabel"><b><bean:write name="user" property="login"/></b></td>
							<td class="formcontrol">
								<html:link href="javascript:doSubmit('getFilesUploadedByUser', 5);" property="Mark" styleClass="button">
									<bean:message key="label.monitoring.Mark.button" />
								</html:link>
							</td>
							</tr>		
						</logic:iterate>
			  		</logic:notEmpty>
			  		<logic:empty name="userlist">
			  			<tr><td colspan="3">NO USERS AVAILABLE</td></tr>
			  		</logic:empty>
				    
	  				<tr>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('viewAllMarks', 5);" property="viewAllMarks" styleClass="button">
							<bean:message key="label.monitoring.viewAllMarks.button" />
						</html:link>
			  			</td>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('releaseMarks');" property="releaseMarks" styleClass="button">
							<bean:message key="label.monitoring.releaseMarks.button" />
						</html:link>
			  			</td>
			  			<td class="formcontrol">
			  			<html:link href="javascript:doSubmit('downloadMarks');" property="downloadMarks" styleClass="button">
							<bean:message key="label.monitoring.downloadMarks.button" />
						</html:link>
			  			</td>
	  				</tr>
				</table>
				<br><br>
			</logic:iterate>

  		</logic:notEmpty>  		
	</div>
