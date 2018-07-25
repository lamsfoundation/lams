<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbData:context id="ctx">
	<bbNG:learningSystemPage>
		<bbNG:jsFile href="includes/javascript/openLamsPage.js" />
		<bbNG:jsBlock>
	        <script type="text/javascript">
	            var authorWin = null;
			</script>
		</bbNG:jsBlock>
	
		<bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="false">
			<bbNG:breadcrumb>LAMS Author</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		
		<bbNG:pageHeader>
			<bbNG:pageTitleBar title="LAMS Author" />
		</bbNG:pageHeader>
		
		To launch LAMS Author, please, click <a href="#" onclick="openAuthor('${param.course_id}'); return false;">here</a>.
	</bbNG:learningSystemPage>
</bbData:context>
