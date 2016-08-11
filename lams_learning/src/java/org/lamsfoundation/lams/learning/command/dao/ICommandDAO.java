package org.lamsfoundation.lams.learning.command.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.command.model.Command;

public interface ICommandDAO extends IBaseDAO {
    List<Command> getNewCommands(Long lessonId, Date lastCheck);
}