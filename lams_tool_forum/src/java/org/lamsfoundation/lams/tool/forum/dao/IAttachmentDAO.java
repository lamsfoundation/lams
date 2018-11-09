package org.lamsfoundation.lams.tool.forum.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;

public interface IAttachmentDAO extends IBaseDAO {

    Attachment getById(Long attachmentId);

}
