package org.jgroups.jmx;

import java.util.Map;
import java.util.Properties;

/**
 * @author Bela Ban
 *
 */
public interface ProtocolMBean {
    String getName();
    String getPropertiesAsString();
    void setProperties(Properties p);
    boolean getStatsEnabled();
    void setStatsEnabled(boolean flag);
    void resetStats();
    String printStats();
    Map dumpStats();
    void create() throws Exception;
    void start() throws Exception;
    void stop();
    void destroy();

}
