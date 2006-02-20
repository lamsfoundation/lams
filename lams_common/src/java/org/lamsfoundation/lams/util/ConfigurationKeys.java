package org.lamsfoundation.lams.util;

/**
 * Known keys for the configuration data.
 * 
 * @author fmalikoff
 *
 */
public class ConfigurationKeys {

	public static String ROOT ="Lams";
	public static String SERVER_URL="ServerURL";
	public static String LAMS_HOME="LamsHome";
	public static String LAMS_TEMP_DIR="TempDir";
	public static String SMTP_SERVER="SMTPServer";
	public static String LAMS_ADMIN_EMAIL="LamsSupportEmail";

	/** Flash crash dump files (see FlashCrashDump servlet) are written to the Dump directory */
	public static String LAMS_DUMP_DIR="DumpDir";
	
	public static String CONTENT_REPOSITORY_PATH = "ContentRepositoryPath";
	public static String UPLOAD_FILE_MAX_SIZE="UploadFileMaxSize";
	public static String UPLOAD_FILE_LARGE_MAX_SIZE="UploadLargeFileMaxSize";
	public static String UPLOAD_FILE_MAX_MEMORY_SIZE="UploadFileMaxMemorySize";
	
	public static String CHAT_SERVER_NAME="ChatServerName";
	public static String CHAT_PORT_NUMBER="ChatPortNumber";
	
	public static String FILEMANAGER_DIRECTORY_NAME="FileManagerDirectory";
	public static String FILEMANAGER_EXE_EXTENSIONS="ExecutableExtensions";
	
	public static String LICENSE_TICKET_FILE="TicketFile";

	public static String PREVIEW_CLEANUP_NUM_DAYS="CleanupPreviewOlderThanDays";

	/** Number of milliseconds before a user is considered "inactive". "Anonymous" sessions
	 * are ended after this period (ie ones that haven't ever access one of the clients ) */
	public static String INACTIVE_TIME="UserInactiveTimeout";
	/** Allow more than one session to exist for one user. Needed for the test harness
	 * Do not set this parameter to true in production. */
	public static String ALLOW_MULTIPLE_LOGIN="AllowMultipleLogin";

	/** Turn on the cache debugging listener. Logs whenever an item is added/removed/evicted
	 * to/from the cache. Not on in production. Must be set to a boolean value */
	public static String USE_CACHE_DEBUG_LISTENER="UseCacheDebugListener";
	
	/** Values for client updates */
	public static String AUTHORING_CLIENT_VERSION = "AuthoringClientVersion";
	
	public static String LEARNER_CLIENT_VERSION = "LearnerClientVersion";
	
	public static String MONITOR_CLIENT_VERSION = "MonitorClientVersion";
	
	public static String SERVER_VERSION_NUMBER = "ServerVersionNumber";
	
	public static String SERVER_LANGUAGE = "ServerLanguage";
	
	public static String DICTIONARY_DATES  = "DictionaryDates";
	
	public static String DICTIONARY = "Dictionary";
	
	public static String DICTIONARY_CREATE_DATE = "createDate";
	
	public static String DICTIONARY_LANGUAGE = "language";	
	
}
