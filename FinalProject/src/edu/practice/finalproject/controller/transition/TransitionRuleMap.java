package edu.practice.finalproject.controller.transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import static edu.practice.finalproject.controller.transition.FormDispatcher.*;

import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.Client;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.utilities.Utils;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;

/**
 * Map that holds transition rules for user interface forms
 * @author Serhii Pylypenko
 *
 */
public final class TransitionRuleMap {
	
	public static TransitionRuleMap getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final TransitionRuleMap INSTANCE = new TransitionRuleMap();
	}
	
	/**
	 * Fills default rules for singleton instance
	 */
	private TransitionRuleMap() {
		//setup transition rule map
		//stray user should be registered or logged in first
		addRule(null, LOGIN_FORM, REGISTER_ACTION, REGISTER_FORM);
		addRule(null, REGISTER_FORM, BACK_ACTION, LOGIN_FORM);

		//rules for client role
		addRule(Client.class, CAR_SELECTION_CRITERIA_FORM, CONFIRM_CAR_CRITERIA_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, BACK_ACTION, CAR_SELECTION_CRITERIA_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, NEXT_PAGE_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, FIRST_PAGE_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, PREVIOUS_PAGE_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, LAST_PAGE_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_BROWSING_FORM, SELECT_CAR_ACTION, ORDER_FORM);
		addRule(Client.class, ORDER_FORM, ORDER_CAR_ACTION, CAR_ORDER_STATUS_FORM);
		addRule(Client.class, ORDER_FORM, BACK_ACTION, CAR_BROWSING_FORM);
		addRule(Client.class, CAR_ORDER_STATUS_FORM, BACK_ACTION, CAR_SELECTION_CRITERIA_FORM);
		addRule(Client.class, null, null, CAR_SELECTION_CRITERIA_FORM);//should always be last rule for Client role

		//rules for manager role
		addRule(Manager.class, MANAGER_TASK_SELECTION_FORM, REVIEW_ORDER_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, BACK_ACTION, MANAGER_TASK_SELECTION_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, NEXT_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, FIRST_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, PREVIOUS_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, LAST_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, BROWSE_ORDER_LIST_FORM, CHECK_ORDER_ACTION, REVIEW_ORDER_FORM);
		addRule(Manager.class, REVIEW_ORDER_FORM, ACCEPT_ORDER_ACTION, NEW_LEASE_INVOICE_FORM);
		addRule(Manager.class, NEW_LEASE_INVOICE_FORM, LEASE_INVOICE_SUBMISSION_ACTION, LEASE_INVOICE_DEMO_FORM);
		addRule(Manager.class, LEASE_INVOICE_DEMO_FORM, BACK_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, REVIEW_ORDER_FORM, REJECT_ORDER_ACTION, REJECTION_NOTIFICATION_FORM);
		addRule(Manager.class, REVIEW_ORDER_FORM, BACK_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, REJECTION_NOTIFICATION_FORM, BACK_ACTION, BROWSE_ORDER_LIST_FORM);
		addRule(Manager.class, MANAGER_TASK_SELECTION_FORM, RECEIVE_CAR_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, SELECT_LEASE_ORDER_ACTION, CAR_INSPECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, BACK_ACTION, MANAGER_TASK_SELECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, NEXT_PAGE_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, FIRST_PAGE_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, PREVIOUS_PAGE_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, LEASE_ORDER_SELECTION_FORM, LAST_PAGE_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, CAR_INSPECTION_FORM, BACK_ACTION, LEASE_ORDER_SELECTION_FORM);
		addRule(Manager.class, CAR_INSPECTION_FORM, CAR_IN_PERFECT_CONDITION_ACTION, MANAGER_TASK_SELECTION_FORM);
		addRule(Manager.class, CAR_INSPECTION_FORM, CAR_NEEDS_MAINTENANCE_ACTION, MAINTENANCE_CHARGE_FORM);
		addRule(Manager.class, MAINTENANCE_CHARGE_FORM, MAINTENANCE_INVOICE_SUBMIT_ACTION, MAINTENANCE_INVOICE_DEMO_FORM);
		addRule(Manager.class, MAINTENANCE_INVOICE_DEMO_FORM, BACK_ACTION, MANAGER_TASK_SELECTION_FORM);
		addRule(Manager.class, null, null, MANAGER_TASK_SELECTION_FORM);//should always be last rule for Manager role

		//rules for admin role
		addRule(Admin.class, ADMIN_TASK_SELECTION_FORM, CAR_MANAGEMENT_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, ADMIN_TASK_SELECTION_FORM, USER_BLOCKING_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, ADMIN_TASK_SELECTION_FORM, REGISTER_ACTION, REGISTER_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, BACK_ACTION, ADMIN_TASK_SELECTION_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, NEXT_PAGE_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, FIRST_PAGE_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, PREVIOUS_PAGE_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, LAST_PAGE_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, ADD_CAR_ACTION, NEW_EDIT_CAR_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, MODIFY_CAR_ACTION, NEW_EDIT_CAR_FORM);
		addRule(Admin.class, CAR_MANAGEMENT_FORM, DROP_CAR_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, NEW_EDIT_CAR_FORM, BACK_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, NEW_EDIT_CAR_FORM, SAVE_CAR_ACTION, CAR_MANAGEMENT_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, BACK_ACTION, ADMIN_TASK_SELECTION_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, CHANGE_USER_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, BLOCK_USER_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, NEXT_PAGE_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, FIRST_PAGE_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, PREVIOUS_PAGE_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, USER_BLOCKING_FORM, LAST_PAGE_ACTION, USER_BLOCKING_FORM);
		addRule(Admin.class, REGISTER_FORM, BACK_ACTION, ADMIN_TASK_SELECTION_FORM);
		addRule(Admin.class, REGISTER_FORM, REGISTER_NEW_ACTION, ADMIN_TASK_SELECTION_FORM);
		addRule(Admin.class, null, null, ADMIN_TASK_SELECTION_FORM);//should always be last rule for Admin role

	}
	
	/**
	 * Map key class that consists of user role, original interface form and taken action which leads to another form
	 * @author Serhii Pylypenko
	 *
	 */
	private static class TransitionRuleKey {
		private static final String PARAMETER_KEY_SHOULD_NOT_BE_NULL = "transition.rule.map.key-not-null";
		
		private final Class<? extends User> userClass;
		private final Form form;
		private final Action action;
		
		private TransitionRuleKey(final Class<? extends User> userClass,final Form form,final Action action){
			this.userClass=userClass;
			this.form=form;
			this.action=action;
		}
		
		@Override public boolean equals(final Object o) {
			if(o instanceof TransitionRuleKey) {
				final TransitionRuleKey key=(TransitionRuleKey)o;
				return 
						Objects.equals(userClass, key.userClass) &&
						Objects.equals(form, key.form) &&
						Objects.equals(action, key.action);
			}
			return false;
		}
		
		@Override public int hashCode() {
			return Objects.hash(userClass,form,action);
		}

		/**
		 * Tests if {@code this.key} is more general than passed {@code key} parameter
		 * @param key map key to check against 
		 * @return true if {@code this.key} is more general and accepts given parameter {@code key} as sub-rule
		 */
		public boolean encompasses(final TransitionRuleKey key) {
			Objects.requireNonNull(key,Utils.message(PARAMETER_KEY_SHOULD_NOT_BE_NULL));
			boolean suits = true;
			if(userClass!=null)	
				suits = userClass.isAssignableFrom(key.userClass);
			else 
				suits = (key.userClass==null);
			if(form!=null) suits = suits && Objects.equals(form, key.form);
			if(action!=null) suits = suits && Objects.equals(action, key.action); 
			return suits;
		}
	}
	
	private final Map<TransitionRuleKey,Form> ruleMap=new LinkedHashMap<>();
	
	public void addRule(Class<? extends User> userClass,Form sourceForm,Action action,Form targetForm) {
		ruleMap.put(new TransitionRuleKey(userClass,sourceForm,action), targetForm);
	}
	
	/**
	 * Fetches first most agreeable rule and determines interface form to transit after user takes action 
	 * @param user authenticated user object if any, or null if user still hasn't logged in
	 * @param sourceForm current interface form to make transition from
	 * @param action represents action/command user takes to perform task  
	 * @return next interface form enveloped in {@code Optional} or {@code Optional.empty} if not found
	 */
	public Optional<Form> getNextForm(final User user,final Form sourceForm,final Action action) {
		final TransitionRuleKey seekKey=new TransitionRuleKey(user==null?null:user.getClass(), sourceForm, action);
		for(final Entry<TransitionRuleKey,Form> entry:ruleMap.entrySet()) {
			if(entry.getKey().encompasses(seekKey)) return Optional.of(entry.getValue());
		}
		return Optional.empty();
	}
}
