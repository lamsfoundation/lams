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
  * Passon
  *	Author: Mitchell Seaton
  *	Description: Passes on progress data to the Flash Learner to update the progress bar.
  * 
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ attribute name="progress" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="version" required="false" rtexprvalue="true" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="redirect" required="false" rtexprvalue="true" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:if test="${!empty progress}">
		<c:set var="randomID">
			<lams:generateID id="${id}"/>
		</c:set>
			<c:set var="passonurl_params" value="${progress}&lessonID=${id}&uniqueID=${randomID}"/>
			<c:if test="${redirect}">
				<c:set var="passonurl_params" value="redirect=true&${passonurl_params}"/>
			</c:if>
			<c:if test="${!empty version}">
				<c:set var="passonurl_params" value="version=${version}&${passonurl_params}"/>
			</c:if>
			
			<c:set var="passonurl_nojs" value="passon.swf?${passonurl_params}"/>
			<c:set var="passonurl_js" value="passon?${passonurl_params}"/>
			<script type="text/javascript">
				AC_FL_RunContent('classid', 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000', 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0','width','1','height','1','align','left','src','<c:out value="${passonurl_js}" escapeXml="false"/>','quality','high','scale','noscale','bgcolor','#FFFFFF','name','passon', 'id', 'passon', 'allowscriptaccess','sameDomain', 'swliveconnect', true, 'type', 'application/x-shockwave-flash', 'pluginspage','http://www.macromedia.com/go/getflashplayer','movie','<c:out value="${passonurl_js}" escapeXml="false"/>' );
			</script>
			
			<noscript>
				<!-- URL's used in the movie-->
				<!-- text used in the movie-->
				<!--Library-->  
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="passon"
				 width="1" height="1" align="left" id="passon">
				  <param name="allowScriptAccess" value="sameDomain" />
				
				  <param name="movie" value="${passonurl_nojs}"/>
				  <param name="quality" value="high">
				  <param name="scale" value="noscale">
				  <param name="bgcolor" value="#FFFFFF">
				  <embed 	
					  src="<c:out value="${passonurl_nojs}" escapeXml="false"/>"
					  quality="high" 
					  scale="noscale" 
					  bgcolor="#FFFFFF"  
					  width="1" 
					  height="1" 
					  swliveconnect=true 
					  id="passon" 
					  name="passon" 
					  align=""
					  type="application/x-shockwave-flash" 
					  pluginspage="http://www.macromedia.com/go/getflashplayer" />
				</object>
			</noscript>
</c:if>