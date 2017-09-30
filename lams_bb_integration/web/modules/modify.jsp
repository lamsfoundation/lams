<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%--
    Step 1 For Modifing an existing LAMS Lesson
    Set the various attributes for the LAMS lesson in Blackboard

    Step 1 - modify.jsp
    Step 2 - LessonManager?method=modify
--%>
<%@ page errorPage="/error.jsp"%>  
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbNG:genericPage title="Modify A LAMS Lesson" ctxId="ctx">

    <%-- Breadcrumbs --%>
    <bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="true">
        <bbNG:breadcrumb title="Modify A LAMS Lesson" />
    </bbNG:breadcrumbBar>
    
    <%-- Page Header --%>
    <bbNG:pageHeader>
        <bbNG:pageTitleBar title="Modify A LAMS Lesson"/>
    </bbNG:pageHeader>
    
    <%-- Form for the LAMS Lesson Attributes --%>
    <form name="lams_form" id="lams_form" action="../LessonManager?method=modify" method="post" onSubmit="return validateModify();">
        <input type="hidden" name="content_id" value="${param.content_id}">
        <input type="hidden" name="course_id" value="${param.course_id}">
  		
        <bbNG:dataCollection>
	
            <bbNG:step title="Name and describe the lesson">
            
                 <bbNG:dataElement label="Name" isRequired="true" labelFor="title">
                    <input id="title" type="text" name="title" value="${bbContent.title}">
                </bbNG:dataElement>
        
                <bbNG:dataElement label="Description" labelFor="descriptiontext">
                    <bbNG:textbox name="description" ftext="${description}" isContentLinking="true"/>
                </bbNG:dataElement>
                
            </bbNG:step> 
            
            <bbNG:step title="Lesson options">
            
                <bbNG:dataElement label="Do you want to make LAMS visible?">
                    <input type="Radio" name="isAvailable" value="true" 
                    	<c:if test="${bbContent.isAvailable}">checked</c:if>>Yes
                    <input type="Radio" name="isAvailable" value="false" 
                    	<c:if test="${!bbContent.isAvailable}">checked</c:if>>No
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Do you want to add a mark/completion column in Gradecenter?" labelFor="isGradecenter">
                    <input type="Radio" name="isGradecenter" value="true" 
                    	<c:if test="${bbContent.isDescribed}">checked</c:if>>Yes
                    <input type="Radio" name="isGradecenter" value="false" 
                    	<c:if test="${!bbContent.isDescribed}">checked</c:if>>No
                </bbNG:dataElement>                
                
                <bbNG:dataElement label="Track number of views">
                    <input type="radio" name="isTracked" value="true" 
                    	<c:if test="${bbContent.isTracked}">checked</c:if>>Yes
                    <input type="radio" name="isTracked" value="false" 
                    	<c:if test="${!bbContent.isTracked}">checked</c:if>>No
                </bbNG:dataElement>
                
                <bbNG:dataElement label="Choose date restrictions">
                    <%--
                        Show start and end dates if they have been set
                        If non ehave been set, leave the tags out so that Blackboard puts the default dates in
                    --%>
                    <c:choose>
                    <c:when test="${startDate==null && endDate==null}">
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" />
                    </c:when>
                    <c:when test="${endDate==null}">
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" startDateTime="${startDate}" />
                    </c:when>
                    <c:when test="${startDate==null}">
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" endDateTime="${endDate}" />
                    </c:when>
                    <c:otherwise>
                    	<bbNG:dateRangePicker baseFieldName="lessonAvailability" showTime="true" startDateTime="${startDate}" endDateTime="${endDate}" />
                    </c:otherwise>
                    </c:choose>
                </bbNG:dataElement>   
                
            </bbNG:step> 
            
            <bbNG:stepSubmit title="Start Lesson" 
            			cancelUrl="/webapps/blackboard/content/listContentEditable.jsp?content_id=${param.content_id}&course_id=${param.course_id}"/>

        </bbNG:dataCollection>
    </form>

    <bbNG:jsBlock>
        <script type="text/javascript">            
            // Do form vaildation. Check that a title has been supplied 
            function validateModify() {
                var title = rettrim(document.lams_form.title.value);
                if ((title == "")||(title == null)) {
                    alert("The title is empty. Please enter a title for the LAMS sequence.");
                    return false;
                }        
            }
            
            // Utility function to trim
            function rettrim(stringToTrim) {
                return stringToTrim.replace(/^\s+|\s+$/g,"");
            }

        </script>
    </bbNG:jsBlock>
    
</bbNG:genericPage>

