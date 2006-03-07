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

			<tr> <td>
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
						  <tr>
						  	<td colspan=2 NOWRAP class="input" valign=top> <font size=2> <b> <bean:message key="label.question"/> <c:out value="${questionEntry.key}" escapeXml="false"/>: </b> 
						  		<c:out value="${questionEntry.value}" escapeXml="false"/> 
						  	</font> </td>
						  </tr>
						  <tr> 
					 		<td NOWRAP class="input" valign=top > <font size=2> <b> <bean:message key="label.answer"/> </b> </font> </td>
					 		<td  NOWRAP class="input" valign=top>
					 			<input type="text" name="answer<c:out value="${questionEntry.key}"  escapeXml="false"/>" size="60" maxlength="255" value=""/> 
					 		</td>
					  	  </tr>
					  	  <tr><td> &nbsp </td> </tr>
				</c:forEach>
			</table>
			
				
