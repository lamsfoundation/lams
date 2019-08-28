/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.util;

/**
 * Known keys for the configuration data.
 *
 * @author fmalikoff
 *
 */
public class ConfigurationKeys {

    public static String ROOT = "Lams";

    public static String SERVER_URL = "ServerURL";

    public static String SERVER_URL_CONTEXT_PATH = "ServerURLContextPath";

    public static String VERSION = "Version";

    public static String LAMS_TEMP_DIR = "TempDir";

    /**
     * Directory in which lams.ear is deployed. Usually {JBOSS}/server/default/deploy/lams.ear
     */
    public static String LAMS_EAR_DIR = "EARDir";

    public static String SMTP_SERVER = "SMTPServer";

    public static String SMTP_PORT = "SMTPPort";

    public static String LAMS_ADMIN_EMAIL = "LamsSupportEmail";

    public static String CONTENT_REPOSITORY_PATH = "ContentRepositoryPath";

    public static String UPLOAD_FILE_MAX_SIZE = "UploadFileMaxSize";

    public static String UPLOAD_FILE_LARGE_MAX_SIZE = "UploadLargeFileMaxSize";

    public static String UPLOAD_FILE_MAX_MEMORY_SIZE = "UploadFileMaxMemorySize";

    public static String CHAT_SERVER_NAME = "ChatServerName";

    public static String CHAT_PORT_NUMBER = "ChatPortNumber";

    public static String EXE_EXTENSIONS = "ExecutableExtensions";

    public static String LICENSE_TICKET_FILE = "TicketFile";

    public static String PREVIEW_CLEANUP_NUM_DAYS = "CleanupPreviewOlderThanDays";

    /**
     * Number of seconds before a user is considered "inactive" and gets logged out.
     */
    public static String INACTIVE_TIME = "UserInactiveTimeout";

    /**
     * Allow more than one session to exist for one user. Needed for the test harness Do not set this parameter to true
     * in production.
     */
    public static String ALLOW_MULTIPLE_LOGIN = "AllowMultipleLogin";

    /** Values for client updates */
    public static String SERVER_VERSION_NUMBER = "ServerVersionNumber";

    /** Default locale for the server. Originally en_AU */
    public static String SERVER_LANGUAGE = "ServerLanguage";

    /**
     * Default country for the server. Originally AU
     */
    public static String SERVER_COUNTRY = "ServerCountry";

    /**
     * Direction (left to right, right to left) for writing on HTML pages. Originally LTR
     */
    public static String SERVER_PAGE_DIRECTION = "ServerPageDirection";

    /** universal date of dictionary updates */
    public static String DICTIONARY_DATE_CREATED = "DictionaryDateCreated";

    public static String HELP_URL = "HelpURL";

    public static String XMPP_DOMAIN = "XmppDomain";

    public static String XMPP_CONFERENCE = "XmppConference";

    public static String XMPP_ADMIN = "XmppAdmin";

    public static String XMPP_PASSWORD = "XmppPassword";

    public static String DEFAULT_THEME = "DefaultTheme";

    public static String ALLOW_DIRECT_LESSON_LAUNCH = "AllowDirectLessonLaunch";

    public static String ALLOW_EDIT_ON_FLY = "AllowLiveEdit";

    public static String ALLOW_KUMALIVE = "AllowKumalive";

    public static String DISPLAY_PORTRAIT = "DisplayPortrait";

    public static String ENABLE_PORTRAIT_EDITING = "EnablePortraitEditing";

    public static String SHOW_ALL_MY_LESSON_LINK = "ShowAllMyLessonLink";

    public static String LDAP_PROVISIONING_ENABLED = "LDAPProvisioningEnabled";

    public static String LDAP_PROVIDER_URL = "LDAPProviderURL";

    public static String LDAP_SECURITY_AUTHENTICATION = "LDAPSecurityAuthentication";

    public static String LDAP_SEARCH_FILTER = "LDAPSearchFilter";

    public static String LDAP_BASE_DN = "LDAPBaseDN";

