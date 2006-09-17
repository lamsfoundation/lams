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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<html:html locale="true">
	<head>
		<%@ include file="/common/header.jsp"%>

</head>
<body>
		<table>
		<c:if test="${userExceptionQuestionEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.question.empty"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightTotal == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.question.weight.total"/> 
			</td> </tr>
		</c:if> 		
		

		<c:if test="${userExceptionPassmarkGreater100 == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.passMark.greater100"/> 
			</td> </tr>
		</c:if> 		
				    
		<c:if test="${userExceptionOptionsCountZero == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="options.count.zero"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionSubmitNone == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.questions.submitted.none"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionPassmarkNotInteger == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.passmark.notInteger"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.weights.empty"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionFilenameEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.fileName.empty"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightNotInteger == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.weights.notInteger"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionWeightZero == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.weights.zero"/> 
			</td> </tr>
		</c:if> 				    


		<c:if test="${userExceptionWeightMustEqual100 == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.weights.total.invalid"/> 
			</td> </tr>
		</c:if> 				
		
		<c:if test="${userExceptionChkboxesEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.checkBoxes.empty"/> 
			</td> </tr>
		</c:if> 				



		<c:if test="${userExceptionNumberFormat == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.numberFormatException"/> 
			</td> </tr>
		</c:if> 				    


		<c:if test="${userExceptionContentRunOffline == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="label.learning.forceOfflineMessage"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentDefineLater == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.defineLater"/> 
			</td> </tr>
		</c:if> 				    


		<c:if test="${userExceptionAnswersDuplicate == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.answers.duplicate"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentInUse == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.content.inUse"/> 
			</td> </tr>
		</c:if> 				    


		<c:if test="${userExceptionSingleOption == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.singleOption"/> 
			</td> </tr>
		</c:if> 				    
		
	</table>
	
</body>
</html:html>


	

