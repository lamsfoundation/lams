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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html:html locale="true">
<head>
	<title> <bean:message key="label.learning"/> </title>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/fckeditorheader.jsp"%>
</head>
<body>

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
							  	 <b>  <bean:message key="label.individual.results.withRetries"/> </b> 
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries == 'false'}"> 							  
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	 <b>  <bean:message key="label.individual.results.withoutRetries"/> </b> 
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <bean:message key="label.learner.redo"/> 
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=right valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <bean:message key="label.learner.bestMark"/>
						  	<b>   <c:out value="${sessionScope.learnerBestMark}"/> </b> 
						  	<bean:message key="label.outof"/> 
						  	<b> <c:out value="${sessionScope.totalQuestionCount}"/> </b> 
					  	</td>
					  </tr>	
					  
					<tr> 
				 		<td NOWRAP colspan=2 valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
					  
					  
				  <tr>
				  	<td NOWRAP colspan=2 align=left valign=top> 
				  			<html:submit property="viewAnswers" styleClass="button">
								<bean:message key="label.view.answers"/>
							</html:submit>	 		
	   						&nbsp&nbsp
	   						<html:submit property="redoQuestionsOk" styleClass="button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 				 		  					
				  	 </td>
				  </tr>

</html:form>	

</body>
</html:html>

