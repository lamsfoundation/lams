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
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

			<tr> <td>
			<table width="80%" cellspacing="8" align="CENTER" class="forms">
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
						  <tr>
						  	<td colspan=2 NOWRAP valign=top> <font size=2> <b> <bean:message key="label.question"/> <c:out value="${questionEntry.key}" escapeXml="false"/>:  </b>  </font> 
						  		<c:out value="${questionEntry.value}" escapeXml="false"/> 
						  	 </td>
						  </tr>
						  <tr> 
					 		<td NOWRAP valign=top > <font size=2> <b> <bean:message key="label.answer"/> </b> </font> </td>
					 		<td  NOWRAP valign=top>
					 			<input type="text" name="answer<c:out value="${questionEntry.key}"  escapeXml="false"/>" size="60" maxlength="255" value=""/> 
					 		</td>
					  	  </tr>
					  	  <tr><td> &nbsp </td> </tr>
				</c:forEach>
			</table>
			
			<table width="80%" cellspacing="8" align="CENTER" class="forms">
			<tr>
				 <td align=right> 
						<html:submit onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
							<bean:message key="button.submitAllContent"/>
						</html:submit>	 				 		  					
				</td> 
			</tr>
			</table>
