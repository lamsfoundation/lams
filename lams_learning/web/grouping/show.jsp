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

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>

	<html:form action="/grouping.do?method=completeActivity&userId=${user.userID}&lessonId=${lesson.lessonId}&activityID=${activityID}" target="_self" styleId="messageForm">

	<div id="content">

		<h1><c:out value="${title}"/></h1>

		<table class="alternative-color" cellspacing="0">
			<tr>
				<th width="25%" class="first">
					<fmt:message key="label.view.groups.title"/>
				</th>
				<th>
					<fmt:message key="label.view.groups.learners"/>
				</th>
			</tr>
			<logic:iterate id="group" name="groups"> 
			<tr>
				<td width="25%" class="first">
					<c:out value="${group.groupName}"/>
				</td>
				<td>
					<c:forEach items="${group.users}" var="user">
						<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/><BR>
					</c:forEach>
					&nbsp;
				</td>
			</tr>
			</logic:iterate>
		</table>

	<c:if test="${finishedButton}">
	      <script type="text/javascript">
	        function submitForm(methodName){
                	 var f = document.getElementById('messageForm');
                	 f.submit();
		}
	      </script>

			<table><tr><td><div id="right" class="right-buttons">
					<html:link href="javascript:;" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
					  <span class="nextActivity">
	                    <c:choose>
		 					<c:when test="${activityPosition.last}">
		 						<fmt:message key="label.submit.button" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finish.button" />
		 					</c:otherwise>
		 				</c:choose>
		 			 </span>
					 </html:link>
			</div></td></tr></table>
	</c:if>

	</div>  <!--closes content-->

	<div id="footer">
	</div><!--closes footer-->

	</html:form>

</div>