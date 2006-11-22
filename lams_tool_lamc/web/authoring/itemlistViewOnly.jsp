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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.questions" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>

<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<table id="itemTable" class="align-left" style="width:650px" >
    <c:set var="queIndex" scope="request" value="0"/>
    
    
    <c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
	<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
	<c:set var="question" scope="request" value="${currentDTO.question}"/>
	<c:set var="feedback" scope="request" value="${currentDTO.feedback}"/>
	<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}"/>	

	<tr>
		<td width="10%" class="align-right">
				<strong> <fmt:message key="label.question" />: </strong>
		</td>

		<td width="60%" class="align-left">
			<div style="overflow: auto;">				
				<c:out value="${question}" escapeXml="false"/> 
			</div>											
		</td>		


		<td width="10%" class="align-right">
		</td>
       	
		<td width="10%" class="align-right">

		</td>

		<td width="10%" class="align-right">

		</td>
	</tr>
</c:forEach>

</table>
</div>
