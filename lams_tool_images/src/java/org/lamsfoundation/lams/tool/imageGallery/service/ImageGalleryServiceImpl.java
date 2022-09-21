/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.imageGallery.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryConfigItemDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemVisitDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGallerySessionDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryUserDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageVoteDAO;
import org.lamsfoundation.lams.tool.imageGallery.dto.ImageGalleryAttachment;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItemVisitLog;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageVote;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.imgscalr.ResizePictureUtil;

/**
 * @author Andrey Balan
 */
public class ImageGalleryServiceImpl implements IImageGalleryService, ToolContentManager, ToolSessionManager {

    private final static String MEDIUM_FILENAME_PREFIX = "medium_";

    private final static String THUMBNAIL_FILENAME_PREFIX = "thumbnail_";

    static Logger log = Logger.getLogger(ImageGalleryServiceImpl.class.getName());

    private ImageGalleryDAO imageGalleryDao;

    private ImageGalleryItemDAO imageGalleryItemDao;

    private ImageVoteDAO imageVoteDao;

    private ImageGalleryUserDAO imageGalleryUserDao;

    private ImageGallerySessionDAO imageGallerySessionDao;

    private ImageGalleryItemVisitDAO imageGalleryItemVisitDao;

    private ImageGalleryConfigItemDAO imageGalleryConfigItemDAO;

    // tool service
    private IToolContentHandler imageGalleryToolContentHandler;

    private MessageService messageService;

    private ImageGalleryOutputFactory imageGalleryOutputFactory;

    // system services

    private ILamsToolService toolService;

    private ILogEventService logEventService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IRatingService ratingService;

    private IEventNotificationService eventNotificationService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public ImageGallery getImageGalleryByContentId(Long contentId) {
	ImageGallery rs = imageGalleryDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public ImageGallery getDefaultContent(Long contentId) throws ImageGalleryException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryException(error);
	}

