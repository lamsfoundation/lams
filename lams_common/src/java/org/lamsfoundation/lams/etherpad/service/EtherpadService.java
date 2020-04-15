package org.lamsfoundation.lams.etherpad.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.etherpad.EtherpadException;
import org.lamsfoundation.lams.etherpad.util.EtherpadUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

import net.gjerull.etherpad.client.EPLiteClient;
import net.gjerull.etherpad.client.EPLiteException;

public class EtherpadService implements IEtherpadService {

    private EPLiteClient client = null;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> createPad(String groupIdentifier) throws EtherpadException {
	EPLiteClient client = getClient();

	Map<String, Object> result = new HashMap<>();
	result.put("client", client);

	// add Etherpad instance ID so we can group LAMS instances to share Etherpads
	String instanceID = Configuration.get(ConfigurationKeys.ETHERPAD_INSTANCE_ID);
	if (StringUtils.isNotBlank(instanceID)) {
	    groupIdentifier = instanceID + "-" + groupIdentifier;
	}
	// create Etherpad Group associated with this session
	Map<String, Object> map = client.createGroupIfNotExistsFor(groupIdentifier);
	String groupId = (String) map.get("groupID");
	result.put("groupId", groupId);
	String padId = EtherpadUtil.getPadId(groupId);
	result.put("padId", padId);

	boolean isPadAlreadyCreated = false;
	try {
	    client.createGroupPad(groupId, DEFAULT_PAD_NAME);
	} catch (EPLiteException e) {
	    // allow recreating existing pads
	    if ("padName does already exist".equals(e.getMessage())) {
		isPadAlreadyCreated = true;
		// throw exception in all other cases
	    } else {
		throw new EtherpadException("Exception while creating a group etherpad", e);
	    }
	}

	result.put("isPadAlreadyCreated", isPadAlreadyCreated);

	// gets read-only id
	String etherpadReadOnlyId = (String) client.getReadOnlyID(padId).get("readOnlyID");
	result.put("readOnlyId", etherpadReadOnlyId);
	return result;
    }

    @Override
    public EPLiteClient getClient() throws EtherpadException {
	if (client == null) {
	    // get the API key from the configuration and create EPLiteClient using it
	    String etherpadServerUrl = Configuration.get(ConfigurationKeys.ETHERPAD_SERVER_URL);
	    String etherpadApiKey = Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY);
	    if (StringUtils.isBlank(etherpadServerUrl) || StringUtils.isBlank(etherpadApiKey)) {
		throw new EtherpadException("Etherpad is not configured in sysadmin console");
	    }

	    // create EPLiteClient
	    client = new EPLiteClient(etherpadServerUrl, etherpadApiKey);
	}
	return client;
    }

    /**
     * Constructs cookie to be stored at a clientside browser.
     */
    @Override
    public Cookie createCookie(String etherpadSessionIds) throws EtherpadException {
	String etherpadServerUrl = Configuration.get(ConfigurationKeys.ETHERPAD_SERVER_URL);
	URI uri;
	try {
	    uri = new URI(etherpadServerUrl);
	} catch (URISyntaxException e) {
	    throw new EtherpadException("Etherpad is not correctly configured in sysadmin console", e);
	}
	//regex to get the top level part of a domain
	Pattern p = Pattern.compile(
		"^(?:\\w+://)?[^:?#/\\s]*?([^.\\s]+\\.(?:[a-z]{2,}|co\\.uk|org\\.uk|ac\\.uk|edu\\.au|org\\.au|com\\.au|edu\\.sg|com\\.sg|net\\.sg|org\\.sg|gov\\.sg|per\\.sg))(?:[:?#/]|$)");
	// eg: uri.getHost() will return "www.foo.com"
	Matcher m = p.matcher(uri.getHost());
	String topLevelDomain = m.matches() ? "." + m.group(1) : uri.getHost();

	Cookie etherpadSessionCookie = new Cookie("sessionID", etherpadSessionIds);
	if (!topLevelDomain.equals("localhost")) {
	    etherpadSessionCookie.setDomain(topLevelDomain);
	}
	// A negative value means that the cookie is not stored persistently and will be deleted when the Web browser
	// exits. A zero value causes the cookie to be deleted.
	etherpadSessionCookie.setMaxAge(-1);
	etherpadSessionCookie.setPath("/");

	return etherpadSessionCookie;
    }

    /**
     * Returns valid Etherpad session. Returns existing one if finds such one and creates the new one otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public String getExistingSessionID(String authorId, String etherpadGroupId, Map<String, Object> etherpadSessions)
	    throws EtherpadException {
	String etherpadSessionId = null;

	// search for already existing user's session
	boolean isValidForMoreThan1Hour = false;
	for (String etherpadSessionIdIter : etherpadSessions.keySet()) {
	    Map<String, Object> sessessionAttributes = (Map<String, Object>) etherpadSessions
		    .get(etherpadSessionIdIter);
	    String groupIdIter = (String) sessessionAttributes.get("groupID");
	    if (groupIdIter.equals(etherpadGroupId)) {

		// check session expiration date
		long validUntil = (Long) sessessionAttributes.get("validUntil") * 1000;
		long now = System.currentTimeMillis();
		isValidForMoreThan1Hour = ((validUntil - now) > 0) && ((validUntil - now) >= 60 * 60 * 1000);

		//use existing session if it's valid for more than 1 hour
		if (isValidForMoreThan1Hour) {
		    etherpadSessionId = etherpadSessionIdIter;
		    break;

		} else {
		    // can't delete expired sessions as Etherpad throws an exception. Nonetheless it returns expired
		    // ones when client.listSessionsOfAuthor(authorId) is requested
		}
	    }

	}

	// if session with validity of more than 1 hour doesn't exist yet  - create it
	if (etherpadSessionId == null) {
	    EPLiteClient client = getClient();
	    Map<String, Object> map2 = client.createSession(etherpadGroupId, authorId, 24);
	    etherpadSessionId = (String) map2.get("sessionID");
	}

	return etherpadSessionId;
    }

    @SuppressWarnings("unchecked")
    public String createAuthor(Integer userId, String userName) throws EtherpadException {
	EPLiteClient client = getClient();

	Map<String, String> map = client.createAuthorIfNotExistsFor(userId.toString(), userName);
	return map.get("authorID");
    }
}