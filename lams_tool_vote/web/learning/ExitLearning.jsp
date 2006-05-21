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

<html:html locale="true">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>

			<table width="80%" cellspacing="8" align="CENTER" class="forms">
					  <tr>
					  	<td NOWRAP align=left class="input" valign=top colspan=2> 
						  	<font size=2> <b>  <bean:message key="label.learning.forceFinishMessage"/> </b> </font>
					  	</td>
					  </tr>
				
					<tr>
						<td NOWRAP align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
				
				  <tr>
				  	<td NOWRAP align=left class="input" valign=top colspan=2> 
					  	<font size=2> <b>  <bean:message key="label.learning.reportMessage.past"/> </b> </font>
				  	</td>
				  </tr>
				
			  		<c:forEach var="entry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
						  <tr>
						  	<td NOWRAP align=left valign=top colspan=2> 
								  <c:out value="${entry.value}" escapeXml="false" />						  																	
						  	</td>
						  </tr>
					</c:forEach>
				
				
		  	   		  <tr>
					  	<td NOWRAP colspan=2 align=center class="input" valign=top> 
						  	<font size=2>
                                <html:submit property="learnerFinished" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('learnerFinished');">
                                    <bean:message key="label.finished"/>
                                </html:submit>
							</font>
					  	 </td>
					  </tr>
					
				</table>
</html:form>

</body>
</html:html>



