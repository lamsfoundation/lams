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

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
	<html:html locale="true">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	 <lams:css/>

	<!-- depending on user / site preference this will get changed probably use passed in variable from flash to select which one to use-->

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
    <link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
</head>
<body>

		<table width="80%" cellspacing="8" align="CENTER" class="forms">
		<c:if test="${userExceptionWeightTotal == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.question.weight.total"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionOptionsCountZero == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="options.count.zero"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionSubmitNone == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.questions.submitted.none"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionPassmarkNotInteger == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.passmark.notInteger"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.weights.empty"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionFilenameEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.fileName.empty"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightNotInteger == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.weights.notInteger"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightZero == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.weights.zero"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightMustEqual100 == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.weights.total.invalid"/> </font>
			</td> </tr>
		</c:if> 				
		
		<c:if test="${userExceptionChkboxesEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.checkBoxes.empty"/> </font>
			</td> </tr>
		</c:if> 				

		<c:if test="${userExceptionContentDoesNotExist == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.content.doesNotExist"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionToolSessionIdRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.toolSessionId.required"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionNumberFormat == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.numberFormatException"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentIdRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.contentId.required"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentRunOffline == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="label.learning.runOffline"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentDefineLater == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.defineLater"/> </font>
			</td> </tr>
		</c:if> 				    


		<c:if test="${userExceptionAnswersDuplicate == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.answers.duplicate"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentInUse == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.content.inUse"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionDefaultContentNotSetup == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.defaultContent.notSetup"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionSingleOption == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.singleOption"/> </font>
			</td> </tr>
		</c:if> 				    
		
</table>

</body>
</html:html>











