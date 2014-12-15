package org.quartz.impl.jdbcjobstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.TriggerKey;
import org.quartz.spi.OperableTrigger;

/**
 * An interface which provides an implementation for storing a particular
 * type of <code>Trigger</code>'s extended properties.
 *  
 * @author jhouse
 */
public interface TriggerPersistenceDelegate {

    public void initialize(String tablePrefix, String schedulerName);
    
    public boolean canHandleTriggerType(OperableTrigger trigger);
    
    public String getHandledTriggerTypeDiscriminator();
    
    public int insertExtendedTriggerProperties(Connection conn, OperableTrigger trigger, String state, JobDetail jobDetail) throws SQLException, IOException;

    public int updateExtendedTriggerProperties(Connection conn, OperableTrigger trigger, String state, JobDetail jobDetail) throws SQLException, IOException;
    
    public int deleteExtendedTriggerProperties(Connection conn, TriggerKey triggerKey) throws SQLException;

    public TriggerPropertyBundle loadExtendedTriggerProperties(Connection conn, TriggerKey triggerKey) throws SQLException;
    
    
    class TriggerPropertyBundle {
        
        private ScheduleBuilder<?> sb;
        private String[] statePropertyNames;
        private Object[] statePropertyValues;
        
        public TriggerPropertyBundle(ScheduleBuilder<?> sb, String[] statePropertyNames, Object[] statePropertyValues) {
            this.sb = sb;
            this.statePropertyNames = statePropertyNames;
            this.statePropertyValues = statePropertyValues;
        }

        public ScheduleBuilder<?> getScheduleBuilder() {
            return sb;
        }

        public String[] getStatePropertyNames() {
            return statePropertyNames;
        }

        public Object[] getStatePropertyValues() {
            return statePropertyValues;
        }
    }
}
