			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="reflectionInstructions">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

					<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> 
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
					</c:choose>

					<c:if test="${mode != 'teacher'}">
						<a href="#nogo" id="finishButton" onclick="return continueReflect()" cssClass="btn btn-sm btn-secondary mt-2">
							<fmt:message key="label.edit" />
						</a>
					</c:if>
				</div>
			</div>
