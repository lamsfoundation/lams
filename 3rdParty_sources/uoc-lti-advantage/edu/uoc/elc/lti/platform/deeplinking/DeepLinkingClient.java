package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.DeepLinkingResponse;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.deeplink.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class DeepLinkingClient {

	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;

	private final String platformName;
	private final String clientId;
	private final String azp;

	private final String deploymentId;
	private final String nonce;
	private final Settings settings;

	@Getter
	private List<Item> itemList = new ArrayList<>();

	public boolean canAddItem() {
		return settings.isAccept_multiple() || itemList.size() == 0;
	}

	public void addItem(Item item) {
		// check for multiple content item
		if (!canAddItem()) {
			throw new InvalidLTICallException("Platform doesn't allow multiple content items");
		}

		ItemValidator itemValidator = ItemValidatorFactory.itemValidatorFor(item, settings);
		if (!itemValidator.isValid(item)) {
			throw new InvalidLTICallException(itemValidator.getMessage());
		}

		itemList.add(item);
	}

	public URL getReturnUrl() {
		try {
			return new URL(settings.getDeep_link_return_url());
		} catch (MalformedURLException e) {
			throw new InvalidLTICallException(e.getMessage());
		}
	}

	public String buildJWT() {
		DeepLinkingResponse deepLinkingResponse = new DeepLinkingResponse(platformName,
						clientId, azp, deploymentId, nonce, settings.getData(), itemList);
		return deepLinkingTokenBuilder.build(deepLinkingResponse);
	}
}
