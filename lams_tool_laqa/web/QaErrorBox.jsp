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

	<table>
		<c:if test="${userExceptionContentDoesNotExist == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.content.doesNotExist"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionNoToolSessions == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.toolSession.notAvailable"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionRunOffline == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="label.learning.runOffline"/>
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionToolSessionIdRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.toolSessionId.required"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionUserDoesNotExist == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.learningUser.notAvailable"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionModeRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.mode.required"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionNumberFormat == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.sessionId.numberFormatException"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentIdRequired == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.contentId.required"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionContentInUse == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.content.inUse"/> 
			</td> </tr>
		</c:if> 				    

		<c:if test="${userExceptionModeInvalid == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.mode.invalid"/> 
			</td> </tr>
		</c:if> 				    
		
		<c:if test="${userExceptionDefaultContentNotSetup == 'true'}"> 			
			<tr> <td NOWRAP valign=top>
						 <bean:message key="error.defaultContent.notSetup"/> 
			</td> </tr>
		</c:if> 				    
	</table>


