package edu.uoc.elc.lti.platform.ags;

import edu.uoc.elc.lti.exception.UnauthorizedAgsCallException;
import edu.uoc.lti.ags.LineItem;
import edu.uoc.lti.ags.LineItemServiceClient;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class ToolLineItemServiceClient {
	private final URI lineItemsUri;
	private final String resourceLinkId;
	private final boolean canRead;
	private final boolean canManage;
	private final LineItemServiceClient lineItemServiceClient;

	public List<LineItem> getLineItems(Integer limit, Integer page, String tag, String resourceId) {
		if (!canRead) {
			throw new UnauthorizedAgsCallException("getLineItems");
		}
		lineItemServiceClient.setServiceUri(lineItemsUri);
		return lineItemServiceClient.getLineItems(limit, page, resourceLinkId, tag, resourceId);
	}

	public LineItem createLineItem(LineItem lineItem) {
		if (!canManage) {
			throw new UnauthorizedAgsCallException("createLineItem");
		}
		lineItemServiceClient.setServiceUri(lineItemsUri);
		return lineItemServiceClient.createLineItem(lineItem);
	}

	public LineItem getLineItem(String id) {
		if (!canRead) {
			throw new UnauthorizedAgsCallException("getLineItems");
		}
		lineItemServiceClient.setServiceUri(lineItemsUri);
		return lineItemServiceClient.getLineItem(id);
	}

	public LineItem updateLineItem(String id, LineItem lineItem) {
		if (!canManage) {
			throw new UnauthorizedAgsCallException("updateLineItem");
		}
		lineItemServiceClient.setServiceUri(lineItemsUri);
		return lineItemServiceClient.updateLineItem(id, lineItem);
	}

	public void deleteLineItem(String id) {
		if (!canManage) {
			throw new UnauthorizedAgsCallException("deleteLineItem");
		}
		lineItemServiceClient.setServiceUri(lineItemsUri);
		lineItemServiceClient.deleteLineItem(id);
	}
}
