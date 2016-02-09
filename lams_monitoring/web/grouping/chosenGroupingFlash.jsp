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
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE html>
<lams:html>
    <lams:head>
       <html:base/>
	   <lams:css/>
      <title><c:out value="${title}"/></title>

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/error.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		var requiredMajorVersion = 9;
		var requiredMinorVersion = 0;
		var requiredRevision = 124;

		function closeWindow(){
			window.close();
		}
	</script>
	
</lams:head>

<body class="stripes">

	<div id="content">

	<h1>
		<c:out value="${title}"/>
	</h1>

	<form>

	<c:if test="${not empty description}">
		<p><c:out value="${description}"/></p>
	</c:if>
	
	<P><STRONG><fmt:message key="label.grouping.general.instructions.heading"/></STRONG> <fmt:message key="label.grouping.general.instructions.line1"/></P>
	
	<c:choose>
		<c:when test="${usedForBranching}"><p><fmt:message key="label.grouping.general.instructions.branching"/></p></c:when>
		<c:otherwise><p><fmt:message key="label.grouping.general.instructions.line2"/></p></c:otherwise>
	</c:choose>
	
	<div align="center">
	<script language="JavaScript" type="text/javascript">
		// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
		var hasProductInstall = DetectFlashVer(6, 0, 65);
		
		// Version check based upon the values defined in globals
		var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
		
		if ( hasProductInstall && !hasRequestedVersion ) {
			// DO NOT MODIFY THE FOLLOWING FOUR LINES
			// Location visited after installation is complete if installation is required
			var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
			var MMredirectURL = window.location;
		    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
		    var MMdoctitle = document.title;
		
			AC_FL_RunContent(
				"src", "<lams:LAMSURL/>/includes/flash/playerProductInstall",
				"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+'&lessonID='+'${lessonID}'+'&activityID='+'${activityID}'+'&serverUrl=<lams:LAMSURL/>'+'&mayDelete='+'${mayDelete}'+'&usedForBranching='+'${usedForBranching}'+'&maxNumberOfGroups='+'${maxNumberOfGroups}'+'&languageXML='+"${languageXML}"+"",
				"width", "640",
				"height", "460",
				"align", "middle",
				"id", "LamsLessonGroupManager",
				"quality", "high",
				"bgcolor", "#ffffff",
				"name", "GroupManager",
				"allowScriptAccess","sameDomain",
				"type", "application/x-shockwave-flash",
				"pluginspage", "http://www.adobe.com/go/getflashplayer"
			);
		} else if (hasRequestedVersion) {
			// if we've detected an acceptable version
			// embed the Flash Content SWF when all tests are passed
			AC_FL_RunContent(
					"src", "<lams:LAMSURL/>/monitoring/grouping/GroupManager",
					"FlashVars", "lessonID="+'${lessonID}'+'&activityID='+'${activityID}'+'&serverUrl=<lams:LAMSURL/>'+'&mayDelete='+'${mayDelete}'+'&usedForBranching='+'${usedForBranching}'+'&maxNumberOfGroups='+'${maxNumberOfGroups}'+'&languageXML='+"${languageXML}"+"",
					"width", "640",
					"height", "460",
					"align", "middle",
					"id", "GroupManager",
					"quality", "high",
					"bgcolor", "#ffffff",
					"name", "GroupManager",
					"allowScriptAccess","sameDomain",
					"type", "application/x-shockwave-flash",
					"pluginspage", "http://www.adobe.com/go/getflashplayer"
			);
		  } else {  // flash is too old or we can't detect the plugin
		    var alternateContent = 'Alternate HTML content should be placed here. '
		  	+ 'This content requires the Adobe Flash Player. '
		   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
		    document.write(alternateContent);  // insert non-flash content
		  }
	</script>
	<noscript>
	  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				id="GroupManager" width="640" height="460"
				codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
				<param name="movie" value="<lams:LAMSURL/>/monitoring/grouping/GroupManager.swf" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#ffffff" />
				<param name="allowScriptAccess" value="sameDomain" />
				<embed src="<lams:LAMSURL/>/monitoring/branching/GroupManager.swf" quality="high" bgcolor="#869ca7"
					width="640" height="460" name="LamsLessonGroupManager" align="middle"
					play="true"
					loop="false"
					quality="high"
					allowScriptAccess="sameDomain"
					type="application/x-shockwave-flash"
					pluginspage="http://www.adobe.com/go/getflashplayer">
				</embed>
		</object>
	</noscript>
	</div>
	</div>

	<div id="footer">
	</div><!--closes footer-->

</div>

</body>
</lams:html>