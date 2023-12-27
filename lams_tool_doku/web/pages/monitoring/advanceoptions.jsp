<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.lockWhenFinished}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.use.select.leader.tool.output" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.useSelectLeaderToolOuput}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.allow.multiple.leaders" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.allowMultipleLeaders}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.show.chat" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.showChat}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.show.line.numbers" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.showLineNumbers}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
		
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.gallery.walk.enabled" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.dokumaran.galleryWalkEnabled}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
		
	<c:choose>
		<c:when test="${sessionMap.dokumaran.galleryWalkEnabled}">
			<tr>
				<td>
					<fmt:message key="label.authoring.advance.gallery.walk.read.only" />
				</td>
				
				<td>
					<c:choose>
						<c:when test="${sessionMap.dokumaran.galleryWalkReadOnly}">
							<fmt:message key="label.on" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.off" />
						</c:otherwise>
					</c:choose>	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>
