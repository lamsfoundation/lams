<%@ include file="/common/taglibs.jsp"%>

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


