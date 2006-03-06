<%@include file="../sharing/share.jsp" %>
	<c:set var="filesUploaded" value ="${userReport}"/>
	<c:set var="user" value="${user}" />
	<b>Following files have been submitted by 
		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
	</b>
	</p>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<c:forEach var ="details" items="${filesUploaded}" >			
		<span><p>			
		<tr>
			<td>
			File Path: </td><td><c:out value="${details.filePath}" /> 
			<c:set var="viewURL">
				<html:rewrite page="/download/?uuid=${details.uuID}&versionID=${details.versionID}&preferDownload=false"/>
			</c:set>
			<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
						<fmt:message key="label.view"/>
			</a>&nbsp;
			<c:set var="downloadURL">
				<html:rewrite page="/download/?uuid=${details.uuID}&versionID=${details.versionID}&preferDownload=true"/>
			</c:set>
			<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<fmt:message key="label.download"/>
			</a>
			</td>
		</tr>
		<tr>
			<td>File Description: </td><td><c:out value="${details.fileDescription}"  escapeXml="false"/></td>
		</tr>
		<tr>
			<td>Date of Submission: </td><td><c:out value="${details.dateOfSubmission}" /></td>
		</tr>
		<tr>
			<td>Marks:</td>
			<td> 		<c:choose>
								<c:when test="${empty details.marks}">
									<c:out value="Not Available"/>
								</c:when>
								<c:otherwise>
									<c:out value="${details.marks}" escapeXml="false"/>
								</c:otherwise>
							</c:choose>
			</td>
		</tr>			
		<tr>
			<td>Comments:</td>
			<td>
							<c:choose>
								<c:when test="${empty details.comments}">
									<c:out value="Not Available"/>								
								</c:when>
								<c:otherwise>
									<c:out value="${details.comments}" escapeXml="false"/>
								</c:otherwise>
							</c:choose>
			</td>
		</tr>

		<tr>
			<td colspan="2">
					<input type="hidden" name="detailID" value=<c:out value='${details.submissionID}' /> >
					<input type="hidden" name="reportID" value=<c:out value='${details.reportID}' /> >
					<input type="hidden" name="toolSessionID" value=<c:out value='${toolSessionID}' /> >
					<input type="hidden" name="userID" value=<c:out value='${user.userID}' /> >
					<html:link href="javascript:doSubmit('markFile');" property="submit" styleClass="button">
						<bean:message key="label.monitoring.updateMarks.button" />
					</html:link>
			</td>
		</tr>
		</span>
	</c:forEach>
</table>			
