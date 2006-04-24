<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>
				
				<table align=center bgcolor="#FFFFFF">

					  <tr>
					  	<td NOWRAP align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.learning.reportMessage"/> </b> </font>
					  	</td>
					  </tr>
				

					<tr>
						<td NOWRAP align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
					
			  		<c:forEach var="entry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=left class="input" valign=top  colspan=2> 
									<c:out value="${entry.value}"/> 
						  	</td>
						  </tr>
					</c:forEach>
										
						<tr> 
							<td NOWRAP align=left class="input" valign=top colspan=2> 
						 	  		<c:out value="${VoteLearningForm.userEntry}"/> 						 			
					 		</td>
					  	</tr>

			  	   	<tr> 
				 		<td NOWRAP colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
						  	<font size=2>
                                <html:submit property="redoQuestions" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('redoQuestions');">
                                    <bean:message key="label.retake"/>
                                </html:submit>
                                <html:submit property="viewAllResults" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('viewAllResults');">
                                    <bean:message key="label.overAllResults"/>
                                </html:submit>
							</font>
					  	 </td>
					  </tr>
					
				</table>
</html:form>

</body>
</html:html>


