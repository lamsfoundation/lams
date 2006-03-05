<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String root = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToLams = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><bean:message key="label.authoring"/></title>

<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
<script type="text/javascript" src="author_page/js/tabcontroller.js"></script>
<script src="<%=pathToLams%>/includes/javascript/common.js"></script>
<!-- this is the custom CSS for hte tool -->
<link href="<%=root%>author_page/css/tool_custom.css" rel="stylesheet" type="text/css">
<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
<link href="author_page/css/aqua.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);


// The following method submit and the submit methods in the included jsp files submit the 
// form as required for the DispatchAction. All form submissions must go via these scripts - do not
// define an submit button with "dispatch" as the property or 
// "document.McAuthoringForm.dispatch.value=buttonResourceText" will not work

// general submit
// actionMethod: name of the method to be called in the DispatchAction
function submitMethod(actionMethod) {
	document.QaAuthoringForm.dispatch.value=actionMethod; 
	document.QaAuthoringForm.submit();
}

function pviiClassNew(obj, new_style) { //v2.7 by PVII
    obj.className=new_style;
}
//-->
</script>


    <!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="/lams/fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="author_page/js/fckcontroller.js"></script>
    <link href="author_page/css/fckeditor_style.css" rel="stylesheet" type="text/css">

    <script>
        function init(){
            initTabSize(3);
            
            //using the URL anchor (the part after # in the URL) as tabId
            var myregexp = new RegExp(/#[a-zA-Z0-9]+/);
            var tabId = myregexp.exec(document.URL);
            if(tabId != null){
                tabId = tabId[0].replace("#tab", "");
                selectTab(tabId);
            }
            else{
                selectTab(1); //select the default tab;
            }
            
            initEditor("Title");
            initEditor("Instructions");
            initEditor("Question0");
            initEditor("OnlineInstructions");
            initEditor("OfflineInstructions");
           
            <c:set var="queIndex" scope="session" value="1"/>
            <c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
                <c:set var="queIndex" scope="session" value="${queIndex +1}"/>
                initEditor("<c:out value="Question${queIndex-1}"/>");
            </c:forEach>
            
        }        
    </script>

    <!-- ******************** END FCK Editor related javascript & HTML ********************** -->


</head>
<body onLoad="init()" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
 
<html:form action="/authoring" target="_self" enctype="multipart/form-data">
 <input type="hidden" name="dispatch" value=""/>
        		<c:if test="${ (sessionScope.activeModule == 'authoring') }"> 			
			        <jsp:include page="/authoring/AuthoringTabs.jsp" />
				</c:if> 											  
				
				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode != 'true') && 
							   (monitoringOriginatedDefineLater != 'true')
							  }">

			        <div class="tabbody" id="tabbody1">
			        	<tr><td>
								<font size=3> <b> <bean:message key="label.authoring.qa"/> </b> </font>
						</td></tr><tr> <td> &nbsp&nbsp&nbsp&nbsp</td> </tr>
				        <jsp:include page="/authoring/BasicContentViewOnly.jsp" />
   				    </div>
				</c:if> 											  				
				
				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode != 'true') &&
							   (monitoringOriginatedDefineLater == 'true')							   
							  }"> 			

					<b> <font size=2> <bean:message key="label.monitoring"/> </font></b>
			        <jsp:include page="/monitoring/MonitoringTabsHeader.jsp" />
			        <div class="tabbody" id="tabbody1">
				        <jsp:include page="/authoring/BasicContentViewOnly.jsp" />
   				    </div>
				</c:if> 											  				
				
				<!-- switching from define later view only to editable -->
				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode == 'true') &&
							   (monitoringOriginatedDefineLater != 'true')							   
							  }"> 			
			        <div class="tabbody" id="tabbody1">
				        <jsp:include page="/authoring/AuthoringTabs.jsp" />				
   				    </div>			        
			    </c:if> 											  						


				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode == 'true') &&
							   (monitoringOriginatedDefineLater == 'true')							   
							  }"> 			
					<b> <font size=2> <bean:message key="label.monitoring"/> </font></b>
			        <jsp:include page="/monitoring/MonitoringTabsHeader.jsp" />
			        <div class="tabbody" id="tabbody1">
				        <jsp:include page="/authoring/AuthoringTabs.jsp" />				
   				    </div>			        
			    </c:if> 											  						
</html:form>
</body>
</html:html>