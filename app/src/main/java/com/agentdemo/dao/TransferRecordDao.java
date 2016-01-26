package com.agentdemo.dao;

import com.agentdemo.entity.TransferRecord;

public interface TransferRecordDao {

	public abstract void addTransferRecord(TransferRecord transferRecord);

	public abstract void updateCheckout(String transnum, int checkout);

}