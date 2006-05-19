<%@ include file="/common/taglibs.jsp" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
		  <c:set var="details" value="${fileDetails}"/>
		  <c:set var="user" value="${user}" />
		  <c:set var="toolSessionID" value="${toolSessionID}" />		  
		  <b>Please assign a mark and a comment for the report by 
		  		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
		  </b></p>
		 <table width="100%"  border="0" cellspacing="0" cellpadding="0">
			  <span><tr>	
					<td>File Path:</td><td><c:out value="${details.filePath}"  escapeXml="false"/></td>
				    </tr>
				    <tr>
					<td>File Description:</td><td> <c:out value="${details.fileDescription}"  escapeXml="false"/></td>
					</tr>
				    <tr>						
					<td>Date of Submission: </td><td><c:out value="${details.dateOfSubmission}" /></td>
					</tr>
		</table>
		<table class="forms">
					<tr><td colspan="2"><html:errors/></td></tr>	
							 <input type="hidden" name="toolSessionID" value="<c:out value='${toolSessionID}'/>" />						
							 <input type="hidden" name="reportID" value="<c:out value='${details.reportID}'/>" />						
 							 <input type="hidden" name="userID" value="<c:out value='${user.userID}'/>" />
 					<tr>						 
 				            <td class="formlabel">Marks:</td>
 				            <td class="formcontrol">
 				        		<input type="text" name="marks" value=<c:out value="${details.marks}"  escapeXml="false"/>>
 				        	</td>
 				    </tr>
 				    <tr>
 				    		<td class="formlabel">Comments:</td>
					        <td class="formcontrol">
					        	<lams:SetEditor id="Comments" text="${details.comments}" small="true"/>
  				            </td>
			        </tr>
					<tr>
						  <td class="formcontrol" colspan="2">
							<html:link href="javascript:doSubmit('updateMarks');" property="submit" styleClass="button">
								<bean:message key="label.monitoring.saveMarks.button" />
							</html:link>
						  </td>
					</tr>
					</form>
				</span>
		</table>
