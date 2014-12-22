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
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>

   <lams:head>
    <title><fmt:message key='export.portfolio.window.title'/></title>
	<lams:css/>

    <script type ="text/javascript">
    	var READY_STATE_UNINITIALIZED=0;
		var READY_STATE_LOADING=1;
		var READY_STATE_LOADED=2;
		var READY_STATE_INTERACTIVE=3;
		var READY_STATE_COMPLETE=4;
		var req;
		var updateindicator="";
		var downloadStarted=new Boolean(false);

		function startTimer() {
			setTimeout('wakeup()', 10000 );						
		}

		function wakeup() {
			if ( downloadStarted != true ) {
				displayGeneratingMessage();
				startTimer();
			}
		}

		function displayGeneratingMessage() {
			updateindicator = updateindicator+"... ";
			document.getElementById("message").innerHTML = "<p><fmt:message key='export.portfolio.generating.message'/>"+" <br><br>  <img src='<lams:LAMSURL />/images/loadingAnimation.gif'>  </p>";
		}

		function sendRequest(url,HttpMethod)
		{
		
			if ( window.XMLHttpRequest ) {
			   req = new XMLHttpRequest();
		    } else {
			   req = new ActiveXObject("MSXML2.XMLHTTP");
		    }
		    
			if (!HttpMethod){
				HttpMethod="GET";
			}
			
			if (req){
				req.onreadystatechange=exportCallback; //callback handler
				req.open(HttpMethod,url,true);
				req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				req.send(null); //sends any parameters along with the request for that url
				startTimer();
			}
		}
		
		function exportCallback(){
			
			var ready=req.readyState;
			var msg;

			if (ready==READY_STATE_COMPLETE){
			 
				switch (req.status)
				{
					case 200: // status 200 OK
						var url = "<lams:LAMSURL/>learning/exportDownload?fileLocation="+req.responseText;

						downloadStarted = new Boolean(true);
						msg = "<p><fmt:message key='export.portfolio.generation.complete.message'/></p>\n"

						<c:if test="${not param.hideClose}">
							document.getElementById("button").style.visibility = "visible";
						</c:if>
						document.getElementById("message").innerHTML = msg;
						
						window.location.href = url;
						break;
					case 204:
						msg = "<fmt:message key='error.learner.not.finished' />";
						document.getElementById("message").innerHTML = msg;
						break;
					case 404: //status 404 Not Found
						msg = "<fmt:message key='error.message.404' />.";
						alert(msg);
						break;
					case 500: // status 500 Internal Server Error
						msg = "<p><fmt:message key='error.system.learner'><fmt:param>Status 500</fmt:param></fmt:message></p>";
						document.getElementById("message").innerHTML = msg;
						break;
				}
				
				
			}else{
				displayGeneratingMessage();
			}
		
		}
		
		window.onload=function(){
			// Safari is really picky about cross domain calls - if the server URL doesn't specify a port, then we mustn't
			// specify a port here, and lams:WebAppURL always specifies the port.
			sendRequest("<lams:LAMSURL/>learning/portfolioExport?<c:out value="${pageContext.request.queryString}" escapeXml="false"/>");
		}  
		
	</script>
      <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
   

  </lams:head>
  
  <body class="stripes">


		<div id="content">
	
			<h1><fmt:message key='export.portfolio.window.title'/></h1>

			<div id="message"></div>
				<div id="button" style="visibility:hidden">
					<p><a href='#' onclick='javascript:window.close()' class='button'><fmt:message key='label.close.button'/></a></p>
				</div>
		</div>  <!--closes content-->


		<div id="footer">
		</div><!--closes footer-->

  </body>
</lams:html>
