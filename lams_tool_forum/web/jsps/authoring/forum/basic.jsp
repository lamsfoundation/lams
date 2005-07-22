<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/messages.jsp" %>

<html:errors property="error" />
<html:javascript formName="forumForm" dynamicJavascript="false" staticJavascript="false"/>
<div align="center">
<%-- <legend><bean:message key="title.forum.details" /></legend> --%>
<html:form action="/authoring/forum/createForum.do" focus="forum.title"
	styleId="forumForm" onsubmit="return validateForumForm(this);" >
<fieldset>
<%@ include file="includes/basic.jsp" %>
 </fieldset>

  </html:form>
        
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
