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
	
	<%@ include file="/common/taglibs.jsp"%>

	<c:set var="lams"><lams:LAMSURL/></c:set>
	<c:set var="tool"><lams:WebAppURL/></c:set>

	<%@ include file="/common/messages.jsp"%>								
	
	<c:if test="${editActivityDTO.monitoredContentInUse != 'true'}"> 			
		<c:if test="${mcGeneralMonitoringDTO.defineLaterInEditMode != 'true'}"> 			
			<jsp:include page="/authoring/BasicContentViewOnly.jsp" />
		</c:if> 				
		<c:if test="${mcGeneralMonitoringDTO.defineLaterInEditMode == 'true'}"> 			
			<jsp:include page="/authoring/BasicContent.jsp" />
		</c:if> 				
	</c:if> 											

	<c:if test="${editActivityDTO.monitoredContentInUse == 'true'}"> 			
		<table border="0" cellspacing="2" cellpadding="2">									
			<tr> <td NOWRAP valign=top>
					<fmt:message key="error.content.inUse"/> 
			</td> </tr>
		</table>
	</c:if> 																									

		
		
