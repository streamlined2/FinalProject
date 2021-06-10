package edu.practice.finalproject.model.entity.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public abstract class Invoice extends NaturalKeyEntity {

	protected Manager manager;
	protected LocalDateTime signTime;
	protected Account account;
	protected BigDecimal sum;
	
	protected Invoice() {}

	public Manager getManager() { return manager;}
	public void setManager(Manager manager) { this.manager = manager;}

	public LocalDateTime getSignTime() { return signTime;}
	public void setSignTime(LocalDateTime signTime) { this.signTime = signTime;}

	public Account getAccount() { return account;}
	public void setAccount(Account account) {this.account = account;}

	public BigDecimal getSum() { return sum;}
	public void setSum(BigDecimal sum) { this.sum = sum;}
}
