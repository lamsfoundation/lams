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


<table>
	<tr><td>
			<font size=3> <b> <bean:message key="label.authoring.qa"/> </b> </font>
	</td></tr><tr> <td> &nbsp&nbsp&nbsp&nbsp</td> </tr>
</table>
 					
	<c:if test="${ sessionScope.showAuthoringTabs == 'true'}"> 			
	    <!-- start tabs -->
	    <div class="tabmenu">
	        <ul>
	            <li id="tab1" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(1);"><bean:message key="label.basic"/></a> <!-- IE CSS Bug, If you remove the space infront this comment then height of the Tab will change in IE - Anthony --></div></div></li>
	            <li id="tab2" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(2);"><bean:message key="label.advanced"/></a> </div></div></li>
	            <li id="tab3" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(3);"><bean:message key="label.instructions"/></a> </div></div></li>
	        </ul>
	    </div>
	    <!-- end tab buttons -->
	</c:if> 			
	<c:if test="${ sessionScope.showAuthoringTabs != 'true'}"> 			
	    <!-- start tabs -->
	    <div class="tabmenu" style="visibility: hidden">
	        <ul>
	            <li id="tab1" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(1);"><bean:message key="label.basic"/></a> <!-- IE CSS Bug, If you remove the space infront this comment then height of the Tab will change in IE - Anthony --></div></div></li>
	            <li id="tab2" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(2);"><bean:message key="label.advanced"/></a> </div></div></li>
	            <li id="tab3" class="tabitem"><div class="tableft"><div class="tabright"><a href="javascript:selectTab(3);"><bean:message key="label.instructions"/></a> </div></div></li>
	        </ul>
	    </div>
	    <!-- end tab buttons -->
	</c:if> 			
	
    <div class="tabbody" id="tabbody1">
        <div id="formtablecontainer">
		            <jsp:include page="/authoring/BasicContent.jsp" />							  
        </div>
    
        <hr>
	        <a href="javascript:window.close()" class="button"><font size=2> <b> Cancel </b> </font></a>
	        <a href="javascript:submitMethod('submitAllContent')" class="button"><font size=2> <b> <bean:message key="label.save"/> </b> </font></a>
    </div>

	<c:if test="${ sessionScope.showAuthoringTabs == 'true'}"> 			
	    <div class="tabbody" id="tabbody2">
	        <h2><font size=2> <b> <bean:message key="label.advanced.definitions"/> </b></font></h2>
	        <div id="formtablecontainer">
	            <jsp:include page="/authoring/AdvancedContent.jsp" />
	        </div>
	    </div>
	</c:if> 				    
	<c:if test="${ sessionScope.showAuthoringTabs != 'true'}"> 			
	    <!-- tab content 2 Advanced-->
	    <div class="tabbody" id="tabbody2" style="visibility: hidden">
	        <h2><font size=2> <b> <bean:message key="label.advanced.definitions"/> </b></font></h2>
	        <div id="formtablecontainer">
	            <jsp:include page="/authoring/AdvancedContent.jsp" />
	        </div>
	    </div>	
	</c:if> 				    

	
	<c:if test="${ sessionScope.showAuthoringTabs == 'true'}"> 			
	    <div class="tabbody" id="tabbody3">
	        <h2><font size=2> <b> <bean:message key="label.authoring.instructions"/> </b></font></h2>
	        <div id="formtablecontainer">
	            <jsp:include page="/authoring/InstructionsContent.jsp" />
	        </div>
	        <hr>
	    </div>
	</c:if> 				    
	<c:if test="${ sessionScope.showAuthoringTabs != 'true'}"> 				
	    <div class="tabbody" id="tabbody3" style="visibility: hidden">
	        <h2><font size=2> <b> <bean:message key="label.authoring.instructions"/> </b></font></h2>
	        <div id="formtablecontainer">
	            <jsp:include page="/authoring/InstructionsContent.jsp" />
	        </div>
	        <hr>
	    </div>
	</c:if> 				    
    

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
                <a href="#" onClick="saveWYSWYGEdittedText(activeEditorIndex); doPreview(activeEditorIndex)"> <font size=2> <b> <bean:message key="label.save"/> </b> </font></a>
                &nbsp&nbsp&nbsp&nbsp
                <a href="#" onClick="doPreview(activeEditorIndex)"><font size=2> <b><bean:message key="label.cancel"/> </font> </b></a>
            </div>
        </div>
    </div>