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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
<%@ attribute name="progress" required="true" rtexprvalue="true" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:if test="${!empty progress}">
		<c:set var="passonurl">passon.swf?${progress}&uniqueID=<c:out value="<script language='text/javascript'>document.write(parent.frames['controlFrame'].uniqueID);</script>" escapeXml="false"/></c:set>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->  
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="passon"
		 width="1" height="1" align="left" id="passon">
		  <param name="allowScriptAccess" value="sameDomain" />
	
		  <param name="movie" value="<c:out value="${passonurl}" escapeXml="false"/>"/>
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#FFFFFF">
		  <embed 	
			  src="<c:out value="${passonurl}" escapeXml="false"/>"
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
</c:if>