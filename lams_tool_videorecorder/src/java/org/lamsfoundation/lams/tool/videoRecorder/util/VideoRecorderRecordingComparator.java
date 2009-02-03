package org.lamsfoundation.lams.tool.videoRecorder.util;

import java.util.Comparator;
import java.util.Date;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;

/**
 * 
 * @author Paul Georges
 *
 */
public class VideoRecorderRecordingComparator implements Comparator<VideoRecorderRecordingDTO> {

	private String sortBy;
	private String sortDirection;
	
	public VideoRecorderRecordingComparator(String sortBy, String sortDirection){
		this.sortBy = sortBy;
		this.sortDirection = sortDirection;
	}
	
	public int compare(VideoRecorderRecordingDTO dto1, VideoRecorderRecordingDTO dto2) {
		if(sortBy.compareTo("author") == 0){
			Long userId1 = dto1.getCreateBy().getUid();
			Long userId2 = dto2.getCreateBy().getUid();
			
			if(userId1 > userId2) {
				if(sortDirection == "ascending")
					return -1;
				else
					return 1;
			} else if(userId1 == userId2) {
				return 0;
			} else {
				if(sortDirection == "ascending")
					return 1;
				else
					return -1;
			}
		}
		else if(sortBy.compareTo("date") == 0){
			Date date1 = dto1.getCreateDate();
			Date date2 = dto2.getCreateDate();
			
			if(sortDirection.compareTo("ascending") == 0){
				int difference = date1.compareTo(date2);
				int result = difference / Math.abs(difference);
				return result;
			}
			else if(sortDirection.compareTo("descending") == 0){
				int difference = date1.compareTo(date2);
				int result = -difference / Math.abs(difference);
				return result;
			}
			
		}
		else if(sortBy.compareTo("title") == 0){
			String title1 = dto1.getTitle();
			String title2 = dto2.getTitle();
			
			if(sortDirection.compareTo("ascending") == 0){
				int difference = title1.compareTo(title2);
				int result = difference / Math.abs(difference);
				return result;
			}
			else if(sortDirection.compareTo("descending") == 0){
				int difference = title1.compareTo(title2);
				int result = -difference / Math.abs(difference);
				return result;
			}
		}

		return 0;
	}
}
