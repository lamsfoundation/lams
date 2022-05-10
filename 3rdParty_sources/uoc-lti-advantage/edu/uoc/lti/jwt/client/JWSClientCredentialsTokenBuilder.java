package edu.uoc.lti.jwt.client;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

import edu.uoc.lti.clientcredentials.ClientCredentialsRequest;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.AlgorithmFactory;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class JWSClientCredentialsTokenBuilder implements ClientCredentialsTokenBuilder {

	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final String publicKey;
	private final String privateKey;

	@Override
	public String build(ClientCredentialsRequest request) {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);

		return Jwts.builder()
						//.setHeaderParam("kid", request.getKid())
						.setHeaderParam("typ", "JWT")
						.setIssuer(request.getClientId())
						.setSubject(request.getClientId())
						.setAudience(request.getOauth2Url())
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey())
						.setId(UUID.randomUUID().toString())
						.compact();
	}
}
