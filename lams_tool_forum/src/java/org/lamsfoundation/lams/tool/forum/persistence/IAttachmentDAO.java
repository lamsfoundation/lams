package org.lamsfoundation.lams.tool.forum.persistence;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface IAttachmentDAO extends IBaseDAO {

    void saveOrUpdate(Attachment attachment);

    void delete(Attachment attachment);

    Attachment getById(Long attachmentId);

}
