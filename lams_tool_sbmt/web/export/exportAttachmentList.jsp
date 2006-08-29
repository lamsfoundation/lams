<%@ taglib uri="tags-core" prefix="c"%>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="attachmentList" value="${sessionMap.attachmentList}"/>

<c:forEach items="${attachmentList}" var="listElement"><c:out value="${listElement}"/>  
</c:forEach>