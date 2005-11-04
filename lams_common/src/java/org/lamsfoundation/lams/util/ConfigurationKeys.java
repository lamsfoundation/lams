package org.lamsfoundation.lams.util;

/**
 * Known keys for the configuration data.
 * 
 * @author fmalikoff
 *
 */
public class ConfigurationKeys {

	public static String SERVER_URL="ServerURL";
	public static String LAMS_HOME="LamsHome";
	public static String LAMS_TEMP_DIR="TempDir";
	public static String SMTP_SERVER="SMTPServer";
	public static String LAMS_ADMIN_EMAIL="LamsSupportEmail";

	public static String CONTENT_REPOSITORY_PATH = "ContentRepositoryPath";
	public static String UPLOAD_FILE_MAX_SIZE="UploadFileSizeMax";
	public static String UPLOAD_FILE_LARGE_MAX_SIZE="UploadFileLargeSizeMax";
	public static String UPLOAD_FILE_MAX_MEMORY_SIZE="UploadFileMaxMemorySize";
	
	public static String CHAT_SERVER_NAME="ChatServerName";
	public static String CHAT_PORT_NUMBER="ChatPortNumber";
	
	public static String FILEMANAGER_DIRECTORY_NAME="FileManagerDirectory";
	public static String FILEMANAGER_EXE_EXTENSIONS="ExecutableExtensions";
	
	public static String LICENSE_TICKET_FILE="TicketFile";

	/** Number of milliseconds before a user is considered "inactive". "Anonymous" sessions
	 * are ended after this period (ie ones that haven't ever access one of the clients ) */
	public static String INACTIVE_TIME="UserInactiveTimeout";
	/** Allow more than one session to exist for one user. Needed for the test harness
	 * Do not set this parameter to true in production. */
	public static String ALLOW_MULTIPLE_LOGIN="AllowMultipleLogin";

	/** Turn on the cache debugging listener. Logs whenever an item is added/removed/evicted
	 * to/from the cache. Not on in production. Must be set to a boolean value */
	public static String USE_CACHE_DEBUG_LISTENER="UseCacheDebugListener";
}
