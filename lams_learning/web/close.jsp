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
<!-- Load Tool Activity (comment needed for the test harness) -->

<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="learner.title" /></c:set>
<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true" hideTitle="true">
	<script type="text/Javascript">
	    function doCloseRedirect() {
	        if ( window.name.match("LearnerActivity") != null ) {
	        	<%-- In popup window (ie have revisited a completed activity. Just the one activity in the window so close --%>
	            window.close();
	        <%-- } else if ( window.parent.name == "LearnerActivity" ) {
	             In a parallel activity in the popup window, so won't actually close the window (btw you would need to 
	              close the parent if you want to close the window. Live with two "close" messages for now. Eventually we 
		      want to display the wait for the first one, and then close on the second. For that we will need
	              location.href = "<c:out value="${param.waitURL}" escapeXml="false"/>"; --%>
	        } else if ( window.parent.name != "LearnerActivity" ) {        	
	        	<%-- In the main learner window, so want to continue with the main progress --%>
	        	<c:if test="${param.nextURL != null}">
	            	location.href = "<c:out value="${param.nextURL}" escapeXml="false"/>";
	            </c:if>
	        }
	    }
	    
	    window.onload = doCloseRedirect;
	</script>
	
	<div id="container-main">
		<div class="mt-2">
			<fmt:message key="message.window.closing"/>
		</div>
	</div>
	
	<c:if test="${not empty lessonFinishUrl}">
		<img width="0" height="0" style="border: none;" src="${lessonFinishUrl}" />
	</c:if>
</lams:PageLearner>
