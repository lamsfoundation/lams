package org.lamsfoundation.lams.tool.videoRecorder.util;

import java.util.Comparator;
import java.util.Date;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderCommentDTO;

/**
 *
 * @author Paul Georges
 *
 */
public class VideoRecorderCommentComparator implements Comparator<VideoRecorderCommentDTO> {

    private String sortBy;
    private String sortDirection;

    public VideoRecorderCommentComparator(String sortBy, String sortDirection) {
	this.sortBy = sortBy;
	this.sortDirection = sortDirection;
    }

    @Override
    public int compare(VideoRecorderCommentDTO dto1, VideoRecorderCommentDTO dto2) {
	if (sortBy.compareTo("date") == 0) {
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

	}

	return 0;
    }
}