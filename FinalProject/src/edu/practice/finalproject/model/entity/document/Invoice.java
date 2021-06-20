package edu.practice.finalproject.model.entity.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.model.entity.userrole.Manager;

public abstract class Invoice extends NaturalKeyEntity {

	protected Manager manager;
	protected LocalDateTime signTime;
	protected String account;
	protected BigDecimal sum;
	
	protected Invoice() {}

	public Manager getManager() { return manager;}
	public void setManager(Manager manager) { this.manager = manager;}

	public LocalDateTime getSignTime() { return signTime;}
	public void setSignTime(LocalDateTime signTime) { this.signTime = signTime;}

	public String getAccount() { return account;}
	public void setAccount(String account) { this.account = account;}

	public BigDecimal getSum() { return sum;}
	public void setSum(BigDecimal sum) { this.sum = sum;}
}
