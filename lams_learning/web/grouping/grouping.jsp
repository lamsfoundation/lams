<!--
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
-->

<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean-el.tld" prefix="bean-el" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>		
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>

<div align="center">

	<html-el:form action="/grouping.do?method=completeActivity&userId=${user.userId}&lessonId=${lesson.lessonId}&activityId=${activityId}" target="_self">

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
			<logic-el:iterate id="group" name="groups" indexId="gId"> 
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
			</logic-el:iterate>
			<tr> 
				<td align="right" valign="top" class="bodyBold">&nbsp;</td>
				<td align="right">
					<html-el:submit styleClass="button" value="Finished" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')"/> 
				</td>
			</tr>
		</table>

	</html-el:form>

</div>