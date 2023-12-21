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
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="error.title" /></c:set>
<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true" hideTitle="true">

	<div id="container-main">
	     <lams:Alert5 type="danger">
		    <fmt:message key="message.progress.broken"/>
		    <br>
		    <fmt:message key="message.progress.broken.try.resume"/>
		 </lams:Alert5>
		    
		 <div class="activity-bottom-buttons">
		 	<button type="button" class="btn btn-primary" id="addNewBtn" onClick="window.close();">
		    	<fmt:message key="label.close.button" />
		    </button>
	     </div>
	</div>
</lams:PageLearner>
