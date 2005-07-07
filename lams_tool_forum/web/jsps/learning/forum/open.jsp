<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>
<%--
<%@ taglib uri="displaytag" prefix="display" %>
<%@ taglib uri="jmage" prefix="jm" %>
--%>

<html:errors property="error" />
<div align="center">
<%-- <legend><bean:message key="title.forum.details" /></legend>
<fieldset>
<%@ include file="includes/basic.jsp" %>
 </fieldset>
   --%>
<div>
    <%--
        <bean:define id="topics" name="forumTopics"/>
        <display:table name="topics" id="topic" class="displayTable" titleclass="displayTitleCell" searchresultsclass="displaySearchResultsCell" export="false" sort="list" summary="Systems" defaultsort="1" pagesize="20" requestURI="open.do">
 	    <display:column property="subject" title="Topic Name" headerClass="displayHeaderCell" class="displayDataCell"/>
        </display:table
     --%>
        <logic:present name="forumTopics">
                 <bean:size id="count" name="forumTopics" />
                  <logic:notEqual name="count" value="0">
                     <div align="center">
                        <table width="100%" cellspacing="8" align="CENTER"  style="background-color:#ffffff;"  cellspacing="3" cellpadding="3">
                        <tr>
                          		<td valign="MIDDLE"><b>Topic Name</b></td>
                                <td colspan="2"/>
                        </tr>
                        <logic:iterate name="forumTopics" id="topic">
                        <tr>
                         	<td valign="MIDDLE"><bean:write name="topic" property="subject"/></td>
                            <td colspan="2" valign="MIDDLE"><html:link page="/learning/message/openTopic.do"  paramId="topicId" paramName="topic" paramProperty="id" styleClass="nav"><b><bean:message key="label.open"/></b></html:link></td>
                        </tr>
                        </logic:iterate>
                          </table>
                     </div>
                     </logic:notEqual>
            </logic:present>

</div>
