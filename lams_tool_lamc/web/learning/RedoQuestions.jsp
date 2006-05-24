<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<html:html locale="true">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table width="80%" cellspacing="8" align="CENTER" class="forms">
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						    <bean:message key="label.assessment"/> 
					  	</th>
					  </tr>
				
			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.individual.results.withRetries"/> </b> </font>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries == 'false'}"> 							  
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.individual.results.withoutRetries"/> </b> </font>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<font size=3>  <bean:message key="label.learner.redo"/> </font>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=right valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<font size=2>  <bean:message key="label.learner.bestMark"/>
						  	<b>   <c:out value="${sessionScope.learnerBestMark}"/> </b> 
						  	<bean:message key="label.outof"/> 
						  	<b> <c:out value="${sessionScope.totalQuestionCount}"/> </b> </font>
					  	</td>
					  </tr>	
					  
					<tr> 
				 		<td NOWRAP colspan=2 valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
					  
					  
				  <tr>
				  	<td NOWRAP colspan=2 align=center valign=top> 
					  	<font size=2>
				  			<html:submit property="viewAnswers" styleClass="button">
								<bean:message key="label.view.answers"/>
							</html:submit>	 		
	   						&nbsp&nbsp&nbsp&nbsp&nbsp
	   						<html:submit property="redoQuestionsOk" styleClass="button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 				 		  					
						</font>
				  	 </td>
				  </tr>

</html:form>	

</body>
</html:html>

