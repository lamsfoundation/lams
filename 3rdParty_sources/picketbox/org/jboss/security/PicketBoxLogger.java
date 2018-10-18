package org.jboss.security;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.*;

import javax.naming.NamingException;
import javax.security.auth.Subject;
import java.net.URL;
import java.security.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@MessageLogger(projectCode = "PBOX", length = 5)
public interface PicketBoxLogger extends BasicLogger {

    PicketBoxLogger LOGGER = Logger.getMessageLogger(PicketBoxLogger.class,
            PicketBoxLogger.class.getPackage().getName());

    PicketBoxLogger AUDIT_LOGGER = Logger.getMessageLogger(PicketBoxLogger.class,
            PicketBoxLogger.class.getPackage().getName() + ".audit");

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 200, value = "Begin isValid, principal: %s, cache entry: %s")
    void traceBeginIsValid(Principal principal, String cacheEntry);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 201, value = "End isValid, result = %s")
    void traceEndIsValid(boolean isValid);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 202, value = "Flushing all entries from security cache")
    void traceFlushWholeCache();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 203, value = "Flushing %s from security cache")
    void traceFlushCacheEntry(String key);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 204, value = "Begin validateCache, domainInfo: %s, credential class: %s")
    void traceBeginValidateCache(String info, Class<?> credentialClass);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 205, value = "End validateCache, result = %s")
    void traceEndValidteCache(boolean isValid);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 206, value = "Login failure")
    void debugFailedLogin(@Cause Throwable t);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 207, value = "updateCache, input subject: %s, cached subject: %s")
    void traceUpdateCache(String inputSubject, String cachedSubject);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 208, value = "Inserted cache info: %s")
    void traceInsertedCacheInfo(String cacheInfo);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 209, value = "defaultLogin, principal: %s")
    void traceDefaultLoginPrincipal(Principal principal);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 210, value = "defaultLogin, login context: %s, subject: %s")
    void traceDefaultLoginSubject(String loginContext, String subject);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 211, value = "Cache entry logout failed")
    void traceCacheEntryLogoutFailure(@Cause Throwable t);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 212, value = "Exception loading file %s")
    void errorLoadingConfigFile(String filename, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 213, value = "Failed to convert username to byte[] using UTF-8")
    void errorConvertingUsernameUTF8(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 214, value = "Charset %s not found. Using platform default")
    void errorFindingCharset(String charSet, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 215, value = "Unsupported hash encoding format: %s")
    void unsupportedHashEncodingFormat(String hashEncoding);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 216, value = "Password hash calculation failed")
    void errorCalculatingPasswordHash(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 217, value = "Failed to check if the strong jurisdiction policy files have been installed")
    void errorCheckingStrongJurisdictionPolicyFiles(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 218, value = "bindDN is not found")
    void traceBindDNNotFound();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 219, value = "Exception while decrypting bindCredential")
    void errorDecryptingBindCredential(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 220, value = "Logging into LDAP server with env %s")
    void traceLDAPConnectionEnv(Properties env);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 221, value = "Begin getAppConfigurationEntry(%s), size: %s")
    void traceBeginGetAppConfigEntry(String appName, int size);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 222, value = "getAppConfigurationEntry(%s), no entry found, trying parent config %s")
    void traceGetAppConfigEntryViaParent(String appName, String parentConfig);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 223, value = "getAppConfigurationEntry(%s), no entry in parent config, trying default %s")
    void traceGetAppConfigEntryViaDefault(String appName, String defaultConfig);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 224, value = "End getAppConfigurationEntry(%s), AuthInfo: %s")
    void traceEndGetAppConfigEntryWithSuccess(String appName, String authInfo);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 225, value = "End getAppConfigurationEntry(%s), failed to find entry")
    void traceEndGetAppConfigEntryWithFailure(String appName);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 226, value = "addAppConfig(%s), AuthInfo: %s")
    void traceAddAppConfig(String appName, String authInfo);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 227, value = "removeAppConfig(%s)")
    void traceRemoveAppConfig(String appName);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 228, value = "Failed to find config: %s")
    void warnFailureToFindConfig(String loginConfig);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 229, value = "Begin loadConfig, loginConfigURL: %s")
    void traceBeginLoadConfig(URL configURL);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 230, value = "End loadConfig, loginConfigURL: %s")
    void traceEndLoadConfigWithSuccess(URL configURL);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 231, value = "End loadConfig, failed to load config: %s")
    void warnEndLoadConfigWithFailure(URL configURL, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 232, value = "Try loading config as XML from %s")
    void debugLoadConfigAsXML(URL configURL);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 233, value = "Failed to load config as XML. Try loading as Sun format from %s")
    void debugLoadConfigAsSun(URL configURL, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 234, value = "Invalid or misspelled module option: %s")
    void warnInvalidModuleOption(String option);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 235, value = "Error getting request from policy context")
    void debugErrorGettingRequestFromPolicyContext(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 236, value = "Begin initialize method")
    void traceBeginInitialize();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 237, value = "Saw unauthenticated indentity: %s")
    void traceUnauthenticatedIdentity(String name);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 238, value = "Failed to create custom unauthenticated identity")
    void warnFailureToCreateUnauthIdentity(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 239, value = "End initialize method")
    void traceEndInitialize();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 240, value = "Begin login method")
    void traceBeginLogin();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 241, value = "End login method, isValid: %s")
    void traceEndLogin(boolean loginOk);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 242, value = "Begin commit method, overall result: %s")
    void traceBeginCommit(boolean loginOk);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 243, value = "Begin logout method")
    void traceBeginLogout();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 244, value = "Begin abort method, overall result: %s")
    void traceBeginAbort(boolean loginOk);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 245, value = "Found security domain: %s")
    void traceSecurityDomainFound(String domain);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 246, value = "The JSSE security domain %s is not valid. All authentication using this login module will fail!")
    void errorGettingJSSESecurityDomain(String domain);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 247, value = "Unable to find the security domain %s")
    void errorFindingSecurityDomain(String domain, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 248, value = "Failed to create X509CertificateVerifier")
    void errorCreatingCertificateVerifier(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 249, value = "javax.security.auth.login.password is not a X509Certificate")
    void debugPasswordNotACertificate();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 250, value = "Authenticating using unauthenticated identity %s")
    void traceUsingUnauthIdentity(String unauthenticatedIdentity);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 251, value = "Failed to create identity for alias %s")
    void debugFailureToCreateIdentityForAlias(String alias, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 252, value = "Begin getAliasAndCert method")
    void traceBeginGetAliasAndCert();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 253, value = "Found certificate, serial number: %s, subject DN: %s")
    void traceCertificateFound(String serialNumber, String subjectDN);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 254, value = "CallbackHandler did not provide a credential")
    void warnNullCredentialFromCallbackHandler();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 255, value = "End getAliasAndCert method")
    void traceEndGetAliasAndCert();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 256, value = "Begin validateCredential method")
    void traceBeginValidateCredential();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 257, value = "Validating certificate using verifier %s")
    void traceValidatingUsingVerifier(Class<?> verifier);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 258, value = "Failed to find certificate for alias &%s")
    void warnFailureToFindCertForAlias(String alias, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 259, value = "Failed to validate certificate: SecurityDomain, Keystore or certificate is null")
    void warnFailureToValidateCertificate();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 260, value = "End validateCredential method, result: %s")
    void traceEndValidateCredential(boolean isValid);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 261, value = "Failed to load users/passwords/roles files")
    void errorLoadingUserRolesPropertiesFiles(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 262, value = "Module options [dsJndiName: %s, principalsQuery: %s, rolesQuery: %s, suspendResume: %s]")
    void traceDBCertLoginModuleOptions(String dsJNDIName, String principalsQuery, String rolesQuery, boolean suspendResume);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 263, value = "Executing query %s with username %s")
    void traceExecuteQuery(String query, String username);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 264, value = "Failed to create principal %s")
    void debugFailureToCreatePrincipal(String name, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 265, value = "The security domain %s has been disabled. All authentication will fail")
    void errorUsingDisabledDomain(String securityDomain);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 266, value = "Binding username %s")
    void traceBindingLDAPUsername(String username);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 267, value = "Rejecting empty password as allowEmptyPasswords option has not been set to true")
    void traceRejectingEmptyPassword();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 268, value = "Assigning user to role %s")
    void traceAssignUserToRole(String role);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 269, value = "Failed to parse %s as number, using default value %s")
    void debugFailureToParseNumberProperty(String property, long defaultValue);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 270, value = "Failed to query %s from %s")
    void debugFailureToQueryLDAPAttribute(String attributeName, String contextName, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 271, value = "Logged into LDAP server, context: %s")
    void traceSuccessfulLogInToLDAP(String context);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 272, value = "Rebind security principal to %s")
    void traceRebindWithConfiguredPrincipal(String principal);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 273, value = "Found user roles context DN: %s")
    void traceFoundUserRolesContextDN(String context);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 274, value = "Searching rolesCtxDN %s with roleFilter: %s, filterArgs: %s, roleAttr: %s, searchScope: %s, searchTimeLimit: %s")
    void traceRolesDNSearch(String dn, String roleFilter, String filterArgs, String roleAttr, int searchScope, int searchTimeLimit);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 275, value = "Checking search result %s")
    void traceCheckSearchResult(String searchResult);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 276, value = "Following roleDN %s")
    void traceFollowRoleDN(String roleDN);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 277, value = "No attribute %s found in search result %s")
    void debugFailureToFindAttrInSearchResult(String attrName, String searchResult);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 278, value = "Failed to locate roles")
    void debugFailureToExecuteRolesDNSearch(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 279, value = "The real host for trust is %s")
    void debugRealHostForTrust(String host);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 280, value = "Failed to load properties file %s")
    void debugFailureToLoadPropertiesFile(String fileName, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 281, value = "Password hashing activated, algorithm: %s, encoding: %s, charset: %s, callback: %s, storeCallBack: %s")
    void debugPasswordHashing(String algorithm, String encoding, String charset, String callback, String storeCallBack);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 282, value = "Failed to instantiate class %s")
    void debugFailureToInstantiateClass(String className, @Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 283, value = "Bad password for username %s")
    void debugBadPasswordForUsername(String username);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 284, value = "Created DigestCallback %s")
    void traceCreateDigestCallback(String callback);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 285, value = "Adding role %s to group %s")
    void traceAdditionOfRoleToGroup(String roleName, String groupName);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 286, value = "Attempting to load resource %s")
    void traceAttemptToLoadResource(String resourceURL);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 287, value = "Failed to open properties file from URL")
    void debugFailureToOpenPropertiesFromURL(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 288, value = "Properties file %s loaded, users: %s")
    void tracePropertiesFileLoaded(String fileName, Set<?> users);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 289, value = "JACC delegate access denied [permission: %s, caller: %s, roles: %s")
    void debugJACCDeniedAccess(String permission, Subject caller, String roles);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 290, value = "No method permissions assigned to method: %s, interface: %s")
    void traceNoMethodPermissions(String methodName, String interfaceName);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 291, value = "Method: %s, interface: %s, required roles: %s")
    void debugEJBPolicyModuleDelegateState(String methodName, String interfaceName, String requiredRoles);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 292, value = "Insufficient method permissions [principal: %s, EJB name: %s, method: %s, interface: %s, required roles: %s, principal roles: %s, run-as roles: %s]")
    void debugInsufficientMethodPermissions(Principal ejbPrincipal, String ejbName, String methodName, String interfaceName,
                                            String requiredRoles, String principalRoles, String runAsRoles);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 293, value = "Exception caught")
    void debugIgnoredException(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 294, value = "Check is not resourcePerm, userDataPerm or roleRefPerm")
    void debugInvalidWebJaccCheck();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 295, value = "hasResourcePermission, permission: %s, allowed: %s")
    void traceHasResourcePermission(String permission, boolean allowed);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 296, value = "hasRolePermission, permission: %s, allowed: %s")
    void traceHasRolePermission(String permission, boolean allowed);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 297, value = "hasUserDataPermission, permission: %s, allowed: %s")
    void traceHasUserDataPermission(String permission, boolean allowed);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 298, value = "Requisite module %s failed")
    void debugRequisiteModuleFailure(String moduleName);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 299, value = "Required module %s failed")
    void debugRequiredModuleFailure(String moduleName);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 300, value = "Denied: matched excluded set, permission %s")
    void traceImpliesMatchesExcludedSet(Permission permission);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 301, value = "Allowed: matched unchecked set, permission %s")
    void traceImpliesMatchesUncheckedSet(Permission permission);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 302, value = "Protection domain principals: %s")
    void traceProtectionDomainPrincipals(List<String> principalNames);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 303, value = "Not principals found in protection domain %s")
    void traceNoPrincipalsInProtectionDomain(ProtectionDomain domain);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 304, value = "Checking role: %s, permissions: %s")
    void debugImpliesParameters(String roleName, Permissions permissions);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 305, value = "Checking result, implies: %s")
    void debugImpliesResult(boolean implies);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 306, value = "No PolicyContext found for contextID %s")
    void traceNoPolicyContextForId(String contextID);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 307, value = "Constructing JBossPolicyConfiguration with contextID %s")
    void debugJBossPolicyConfigurationConstruction(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 308, value = "addToExcludedPolicy, permission: %s")
    void traceAddPermissionToExcludedPolicy(Permission permission);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 309, value = "addToExcludedPolicy, permission collection: %s")
    void traceAddPermissionsToExcludedPolicy(PermissionCollection permissions);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 310, value = "addToRole, permission: %s")
    void traceAddPermissionToRole(Permission permission);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 311, value = "addToRole, permission collection: %s")
    void traceAddPermissionsToRole(PermissionCollection permissions);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 312, value = "addToUncheckedPolicy, permission: %s")
    void traceAddPermissionToUncheckedPolicy(Permission permission);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 313, value = "addToUncheckedPolicy, permission collection: %s")
    void traceAddPermissionsToUncheckedPolicy(PermissionCollection permissions);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 314, value = "commit, contextID: %s")
    void tracePolicyConfigurationCommit(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 315, value = "delete, contextID: %s")
    void tracePolicyConfigurationDelete(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 316, value = "linkConfiguration, link to contextID: %s")
    void traceLinkConfiguration(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 317, value = "removeExcludedPolicy, contextID: %s")
    void traceRemoveExcludedPolicy(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 318, value = "removeRole, role name: %s, contextID: %s")
    void traceRemoveRole(String roleName, String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 319, value = "removeUncheckedPolicy, contextID: %s")
    void traceRemoveUncheckedPolicy(String contextID);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 320, value = "Mapped X500 principal, new principal: %s")
    void traceMappedX500Principal(Principal newPrincipal);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 321, value = "Query returned an empty result")
    void traceQueryWithEmptyResult();

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 322, value = "Mapping provider options [principal: %s, principal to roles map: %s, subject principals: %s]")
    void debugMappingProviderOptions(Principal principal, Map<String, Set<String>> principalRolesMap, Set<Principal> subjectPrincipals);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 323, value = "No audit context found for security domain %s; using default context")
    void traceNoAuditContextFoundForDomain(String securityDomain);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 324, value = "AuthorizationManager is null for security domain %s")
    void debugNullAuthorizationManager(String securityDomain);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 325, value = "Authorization processing error")
    void debugAuthorizationError(@Cause Throwable throwable);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 326, value = "%s processing failed")
    void debugFailureExecutingMethod(String methodName);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 327, value = "Returning host %s from thread [id: %s]")
    void traceHostThreadLocalGet(String host, long threadId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 328, value = "Setting host %s on thread [id: %s]")
    void traceHostThreadLocalSet(String host, long threadId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 329, value = "Begin doesUserHaveRole, principal: %s, roles: %s")
    void traceBeginDoesUserHaveRole(Principal principal, String roles);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 330, value = "End doesUserHaveRole, result: %s")
    void traceEndDoesUserHaveRole(boolean hasRole);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 331, value = "Roles before mapping: %s")
    void traceRolesBeforeMapping(String roles);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 332, value = "Roles after mapping: %s")
    void traceRolesAfterMapping(String roles);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 333, value = "Deregistered policy for contextID: %s, type: %s")
    void traceDeregisterPolicy(String contextID, String type);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 334, value = "Registered policy for contextID: %s, type: %s, location: %s")
    void traceRegisterPolicy(String contextID, String type, String location);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 335, value = "SecurityManagement is not set, creating a default one")
    void warnSecurityMagementNotSet();

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 336, value = "AuthenticationManager is null for security domain %s")
    void debugNullAuthenticationManager(String securityDomain);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 337, value = "nextState for action %s: %s")
    void traceStateMachineNextState(String action, String nextState);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 338, value = "Ignore attribute [uri: %s, qname: %s, value: %s]")
    void traceIgnoreXMLAttribute(String uri, String qName, String value);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 339, value = "systemId argument '%s' for publicId '%s' is different from the registered systemId '%s', resolution will be based on the argument")
    void traceSystemIDMismatch(String systemId, String publicId, String registeredId);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 340, value = "Cannot resolve entity, systemId: %s, publicId: %s")
    void debugFailureToResolveEntity(String systemId, String publicId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 341, value = "Begin resolvePublicId, publicId: %s")
    void traceBeginResolvePublicID(String publicId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 342, value = "Found entity from %s: %s, filename: %s")
    void traceFoundEntityFromID(String idName, String idValue, String fileName);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 343, value = "Cannot load %s from %s resource: %s")
    void warnFailureToLoadIDFromResource(String idName, String resourceType, String resourceName);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 344, value = "Begin resolveSystemId, systemId: %s")
    void traceBeginResolveSystemID(String systemId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 345, value = "Begin resolveSystemIdasURL, systemId: %s")
    void traceBeginResolveSystemIDasURL(String systemId);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 346, value = "Trying to resolve systemId %s as a non-file URL")
    void warnResolvingSystemIdAsNonFileURL(String systemId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 347, value = "Begin resolveClasspathName, systemId: %s")
    void traceBeginResolveClasspathName(String systemId);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 348, value = "Mapped systemId to filename %s")
    void traceMappedSystemIdToFilename(String filename);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 349, value = "Mapped resource %s to URL %s")
    void traceMappedResourceToURL(String resource, URL url);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 350, value = "Module option: %s, value: %s")
    void debugModuleOption(String optionName, Object optionValue);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 351, value = "Obtained auth info from handler, principal: %s, credential class: %s")
    void traceObtainedAuthInfoFromHandler(Principal loginPrincipal, Class<?> credentialClass);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 352, value = "JSSE domain got request for key with alias %s")
    void traceJSSEDomainGetKey(String alias);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 353, value = "JSSE domain got request for certificate with alias %s")
    void traceJSSEDomainGetCertificate(String alias);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 354, value = "Setting security roles ThreadLocal: %s")
    void traceSecRolesAssociationSetSecurityRoles(Map<String, Set<String>> roles);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 355, value = "Begin execPasswordCmd, command: %s")
    void traceBeginExecPasswordCmd(String passwordCmd);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 356, value = "End execPasswordCmd, exit code: %s")
    void traceEndExecPasswordCmd(int exitCode);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 357, value = "Begin getIdentity, username: %s")
    void traceBeginGetIdentity(String username);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 358, value = "Begin getRoleSets")
    void traceBeginGetRoleSets();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 359, value = "Current calling principal: %s, thread name: %s")
    void traceCurrentCallingPrincipal(String username, String threadName);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 360, value = "Creating login module with empty password")
    void warnModuleCreationWithEmptyPassword();

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 361, value = "Default Security Vault Implementation Initialized and Ready")
    void infoVaultInitialized();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 362, value = "Cannot get MD5 algorithm instance for hashing password commands. Using NULL.")
    void errorCannotGetMD5AlgorithmInstance();

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 363, value = "Retrieving password from the cache for key: %s")
    void traceRetrievingPasswordFromCache(String newKey);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 364, value = "Storing password to the cache for key: %s")
    void traceStoringPasswordToCache(String newKey);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 365, value = "Resetting cache")
    void traceResettingCache();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 366, value = "Error parsing time out number.")
    void errorParsingTimeoutNumber();

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 367, value = "Reading security vault data version %s target version is %s")
    void securityVaultContentVersion(String dataVersion, String targetVersion);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 368, value = "Security Vault contains both covnerted (%s) and pre-conversion data (%s). Try to delete %s file and start over again.")
    void mixedVaultDataFound(String vaultDatFile, String encDatFile, String encDatFile2);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 369, value = "Ambiguos vault block and attribute name stored in original security vault. Delimiter (%s) is part of vault block or attribute name. Took the first delimiter. Result vault block (%s) attribute name (%s). Modify security vault manually.")
    void ambiguosKeyForSecurityVaultTransformation(String delimiter, String vaultBlock, String attributeName);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 370, value = "Cannot delete original security vault file (%s). Delete the file manually before next start, please.")
    void cannotDeleteOriginalVaultFile(String file);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 371, value = "Security Vault does not contain SecretKey entry under alias (%s)")
    void vaultDoesnotContainSecretKey(String alias);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 372, value = "Security Vault key store successfuly converted to JCEKS type (%s). From now on use JCEKS as KEYSTORE_TYPE in Security Vault configuration.")
    void keyStoreConvertedToJCEKS(String keyStoreFile);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 373, value = "Error getting ServerAuthConfig for layer %s and appContext %s")
    void errorGettingServerAuthConfig(String layer, String appContext, @Cause Throwable cause);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 374, value = "Error getting ServerAuthContext for authContextId %s and security domain %s")
    void errorGettingServerAuthContext(String authContextId, String securityDomain, @Cause Throwable cause);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(id = 375, value = "Error getting the module classloader informations for cache")
    void errorGettingModuleInformation(@Cause Throwable cause);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 376, value = "Wrong Base64 string used with masked password utility. Following is correct (%s)")
    void wrongBase64StringUsed(String fixedBase64);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 377, value = "JAAS logout, login context: %s, subject: %s")
    void traceLogoutSubject(String loginContext, String subject);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = 378, value = "Problem when closing original LDAP context during role search rebind. Trying to create new LDAP context.")
    void warnProblemClosingOriginalLdapContextDuringRebind(@Cause NamingException e);

    @LogMessage(level = Logger.Level.DEBUG)
    @Message(id = 379, value = "Password validation failed")
    void passwordValidationFailed(@Cause Throwable cause);

    @LogMessage(level = Logger.Level.TRACE)
    @Message(id = 380, value = "%s processing failed")
    void traceFailureExecutingMethod(String methodName, @Cause Throwable throwable);

}
