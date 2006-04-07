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



<table border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
		<c:if test="${userExceptionQuestionEmpty == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.question.empty"/> </font>
			</td> </tr>
		</c:if> 				    

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

		<c:if test="${userExceptionNoToolSessions == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.toolSession.doesNoExist"/> </font>
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

		<c:if test="${userExceptionLearnerRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.learner.user.doesNoExist"/> </font>
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

		<c:if test="${userExceptionUserDoesNotExist == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.learner.userId.required"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentIdRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.contentId.required"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionToolSessionIdInconsistent == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.learner.sessionId.inconsistent"/> </font>
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

		<c:if test="${userExceptionContentBeingModified == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.content.beingModified"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionDefaultContentNotSetup == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.defaultContent.notSetup"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionModeInvalid == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.mode.invalid"/> </font>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionSingleOption == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						<font size=2> <bean:message key="error.singleOption"/> </font>
			</td> </tr>
		</c:if> 				    
		
</table>



