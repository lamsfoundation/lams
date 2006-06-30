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
					  	<td NOWRAP align=left class="input" valign=top colspan=2> 
						  	 <b>  <bean:message key="label.learning.forceFinishMessage"/> </b> 
					  	</td>
					  </tr>
				
					<tr>
						<td NOWRAP align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
				
				  <tr>
				  	<td NOWRAP align=left class="input" valign=top colspan=2> 
					  	 <b>  <bean:message key="label.learning.reportMessage.past"/> </b> 
				  	</td>
				  </tr>
				
			  		<c:forEach var="entry" items="${requestScope.mapGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
								  <c:out value="${entry.value}" escapeXml="false" />						  																	
						  	</td>
						  </tr>
					</c:forEach>
				
				
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
                                <html:submit property="learnerFinished" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('learnerFinished');">
                                    <bean:message key="label.finished"/>
                                </html:submit>
					  	 </td>
					  </tr>
					
				</table>
</html:form>

</body>
</html:html>



