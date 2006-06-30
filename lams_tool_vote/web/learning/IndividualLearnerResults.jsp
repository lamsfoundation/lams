<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html:html locale="true">
<head>
	<title> <bean:message key="label.learning"/> </title>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/fckeditorheader.jsp"%>

	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) 
		{
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>

	
</head>
<body>

<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>
				
			<table class="forms">

					  <tr>
					  	<td NOWRAP align=left  valign=top  colspan=2> 
						  	 <b>  <bean:message key="label.learning.reportMessage"/> </b> 
					  	</td>
					  </tr>
				

					<tr>
						<td NOWRAP align=right  valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
					
			  		<c:forEach var="entry" items="${requestScope.mapGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
								  <c:out value="${entry.value}" escapeXml="false" />						  																	
						  	</td>
						  </tr>
					</c:forEach>
										
						<tr> 
							<td NOWRAP align=center valign=top colspan=2> 
						 	  		<c:out value="${VoteLearningForm.userEntry}"/> 						 			
					 		</td>
					  	</tr>

			  	   	<tr> 
				 		<td NOWRAP colspan=2  valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center  valign=top> 
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
					  	 </td>
					  </tr>
					
				</table>
</html:form>

</body>
</html:html>


