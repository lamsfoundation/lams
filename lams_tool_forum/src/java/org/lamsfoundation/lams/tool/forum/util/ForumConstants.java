package org.lamsfoundation.lams.tool.forum.util;

/**
 * User: conradb
 * Date: 14/06/2005
 * Time: 10:33:00
 */
public interface ForumConstants {
	public static final int COMPLETED = 1;
	
	public static final String TOOLSIGNNATURE = "lafrum11";
	
    public final static int MAX_FILE_SIZE = 250 * 1000;
    public final static String FORUM_SERVICE = "forumService";
    
    public final static String CONTENT_HANDLER = "toolContentHandler";

	public static final String AUTHORING_DTO = "authoring";
	public static final String AUTHORING_TOPICS_LIST = "topicList";
	public static final String AUTHORING_TOPICS_INDEX = "topicIndex";
	public static final String AUTHORING_TOPIC_THREAD = "topicThread";
	public static final String AUTHORING_TOPIC = "topic";
	
	public static final String DEFAULT_TITLE = "Forum";
	//TODO:hard code!!! need to read from config
	public static final String TOOL_URL_BASE = "/lams/tool/lafrum11/";
	public static final String SUCCESS_FLAG = "SUCCESS_FLAG";
	public static final String FORUM_ID = "forum_id";

	public static final int SESSION_STATUS_FINISHED = 1;
	public static final String ALLOW_EDIT = "allowEdit";
	public static final String ALLOW_RICH_EDITOR = "allowRichEditor";
	public static final String LIMITED_CHARS = "limitedChars";

	public static final String ONLINE_ATTACHMENT = "online_att";
	public static final String OFFLINE_ATTACHMENT = "offline_att";

	public static final String ATTACHMENT_LIST = "attachmentList";
	public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";

	public static final String TOPIC_DELETED_ATTACHMENT_LIST = "topicDeletedAttachmentList";

	public static final String DELETED_AUTHORING_TOPICS_LIST = "deletedAuthoringTopicList";

	public static final String USER_UID = "userID";

	public static final String MESSAGE_UID = "messageID";

	public static final String MODE = "mode";
	public static final String AUTHOR_MODE = "author_mode";
	public static final String MONITOR_MODE = "monitor_mode";

	public static final String FINISHEDLOCK = "finishedLock";
	
	// used in monitoring 
	public static final String TITLE = "title";
	public static final String INSTRUCTIONS = "instructions";
	public static final String PAGE_EDITABLE = "isPageEditable";
	
}
