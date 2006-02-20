<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
<h1>Question & Answers</h1>
  
    
    <!-- start tabs -->
    <div class="tabmenu">
        <ul>
            <li id="tab1" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(1);">Basic</a> <!-- IE CSS Bug, If you remove the space infront this comment then height of the Tab will change in IE - Anthony --></div></div></li>
            <li id="tab2" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(2);">Advanced</a> </div></div></li>
            <li id="tab3" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(3);">Instructions</a> </div></div></li>
        </ul>
    </div>
    <!-- end tab buttons -->
    
        

    <!-- tab content one (basic)-->
    <div class="tabbody" id="tabbody1">
        <h2><font size=2> <b> <bean:message key="label.authoring.qa.basic"/> </b></font></h2>
        <div id="formtablecontainer">
            <jsp:include page="BasicContent.jsp" />
        </div>
    
        <hr>
        <a href="javascript:window.close()" class="button">Cancel</a>
        <a href="javascript:submitMethod('submitAllContent')" class="button">Save</a>
    </div>
    <!-- end of content_b -->


    <!-- tab content 2 Advanced-->
    <div class="tabbody" id="tabbody2">
        <h2><font size=2> <b> <bean:message key="label.advanced.definitions"/> </b></font></h2>
        <div id="formtablecontainer">
            <jsp:include page="AdvancedContent.jsp" />
        </div>
        <a href="javascript:window.close()" class="button">Cancel</a>
        <a href="javascript:submitMethod('submitAllContent')" class="button">Save</a>
    </div>
    <!-- end of content_b -->

    <!-- tab content 3 instructions -->
    <div class="tabbody" id="tabbody3">
        <h2><font size=2> <b> <bean:message key="label.authoring.instructions"/> </b></font></h2>
        <div id="formtablecontainer">
            <jsp:include page="InstructionsContent.jsp" />
        </div>
        <hr>
        <a href="javascript:window.close()" class="button"><font size=2> <b> Cancel </font> </b> </a>
        <a href="javascript:submitMethod('submitAllContent')" class="button"><font size=2> <b> Save </font> </b></a>
    </div>
    <!-- end of content_a -->

    <div id="wyswygEditorScreen" style="visibility: hidden"> <!-- position: absolute; z-index: 1000; top: 16px; left: 230px; -->
        <div id="wyswygEditor">
            <div>
                <FCK:editor id="FCKeditor1" basePath="/lams/fckeditor/"
                    imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector"
                    linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
                    flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&Connector=connectors/jsp/connector"
                    imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
                    linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
                    flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash">
                    
                </FCK:editor>  
            </div>
            <div style="text-align: center">
                <a href="#" onClick="saveWYSWYGEdittedText(activeEditorIndex); doPreview(activeEditorIndex)"> <font size=2> <b> Save </b> </font></a>
                &nbsp&nbsp&nbsp&nbsp
                <a href="#" onClick="doPreview(activeEditorIndex)"><font size=2> <b>Cancel </font> </b></a>
            </div>
        </div>
    </div>

</html:form>

</body>

</html:html>