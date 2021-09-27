/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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
 * ************************************************************************
 */

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.Nullable;

/**
 * Base class for all activities. If you add another subclass, you must update ActivityDAO.getActivityByActivityId() and
 * add a ACTIVITY_TYPE constant.
 */
@Entity
@Table(name = "lams_learning_activity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "learning_activity_type_id", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Activity implements Serializable, Nullable, Comparable<Activity> {

    private static final long serialVersionUID = -7181715764819934724L;
    // ---------------------------------------------------------------------
    // Class Level Constants
    // ---------------------------------------------------------------------
    /*
     * static final variables indicating the type of activities available for a LearningDesign. As new types of
     * activities are added, these constants must be updated, as well as ActivityDAO.getActivityByActivityId()
     *
     * OPTIONS_WITH_SEQUENCES_TYPE is set up just to support Authoring. The server treads OptionsActivity and
     * OptionalSequenceActivity the same.
     */
    /* **************************************************************** */
    public static final int TOOL_ACTIVITY_TYPE = 1;
    public static final int GROUPING_ACTIVITY_TYPE = 2;
    public static final int SYNCH_GATE_ACTIVITY_TYPE = 3;
    public static final int SCHEDULE_GATE_ACTIVITY_TYPE = 4;
    public static final int PERMISSION_GATE_ACTIVITY_TYPE = 5;
    public static final int PARALLEL_ACTIVITY_TYPE = 6;
    public static final int OPTIONS_ACTIVITY_TYPE = 7;
    public static final int SEQUENCE_ACTIVITY_TYPE = 8;
    public static final int SYSTEM_GATE_ACTIVITY_TYPE = 9;
    public static final int CHOSEN_BRANCHING_ACTIVITY_TYPE = 10;
    public static final int GROUP_BRANCHING_ACTIVITY_TYPE = 11;
    public static final int TOOL_BRANCHING_ACTIVITY_TYPE = 12;
    public static final int OPTIONS_WITH_SEQUENCES_TYPE = 13;
    public static final int CONDITION_GATE_ACTIVITY_TYPE = 14;
    public static final int FLOATING_ACTIVITY_TYPE = 15;
    public static final int PASSWORD_GATE_ACTIVITY_TYPE = 16;
    /** *************************************************************** */

    /** *************************************************************** */

    /***************************************************************************
     * static final variables indicating the grouping_support of activities
     **************************************************************************/
    public static final int GROUPING_SUPPORT_NONE = 1;
    public static final int GROUPING_SUPPORT_OPTIONAL = 2;
    public static final int GROUPING_SUPPORT_REQUIRED = 3;
    /** *************************************************************** */

    /**
     * Entries for an activity in a language property file
     */
    public static final String I18N_TITLE = "activity.title";
    public static final String I18N_DESCRIPTION = "activity.description";
    public static final String I18N_HELP_TEXT = "activity.helptext";

    @Id
    @Column(name = "activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    /**
     * Authoring generated value. Unique per LearningDesign.
     */
    @Column(name = "activity_ui_id")
    private Integer activityUIID;

    @Column
    private String description;

    @Column
    private String title;

    /**
     * UI specific attribute indicating the position of the activity
     */
    @Column
    private Integer xcoord;

    /**
     * UI specific attribute indicating the position of the activity
     */
    @Column
    private Integer ycoord;

    /**
     * Indicates the order in which the activities appear inside complex activities. Starts from 0, 1 and so on.
     */
    @Column(name = "order_id")
    private Integer orderId;

    /** Date this activity was created */
    @Column(name = "create_date_time")
    private Date createDateTime;

    /**
     * The image that represents the icon of this activity in the UI
     */
    @Column(name = "library_activity_ui_image")
    private String libraryActivityUiImage;

    /** The LearningLibrary of which this activity is a part */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_library_id")
    private LearningLibrary learningLibrary;

    /**
     * The activity that acts as a container/parent for this activity. Normally would be one of the complex activities
     * which have child activities defined inside them.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_activity_id")
    private Activity parentActivity;

    /**
     * Single Library can have one or more activities defined inside it. This field indicates which activity is this.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_activity_id")
    private Activity libraryActivity;

    /** The LearningDesign to which this activity belongs */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_design_id")
    private LearningDesign learningDesign;

    /** The Grouping that applies to this activity */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grouping_id")
    private Grouping grouping;

    /**
     * The grouping_ui_id of the Grouping that applies that to this activity
     */
    @Column(name = "grouping_ui_id")
    private Integer groupingUIID;

    @Column(name = "learning_activity_type_id", insertable = false, updatable = false)
    private Integer activityTypeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transition_to_id")
    private Transition transitionTo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transition_from_id")
    private Transition transitionFrom;

    /** the activity_ui_id of the parent activity */
    @Column(name = "parent_ui_id")
    private Integer parentUIID;

    @Column(name = "apply_grouping_flag")
    private Boolean applyGrouping;

    @Column(name = "grouping_support_type_id")
    private Integer groupingSupportType;

    /**
     * Name of the file (including the package) that contains the text strings for this activity. e.g.
     * org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
     */
    @Column(name = "language_file")
    private String languageFile;

    /**
     * An activity is readOnly when a learner starts doing the activity. Used in editOnFly.
     */
    @Column(name = "read_only")
    private Boolean readOnly;

    /**
     * An activity is initialised if it is ready to be used in lesson ie the tool content is set up, schedule gates are
     * scheduled, etc. Used to detect which activities need to be initialised for live edit.
     */
    @Column
    private Boolean initialised;

    /**
     * If stopAfterActivity is true, then the progress engine should "end" the lesson at this point. Used to arbitrarily
     * stop somewhere in a design, such as at the end of the branch. The normal final activity of a design does not
     * necessarily have this set - the progress engine will just stop when it runs out of transitions to follow.
     */
    @Column(name = "stop_after_activity")
    private Boolean stopAfterActivity;

    /**
     * The activities that supplied inputs to this activity.
     */
    @ManyToMany
    @JoinTable(name = "lams_input_activity", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "input_activity_id"))
    @OrderBy("activity_id")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Activity> inputActivities = new HashSet<>();

    /**
     * The BranchActivityEntries that map conditions to this Activity; bi-directional association required (e.g.
     * LDEV-1910)
     */
    @OneToMany(mappedBy = "branchingActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<BranchActivityEntry> branchActivityEntries = new HashSet<>();

    // ---------------------------------------------------------------------
    // Object constructors
    // ---------------------------------------------------------------------

    /*
     * For the createDateTime fields, if the value is null, then it will default to the current time.
     */

    /** full constructor */
    public Activity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
	    Integer orderId, Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign, Grouping grouping,
	    Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
	    Boolean stopAfterActivity, Set<Activity> inputActivities, Set<BranchActivityEntry> branchActivityEntries) {
	this.activityId = activityId;
	activityUIID = id;
	this.description = description;
	this.title = title;
	this.xcoord = xcoord;
	this.ycoord = ycoord;
	this.orderId = orderId;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.learningLibrary = learningLibrary;
	this.parentActivity = parentActivity;
	this.parentUIID = parentUIID;
	this.libraryActivity = libraryActivity;
	this.learningDesign = learningDesign;
	this.grouping = grouping;
	this.activityTypeId = activityTypeId;
	this.transitionTo = transitionTo;
	this.transitionFrom = transitionFrom;
	this.languageFile = languageFile;
	readOnly = false;
	initialised = false;
	this.stopAfterActivity = stopAfterActivity;
	this.inputActivities = inputActivities;
	this.branchActivityEntries = branchActivityEntries;
    }

    /** default constructor */
    public Activity() {
	grouping = null;
	createDateTime = new Date(); // default value is set to when the object is created
	readOnly = false;
	initialised = false;
	stopAfterActivity = false;
    }

    /** minimal constructor */
    public Activity(Long activityId, Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    LearningDesign learningDesign, Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom) {
	this.activityId = activityId;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.learningLibrary = learningLibrary;
	this.parentActivity = parentActivity;
	this.learningDesign = learningDesign;
	this.grouping = grouping;
	this.activityTypeId = activityTypeId;
	this.transitionTo = transitionTo;
	this.transitionFrom = transitionFrom;
	readOnly = false;
	initialised = false;
	stopAfterActivity = false;
    }

    public static Activity getActivityInstance(int activityType) {
	// the default constructors don't set up the activity type
	// so we need to do that manually
	Activity activity = null;
	switch (activityType) {
	    case TOOL_ACTIVITY_TYPE:
		activity = new ToolActivity();
		break;
	    case OPTIONS_ACTIVITY_TYPE:
		activity = new OptionsActivity();
		break;
	    case PARALLEL_ACTIVITY_TYPE:
		activity = new ParallelActivity();
		break;
	    case SEQUENCE_ACTIVITY_TYPE:
		activity = new SequenceActivity();
		break;
	    case SYNCH_GATE_ACTIVITY_TYPE:
		activity = new SynchGateActivity();
		break;
	    case SCHEDULE_GATE_ACTIVITY_TYPE:
		activity = new ScheduleGateActivity();
		break;
	    case PERMISSION_GATE_ACTIVITY_TYPE:
		activity = new PermissionGateActivity();
		break;
	    case SYSTEM_GATE_ACTIVITY_TYPE:
		activity = new SystemGateActivity();
		break;
	    case CONDITION_GATE_ACTIVITY_TYPE:
		activity = new ConditionGateActivity();
		break;
	    case CHOSEN_BRANCHING_ACTIVITY_TYPE:
		activity = new ChosenBranchingActivity();
		break;
	    case GROUP_BRANCHING_ACTIVITY_TYPE:
		activity = new GroupBranchingActivity();
		break;
	    case TOOL_BRANCHING_ACTIVITY_TYPE:
		activity = new ToolBranchingActivity();
		break;
	    case OPTIONS_WITH_SEQUENCES_TYPE:
		activity = new OptionsWithSequencesActivity();
		break;
	    case FLOATING_ACTIVITY_TYPE:
		activity = new FloatingActivity();
		break;
	    case PASSWORD_GATE_ACTIVITY_TYPE:
		activity = new PasswordGateActivity();
		break;
	    default:
		activity = new GroupingActivity();
		break;
	}
	activity.setActivityTypeId(activityType);
	return activity;
    }

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public Integer getActivityUIID() {
	return activityUIID;
    }

    public void setActivityUIID(Integer id) {
	activityUIID = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Integer getXcoord() {
	return xcoord;
    }

    public void setXcoord(Integer xcoord) {
	this.xcoord = xcoord;
    }

    public Integer getYcoord() {
	return ycoord;
    }

    public void setYcoord(Integer ycoord) {
	this.ycoord = ycoord;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    /* If createDateTime is null, then it will default to the current date/time */
    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
    }

    public org.lamsfoundation.lams.learningdesign.LearningLibrary getLearningLibrary() {
	return learningLibrary;
    }

    public void setLearningLibrary(org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary) {
	this.learningLibrary = learningLibrary;
    }

    public org.lamsfoundation.lams.learningdesign.Activity getParentActivity() {
	return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
	this.parentActivity = parentActivity;
    }

    public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
	return learningDesign;
    }

    public void setLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
	this.learningDesign = learningDesign;
    }

    public org.lamsfoundation.lams.learningdesign.Grouping getGrouping() {
	return grouping;
    }

    public void setGrouping(org.lamsfoundation.lams.learningdesign.Grouping grouping) {
	this.grouping = grouping;
    }

    public Integer getActivityTypeId() {
	return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
	this.activityTypeId = activityTypeId;
    }

    /**
     * @return Returns the applyGrouping.
     */
    public Boolean getApplyGrouping() {
	return applyGrouping;
    }

    /**
     * @param applyGrouping
     *            The applyGrouping to set.
     */
    public void setApplyGrouping(Boolean applyGrouping) {
	this.applyGrouping = applyGrouping;
    }

    /**
     * @return Returns the groupingSupportType.
     */
    public Integer getGroupingSupportType() {
	return groupingSupportType;
    }

    /**
     * @param groupingSupportType
     *            The groupingSupportType to set.
     */
    public void setGroupingSupportType(Integer groupingSupportType) {
	this.groupingSupportType = groupingSupportType;
    }

    /**
     * @return Returns the readOnly.
     */
    public Boolean getReadOnly() {
	return readOnly;
    }

    /**
     * @param readOnly
     *            The readOnly to set.
     */
    public void setReadOnly(Boolean readOnly) {
	this.readOnly = readOnly;
    }

    /**
     * @return Returns the initialised flag.
     */
    public Boolean isInitialised() {
	return initialised;
    }

    /**
     * @param readOnly
     *            The readOnly to set.
     */
    public void setInitialised(Boolean initialised) {
	this.initialised = initialised;
    }

    /**
     * @return Returns the stopAfterActivity flag.
     */
    public Boolean isStopAfterActivity() {
	return stopAfterActivity;
    }

    /**
     * @param readOnly
     *            The stopAfterActivity to set.
     */
    public void setStopAfterActivity(Boolean stopAfterActivity) {
	this.stopAfterActivity = stopAfterActivity;
    }

    /**
     * @return Returns the inputActivities.
     */
    public Set<Activity> getInputActivities() {
	return inputActivities;
    }

    /**
     * @param InputActivities
     *            The InputActivities to set.
     */
    public void setInputActivities(Set<Activity> inputActivities) {
	this.inputActivities = inputActivities;
    }

    /**
     * @return Returns the branchActivityEntries.
     */
    public Set<BranchActivityEntry> getBranchActivityEntries() {
	return branchActivityEntries;
    }

    /**
     * @param branchActivityEntries
     *            The branchActivityEntries to set.
     */
    public void setBranchActivityEntries(Set<BranchActivityEntry> branchActivityEntries) {
	this.branchActivityEntries = branchActivityEntries;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", activityId).append("activityUIID", activityUIID)
		.append("description", description).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof Activity)) {
	    return false;
	}
	Activity castOther = (Activity) other;
	return new EqualsBuilder().append(this.getActivityId(), castOther.getActivityId())
		.append(this.getActivityUIID(), castOther.getActivityUIID()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getActivityId()).append(getActivityUIID()).toHashCode();
    }

    /**
     * @return Returns the libraryActivityUiImage.
     */
    public String getLibraryActivityUiImage() {
	return libraryActivityUiImage;
    }

    /**
     * @param libraryActivityUiImage
     *            The libraryActivityUiImage to set.
     */
    public void setLibraryActivityUiImage(String libraryActivityUiImage) {
	this.libraryActivityUiImage = libraryActivityUiImage;
    }

    /**
     * This function returns the Transition that STARTS FROM THIS ACTIVITY. In simpler words the next activity in the
     * transition.
     *
     * For example, if we have a transition as following A --> B --> C. For activity B this function would return C.
     * That is the Transition FROM activity B.
     *
     * @return Returns the transitionFrom.
     */
    public Transition getTransitionFrom() {
	return transitionFrom;
    }

    /**
     * @param transitionFrom
     *            The transitionFrom to set.
     */
    public void setTransitionFrom(Transition transitionFrom) {
	this.transitionFrom = transitionFrom;
    }

    /**
     * This function returns the Transition that POINTS TO THIS ACTIVITY and NOT the transition that this activity
     * points to.
     *
     * For example, if we have a transition as following A --> B --> C. For activity B this function would return A.
     * That is the Transition that points TO activity B.
     *
     * @return Returns the transitionTo.
     */
    public Transition getTransitionTo() {
	return transitionTo;
    }

    /**
     * @param transitionTo
     *            The transitionTo to set.
     */
    public void setTransitionTo(Transition transitionTo) {
	this.transitionTo = transitionTo;
    }

    public Integer getParentUIID() {
	return parentUIID;
    }

    public void setParentUIID(Integer parent_ui_id) {
	parentUIID = parent_ui_id;
    }

    public Activity getLibraryActivity() {
	return libraryActivity;
    }

    public void setLibraryActivity(Activity libraryActivity) {
	this.libraryActivity = libraryActivity;
    }

    public Integer getGroupingUIID() {
	return groupingUIID;
    }

    public void setGroupingUIID(Integer groupingUIID) {
	this.groupingUIID = groupingUIID;
    }

    public String getLanguageFile() {
	return languageFile;
    }

    public void setLanguageFile(String languageFile) {
	this.languageFile = languageFile;
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------

    /**
     * This method that get all tool activities belong to the current activity.
     *
     * As the activity object structure might be infinite, we recursively loop through the entire structure and added
     * all tool activities into the set that we want to return. This method calls a method getToolActivitiesInActivity()
     * which must be defined in subclasses for tool or a complex activities. This handles the polymorphic aspect of this
     * function. (Note: we can't use instanceOf as we are dealing with Hibernate proxies.)
     *
     * @return the set of all tool activities.
     */
    public Set<ToolActivity> getAllToolActivities() {
	SortedSet<ToolActivity> toolActivities = new TreeSet<>(new ActivityOrderComparator());
	getToolActivitiesInActivity(toolActivities);
	return toolActivities;
    }

    protected void getToolActivitiesInActivity(SortedSet<ToolActivity> toolActivities) {
	// a simple activity doesn't have any tool activities
    }

    /**
     * Return the group information for the requested user when he is running current activity instance, based on the
     * grouping data in the activity.
     *
     * @param learner
     *            the requested user
     * @return the group that this user belongs to.
     */
    public Group getGroupFor(User learner) {
	return getGroupFor(learner, this.getGrouping());
    }

    /**
     * Return the group information for the requested user when he is running current activity instance, based on the
     * given grouping.
     * <P>
     * If we are using the grouping set up in the activity, the grouping will be this.getGrouping(). If the activity
     * isn't grouped, then it should use the class grouping.
     *
     * @param learner
     *            the requested user
     * @return the group that this user belongs to.
     */
    protected Group getGroupFor(User learner, Grouping inGrouping) {
	if (inGrouping != null) {
	    for (Iterator<Group> i = inGrouping.getGroups().iterator(); i.hasNext();) {
		Group group = i.next();
		if (inGrouping.isLearnerGroup(group) && group.hasLearner(learner)) {
		    return group;
		}
	    }
	}

	return new NullGroup();
    }

    // ---------------------------------------------------------------------
    // Activity Type checking methods
    // ---------------------------------------------------------------------
    /**
     * Check up whether an activity is tool activity or not.
     *
     * @return is this activity a tool activity?
     */
    public boolean isToolActivity() {
	return getActivityTypeId().intValue() == Activity.TOOL_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is sequence activity or not.
     *
     * @return is this activity a sequence activity?
     */
    public boolean isSequenceActivity() {
	return getActivityTypeId().intValue() == Activity.SEQUENCE_ACTIVITY_TYPE;
    }

    public boolean isParallelActivity() {
	return getActivityTypeId().intValue() == Activity.PARALLEL_ACTIVITY_TYPE;
    }

    public boolean isOptionsActivity() {
	return (getActivityTypeId().intValue() == Activity.OPTIONS_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.OPTIONS_WITH_SEQUENCES_TYPE);
    }

    public boolean isOptionsWithSequencesActivity() {
	return getActivityTypeId().intValue() == Activity.OPTIONS_WITH_SEQUENCES_TYPE;
    }

    public boolean isComplexActivity() {
	return (getActivityTypeId().intValue() == Activity.SEQUENCE_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.PARALLEL_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.OPTIONS_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.OPTIONS_WITH_SEQUENCES_TYPE)
		|| (getActivityTypeId().intValue() == Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.TOOL_BRANCHING_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.FLOATING_ACTIVITY_TYPE);
    }

    public boolean isSystemToolActivity() {
	return ISystemToolActivity.class.isInstance(this);
    }

    public boolean isGateActivity() {
	return (getActivityTypeId().intValue() == Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.SYNCH_GATE_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.SYSTEM_GATE_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.CONDITION_GATE_ACTIVITY_TYPE
			|| (getActivityTypeId().intValue() == Activity.PASSWORD_GATE_ACTIVITY_TYPE));
    }

    /**
     * Check up whether an activity is synch gate activity or not.
     *
     * @return is this activity a synch gate activity?
     */
    public boolean isSynchGate() {
	return getActivityTypeId().intValue() == Activity.SYNCH_GATE_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is permission gate activity or not.
     *
     * @return is this activity a permission gate activity.
     */
    public boolean isPermissionGate() {
	return getActivityTypeId().intValue() == Activity.PERMISSION_GATE_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is schedule gate activity or not.
     *
     * @return is this activity a schedule gate activity.
     */
    public boolean isScheduleGate() {
	return getActivityTypeId().intValue() == Activity.SCHEDULE_GATE_ACTIVITY_TYPE;
    }

    public boolean isConditionGate() {
	return getActivityTypeId().intValue() == Activity.CONDITION_GATE_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is schedule gate activity or not.
     *
     * @return is this activity a schedule gate activity.
     */
    public boolean isSystemGate() {
	return getActivityTypeId().intValue() == Activity.SYSTEM_GATE_ACTIVITY_TYPE;
    }

    public boolean isPasswordGate() {
	return getActivityTypeId().intValue() == Activity.PASSWORD_GATE_ACTIVITY_TYPE;
    }

    /**
     * /** Check up whether an activity is grouping activity or not.
     *
     * @return is this activity a grouping activity
     */
    public boolean isGroupingActivity() {
	return getActivityTypeId().intValue() == Activity.GROUPING_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is some sort of branching activity or not
     *
     * @return is this activity a branching activity
     */
    public boolean isBranchingActivity() {
	return (getActivityTypeId().intValue() == Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| (getActivityTypeId().intValue() == Activity.TOOL_BRANCHING_ACTIVITY_TYPE);
    }

    /**
     * Check up whether an activity is branching activity based on the monitor choice or not.
     *
     * @return is this activity a branching activity
     */
    public boolean isChosenBranchingActivity() {
	return getActivityTypeId().intValue() == Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is branching activity based on an existing group or not.
     *
     * @return is this activity a branching activity
     */
    public boolean isGroupBranchingActivity() {
	return getActivityTypeId().intValue() == Activity.GROUP_BRANCHING_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is branching activity based on another activity's output or not.
     *
     * @return is this activity a branching activity
     */
    public boolean isToolBranchingActivity() {
	return getActivityTypeId().intValue() == Activity.TOOL_BRANCHING_ACTIVITY_TYPE;
    }

    /**
     * Check up whether an activity is floating activity.
     *
     * @return is this activity a floating activity
     */
    public boolean isFloatingActivity() {
	return getActivityTypeId().intValue() == Activity.FLOATING_ACTIVITY_TYPE;
    }

    public boolean isActivityReadOnly() {
	return readOnly;
    }

    // ---------------------------------------------------------------------
    // Data Transfer object creation methods
    // ---------------------------------------------------------------------
    /**
     * Return the activity dto for progress view.
     *
     * @return the activity dto.
     */
    public ProgressActivityDTO getProgressActivityData() {
	return new ProgressActivityDTO(activityId);
    }

    /**
     * Get the authoring DTO for this activity. Overidden by ComplexActivity.
     */
    public Set<AuthoringActivityDTO> getAuthoringActivityDTOSet(ArrayList<BranchActivityEntryDTO> branchMappings,
	    String languageCode) {
	Set<AuthoringActivityDTO> dtoSet = new TreeSet<>(new ActivityDTOOrderComparator());
	dtoSet.add(new AuthoringActivityDTO(this, branchMappings, languageCode));
	return dtoSet;
    }

    public LibraryActivityDTO getLibraryActivityDTO(String languageCode) {
	return new LibraryActivityDTO(this, languageCode);
    }

    /**
     * Create a deep copy of the this activity. It should return the same subclass as the activity being copied.
     * Generally doesn't copy the "link" type fields like transitions, learning design, etc.
     *
     * @param uiidOffset
     *            - this should be added to UIID fields in any new objects. Used when importing a design into another
     *            design.
     * @return deep copy of this object
     */
    public abstract Activity createCopy(int uiidOffset);

    protected void copyToNewActivity(Activity newActivity, int uiidOffset) {

	newActivity.setActivityUIID(LearningDesign.addOffset(this.getActivityUIID(), uiidOffset));
	newActivity.setDescription(this.getDescription());
	newActivity.setTitle(this.getTitle());
	newActivity.setXcoord(this.getXcoord());
	newActivity.setYcoord(this.getYcoord());
	newActivity.setActivityTypeId(this.getActivityTypeId());

	newActivity.setGroupingSupportType(this.getGroupingSupportType());
	newActivity.setApplyGrouping(this.getApplyGrouping());

	newActivity.setGrouping(this.getGrouping());
	newActivity.setGroupingUIID(LearningDesign.addOffset(this.getGroupingUIID(), uiidOffset));

	newActivity.setCreateDateTime(new Date());
	newActivity.setLearningLibrary(this.getLearningLibrary());
	newActivity.setLibraryActivity(this.getLibraryActivity());
	newActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());
	newActivity.setLanguageFile(this.getLanguageFile());

	newActivity.setOrderId(this.getOrderId());
	newActivity.setReadOnly(this.getReadOnly());
	newActivity.setStopAfterActivity(this.isStopAfterActivity());

	newActivity.setParentActivity(this.getParentActivity());
	newActivity.setParentUIID(LearningDesign.addOffset(this.getParentUIID(), uiidOffset));

	if ((this.getInputActivities() != null) && (this.getInputActivities().size() > 0)) {
	    newActivity.setInputActivities(new HashSet<Activity>());
	    newActivity.getInputActivities().addAll(this.getInputActivities());
	}
    }

    // ---------------------------------------------------------------------
    // Data Validation methods
    // ---------------------------------------------------------------------

    /**
     * Validate activity
     *
     */
    public Vector<ValidationErrorDTO> validateActivity(MessageService messageService) {
	return null;
    }

    /**
     * Get the input activity UIIDs in a format suitable for Authoring. See also getToolInputActivityID
     */
    public ArrayList<Integer> getInputActivityUIIDs() {
	ArrayList<Integer> list = new ArrayList<>();
	if ((getInputActivities() != null) && (getInputActivities().size() > 0)) {
	    Iterator<Activity> iter = getInputActivities().iterator();
	    while (iter.hasNext()) {
		Activity inputAct = iter.next();
		list.add(inputAct.getActivityUIID());
	    }
	}
	return list;
    }

    /**
     * Get the first input activity's UIID as the tool input activity. The db is set up to allow multiple input
     * activities, but at present we only support one. See also getInputActivityUIIDs.
     */
    public Integer getToolInputActivityUIID() {
	if (getInputActivities() != null) {
	    Iterator<Activity> iter = getInputActivities().iterator();
	    if (iter.hasNext()) {
		return iter.next().getActivityUIID();
	    }
	}
	return null;
    }

    /**
     * Is this activity inside a branch? If so, return turn branch activity (ie the sequence, not the branching
     * activity. Returns null if not in a branch.
     */
    public Activity getParentBranch() {
	if (isSequenceActivity() && (getParentActivity() != null) && getParentActivity().isBranchingActivity()) {
	    // I'm a branch, so start the process off with my parent!
	    return getParentBranch(getParentActivity(), new HashSet<Long>());
	} else {
	    return getParentBranch(this, new HashSet<Long>());
	}
    }

    private Activity getParentBranch(Activity activity, Set<Long> processedActivityIds) {

	Activity parent = activity.getParentActivity();

	if (parent == null) {
	    return null;
	}

	if (parent.isBranchingActivity()) {
	    return activity;
	}

	// double check that we haven't already processed this activity. Should never happen but if it does it
	// would cause an infinite loop.
	if (processedActivityIds.contains(activity.getActivityId())) {
	    return null;
	}

	processedActivityIds.add(activity.getActivityId());

	return getParentBranch(parent, processedActivityIds);
    }

    public boolean isFloating() {
	if (parentActivity == null) {
	    return false;
	}

	return (parentActivity.isFloatingActivity()) ? true : parentActivity.isFloating();
    }

    @Override
    public int compareTo(Activity anotherActivity) {

	Long activityID1 = getActivityId();
	Long activityID2 = anotherActivity.getActivityId();

	if ((activityID1 == null) || (activityID2 == null)) {
	    return 0;
	}

	Long result = (activityID1 - activityID2) * -1;
	return result.intValue();
    }

}