<%@ include file="/common/taglibs.jsp"%>

<c:set var="summaryList" value="${sessionMap.summaryList}" />
<c:set var="title" value="${sessionMap.title}" />
<c:set var="instructions" value="${sessionMap.instructions}" />
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="export.title" /> </title>

	<style type="text/css">
    	#scratches {margin: 40px 0px; border-spacing: 0;}
    	#scratches tr td { padding: 12px 15px; }
    	#scratches a, #scratches a:hover {border-bottom: none;}
    	.scartchie-image {border: 0;}
    </style>

</lams:head>
<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${sessionMap.scratchie.title}"/>
		</h1>
		
		<div>
			<c:out value="${sessionMap.scratchie.instructions}" escapeXml="false"/>
		</div>

		<table id="scratches" class="alternative-color">
			<c:forEach var="item" items="${sessionMap.itemList}">
				<tr id="tr${item.uid}">
					<td>
						<c:out value="${item.title}"/>
						<br/>
						<c:out value="${item.description}" escapeXml="false"/>
					</td>
					<td align="right">
						<c:choose>
							<c:when test="${item.scratched && item.correct}">
								<img src="./includes/scratchie-correct.gif" class="scartchie-image">
							</c:when>
							<c:when test="${item.scratched && !item.correct}">
								<img src="./includes/scratchie-wrong.gif" class="scartchie-image">
							</c:when>
							<c:otherwise>
								<img src="./includes/scratchie.gif" class="scartchie-image">
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>

	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>
