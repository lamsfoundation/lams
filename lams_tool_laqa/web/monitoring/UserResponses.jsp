<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

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
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<tr> 
	<c:if test="${editResponse != 'true'}">	  	 									 			
		 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
		 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
		 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
		 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.response}"/> </font> </td>

		<c:if test="${(requestLearningReport != 'true')}"> 	
			 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
			 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','deleteResponse');">	</td>	  	 
		</c:if>				 
	</c:if>
	
	<c:if test="${editResponse == 'true'}">	  	
		<c:if test="${editableResponseId == responseUid}">	  	 									 			
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
			 <td NOWRAP valign=top>  <font size=2> <input type="text" name="updatedResponse" value='<c:out value="${userData.response}"/>'> </font> </td>
	
			<c:if test="${(requestLearningReport != 'true')}"> 				 
				 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/tick.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','updateResponse');">	</td>	  	 
				 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','deleteResponse');">	</td>	  	 
			</c:if>				 				 
	</c:if>
	
	<c:if test="${editableResponseId != responseUid}">	  	 									 			
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
			 <td NOWRAP valign=top>  <font size=2> <c:out value="${userData.response}"/> </font> </td>
	
			<c:if test="${(requestLearningReport != 'true')}"> 	
				 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
				 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','deleteResponse');">	</td>	  	 
			</c:if>				 			 
		</c:if>														  					 									  													  			
	</c:if>														  					 									  													  			
</tr>		
