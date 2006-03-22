package org.lamsfoundation.lams.tool.example;

import java.util.Date;
import java.util.Set;


/**
 *        @hibernate.class
 *         table="tl_laex11_example"
 *     
 */

public class Example  implements java.io.Serializable {


    // Fields    

     private Long uid;
     private Date createDate;
     private Date updateDate;
     private Long createBy;
     private String title;
     private String instructions;
     private Boolean runOffline;
     private Boolean lockOnFinished;
     private String onlineInstructions;
     private String offlineInstructions;
     private Boolean contentInUse;
     private Boolean defineLater;
     private Long toolContentId;
     private Set exampleAttachments;
     private Set exampleSessions;


    // Constructors

    /** default constructor */
    public Example() {
    }

    
    /** full constructor */
    public Example(Date createDate, Date updateDate, Long createBy, String title, String instructions, Boolean runOffline, Boolean lockOnFinished, String onlineInstructions, String offlineInstructions, Boolean contentInUse, Boolean defineLater, Long toolContentId, Set exampleAttachments, Set exampleSessions) {
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createBy = createBy;
        this.title = title;
        this.instructions = instructions;
        this.runOffline = runOffline;
        this.lockOnFinished = lockOnFinished;
        this.onlineInstructions = onlineInstructions;
        this.offlineInstructions = offlineInstructions;
        this.contentInUse = contentInUse;
        this.defineLater = defineLater;
        this.toolContentId = toolContentId;
        this.exampleAttachments = exampleAttachments;
        this.exampleSessions = exampleSessions;
    }

   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="uid"
     *         
     */

    public Long getUid() {
        return this.uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    /**       
     *            @hibernate.property
     *             column="create_date"
     *         
     */

    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**       
     *            @hibernate.property
     *             column="update_date"
     *         
     */

    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**       
     *            @hibernate.property
     *             column="create_by"
     *             length="20"
     *         
     */

    public Long getCreateBy() {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    /**       
     *            @hibernate.property
     *             column="title"
     *             length="255"
     *         
     */

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    /**       
     *            @hibernate.property
     *             column="instructions"
     *             length="65535"
     *         
     */

    public String getInstructions() {
        return this.instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    /**       
     *            @hibernate.property
     *             column="run_offline"
     *             length="1"
     *         
     */

    public Boolean getRunOffline() {
        return this.runOffline;
    }
    
    public void setRunOffline(Boolean runOffline) {
        this.runOffline = runOffline;
    }
    /**       
     *            @hibernate.property
     *             column="lock_on_finished"
     *             length="1"
     *         
     */

    public Boolean getLockOnFinished() {
        return this.lockOnFinished;
    }
    
    public void setLockOnFinished(Boolean lockOnFinished) {
        this.lockOnFinished = lockOnFinished;
    }
    /**       
     *            @hibernate.property
     *             column="online_instructions"
     *             length="65535"
     *         
     */

    public String getOnlineInstructions() {
        return this.onlineInstructions;
    }
    
    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }
    /**       
     *            @hibernate.property
     *             column="offline_instructions"
     *             length="65535"
     *         
     */

    public String getOfflineInstructions() {
        return this.offlineInstructions;
    }
    
    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }
    /**       
     *            @hibernate.property
     *             column="content_in_use"
     *             length="1"
     *         
     */

    public Boolean getContentInUse() {
        return this.contentInUse;
    }
    
    public void setContentInUse(Boolean contentInUse) {
        this.contentInUse = contentInUse;
    }
    /**       
     *            @hibernate.property
     *             column="define_later"
     *             length="1"
     *         
     */

    public Boolean getDefineLater() {
        return this.defineLater;
    }
    
    public void setDefineLater(Boolean defineLater) {
        this.defineLater = defineLater;
    }
    /**       
     *            @hibernate.property
     *             column="tool_content_id"
     *             length="20"
     *         
     */

    public Long getToolContentId() {
        return this.toolContentId;
    }
    
    public void setToolContentId(Long toolContentId) {
        this.toolContentId = toolContentId;
    }
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="example_uid"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.example.ExampleAttachment"
     *         
     */

    public Set getExampleAttachments() {
        return this.exampleAttachments;
    }
    
    public void setExampleAttachments(Set exampleAttachments) {
        this.exampleAttachments = exampleAttachments;
    }
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="example_uid"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.example.ExampleSession"
     *         
     */

    public Set getExampleSessions() {
        return this.exampleSessions;
    }
    
    public void setExampleSessions(Set exampleSessions) {
        this.exampleSessions = exampleSessions;
    }
   

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuffer buffer = new StringBuffer();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("title").append("='").append(getTitle()).append("' ");			
      buffer.append("instructions").append("='").append(getInstructions()).append("' ");			
      buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");			
      buffer.append("]");
      
      return buffer.toString();
     }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof Example) ) return false;
		 Example castOther = ( Example ) other; 
         
		 return ( (this.getUid()==castOther.getUid()) || ( this.getUid()!=null && castOther.getUid()!=null && this.getUid().equals(castOther.getUid()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         result = 37 * result + ( getUid() == null ? 0 : this.getUid().hashCode() );
         return result;
   }   





}