    public static String LDAP_BIND_USER_DN = "LDAPBindUserDN";

    public static String LDAP_BIND_USER_PASSWORD = "LDAPBindUserPassword";

    public static String LDAP_SECURITY_PROTOCOL = "LDAPSecurityProtocol";

    public static String LDAP_LOGIN_ATTR = "LDAPLoginAttr";

    public static String LDAP_FIRST_NAME_ATTR = "LDAPFNameAttr";

    public static String LDAP_LAST_NAME_ATTR = "LDAPLNameAttr";

    public static String LDAP_EMAIL_ATTR = "LDAPEmailAttr";

    public static String LDAP_ADDR1_ATTR = "LDAPAddr1Attr";

    public static String LDAP_ADDR2_ATTR = "LDAPAddr2Attr";

    public static String LDAP_ADDR3_ATTR = "LDAPAddr3Attr";

    public static String LDAP_CITY_ATTR = "LDAPCityAttr";

    public static String LDAP_STATE_ATTR = "LDAPStateAttr";

    public static String LDAP_POSTCODE_ATTR = "LDAPPostcodeAttr";

    public static String LDAP_COUNTRY_ATTR = "LDAPCountryAttr";

    public static String LDAP_DAY_PHONE_ATTR = "LDAPDayPhoneAttr";

    public static String LDAP_EVENING_PHONE_ATTR = "LDAPEveningPhoneAttr";

    public static String LDAP_FAX_ATTR = "LDAPFaxAttr";

    public static String LDAP_MOBILE_ATTR = "LDAPMobileAttr";

    public static String LDAP_LOCALE_ATTR = "LDAPLocaleAttr";

    public static String LDAP_DISABLED_ATTR = "LDAPDisabledAttr";

    public static String LDAP_ORG_ATTR = "LDAPOrgAttr";

    public static String LDAP_ROLES_ATTR = "LDAPRolesAttr";

    public static String LDAP_LEARNER_MAP = "LDAPLearnerMap";

    public static String LDAP_MONITOR_MAP = "LDAPMonitorMap";

    public static String LDAP_AUTHOR_MAP = "LDAPAuthorMap";

    public static String LDAP_GROUP_ADMIN_MAP = "LDAPGroupAdminMap";

    public static String LDAP_GROUP_MANAGER_MAP = "LDAPGroupManagerMap";

    public static String LDAP_UPDATE_ON_LOGIN = "LDAPUpdateOnLogin";

    public static String LDAP_ORG_FIELD = "LDAPOrgField";

    public static String LDAP_ONLY_ONE_ORG = "LDAPOnlyOneOrg";

    public static String LDAP_SEARCH_RESULTS_PAGE_SIZE = "LDAPSearchResultsPageSize";

    /** Custom tab for the main page */
    public static String CUSTOM_TAB_LINK = "CustomTabLink";

    public static String CUSTOM_TAB_TITLE = "CustomTabTitle";

    /**
     * Configurable screen sizes for authoring, monitor, learner and admin (LDEV-1598)
     */
    public static String AUTHORING_SCREEN_SIZE = "AuthoringScreenSize";

    public static String MONITOR_SCREEN_SIZE = "MonitorScreenSize";

    public static String LEARNER_SCREEN_SIZE = "LearnerScreenSize";

    public static String ADMIN_SCREEN_SIZE = "AdminScreenSize";

    public static String GMAP_KEY = "GmapKey";

    public static String RED5_SERVER_URL = "Red5ServerUrl";

    public static String RED5_RECORDINGS_URL = "Red5RecordingsUrl";

    public static String SMTP_AUTH_USER = "SMTPUser";

    public static String SMTP_AUTH_PASSWORD = "SMTPPassword";

    public static String SMTP_AUTH_SECURITY = "SMTPAuthSecurity";

    public static String PROFILE_EDIT_ENABLE = "ProfileEditEnable";

    public static String FORGOT_YOUR_PASSWORD_LINK_ENABLE = "EnableForgotYourPasswordLink";