	ImageGallery defaultContent = getDefaultImageGallery();
	// save default content by given ID.
	ImageGallery content = new ImageGallery();
	content = ImageGallery.newInstance(defaultContent, contentId);
	// content.setNextImageTitle(1L);
	return content;
    }

    @Override
    public List<ImageGalleryItem> getAuthoredItems(Long imageGalleryUid) {
	return imageGalleryItemDao.getAuthoringItems(imageGalleryUid);
    }

    @Override
    public void saveUser(ImageGalleryUser imageGalleryUser) {
	imageGalleryUserDao.saveObject(imageGalleryUser);
    }

    @Override
    public ImageGalleryUser getUserByIDAndContent(Long userId, Long contentId) {
	return imageGalleryUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public ImageGalleryUser getUserByIDAndSession(Long userId, Long sessionId) {
	return imageGalleryUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public void saveOrUpdateImageGallery(ImageGallery imageGallery) {
	imageGalleryDao.saveObject(imageGallery);
    }

    @Override
    public ImageGalleryItem getImageGalleryItemByUid(Long itemUid) {
	return imageGalleryItemDao.getByUid(itemUid);
    }

    @Override
    public void saveOrUpdateImageGalleryItem(ImageGalleryItem image) {
	imageGalleryItemDao.saveObject(image);
    }

    @Override
    public void deleteImage(Long toolSessionId, Long imageUid) {
	ImageGallery imageGallery = getImageGalleryBySessionId(toolSessionId);
	ImageGalleryItem image = getImageGalleryItemByUid(imageUid);

	Set<ImageGalleryItem> imageList = imageGallery.getImageGalleryItems();
	imageList.remove(image);
	imageGallery.setImageGalleryItems(imageList);
	saveOrUpdateImageGallery(imageGallery);
    }

    @Override
    public ImageVote getImageVoteByImageAndUser(Long imageUid, Long userId) {
	return imageVoteDao.getImageVoteByImageAndUser(imageUid, userId);
    }

    @Override
    public int getNumberVotesByUserId(Long userId) {
	return imageVoteDao.getNumImageVotesByUserId(userId);
    }

    @Override
    public void saveOrUpdateImageVote(ImageVote vote) {
	imageVoteDao.saveObject(vote);
    }

    @Override
    public void deleteImageGalleryItem(Long uid) {
	imageGalleryItemDao.removeObject(ImageGalleryItem.class, uid);
    }

    @Override
    public ImageGallery getImageGalleryBySessionId(Long sessionId) {
	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getImageGallery().getContentId();
	ImageGallery res = imageGalleryDao.getByContentId(contentId);
	return res;
    }

    @Override
    public ImageGallerySession getImageGallerySessionBySessionId(Long sessionId) {
	return imageGallerySessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateImageGallerySession(ImageGallerySession resSession) {
	imageGallerySessionDao.saveObject(resSession);
    }

    @Override
    public void setItemAccess(Long imageGalleryItemUid, Long userId, Long sessionId) {
	ImageGalleryItemVisitLog log = imageGalleryItemVisitDao.getImageGalleryItemLog(imageGalleryItemUid, userId);
	if (log == null) {
	    log = new ImageGalleryItemVisitLog();
	    ImageGalleryItem item = imageGalleryItemDao.getByUid(imageGalleryItemUid);
	    log.setImageGalleryItem(item);
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    imageGalleryItemVisitDao.saveObject(log);
	}
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws ImageGalleryException {
	ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	imageGalleryUserDao.saveObject(user);

	// ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(ImageGalleryConstants.COMPLETED);
	// imageGallerySessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new ImageGalleryException(e);
	} catch (ToolException e) {
	    throw new ImageGalleryException(e);
	}
	return nextUrl;
    }

    @Override
    public List<ImageGalleryUser> getUserListBySessionId(Long sessionId) {
	return imageGalleryUserDao.getBySessionID(sessionId);
    }

    @Override
    public void toggleImageVisibility(Long itemUid, Long toolContentId) {
	ImageGalleryItem image = imageGalleryItemDao.getByUid(itemUid);
	if (image != null) {
	    boolean isHidden = image.isHide();
	    image.setHide(!isHidden);
	    imageGalleryItemDao.saveObject(image);

	    // audit log
	    Long userId = image.getCreateBy() == null ? 0L : image.getCreateBy().getUserId();
	    String loginName = image.getCreateBy() == null ? "No user" : image.getCreateBy().getLoginName();
	    if (isHidden) {
		logEventService.logShowLearnerContent(userId, loginName, toolContentId, image.toString());
	    } else {
		logEventService.logHideLearnerContent(userId, loginName, toolContentId, image.toString());
	    }
	}
    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public ItemRatingDTO getRatingCriteriaDtos(Long contentId, Long toolSessionId, Long imageUid, Long userId) {

	LinkedList<Long> itemIds = new LinkedList<>();
	itemIds.add(imageUid);
	ItemRatingDTO ratingCriteria = getRatingCriteriaDtos(contentId, toolSessionId, itemIds, true, userId).get(0);

	return ratingCriteria;
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {
	return ratingService.getRatingCriteriaDtos(contentId, toolSessionId, itemIds, isCommentsByOtherUsersRequired,
		userId);
    }

    @Override
    public int getCountItemsRatedByUser(Long toolContentId, Integer userId) {
	return ratingService.getCountItemsRatedByUser(toolContentId, userId);
    }

    @Override
    public List<RatingCriteria> getRatingCriterias(Long toolContentId) {
	List<RatingCriteria> ratingCriterias = ratingService.getCriteriasByToolContentId(toolContentId);
	return ratingCriterias;
    }

    @Override
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {
	ratingService.saveRatingCriterias(request, oldCriterias, toolContentId);
    }

    @Override
    public boolean isCommentsEnabled(Long toolContentId) {
	return ratingService.isCommentsEnabled(toolContentId);
    }

    @Override
    public ImageGalleryUser getUser(Long uid) {
	return (ImageGalleryUser) imageGalleryUserDao.getObject(ImageGalleryUser.class, uid);
    }

    @Override
    public List<List<Summary>> getSummary(Long contentId) {
	List<List<Summary>> groupList = new ArrayList<>();
	List<Summary> group = new ArrayList<>();

	// get all item which is accessed by user
	ImageGallery imageGallery = imageGalleryDao.getByContentId(contentId);

	List<ImageGallerySession> sessionList = imageGallerySessionDao.getByContentId(contentId);
	for (ImageGallerySession session : sessionList) {
	    // one new group for one session.
	    group = new ArrayList<>();
	    Set<ImageGalleryItem> groupImages = getImagesForGroup(imageGallery, session.getSessionId());

	    List<ItemRatingDTO> itemRatingDtos = null;
	    if (imageGallery.isAllowRank()) {
		// create itemIds list
		List<Long> itemIds = new LinkedList<>();
		for (ImageGalleryItem image : groupImages) {
		    itemIds.add(image.getUid());
		}
		final Long USER_ID = -1L;
		final boolean IS_COMMENTS_BY_OTHER_USERS_REQUIRED = true;
		// ! TODO calculate average ratings based on one session data
		itemRatingDtos = getRatingCriteriaDtos(contentId, session.getSessionId(), itemIds,
			IS_COMMENTS_BY_OTHER_USERS_REQUIRED, USER_ID);
	    }

	    for (ImageGalleryItem image : groupImages) {
		Summary summary = new Summary(session.getSessionId(), session.getSessionName(), image);

		if (imageGallery.isAllowVote()) {
		    int numberOfVotes = imageVoteDao.getNumImageVotesByImageUid(image.getUid(), session.getSessionId());
		    summary.setNumberOfVotes(numberOfVotes);
		}

		if (imageGallery.isAllowRank()) {
		    // find corresponding itemRatingDto
		    ItemRatingDTO itemRatingDto = null;
		    for (ItemRatingDTO itemRatingDtoIter : itemRatingDtos) {
			if (itemRatingDtoIter.getItemId().equals(image.getUid())) {
			    itemRatingDto = itemRatingDtoIter;
			    break;
			}
		    }
		    summary.setItemRatingDto(itemRatingDto);
		}

		group.add(summary);
	    }
	    // if there is no any item available, then just put session name into Summary
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null));
	    }
	    groupList.add(group);
	}

	return groupList;
    }

    @Override
    public List<List<UserImageContributionDTO>> getImageSummary(Long contentId, Long imageUid) {
	List<List<UserImageContributionDTO>> imageSummary = new ArrayList<>();
	List<UserImageContributionDTO> group = new ArrayList<>();
	ImageGalleryItem image = imageGalleryItemDao.getByUid(imageUid);
	ImageGallery imageGallery = getImageGalleryByContentId(contentId);

	List<ImageGallerySession> sessionList;
	if (image.isCreateByAuthor()) {
	    sessionList = imageGallerySessionDao.getByContentId(contentId);
	} else {
	    sessionList = new ArrayList<>();
	    sessionList.add(image.getCreateBy().getSession());
	}

	for (ImageGallerySession session : sessionList) {
	    // one new group for one session.
	    group = new ArrayList<>();
	    //	    Object[] ratingForGroup = getRatingForGroup(image.getUid(), session.getSessionId());

	    List<ImageGalleryUser> users = imageGalleryUserDao.getBySessionID(session.getSessionId());
	    for (ImageGalleryUser user : users) {

		//create UserContribution
		UserImageContributionDTO userContribution = new UserImageContributionDTO(session.getSessionName(),
			user);

		if (imageGallery.isAllowVote()) {
		    int numberOfVotesForImage = imageVoteDao.getNumImageVotesByImageUid(image.getUid(),
			    session.getSessionId());
		    userContribution.setNumberOfVotesForImage(numberOfVotesForImage);

		    boolean isVotedForThisImage = false;
		    ImageVote imageVote = imageVoteDao.getImageVoteByImageAndUser(image.getUid(), user.getUserId());
		    if ((imageVote != null) && imageVote.isVoted()) {
			isVotedForThisImage = true;
		    }
		    userContribution.setVotedForThisImage(isVotedForThisImage);
		}

		if (imageGallery.isAllowRank()) {
		    // userContribution.setNumberRatings(((Long) ratingForGroup[0]).intValue());
		    // userContribution.setAverageRating(((Float) ratingForGroup[1]).floatValue());

		    // ImageRating rating = imageRatingDao.getImageRatingByImageAndUser(image.getUid(),
		    // user.getUserId());
		    //		    if (rating != null) {
		    //			userContribution.setRating(rating.getRating());
		    //		    }
		}

		group.add(userContribution);
	    }

	    imageSummary.add(group);
	}

	return imageSummary;

    }

    @Override
    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<>();

	List<ImageGallerySession> sessionList = imageGallerySessionDao.getByContentId(contentId);
	for (ImageGallerySession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getImageGallery().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<>(new ReflectDTOComparator());
	    // get all users in this session
	    List<ImageGalleryUser> users = imageGalleryUserDao.getBySessionID(sessionId);
	    for (ImageGalleryUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    ImageGalleryConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		    if (entry != null) {
			ref.setReflect(entry.getEntry());
		    }
		}

		ref.setHasRefection(hasRefection);
		list.add(ref);
	    }
	    map.put(sessionId, list);
	}

	return map;
    }

    @Override
    public void uploadImageGalleryItemFile(ImageGalleryItem image, File file) throws UploadImageGalleryFileException {

	ImageGalleryConfigItem mediumImageDimensionsKey = getConfigItem(
		ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	int mediumImageDimensions = Integer.parseInt(mediumImageDimensionsKey.getConfigValue());

	ImageGalleryConfigItem thumbnailImageDimensionsKey = getConfigItem(
		ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	int thumbnailImageDimensions = Integer.parseInt(thumbnailImageDimensionsKey.getConfigValue());

	try {
	    // upload file
	    NodeKey nodeKey = uploadMultipartFile(file);
	    image.setFileName(file.getName());
	    image.setOriginalFileUuid(nodeKey.getUuid());
	    String fileName = file.getName();

	    InputStream originalIS = imageGalleryToolContentHandler.getFileInputStream(nodeKey.getUuid());
	    BufferedImage originalImage = ImageIO.read(originalIS);
	    //throw exception if image was not successfully read
	    if (originalImage == null) {
		throw new UploadImageGalleryFileException("Impossible to read image file");
	    }
	    //store orginalImageWidth and orginalImageHeight
	    image.setOriginalImageWidth(originalImage.getWidth(null));
	    image.setOriginalImageHeight(originalImage.getHeight(null));

	    // prepare medium image
	    originalIS.close();
	    originalIS = imageGalleryToolContentHandler.getFileInputStream(nodeKey.getUuid());
	    InputStream mediumIS = ResizePictureUtil.resize(originalIS, mediumImageDimensions);
	    if (mediumIS == null) {
		throw new UploadImageGalleryFileException("Impossible to resize image");
	    }
	    String mediumFileName = ImageGalleryServiceImpl.MEDIUM_FILENAME_PREFIX
		    + fileName.substring(0, fileName.indexOf('.')) + ".jpg";
	    NodeKey mediumNodeKey = imageGalleryToolContentHandler.uploadFile(mediumIS, mediumFileName, "image/jpeg");
	    image.setMediumFileUuid(mediumNodeKey.getUuid());
	    //store MediumImageWidth and MediumImageHeight
	    InputStream mediumIS2 = imageGalleryToolContentHandler.getFileInputStream(mediumNodeKey.getUuid());
	    BufferedImage mediumImage = ImageIO.read(mediumIS2);
	    image.setMediumImageWidth(mediumImage.getWidth(null));
	    image.setMediumImageHeight(mediumImage.getHeight(null));
	    mediumIS2.close();

	    // prepare thumbnail image
	    InputStream originalIS2 = imageGalleryToolContentHandler.getFileInputStream(nodeKey.getUuid());
	    InputStream thumbnailIS = ResizePictureUtil.resize(originalIS2, thumbnailImageDimensions);
	    String thumbnailFileName = ImageGalleryServiceImpl.THUMBNAIL_FILENAME_PREFIX
		    + fileName.substring(0, fileName.indexOf('.')) + ".jpg";
	    NodeKey thumbnailNodeKey = imageGalleryToolContentHandler.uploadFile(thumbnailIS, thumbnailFileName,
		    "image/jpeg");
	    image.setThumbnailFileUuid(thumbnailNodeKey.getUuid());

	} catch (RepositoryCheckedException e) {
	    throw new UploadImageGalleryFileException(
		    messageService.getMessage("error.msg.repository.checked.exception"));
	} catch (NumberFormatException e) {
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.number.format.exception"));
	} catch (IOException e) {
	    throw new ImageGalleryException(messageService.getMessage("error.msg.io.exception.resizing"));
	}
    }

    /**
     * Process an uploaded file.
     *
     * @throws ImageGalleryException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey uploadMultipartFile(File file) throws UploadImageGalleryFileException {
	if ((file == null) || StringUtils.isEmpty(file.getName())) {
	    throw new UploadImageGalleryFileException(
		    messageService.getMessage("error.msg.upload.file.not.found", new Object[] { file }));
	}

	NodeKey node = null;
	try {
	    node = imageGalleryToolContentHandler.uploadFile(new FileInputStream(file), file.getName(), null);
	} catch (InvalidParameterException e) {
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	} catch (FileNotFoundException e) {
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (RepositoryCheckedException e) {
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.repository"));
	}
	return node;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    @Override
    public void notifyTeachersOnImageSumbit(Long sessionId, ImageGalleryUser imageGalleryUser) {
	String userName = imageGalleryUser.getLastName() + " " + imageGalleryUser.getFirstName();
	String message = messageService.getMessage("event.imagesubmit.body", new Object[] { userName });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setImageGalleryDao(ImageGalleryDAO imageGalleryDao) {
	this.imageGalleryDao = imageGalleryDao;
    }

    public void setImageGalleryItemDao(ImageGalleryItemDAO imageGalleryItemDao) {
	this.imageGalleryItemDao = imageGalleryItemDao;
    }

    public void setImageVoteDao(ImageVoteDAO imageVoteDao) {
	this.imageVoteDao = imageVoteDao;
    }

    public void setImageGallerySessionDao(ImageGallerySessionDAO imageGallerySessionDao) {
	this.imageGallerySessionDao = imageGallerySessionDao;
    }

    public void setImageGalleryToolContentHandler(IToolContentHandler imageGalleryToolContentHandler) {
	this.imageGalleryToolContentHandler = imageGalleryToolContentHandler;
    }

    public void setImageGalleryUserDao(ImageGalleryUserDAO imageGalleryUserDao) {
	this.imageGalleryUserDao = imageGalleryUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ImageGalleryItemVisitDAO getImageGalleryItemVisitDao() {
	return imageGalleryItemVisitDao;
    }

    public void setImageGalleryItemVisitDao(ImageGalleryItemVisitDAO imageGalleryItemVisitDao) {
	this.imageGalleryItemVisitDao = imageGalleryItemVisitDao;
    }

    public ImageGalleryConfigItemDAO getImageGalleryConfigItemDAO() {
	return imageGalleryConfigItemDAO;
    }

    public void setImageGalleryConfigItemDAO(ImageGalleryConfigItemDAO imageGalleryConfigItemDAO) {
	this.imageGalleryConfigItemDAO = imageGalleryConfigItemDAO;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	ImageGallery toolContentObj = imageGalleryDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultImageGallery();
	    } catch (ImageGalleryException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the imageGallery tool");
	}

	// don't export following fields
	Set<LearnerItemRatingCriteria> criterias = toolContentObj.getRatingCriterias();
	if (criterias != null) {
	    for (LearnerItemRatingCriteria criteria : criterias) {
		criteria.setToolContentId(null);
	    }
	}

	// set ImageGalleryToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = ImageGallery.newInstance(toolContentObj, toolContentId);
	Set<ImageGalleryItem> images = toolContentObj.getImageGalleryItems();
	for (ImageGalleryItem image : images) {

	    // convert file extension to lower case
	    String fileName = image.getFileName();
	    String[] fileNameParts = fileName.split("\\.");
	    String fileExtension = fileNameParts[fileNameParts.length - 1];
	    fileName = fileName.replaceAll(fileExtension + "$", fileExtension.toLowerCase());
	    image.setFileName(fileName);

	    ImageGalleryAttachment originalFile = new ImageGalleryAttachment();
	    originalFile.setFileUuid(image.getOriginalFileUuid());
	    originalFile.setFileName(fileName);
	    image.setOriginalFile(originalFile);

	    ImageGalleryAttachment mediumFile = new ImageGalleryAttachment();
	    mediumFile.setFileUuid(image.getMediumFileUuid());
	    mediumFile.setFileName(ImageGalleryServiceImpl.MEDIUM_FILENAME_PREFIX + fileName);
	    image.setMediumFile(mediumFile);

	    ImageGalleryAttachment thumbnailFile = new ImageGalleryAttachment();
	    thumbnailFile.setFileUuid(image.getThumbnailFileUuid());
	    thumbnailFile.setFileName(ImageGalleryServiceImpl.THUMBNAIL_FILENAME_PREFIX + fileName);
	    image.setThumbnailFile(thumbnailFile);
	}

	try {
	    exportContentService.registerFileClassForExport(ImageGalleryAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, imageGalleryToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ImageGalleryImportContentVersionFilter.class);

	    exportContentService.registerFileClassForImport(ImageGalleryAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, imageGalleryToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof ImageGallery)) {
		throw new ImportToolContentException(
			"Import ImageGallery tool content failed. Deserialized object is " + toolPOJO);
	    }
	    ImageGallery toolContentObj = (ImageGallery) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    Set<LearnerItemRatingCriteria> criterias = toolContentObj.getRatingCriterias();
	    if (criterias != null) {
		for (LearnerItemRatingCriteria criteria : criterias) {
		    criteria.setToolContentId(toolContentId);
		    if (criteria.getMaxRating() == null || criteria.getRatingStyle() == null) {
			if (criteria.getOrderId() == 0) {
			    criteria.setMaxRating(0);
			    criteria.setRatingStyle(RatingCriteria.RATING_STYLE_COMMENT);
			} else {
			    criteria.setMaxRating(RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX);
			    criteria.setRatingStyle(RatingCriteria.RATING_STYLE_STAR);
			}
		    }
		}
	    }
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndContentID(newUserUid.longValue(),
		    toolContentId);
	    if (user == null) {
		user = new ImageGalleryUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
		user.setImageGallery(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all imageGalleryItem createBy user
	    Set<ImageGalleryItem> images = toolContentObj.getImageGalleryItems();
	    for (ImageGalleryItem image : images) {
		image.setCreateBy(user);

		image.setOriginalFileUuid(image.getOriginalFile().getFileUuid());
		image.setMediumFileUuid(image.getMediumFile().getFileUuid());
		image.setThumbnailFileUuid(image.getThumbnailFile().getFileUuid());

		image.setOriginalFile(null);
		image.setMediumFile(null);
		image.setThumbnailFile(null);
	    }
	    imageGalleryDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	ImageGallery imageGallery = getImageGalleryByContentId(toolContentId);
	if (imageGallery == null) {
	    imageGallery = getDefaultImageGallery();
	}
	return getImageGalleryOutputFactory().getToolOutputDefinitions(imageGallery, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedImageGalleryFiles tool seession");
	}

	ImageGallery imageGallery = null;
	if (fromContentId != null) {
	    imageGallery = imageGalleryDao.getByContentId(fromContentId);
	}
	if (imageGallery == null) {
	    try {
		imageGallery = getDefaultImageGallery();
	    } catch (ImageGalleryException e) {
		throw new ToolException(e);
	    }
	}

	ImageGallery toContent = ImageGallery.newInstance(imageGallery, toContentId);
	// save imageGallery items first
	Set<ImageGalleryItem> items = toContent.getImageGalleryItems();
	if (items != null) {
	    Iterator<ImageGalleryItem> iter = items.iterator();
	    while (iter.hasNext()) {
		ImageGalleryItem item = iter.next();
		if (item.isCreateByAuthor()) {
		    imageGalleryUserDao.saveObject(item.getCreateBy());
		    imageGalleryItemDao.saveObject(item);
		} else {
		    iter.remove();
		}
	    }
	}

	imageGalleryDao.saveObject(toContent);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getImageGalleryByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	if (imageGallery == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	imageGallery.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getImageGalleryByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	for (ImageGallerySession session : imageGallerySessionDao.getByContentId(toolContentId)) {
	    if (!imageGalleryUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	if (imageGallery == null) {
	    ImageGalleryServiceImpl.log
		    .warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (ImageGallerySession session : imageGallerySessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ImageGalleryConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	imageGalleryDao.delete(imageGallery);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (ImageGalleryServiceImpl.log.isDebugEnabled()) {
	    ImageGalleryServiceImpl.log.debug(
		    "Removing Image Gallery content for user ID " + userId + " and toolContentId " + toolContentId);
	}
	ImageGallery gallery = imageGalleryDao.getByContentId(toolContentId);
	if (gallery == null) {
	    ImageGalleryServiceImpl.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	Iterator<ImageGalleryItem> itemIterator = gallery.getImageGalleryItems().iterator();
	while (itemIterator.hasNext()) {
	    ImageGalleryItem item = itemIterator.next();

	    ImageVote vote = imageVoteDao.getImageVoteByImageAndUser(item.getUid(), userId.longValue());
	    if (vote != null) {
		imageVoteDao.removeObject(ImageVote.class, vote.getUid());
	    }

	    if (!item.isCreateByAuthor() && item.getCreateBy().getUserId().equals(userId.longValue())) {
		try {
		    if (item.getOriginalFileUuid() != null) {
			imageGalleryToolContentHandler.deleteFile(item.getOriginalFileUuid());
		    }
		    if (item.getMediumFileUuid() != null) {
			imageGalleryToolContentHandler.deleteFile(item.getMediumFileUuid());
		    }
		    if (item.getThumbnailFileUuid() != null) {
			imageGalleryToolContentHandler.deleteFile(item.getThumbnailFileUuid());
		    }
		} catch (Exception e) {
		    throw new ToolException("Error while removing a file in Image Gallery", e);
		}

		imageGalleryItemDao.removeObject(ImageGalleryItem.class, item.getUid());
		itemIterator.remove();
	    }
	}

	for (ImageGallerySession session : imageGallerySessionDao.getByContentId(toolContentId)) {
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(user.getSession().getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ImageGalleryConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    imageGalleryDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		imageGalleryUserDao.removeObject(ImageGalleryUser.class, user.getUid());
	    }
	}

    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ImageGallerySession session = new ImageGallerySession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	session.setImageGallery(imageGallery);
	imageGallerySessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ImageGalleryConstants.COMPLETED);
	    imageGallerySessionDao.saveObject(session);
	} else {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session.Could not find shared imageGallery "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared imageGallery session by given session id: " + toolSessionId);
	}
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	imageGallerySessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return imageGalleryOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return imageGalleryOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ImageGalleryUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = null;
	ImageGallery imageGallery = getImageGallerySessionBySessionId(toolSessionId).getImageGallery();
	Set<ImageGalleryItem> items = getImagesForGroup(imageGallery, toolSessionId);
	for (ImageGalleryItem item : items) {
	    if (item.getCreateBy().getUserId() == learnerId) {
		Date newDate = item.getCreateDate();
		if (newDate != null) {
		    if (startDate == null || newDate.before(startDate)) {
			startDate = newDate;
		    }
		    if (endDate == null || newDate.after(endDate)) {
			endDate = newDate;
		    }
		}
	    }
	}

	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }

    /* =================================================================================== */

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    @Override
    public String generateNextImageTitle(Long nextImageTitleNumber) {
	String imageWord = messageService.getMessage("label.authoring.image");
	return imageWord + " " + nextImageTitleNumber;
    }

    public ImageGalleryOutputFactory getImageGalleryOutputFactory() {
	return imageGalleryOutputFactory;
    }

    public void setImageGalleryOutputFactory(ImageGalleryOutputFactory imageGalleryOutputFactory) {
	this.imageGalleryOutputFactory = imageGalleryOutputFactory;
    }

    @Override
    public ImageGalleryConfigItem getConfigItem(String key) {
	return imageGalleryConfigItemDAO.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateImageGalleryConfigItem(ImageGalleryConfigItem item) {
	imageGalleryConfigItemDAO.saveOrUpdate(item);
    }

    @Override
    public Set<ImageGalleryItem> getImagesForGroup(ImageGallery imageGallery, Long sessionId) {
	TreeSet<ImageGalleryItem> images = new TreeSet<>(new ImageGalleryItemComparator());

	List<ImageGalleryUser> grouppedUsers = getUserListBySessionId(sessionId);
	Set<ImageGalleryItem> allImages = imageGallery.getImageGalleryItems();

	for (ImageGalleryItem image : allImages) {
	    for (ImageGalleryUser grouppedUser : grouppedUsers) {
		if (image.isCreateByAuthor() || grouppedUser.getUserId().equals(image.getCreateBy().getUserId())) {
		    images.add(image);
		}
	    }
	}

	return images;
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private ImageGallery getDefaultImageGallery() throws ImageGalleryException {
	Long defaultImageGalleryId = getToolDefaultContentIdBySignature(ImageGalleryConstants.TOOL_SIGNATURE);
	ImageGallery defaultImageGallery = getImageGalleryByContentId(defaultImageGalleryId);
	if (defaultImageGallery == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryException(error);
	}

	return defaultImageGallery;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ImageGalleryException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryException(error);
	}
	return contentId;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getImageGalleryOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}
