<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="displaytag" prefix="display" %>
<%@ taglib uri="jmage" prefix="jm" %>

<html:errors property="error" />
<html:javascript formName="forumForm" dynamicJavascript="true" staticJavascript="false"/>
<div align="center">
<%-- <legend><bean:message key="title.forum.details" /></legend> --%>
<html:form action="/authoring/forum/createForum.do" focus="forum.title"
	styleId="forumForm" onsubmit="return validateForumForm(this);" >
<fieldset>
<%@ include file="includes/basic.jsp" %>
 </fieldset>

  </html:form>
             <logic:present name="topicList">
              <bean:define id="topics" name="topicList" scope="session"/>
              <bean:size id="count" name="topics"/>
              <bean:write name="count" />
             <div align="center">
                   <display:table name="topics" id="topic" class="displayTable" titleclass="displayTitleCell" searchresultsclass="displaySearchResultsCell" export="false" sort="list" summary="Topics" defaultsort="1" pagesize="20" requestURI="open.do">
 	                <display:column property="subject" title="Topic Name" headerClass="displayHeaderCell" class="displayDataCell"/>
                    <display:column title="Actions" sortable="false" headerClass="displayHeaderCell" class="displayDataCellCenter">
                         <html:link page="/authoring/forum/deleteTopic.do" paramId="topicName" paramName="topic" paramProperty="subject">
                         <html:img page="/images/icon_edit.gif" hspace="0" vspace="0" border="0" altKey="forum.topic.label.delete" titleKey="label.delete"/>
                         </html:link>
                    </display:column>
                 </display:table>
             </div>
            </logic:present>

           <logic:present name="topicList">
                 <bean:size id="count" name="topicList" />
                  <logic:notEqual name="count" value="0">
                     <div align="center">
                        <table width="100%" cellspacing="8" align="CENTER"  style="background-color:#ffffff;"  cellspacing="3" cellpadding="3">
                        <tr>
                          		<td valign="MIDDLE"><b>Topic</b></td>
                                <td colspan="2"/>
                        </tr>
                        <logic:iterate name="topicList" id="topic">
                        <tr>
                         	<td valign="MIDDLE"><bean:write name="topic" property="subject"/></td>
                            <td colspan="2" valign="MIDDLE"><html:link page="/authoring/forum/deleteTopic.do"  paramId="topicName" paramName="topic" paramProperty="subject" styleClass="nav"><b><bean:message key="label.delete"/></b></html:link></td>
                        </tr>
                        </logic:iterate>
                        </table>
                     </div>
                     </logic:notEqual>
          </logic:present>



</div>
