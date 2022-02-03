package edu.uoc.elc.lti.platform.ags;

import edu.uoc.elc.lti.exception.UnauthorizedAgsCallException;
import edu.uoc.elc.lti.platform.ags.empty.EmptyResultServiceClient;
import edu.uoc.elc.lti.platform.ags.empty.EmptyScoreServiceClient;
import edu.uoc.elc.lti.platform.ags.empty.EmptyToolLineItemServiceClient;
import edu.uoc.elc.lti.tool.AssignmentGradeService;
import edu.uoc.elc.lti.tool.ResourceLink;
import edu.uoc.lti.ags.LineItemServiceClient;
import edu.uoc.lti.ags.ResultServiceClient;
import edu.uoc.lti.ags.ScoreServiceClient;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class AgsClientFactory {
	private final AssignmentGradeService assignmentGradeService;
	private final ResourceLink resourceLink;

	public GenericResultServiceClient getResultServiceClient(ResultServiceClient resultServiceClient) {
		if (!hasAgsService()) {
			return new EmptyResultServiceClient();
		}
		return new GenericResultServiceClient(assignmentGradeService.canReadResults(), resultServiceClient);
	}

	public GenericScoreServiceClient getScoreServiceClient(ScoreServiceClient scoreServiceClient) {
		if (!hasAgsService()) {
			return new EmptyScoreServiceClient();
		}
		return new GenericScoreServiceClient(assignmentGradeService.canScore(), scoreServiceClient);
	}

	public ToolLineItemServiceClient getLineItemServiceClient(LineItemServiceClient lineItemServiceClient) {
		if (!hasAgsService()) {
			return new EmptyToolLineItemServiceClient();
		}
		return new ToolLineItemServiceClient(getServerUri(),
						getResourceLinkId(),
						assignmentGradeService.canReadLineItems(),
						assignmentGradeService.canManageLineItems(),
						lineItemServiceClient);
	}

	public boolean hasAgsService() {
		return assignmentGradeService != null;
	}

	private URI getServerUri() {
		try {
			return new URI(assignmentGradeService.getLineitems());
		} catch (URISyntaxException e) {
			throw new UnauthorizedAgsCallException("Lineitems URI is invalid");
		}
	}

	private String getResourceLinkId() {
		if (resourceLink != null) {
			return resourceLink.getId();
		}
		return null;
	}
}
