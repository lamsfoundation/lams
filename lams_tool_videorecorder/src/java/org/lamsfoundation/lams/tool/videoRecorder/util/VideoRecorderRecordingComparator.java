package org.lamsfoundation.lams.tool.videoRecorder.util;

import java.util.Comparator;
import java.util.Date;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;

/**
 *
 * @author Paul Georges
 *
 */
public class VideoRecorderRecordingComparator implements Comparator<VideoRecorderRecordingDTO> {

    private String sortBy;
    private String sortDirection;

    public VideoRecorderRecordingComparator(String sortBy, String sortDirection) {
	this.sortBy = sortBy;
	this.sortDirection = sortDirection;
    }

    @Override
    public int compare(VideoRecorderRecordingDTO dto1, VideoRecorderRecordingDTO dto2) {
	if (sortBy.compareTo("author") == 0) {
	    VideoRecorderUser user1 = dto1.getCreateBy();
	    VideoRecorderUser user2 = dto2.getCreateBy();

	    if (user1 == null) {
		return -1;
	    }

	    if (user2 == null) {
		return 1;
	    }

	    String userName1 = user1.getFirstName() + " " + user1.getLastName();
	    String userName2 = user2.getFirstName() + " " + user2.getLastName();
	    userName1 = userName1.toLowerCase();
	    userName2 = userName2.toLowerCase();

	    int difference = userName1.compareTo(userName2);

	    if (Math.abs(difference) == 0) {
		return 1;
	    }

	    if (sortDirection.compareTo("ascending") == 0) {

		int result = difference / Math.abs(difference);
		return result;
	    } else if (sortDirection.compareTo("descending") == 0) {
		int result = -difference / Math.abs(difference);
		return result;
	    }
	} else if (sortBy.compareTo("date") == 0) {
	    VideoRecorderUser user1 = dto1.getCreateBy();
	    VideoRecorderUser user2 = dto2.getCreateBy();

	    if (user1 == null) {
		return -1;
	    }

	    if (user2 == null) {
		return 1;
	    }

	    Date date1 = dto1.getCreateDate();
	    Date date2 = dto2.getCreateDate();

	    int difference = date1.compareTo(date2);

	    if (Math.abs(difference) == 0) {
		return 1;
	    }

	    if (sortDirection.compareTo("ascending") == 0) {
		int result = difference / Math.abs(difference);
		return result;
	    } else if (sortDirection.compareTo("descending") == 0) {
		int result = -difference / Math.abs(difference);
		return result;
	    }

	} else if (sortBy.compareTo("title") == 0) {
	    VideoRecorderUser user1 = dto1.getCreateBy();
	    VideoRecorderUser user2 = dto2.getCreateBy();

	    if (user1 == null) {
		return -1;
	    }

	    if (user2 == null) {
		return 1;
	    }

	    String title1 = dto1.getTitle().toLowerCase();
	    String title2 = dto2.getTitle().toLowerCase();

	    int difference = title1.compareTo(title2);

	    if (Math.abs(difference) == 0) {
		return 1;
	    }

	    if (sortDirection.compareTo("ascending") == 0) {

		int result = difference / Math.abs(difference);
		return result;
	    } else if (sortDirection.compareTo("descending") == 0) {
		int result = -difference / Math.abs(difference);
		return result;
	    }
	}

	return 0;
    }
}
