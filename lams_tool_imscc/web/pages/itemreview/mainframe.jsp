<%@ include file="/common/taglibs.jsp" %>

<frameset rows="95,*" frameborder="no">
	<frame src="<c:url value="/pages/itemreview/initnav.jsp"/>?mode=${mode}&itemIndex=${itemIndex}&itemUid=${itemUid}&toolSessionID=${toolSessionID}&sessionMapID=${sessionMapID}" 
			name=headerFrame" marginheight="0" scrolling="YES">
	<frame src="<c:url value='${commonCartridgeItemReviewUrl}'/>" name="commonCartridgeFrame" marginheight="0" scrolling="YES">
</frameset>
