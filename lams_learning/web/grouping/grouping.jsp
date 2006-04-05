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

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<div align="center">

	<html:form action="/grouping.do?method=completeActivity&userId=${user.userId}&lessonId=${lesson.lessonId}&activityId=${activityId}" target="_self">

		<span class="error">
			<%-- Struts error messages --%>
		</span>
		
		<table width="100%" border="0" cellpadding="3" cellspacing="4" class="body" summary="This table is being used for layout purposes">
			<tr bgcolor="#999999"> 
				<td colspan="2" align="left" class="bodyBold">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="bodyBold"><font color="#FFFFFF">Grouping Result</font></td>
							<td align="right" class="smallText">
								<font color="#FFFFFF">
								</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<logic:iterate id="group" name="groups" indexId="gId"> 
			<tr>
				<td align="right" class="bodyBold" style="{border-right: solid #CCCCCC 1px; border-bottom: solid #CCCCCC 1px; }">
					Group <c:out value="${gId+1}"/>
				</td>
				<td width="85%" align="left" class="body"  style="{border-bottom: solid #CCCCCC 1px; }">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<c:forEach items="${group.users}" var="user">
						<tr>
							<td class="bodyBold">
								<c:out value="${user.firstName}"/>,
								<c:out value="${user.lastName}"/>
							</td>
						</tr>
						</c:forEach>
					</table>		
				</td>
			</tr>
			</logic:iterate>
			<tr> 
				<td align="right" valign="top" class="bodyBold">&nbsp;</td>
				<td align="right">
					<html:submit styleClass="button" value="Finished" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')"/> 
				</td>
			</tr>
		</table>

	</html:form>

</div>