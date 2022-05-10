package edu.uoc.lti.jwt.deeplink;

import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.deeplink.DeepLinkingResponse;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.jwt.AlgorithmFactory;
import edu.uoc.lti.ResponseMessageTypeEnum;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class JWSTokenBuilder implements DeepLinkingTokenBuilder {
	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final static String AUTHORIZED_PART = "azp";

	private final String publicKey;
	private final String privateKey;

	@Override
	public String build(DeepLinkingResponse deepLinkingResponse) {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);

		final JwtBuilder builder = Jwts.builder()
						.setIssuer(deepLinkingResponse.getClientId())
						.setAudience(deepLinkingResponse.getPlatformName())
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey())
						.claim(ClaimsEnum.MESSAGE_TYPE.getName(), ResponseMessageTypeEnum.LtiDeepLinkingResponse.name())
						.claim(ClaimsEnum.VERSION.getName(), "1.3.0")
						.claim(ClaimsEnum.DEPLOYMENT_ID.getName(), deepLinkingResponse.getDeploymentId())
						.claim(ClaimsEnum.NONCE.getName(), deepLinkingResponse.getNonce())
						.claim(ClaimsEnum.DEEP_LINKING_CONTENT_ITEMS.getName(), deepLinkingResponse.getItemList());

		if (deepLinkingResponse.getAzp() != null) {
			builder.claim(AUTHORIZED_PART, deepLinkingResponse.getAzp());
		}

		if (deepLinkingResponse.getData() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_DATA.getName(), deepLinkingResponse.getData());
		}

		if (deepLinkingResponse.getMessage() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_MESSAGE.getName(), deepLinkingResponse.getMessage());
		}

		if (deepLinkingResponse.getLog() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_LOG.getName(), deepLinkingResponse.getLog());
		}

		if (deepLinkingResponse.getErrorMessage() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_MESSAGE.getName(), deepLinkingResponse.getErrorMessage());
		}

		if (deepLinkingResponse.getErrorLog() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_LOG.getName(), deepLinkingResponse.getErrorLog());
		}

		return builder.compact();
	}
}