    public static String PROFILE_PARTIAL_EDIT_ENABLE = "ProfilePartialEditEnable";

    public static String KALTURA_SERVER = "KalturaServer";

    public static String KALTURA_PARTNER_ID = "KalturaPartnerId";

    public static String KALTURA_SUB_PARTNER_ID = "KalturaSubPartnerId";

    public static String KALTURA_USER_SECRET = "KalturaUserSecret";

    public static String KALTURA_KCW_UI_CONF_ID = "KalturaKCWUiConfId";

    public static String KALTURA_KDP_UI_CONF_ID = "KalturaKDPUiConfId";

    public static String USER_VALIDATION_REQUIRED_USERNAME = "UserValidationUsername";

    public static String USER_VALIDATION_REQUIRED_FIRST_LAST_NAME = "UserValidationFirstLastName";

    public static String USER_VALIDATION_REQUIRED_EMAIL = "UserValidationEmail";

    // LDEV-2747
    public static String ENABLE_SERVER_REGISTRATION = "EnableServerRegistration";

    // CNG-26 Add to lams_configuration and set to false
    // if you don't want imported LD to have _<timestamp>_<seq_number> appended
    public static String SUFFIX_IMPORTED_LD = "SuffixImportedLD";

    // LDEV-3254
    public static String CONFIGURATION_CACHE_REFRESH_INTERVAL = "ConfigCacheRefreshInterval";

    // LDEV-3961
    public static String SITE_NAME = "SiteName";

    // LDEV-4023 Password policy
    public static String PASSWORD_POLICY_MINIMUM_CHARACTERS = "PasswordPolicyMinChars";

    public static String PASSWORD_POLICY_UPPERCASE = "PasswordPolicyUppercase";

    public static String PASSWORD_POLICY_LOWERCASE = "PasswordPolicyLowercase";

    public static String PASSWORD_POLICY_NUMERICS = "PasswordPolicyNumerics";

    public static String PASSWORD_POLICY_SYMBOLS = "PasswordPolicySymbols";

    // LDEV-4049 Option for not displaying stacktraces in config settings
    public static String ERROR_STACK_TRACE = "ErrorStackTrace";

    // LDEV-4030 Disable login for a few minutes after X number of attempts
    public static String FAILED_ATTEMPTS = "FailedAttempts";

    public static String LOCK_OUT_TIME = "LockOutTime";

    // LDEV-4144
    public static String SHOW_TIMEZONE_WARNING = "ShowTimezoneWarning";

    // LDEV-4594 / LDEV-4583 Allow/Block access to index.do for integration learners. Default to false - do not allow direct access.
    public static String ALLOW_DIRECT_ACCESS_FOR_INTEGRATION_LEARNERS = "AllowDirectAccessIntgrtnLrnr";

    // LDEV-4755 Antivirus
    public static String ANTIVIRUS_ENABLE = "AntivirusEnable";
    public static String ANTIVIRUS_HOST = "AntivirusHost";
    public static String ANTIVIRUS_PORT = "AntivirusPort";
    
    // LDEV-4819
    public static String LEARNING_OUTCOME_QUICK_ADD_ENABLE = "LearningOutcomeQuickAddEnable";

    // LDEV-4827 Question Bank
    public static String QB_QTI_ENABLE = "QbQtiEnable";
    public static String QB_WORD_ENABLE = "QbWordEnable";
    public static String QB_COLLECTIONS_CREATE_ALLOW = "QbCollectionsCreateEnable";
    public static String QB_COLLECTIONS_TRANSFER_ALLOW = "QbCollectionsTransferEnable";
    public static String QB_MONITORS_READ_ONLY = "QbMonitorsReadOnly";
    public static String QB_STATS_MIN_PARTICIPANTS = "QbStatsMinParticipants";
    public static String QB_STATS_GROUP_SIZE = "QbStatsGroupSize";
    
    // LKC-213 LKC Workflow Automation
    public static String WORKFLOW_AUTOMATION_ENABLE = "WorkflowAutomationEnable";
}
