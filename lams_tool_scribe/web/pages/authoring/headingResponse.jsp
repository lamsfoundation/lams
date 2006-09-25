<%@ include file="/common/taglibs.jsp"%>

<div style="display: none;">

	<div id="itemList">
		<%@ include file="parts/headingList.jsp"%>
	</div>

	<script type="text/javascript">
		if(window.top != null){
			window.top.hideMessage();
			var obj = window.top.document.getElementById('itemListArea');
			obj.innerHTML= document.getElementById("itemList").innerHTML;
		}
	</script>

</div>


