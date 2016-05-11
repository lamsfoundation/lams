package org.jgroups.protocols.pbcast;

import org.jgroups.*;
import org.jgroups.annotations.GuardedBy;
import org.jgroups.stack.Protocol;
import org.jgroups.util.Digest;
import org.jgroups.util.Promise;
import org.jgroups.util.Streamable;
import org.jgroups.util.Util;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flush, as it name implies, forces group members to flush their pending
 * messages while blocking them to send any additional messages. The process of
 * flushing acquiesces the group so that state transfer or a join can be done.
 * It is also called stop-the-world model as nobody will be able to send
 * messages while a flush is in process.
 * 
 * <p>
 * Flush is needed for:
 * <p>
 * (1) State transfer. When a member requests state transfer, the coordinator
 * tells everyone to stop sending messages and waits for everyone's ack. Then it
 * asks the application for its state and ships it back to the requester. After
 * the requester has received and set the state successfully, the coordinator
 * tells everyone to resume sending messages.
 * <p>
 * (2) View changes (e.g.a join). Before installing a new view V2, flushing
 * would ensure that all messages *sent* in the current view V1 are indeed
 * *delivered* in V1, rather than in V2 (in all non-faulty members). This is
 * essentially Virtual Synchrony.
 * 
 * 
 * 
 * @author Vladimir Blagojevic
 *
 * @since 2.4
 */
public class FLUSH extends Protocol {
    
    public static final String NAME = "FLUSH";
    
    
    /* ------------------------------------------ Properties  ------------------------------------------ */
    private long timeout = 8000;
     
    private long start_flush_timeout = 2000;

    private boolean enable_reconciliation = true;

    
    /* --------------------------------------------- JMX  ---------------------------------------------- */
    
    
    private long startFlushTime;

    private long totalTimeInFlush;

    private int numberOfFlushes;

    private double averageFlushDuration;
    
    
    
    /* --------------------------------------------- Fields ------------------------------------------------------ */
    
    
    @GuardedBy("sharedLock")
    private View currentView;

    private Address localAddress;

    /**
     * Group member that requested FLUSH. For view installations flush
     * coordinator is the group coordinator For state transfer flush coordinator
     * is the state requesting member
     */
    @GuardedBy("sharedLock")
    private Address flushCoordinator;

    @GuardedBy("sharedLock")
    private final List<Address> flushMembers;
    
    private final AtomicInteger viewCounter = new AtomicInteger(0);  

    @GuardedBy("sharedLock")
    private final Map<Address, Digest> flushCompletedMap;   
   
    @GuardedBy("sharedLock")
    private final List<Address> flushNotCompletedMap;   
     

    @GuardedBy("sharedLock")
    private final Set<Address> suspected;
    
    @GuardedBy("sharedLock")
    private final List<Address> reconcileOks;

    private final Object sharedLock = new Object();

    private final Object blockMutex = new Object();

    /**
     * Indicates if FLUSH.down() is currently blocking threads Condition
     * predicate associated with blockMutex
     */
    @GuardedBy("blockMutex")
    private volatile boolean isBlockingFlushDown = true;
    
    @GuardedBy("sharedLock")
    private boolean flushCompleted = false;
    
    private volatile boolean allowMessagesToPassUp = false;

    private final Promise<Boolean> flush_promise = new Promise<Boolean>();    
    
    private final AtomicBoolean flushInProgress = new AtomicBoolean(false);
    
    private final AtomicBoolean sentBlock = new AtomicBoolean(false);
    
    private final AtomicBoolean sentUnblock = new AtomicBoolean(false);



    public FLUSH(){
        super();
        currentView = new View(new ViewId(), new Vector<Address>());      
        flushCompletedMap = new HashMap<Address, Digest>();        
        flushNotCompletedMap = new ArrayList<Address>();
        reconcileOks = new ArrayList<Address>();
        flushMembers = new ArrayList<Address>();
        suspected = new TreeSet<Address>();
    }

    public String getName() {
        return NAME;
    }

    public long getStartFlushTimeout() {
        return start_flush_timeout;
    }

    public void setStartFlushTimeout(long start_flush_timeout) {
        this.start_flush_timeout=start_flush_timeout;
    }

