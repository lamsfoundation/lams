<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
	</lams:head>
	<body>
		<div style="display: none;">
	
			<div id="itemList">
				<%@ include file="parts/headingList.jsp"%>
			</div>
		
			<script lang="javascript">
				hideMessage();
				var obj = document.getElementById('itemListArea');
				obj.innerHTML= document.getElementById("itemList").innerHTML;
			</script>
		
		</div>
	</body>
</lams:html>




