<% 
/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
 
 /**
  * Progress Output
  *	Author: Fiona Malikoff
  *	Description: Outputs the Activity details on the jsp progress page. Recursive tag
  * 
  */
 
	/** Need to add current ! */

 %>
<%@ tag body-content="empty" %>
<%@ attribute name="activity" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.learning.web.bean.ActivityURL" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<LI><c:choose>
		<c:when test="${activity.activityId==currentActivityID}">
			<c:set var="image" value="progress_current.gif"/>
			<c:set var="colour" value="990000"/>
		</c:when>
		<c:when test="${activity.status==1}">
			<c:set var="image" value="progress_completed.gif"/>
			<c:set var="colour" value="003399"/>
		</c:when>
		<c:when test="${activity.status==2}">
			<c:set var="image" value="progress_attempted.gif"/>
			<c:set var="colour" value="990000"/>
		</c:when>
		<c:otherwise>
			<c:set var="image" value="progress_tostart.gif"/>
			<c:set var="colour" value="009900"/>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
	<c:when test="${not empty activity.url}">
		<img src="<lams:LAMSURL/>/images/${image}" width="10"/> <a href="#" onclick="javascript:loadFrame('${activity.url}');"><font color="#${colour}"><c:out value="${activity.title}"/></font></a>
	</c:when>
	<c:otherwise>
		<img src="<lams:LAMSURL/>/images/${image}" width="10"/> <font color="#${colour}">${activity.title}</font>		
	</c:otherwise>
	</c:choose>

	<c:forEach var="childActivity" items="${activity.childActivities}" varStatus="childstatus">
		<c:if test="${childstatus.first}">
			<BR><UL style="margin-right:0">
		</c:if>
		<lams:ProgressOutput activity="${childActivity}"/>
		<c:if test="${childstatus.last}">
			</UL>
		</c:if>
	</c:forEach>

</LI>						

