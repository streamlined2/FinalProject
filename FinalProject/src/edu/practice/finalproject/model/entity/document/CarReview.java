package edu.practice.finalproject.model.entity.document;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.model.entity.userrole.Manager;

public class CarReview extends NaturalKeyEntity {
	public enum CarCondition { PERFECT, NEEDS_MAINTENANCE}

	private LeaseOrder leaseOrder;
	private Manager manager;
	private LocalDateTime reviewTime;
	private CarCondition carCondition;

	public LeaseOrder getLeaseOrder() { return leaseOrder;}
	public void setLeaseOrder(final LeaseOrder leaseOrder) { this.leaseOrder = leaseOrder;}

	public Manager getManager() { return manager;}
	public void setManager(final Manager manager) { this.manager = manager;	}

	public LocalDateTime getReviewTime() { return reviewTime;}
	public void setReviewTime(final LocalDateTime reviewTime) { this.reviewTime = reviewTime;	}

	public CarCondition getCarCondition() { return carCondition;}
	public void setCarCondition(final CarCondition carCondition) { this.carCondition = carCondition;}

	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(
					CarReview.class.getMethod("getLeaseOrder")
			);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}
}