    public boolean setProperties(Properties props) {
        super.setProperties(props);

        timeout = Util.parseLong(props, "timeout", timeout);
        start_flush_timeout = Util.parseLong(props, "start_flush_timeout", start_flush_timeout);        
        enable_reconciliation = Util.parseBoolean(props,
                                                  "enable_reconciliation",
                                                  enable_reconciliation);
        
        
        String str = props.getProperty("retry_timeout");
        if(str != null){
            log.warn("retry_timeout has been deprecated and its value will be ignored");
            props.remove("retry_timeout");
        }
        
        str = props.getProperty("flush_retry_count");
        if(str != null){
            log.warn("flush_retry_count has been deprecated and its value will be ignored");
            props.remove("flush_retry_count");
        }
        
        str = props.getProperty("auto_flush_conf");
        if(str != null){
            log.warn("auto_flush_conf has been deprecated and its value will be ignored");
            props.remove("auto_flush_conf");
        }

        if(!props.isEmpty()){
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void start() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flush_supported", Boolean.TRUE);
        up_prot.up(new Event(Event.CONFIG, map));
        down_prot.down(new Event(Event.CONFIG, map));

        viewCounter.set(0);       
        synchronized(blockMutex){
            isBlockingFlushDown = true;
        }
        allowMessagesToPassUp = false;
    }

    public void stop() {
        synchronized(sharedLock){
            currentView = new View(new ViewId(), new Vector<Address>());
            flushCompletedMap.clear();               
            flushNotCompletedMap.clear();
            flushMembers.clear();
            suspected.clear();
            flushCoordinator = null;
        }
    }

    /* -------------------JMX attributes and operations --------------------- */
    public double getAverageFlushDuration() {
        return averageFlushDuration;
    }

    public long getTotalTimeInFlush() {
        return totalTimeInFlush;
    }

    public int getNumberOfFlushes() {
        return numberOfFlushes;
    }

    public boolean startFlush() {        
        return startFlush(new Event(Event.SUSPEND));
    }
    
    private boolean startFlush(Event evt){
    	if(log.isDebugEnabled())
            log.debug("Received " + evt + " at " + localAddress + ". Running FLUSH...");
    	
    	List<Address> flushParticipants = (List<Address>) evt.getArg();
        return startFlush(flushParticipants);
    }
    
    private boolean startFlush(List<Address> flushParticipants) {
		boolean successfulFlush = false;
		if (!flushInProgress.get()) {
			flush_promise.reset();
			onSuspend(flushParticipants);
			try {
				Boolean r = flush_promise.getResultWithTimeout(start_flush_timeout);
				successfulFlush = r.booleanValue();
			} catch (TimeoutException e) {
				if (log.isDebugEnabled())
					log.debug("At " + localAddress
							+ " timed out waiting for flush responses after "
							+ start_flush_timeout + " msec");
			}
		}
		return successfulFlush;
	}

    public void stopFlush() {
        down(new Event(Event.RESUME));
    }

    /*
     * ------------------- end JMX attributes and operations
     * ---------------------
     */

    public Object down(Event evt) {
        switch(evt.getType()){
        case Event.MSG:
            Message msg = (Message) evt.getArg();
            Address dest = msg.getDest();
            if(dest == null || dest.isMulticastAddress()){
                //mcasts
                FlushHeader fh = (FlushHeader) msg.getHeader(getName());
                if(fh != null && fh.type == FlushHeader.FLUSH_BYPASS){
                    return down_prot.down(evt);
                }
                else{                   
                    blockMessageDuringFlush();  
                }                                     
            }else{   
                //unicasts are irrelevant in virtual synchrony, let them through
                return down_prot.down(evt);
            }
            break;
            
        case Event.CONNECT:
        case Event.CONNECT_WITH_STATE_TRANSFER:   
            if(sentBlock.compareAndSet(false, true)){                
                sendBlockUpToChannel();              
            }

            Object result=down_prot.down(evt);
            if(result instanceof Throwable) {
                sentBlock.set(false); // set the var back to its original state if we cannot connect successfully
            }
            return result;

        case Event.SUSPEND:
            return startFlush(evt);

        case Event.RESUME:
            onResume(evt);
            return null;
        }
        return down_prot.down(evt);
    }

    private void blockMessageDuringFlush() {
        boolean shouldSuspendByItself = false;
        long start = 0, stop = 0;
        synchronized(blockMutex){
            while(isBlockingFlushDown){
                if(log.isDebugEnabled())
                    log.debug("FLUSH block at " + localAddress
                              + " for "
                              + (timeout <= 0 ? "ever" : timeout + "ms"));
                try{
                    start = System.currentTimeMillis();
                    if(timeout <= 0)
                        blockMutex.wait();
                    else
                        blockMutex.wait(timeout);
                    stop = System.currentTimeMillis();
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt(); // set interrupt flag again
                }
                if(isBlockingFlushDown){
                    isBlockingFlushDown = false;
                    shouldSuspendByItself = true;
                    blockMutex.notifyAll();
                }
            }
        }
        if(shouldSuspendByItself){
            log.warn("unblocking FLUSH.down() at " + localAddress
                     + " after timeout of "
                     + (stop - start)
                     + "ms");
            flush_promise.setResult(Boolean.TRUE);
        }
    }

    public Object up(Event evt) {

        switch(evt.getType()){
        case Event.MSG:
            Message msg = (Message) evt.getArg();
            final FlushHeader fh = (FlushHeader) msg.getHeader(getName());
            if(fh != null){
                switch(fh.type){
                    case FlushHeader.FLUSH_BYPASS:
                        return up_prot.up(evt);                     
                    case FlushHeader.START_FLUSH:
                        Collection<Address> fp=fh.flushParticipants;
                        boolean amIParticipant = (fp != null && fp.contains(localAddress)) || msg.getSrc().equals(localAddress);
                        if(amIParticipant){
                            handleStartFlush(msg, fh);
                        }         
                        else{ 
                            if (log.isDebugEnabled())                        
                                log.debug("Received START_FLUSH at " + localAddress
                                      + " but I am not flush participant, not responding");                                            
                        }
                        break;
                    case FlushHeader.FLUSH_RECONCILE:
                        handleFlushReconcile(msg, fh);
                        break;
                    case FlushHeader.FLUSH_RECONCILE_OK:
                        onFlushReconcileOK(msg);
                        break;
                    case FlushHeader.STOP_FLUSH:
                        onStopFlush();
                        break;
                    case FlushHeader.ABORT_FLUSH:                     	
                    	Collection<Address> flushParticipants = fh.flushParticipants;
						
                    	if(flushParticipants != null && flushParticipants.contains(localAddress)){
                    		if (log.isDebugEnabled()) {
    							log.debug("At " + localAddress
    									+ " received ABORT_FLUSH from flush coordinator " + msg.getSrc()
    									+ ",  am i flush participant="
    									+ flushParticipants.contains(localAddress));
                    		}
                    		flushInProgress.set(false);	
                    		flushNotCompletedMap.clear();
                        	flushCompletedMap.clear();
                    	}
                        break;                               
                    case FlushHeader.FLUSH_NOT_COMPLETED:
                    	if (log.isDebugEnabled()) {
							log.debug("At " + localAddress
									+ " received FLUSH_NOT_COMPLETED from "
									+ msg.getSrc());
                    	}
                    	boolean flushCollision = false;
                    	synchronized(sharedLock){
                    		flushNotCompletedMap.add(msg.getSrc());
                            flushCollision = !flushCompletedMap.isEmpty();
                            if(flushCollision){
                            	flushNotCompletedMap.clear();
                            	flushCompletedMap.clear();
                            }
                        }     
                    	
                    	if (log.isDebugEnabled()) {
							log.debug("At " + localAddress
									+ " received FLUSH_NOT_COMPLETED from "
									+ msg.getSrc() + " collision=" + flushCollision);
                    	}
                    	
                    	//reject flush if we have at least one OK and at least one FAIL
                    	if(flushCollision){
                    		Runnable r = new Runnable(){
                    			public void run() {
                    				//wait a bit so ABORTs do not get received before other possible FLUSH_COMPLETED
                    				Util.sleep(1000);
                    				rejectFlush(fh.flushParticipants, fh.viewID);
								}
                    		};
                    		new Thread(r).start();                    	
                    	}
                    	//however, flush should fail/retry as soon as one FAIL is received
                    	flush_promise.setResult(Boolean.FALSE);
                    	break;
                    	
                    case FlushHeader.FLUSH_COMPLETED:
                        if(isCurrentFlushMessage(fh))
                            onFlushCompleted(msg.getSrc(), fh);
                        break;
                }
                return null; // do not pass FLUSH msg up
            }else{               
                // http://jira.jboss.com/jira/browse/JGRP-575
                // for processing of application messages after we join, 
                // lets wait for STOP_FLUSH to complete
                // before we start allowing message up.
                Address dest=msg.getDest();
                if(dest != null && !dest.isMulticastAddress()) {
                    return up_prot.up(evt); // allow unicasts to pass, virtual synchrony olny applies to multicasts
                }

                if(!allowMessagesToPassUp)
                    return null;
            }
            break;

        case Event.VIEW_CHANGE:                       
            /*
             * [JGRP-618] - FLUSH coordinator transfer reorders
             * block/unblock/view events in applications (TCP stack only)
             *                          
             */
            up_prot.up(evt);            
            View newView = (View) evt.getArg();
            boolean coordinatorLeft = onViewChange(newView);
            boolean singletonMember = newView.size() == 1 && newView.containsMember(localAddress);
            boolean isThisOurFirstView = viewCounter.addAndGet(1) == 1;
            // if this is channel's first view and its the only member of the group - no flush was run
            // but the channel application should still receive BLOCK,VIEW,UNBLOCK 
            
            //also if coordinator of flush left each member should run stopFlush individually.
            if((isThisOurFirstView && singletonMember) || coordinatorLeft){                
                onStopFlush();              
            }
            return null;            

        case Event.TMP_VIEW:
            /*
             * April 25, 2007
             * 
             * Accommodating current NAKACK (1.127)
             * 
             * Updates field currentView of a leaving coordinator. Leaving
             * coordinator, after it sends out the view, does not need to
             * participate in second flush phase.
             * 
             * see onStopFlush();
             * 
             * TODO: revisit if still needed post NAKACK 1.127
             * 
             */
            View tmpView = (View) evt.getArg();
            if(!tmpView.containsMember(localAddress)){
                onViewChange(tmpView);
            }
            break;

        case Event.SET_LOCAL_ADDRESS:
            localAddress = (Address) evt.getArg();
            break;

        case Event.SUSPECT:
            onSuspect((Address) evt.getArg());
            break;

        case Event.SUSPEND:
            return startFlush(evt);

        case Event.RESUME:
            onResume(evt);
            return null;

        }

        return up_prot.up(evt);
    }

    private void onFlushReconcileOK(Message msg) {
        if(log.isDebugEnabled())
            log.debug(localAddress + " received reconcile ok from " + msg.getSrc());

        synchronized(sharedLock){
            reconcileOks.add(msg.getSrc());
            if(reconcileOks.size() >= flushMembers.size()){
                flush_promise.setResult(Boolean.TRUE);
                if(log.isDebugEnabled())
                    log.debug("All FLUSH_RECONCILE_OK received at " + localAddress);
            }
        }
    }

    private void handleFlushReconcile(Message msg, FlushHeader fh) {
        Address requester = msg.getSrc();
        Digest reconcileDigest = fh.digest;

        if(log.isDebugEnabled())
            log.debug("Received FLUSH_RECONCILE at " + localAddress
                      + " passing digest to NAKACK "
                      + reconcileDigest);

        // Let NAKACK reconcile missing messages
        down_prot.down(new Event(Event.REBROADCAST, reconcileDigest));

        if(log.isDebugEnabled())
            log.debug("Returned from FLUSH_RECONCILE at " + localAddress
                      + " Sending RECONCILE_OK to "
                      + requester
                      + ", thread "
                      + Thread.currentThread());

        Message reconcileOk = new Message(requester);
        reconcileOk.setFlag(Message.OOB);
        reconcileOk.putHeader(getName(), new FlushHeader(FlushHeader.FLUSH_RECONCILE_OK));
        down_prot.down(new Event(Event.MSG, reconcileOk));
    }

    private void handleStartFlush(Message msg, FlushHeader fh) {                
        Address flushRequester = msg.getSrc();
        
        boolean proceed = flushInProgress.compareAndSet(false, true);  
        if (proceed) {
			synchronized (sharedLock) {
				flushCoordinator = flushRequester;
			}
			onStartFlush(flushRequester, fh);
		}
        else{
        	 FlushHeader fhr=new FlushHeader(FlushHeader.FLUSH_NOT_COMPLETED, fh.viewID,fh.flushParticipants);
             Message response=new Message(flushRequester);
             response.putHeader(getName(), fhr);
             down_prot.down(new Event(Event.MSG, response));
             if(log.isDebugEnabled())
                 log.debug("Received START_FLUSH at " + localAddress
                           + " responded with FLUSH_NOT_COMPLETED to "
                           + flushRequester);
		}
    }

    private void rejectFlush(Collection<Address> participants,long viewId) {
    	for(Address flushMember:participants){
    		Message reject = new Message(flushMember, localAddress, null);
            reject.putHeader(getName(), new FlushHeader(FlushHeader.ABORT_FLUSH,viewId,participants));
            down_prot.down(new Event(Event.MSG, reject));
    	}
	}

	public Vector<Integer> providedDownServices() {
        Vector<Integer> retval = new Vector<Integer>(2);
        retval.addElement(new Integer(Event.SUSPEND));
        retval.addElement(new Integer(Event.RESUME));
        return retval;
    }

    private void sendBlockUpToChannel() {
        up_prot.up(new Event(Event.BLOCK));
        sentUnblock.set(false);
    }
    
    private void sendUnBlockUpToChannel() {
        sentBlock.set(false);
        up_prot.up(new Event(Event.UNBLOCK));       
    }

    private boolean isCurrentFlushMessage(FlushHeader fh) {
        return fh.viewID == currentViewId();
    }

    private long currentViewId() {
        long viewId = -1;
        synchronized(sharedLock){
            ViewId view = currentView.getVid();
            if(view != null){
                viewId = view.getId();
            }
        }
        return viewId;
    }

    private boolean onViewChange(View view) {        
        boolean coordinatorLeft = false;
        synchronized(sharedLock){            
            suspected.retainAll(view.getMembers());
            currentView = view;
            coordinatorLeft =!view.getMembers().isEmpty() && !view.containsMember(view.getCreator());            
        }      
        if(log.isDebugEnabled())
            log.debug("Installing view at  " + localAddress + " view is " + view);

        return coordinatorLeft;
    }

    private void onStopFlush() {        
        if(stats){
            long stopFlushTime = System.currentTimeMillis();
            totalTimeInFlush += (stopFlushTime - startFlushTime);
            if(numberOfFlushes > 0){
                averageFlushDuration = totalTimeInFlush / (double) numberOfFlushes;
            }
        }
                            
        synchronized(sharedLock){                     
            flushCompletedMap.clear();
            flushNotCompletedMap.clear();
            flushMembers.clear();
            suspected.clear();
            flushCoordinator = null;
            allowMessagesToPassUp = true;
            flushCompleted = false;
        }

        if(log.isDebugEnabled())
            log.debug("At " + localAddress
                      + " received STOP_FLUSH, unblocking FLUSH.down() and sending UNBLOCK up");

        synchronized(blockMutex){
            isBlockingFlushDown = false;
            blockMutex.notifyAll();
        }
                
        if(sentUnblock.compareAndSet(false,true)){
            //ensures that we do not repeat unblock event            
            sendUnBlockUpToChannel();                  
        }                
        flushInProgress.set(false);
    }

    private void onSuspend(List<Address> members) {
        Message msg = null;
        Collection<Address> participantsInFlush = null;
        synchronized(sharedLock){
            // start FLUSH only on group members that we need to flush
            if(members != null){
                participantsInFlush = members;
                participantsInFlush.retainAll(currentView.getMembers());
            }else{
                participantsInFlush = new ArrayList<Address>(currentView.getMembers());
            }
            msg = new Message(null, localAddress, null);
            msg.putHeader(getName(), new FlushHeader(FlushHeader.START_FLUSH,
                                                     currentViewId(),
                                                     participantsInFlush));
        }
        if(participantsInFlush.isEmpty()){
            flush_promise.setResult(Boolean.TRUE);
        }else{
            down_prot.down(new Event(Event.MSG, msg));
            if(log.isDebugEnabled())
                log.debug("Flush coordinator " + localAddress
                          + " is starting FLUSH with participants "
                          + participantsInFlush);
        }
    }

    private void onResume(Event evt) {
        List<Address> members = (List<Address>) evt.getArg();
        long viewID = currentViewId();
        if(members == null || members.isEmpty()){
            Message msg = new Message(null, localAddress, null);    
            //Cannot be OOB since START_FLUSH is not OOB
            //we have to FIFO order two subsequent flushes       
            msg.putHeader(getName(), new FlushHeader(FlushHeader.STOP_FLUSH, viewID));
            down_prot.down(new Event(Event.MSG, msg));
            if(log.isDebugEnabled())
                log.debug("Received RESUME at " + localAddress + ", sent STOP_FLUSH to all");
        }else{
            for (Address address : members) {
                Message msg = new Message(address, localAddress, null);    
                //Cannot be OOB since START_FLUSH is not OOB
                //we have to FIFO order two subsequent flushes       
                msg.putHeader(getName(), new FlushHeader(FlushHeader.STOP_FLUSH, viewID));
                down_prot.down(new Event(Event.MSG, msg));
                if(log.isDebugEnabled())
                    log.debug("Received RESUME at " + localAddress + ", sent STOP_FLUSH to " + address);
            }
        }
    }

    private void onStartFlush(Address flushStarter, FlushHeader fh) {                           
        if(stats){
            startFlushTime = System.currentTimeMillis();
            numberOfFlushes += 1;
        }
        boolean proceed = false;
        synchronized(sharedLock){
            flushCoordinator = flushStarter;
            flushMembers.clear();
            if(fh.flushParticipants != null){
                flushMembers.addAll(fh.flushParticipants);
            }
            proceed = flushMembers.contains(localAddress);
            flushMembers.removeAll(suspected);           
        }
        
        if(proceed) {
            if(sentBlock.compareAndSet(false, true)) {
                //ensures that we do not repeat block event
                //and that we do not send block event to non participants            
                sendBlockUpToChannel();
                synchronized(blockMutex) {
                    isBlockingFlushDown=true;
                }
            }
            else {
                if(log.isDebugEnabled())
                    log.debug("Received START_FLUSH at " + localAddress
                              + " but not sending BLOCK up");
            }

            Digest digest=(Digest)down_prot.down(new Event(Event.GET_DIGEST));
            FlushHeader fhr=new FlushHeader(FlushHeader.FLUSH_COMPLETED, fh.viewID,fh.flushParticipants);
            fhr.addDigest(digest);

            Message msg=new Message(flushStarter);
            msg.putHeader(getName(), fhr);
            down_prot.down(new Event(Event.MSG, msg));
            if(log.isDebugEnabled())
                log.debug("Received START_FLUSH at " + localAddress
                          + " responded with FLUSH_COMPLETED to "
                          + flushStarter);
        }
              
    }
    
    private void onFlushCompleted(Address address, final FlushHeader header) {        
        Message msg = null;
        boolean needsReconciliationPhase = false;
        boolean collision = false;
        Digest digest = header.digest;
        synchronized(sharedLock){
            flushCompletedMap.put(address, digest);
            flushCompleted = flushCompletedMap.size() >= flushMembers.size()
					&& !flushMembers.isEmpty()
					&& flushCompletedMap.keySet().containsAll(flushMembers);           

            collision = !flushNotCompletedMap.isEmpty();
            if(log.isDebugEnabled())
                log.debug("At " + localAddress
                          + " FLUSH_COMPLETED from "
                          + address
                          + ",completed "
                          + flushCompleted
                          + ",flushMembers "
                          + flushMembers
                          + ",flushCompleted "
                          + flushCompletedMap.keySet());

            needsReconciliationPhase = enable_reconciliation && flushCompleted
                                       && hasVirtualSynchronyGaps();
            if(needsReconciliationPhase){

                Digest d = findHighestSequences();
                msg = new Message();
                msg.setFlag(Message.OOB);
                FlushHeader fh = new FlushHeader(FlushHeader.FLUSH_RECONCILE,
                                                 currentViewId(),
                                                 flushMembers);
                reconcileOks.clear();
                fh.addDigest(d);
                msg.putHeader(getName(), fh);

                if(log.isDebugEnabled())
                    log.debug("At "+ localAddress + " reconciling flush mebers due to virtual synchrony gap, digest is " + d
                              + " flush members are "
                              + flushMembers);

                flushCompletedMap.clear();
            } else if (flushCompleted){
                flushCompletedMap.clear();
            } else if (collision){
            	flushNotCompletedMap.clear();
            	flushCompletedMap.clear();
            }
        }
        if(needsReconciliationPhase){
            down_prot.down(new Event(Event.MSG, msg));
        }else if(flushCompleted){
            flush_promise.setResult(Boolean.TRUE);
            if(log.isDebugEnabled())
                log.debug("All FLUSH_COMPLETED received at " + localAddress);
        }else if(collision){
        	//reject flush if we have at least one OK and at least one FAIL
        	Runnable r = new Runnable(){
    			public void run() {
    				//wait a bit so ABORTs do not get received before other possible FLUSH_COMPLETED
    				Util.sleep(1000);
    				rejectFlush(header.flushParticipants, header.viewID);
				}
    		};
    		new Thread(r).start();      
        }
    }

    private boolean hasVirtualSynchronyGaps() {
        ArrayList<Digest> digests = new ArrayList<Digest>();
        digests.addAll(flushCompletedMap.values());
        Digest firstDigest = digests.get(0);
        List<Digest> remainingDigests = digests.subList(1, digests.size());
        for(Digest digest:remainingDigests){
            Digest diff = firstDigest.difference(digest);
            if(diff != Digest.EMPTY_DIGEST){
                return true;
            }
        }
        return false;
    }

    private Digest findHighestSequences() {
        Digest result = null;
        List<Digest> digests = new ArrayList<Digest>(flushCompletedMap.values());

        result = digests.get(0);
        List<Digest> remainingDigests = digests.subList(1, digests.size());

        for(Digest digestG:remainingDigests){
            result = result.highestSequence(digestG);
        }
        return result;
    }

    private void onSuspect(Address address) {
    	
    	//handles FlushTest#testFlushWithCrashedFlushCoordinator
        boolean amINeighbourOfCrashedFlushCoordinator = false;
        ArrayList<Address> flushMembersCopy = null;
        synchronized(sharedLock){
        	boolean flushCoordinatorSuspected = address.equals(flushCoordinator);
        	if(flushCoordinatorSuspected && flushMembers != null){
	        	int indexOfCoordinator = flushMembers.indexOf(flushCoordinator);
	        	int myIndex = flushMembers.indexOf(localAddress);
	        	int diff = myIndex - indexOfCoordinator;
	        	amINeighbourOfCrashedFlushCoordinator = (diff == 1 || (myIndex==0 && indexOfCoordinator == flushMembers.size()));
	        	if(amINeighbourOfCrashedFlushCoordinator){
	        		flushMembersCopy = new ArrayList<Address>(flushMembers);
	        	}
        	}
        }
        if(amINeighbourOfCrashedFlushCoordinator){
        	if(log.isDebugEnabled())
        		log.debug("Flush coordinator " + flushCoordinator + " suspected, " + localAddress + " is neighbour, completing flush ");
    		
        	onResume(new Event(Event.RESUME, flushMembersCopy));
    	}
        
        //handles FlushTest#testFlushWithCrashedNonCoordinators
        boolean flushOkCompleted = false;
        Message m = null;
        long viewID = 0;
        synchronized(sharedLock){
            suspected.add(address);
            flushMembers.removeAll(suspected);
            viewID = currentViewId();
            flushOkCompleted = !flushCompletedMap.isEmpty() && flushCompletedMap.keySet().containsAll(flushMembers);
            if(flushOkCompleted){
                m = new Message(flushCoordinator, localAddress, null);
            }
            if(log.isDebugEnabled())
                log.debug("Suspect is " + address
                          + ",completed "
                          + flushOkCompleted
                          + ",  flushOkSet "
                          + flushCompletedMap
                          + " flushMembers "
                          + flushMembers);
        }
        if(flushOkCompleted){
            Digest digest = (Digest) down_prot.down(new Event(Event.GET_DIGEST));
            FlushHeader fh = new FlushHeader(FlushHeader.FLUSH_COMPLETED, viewID);
            fh.addDigest(digest);
            m.putHeader(getName(), fh);
            down_prot.down(new Event(Event.MSG, m));
            if(log.isDebugEnabled())
                log.debug(localAddress + " sent FLUSH_COMPLETED message to " + flushCoordinator);
        }
    }
    
    public static class FlushHeader extends Header implements Streamable {
        public static final byte START_FLUSH = 0;

        public static final byte STOP_FLUSH = 2;

        public static final byte FLUSH_COMPLETED = 3;       

        public static final byte ABORT_FLUSH = 5;

        public static final byte FLUSH_BYPASS = 6;

        public static final byte FLUSH_RECONCILE = 7;

        public static final byte FLUSH_RECONCILE_OK = 8;
        
        public static final byte FLUSH_NOT_COMPLETED = 9;

        byte type;

        long viewID;

        Collection<Address> flushParticipants;

        Digest digest = null;
        private static final long serialVersionUID=-6248843990215637687L;

        public FlushHeader(){
            this(START_FLUSH, 0);
        } // used for externalization

        public FlushHeader(byte type){
            this(type, 0);
        }

        public FlushHeader(byte type,long viewID){
            this(type, viewID, null);
        }

        public FlushHeader(byte type,long viewID,Collection<Address> flushView){
            this.type = type;
            this.viewID = viewID;
            if(flushView != null){
                this.flushParticipants = new ArrayList<Address>(flushView);
            }
        }             

        @Override
        public int size() {
            int retval=Global.BYTE_SIZE; // type            
            retval+=Global.LONG_SIZE; // viewID            
            retval+= Util.size(flushParticipants);                      
            retval+=Global.BYTE_SIZE; // presence for digest
            if(digest != null){
                retval += digest.serializedSize();
            }
            return retval;
        }

        public void addDigest(Digest digest) {
            this.digest = digest;
        }

        public String toString() {
            switch(type){
            case START_FLUSH:
                return "FLUSH[type=START_FLUSH,viewId=" + viewID
                       + ",members="
                       + flushParticipants
                       + "]";          
            case STOP_FLUSH:
                return "FLUSH[type=STOP_FLUSH,viewId=" + viewID + "]";          
            case ABORT_FLUSH:
                return "FLUSH[type=ABORT_FLUSH,viewId=" + viewID + "]";
            case FLUSH_COMPLETED:
                return "FLUSH[type=FLUSH_COMPLETED,viewId=" + viewID + "]";
            case FLUSH_BYPASS:
                return "FLUSH[type=FLUSH_BYPASS,viewId=" + viewID + "]";
            case FLUSH_RECONCILE:
                return "FLUSH[type=FLUSH_RECONCILE,viewId=" + viewID + ",digest=" + digest + "]";
            case FLUSH_RECONCILE_OK:
                return "FLUSH[type=FLUSH_RECONCILE_OK,viewId=" + viewID + "]";
            default:
                return "[FLUSH: unknown type (" + type + ")]";
            }
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(type);
            out.writeLong(viewID);
            out.writeObject(flushParticipants);
            out.writeObject(digest);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type = in.readByte();
            viewID = in.readLong();
            flushParticipants = (Collection<Address>) in.readObject();
            digest = (Digest) in.readObject();
        }

        public void writeTo(DataOutputStream out) throws IOException {
            out.writeByte(type);
            out.writeLong(viewID);
            Util.writeAddresses(flushParticipants, out);           
            Util.writeStreamable(digest, out);            
        }

        public void readFrom(DataInputStream in) throws IOException,
                                                IllegalAccessException,
                                                InstantiationException {
            type = in.readByte();
            viewID = in.readLong();
            flushParticipants = Util.readAddresses(in, ArrayList.class);
            digest = (Digest) Util.readStreamable(Digest.class, in);            
        }
    }
}
