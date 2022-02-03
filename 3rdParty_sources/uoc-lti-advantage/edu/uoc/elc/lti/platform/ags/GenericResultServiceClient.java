package edu.uoc.elc.lti.platform.ags;

import edu.uoc.elc.lti.exception.UnauthorizedAgsCallException;
import edu.uoc.lti.ags.Result;
import edu.uoc.lti.ags.ResultServiceClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class GenericResultServiceClient {
	private final boolean canRead;
	private final ResultServiceClient resultServiceClient;

	public List<Result> getLineItemResults(String id, Integer limit, Integer page, String userId) {
		if (!canRead) {
			throw new UnauthorizedAgsCallException("getLineItemResults");
		}
		return resultServiceClient.getLineItemResults(id, limit, page, userId);
	}
}
