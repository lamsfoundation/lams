package edu.uoc.lti.claims;

import java.util.Date;

/**
 * @author Created by xaracil@uoc.edu
 */
public interface ClaimAccessor {

	void decode(String token);

	/**
	 * Get the kid
	 * @return claim content identifying the kid
	 */
	String getKId();

	/**
	 * Get the issuer
	 * @return claim content identifying the issuer
	 */
	String getIssuer();

	/**
	 * Get the subject
	 * @return claim content identifying the subject
	 */
	String getSubject();

	/**
	 * Get the audience
	 * @return claim content identifying the audience
	 */
	String getAudience();


	/**
	 * Get the authorized part
	 * @return claim content identifying the azp
	 */
	String getAzp();

	/**
	 * Get the issued date
	 * @return claim content identifying the issuetAt
	 */
	Date getIssuedAt();

	/**
	 * Get the expiration date
	 * @return claim content identifying the expiration date
	 */
	Date getExpiration();

	/**
	 * Get claim as String object
	 * @param name claim to get
	 * @return String with claim's content or null if not found
	 */
	String get(ClaimsEnum name);

	/**
	 * Get claim content as a given type
	 * @param name claim to get
	 * @param returnClass type to return claim as
	 * @param <T> type to return claim as
	 * @return object of type T with claim's content or null if not found
	 */
	<T> T get(ClaimsEnum name, Class<T> returnClass);
}
