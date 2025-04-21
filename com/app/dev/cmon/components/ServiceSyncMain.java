package com.app.dev.cmon.components;

import java.util.Date;

public class ServiceSyncMain {
    private int syncID;
    private Date syncDate;
    private Date completedDate;
    private boolean isFetching;
    private Date updateStartDate;
    private Date updateCompletedDate;
    private int updateCount;
    private boolean isFetchingUpdate;
    private Integer cycleID; 
    private boolean isCycle;
    
    public ServiceSyncMain() {
        this.isFetching = false;
        this.updateCount = 0;
        this.isFetchingUpdate = false;
        this.isCycle = false;
    }

	public int getSyncID() {
		return syncID;
	}

	public void setSyncID(int syncID) {
		this.syncID = syncID;
	}

	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public boolean isFetching() {
		return isFetching;
	}

	public void setFetching(boolean isFetching) {
		this.isFetching = isFetching;
	}

	public Date getUpdateStartDate() {
		return updateStartDate;
	}

	public void setUpdateStartDate(Date updateStartDate) {
		this.updateStartDate = updateStartDate;
	}

	public Date getUpdateCompletedDate() {
		return updateCompletedDate;
	}

	public void setUpdateCompletedDate(Date updateCompletedDate) {
		this.updateCompletedDate = updateCompletedDate;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public boolean isFetchingUpdate() {
		return isFetchingUpdate;
	}

	public void setFetchingUpdate(boolean isFetchingUpdate) {
		this.isFetchingUpdate = isFetchingUpdate;
	}

	public Integer getCycleID() {
		return cycleID;
	}

	public void setCycleID(Integer cycleID) {
		this.cycleID = cycleID;
	}

	public boolean isCycle() {
		return isCycle;
	}

	public void setCycle(boolean isCycle) {
		this.isCycle = isCycle;
	}
    
}
