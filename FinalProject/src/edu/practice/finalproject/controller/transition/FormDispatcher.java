package edu.practice.finalproject.controller.transition;

import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ReviewOrderAction;
import edu.practice.finalproject.view.action.BackAction;
import edu.practice.finalproject.view.action.CheckOrderAction;
import edu.practice.finalproject.view.action.ConfirmCarCriteriaAction;
import edu.practice.finalproject.view.action.CreateLeaseInvoiceAction;
import edu.practice.finalproject.view.action.FirstPageAction;
import edu.practice.finalproject.view.action.LastPageAction;
import edu.practice.finalproject.view.action.SwitchLocaleAction;
import edu.practice.finalproject.view.action.LoginAction;
import edu.practice.finalproject.view.action.LogoutAction;
import edu.practice.finalproject.view.action.NextAction;
import edu.practice.finalproject.view.action.NextPageAction;
import edu.practice.finalproject.view.action.OrderCarAction;
import edu.practice.finalproject.view.action.PreviousPageAction;
import edu.practice.finalproject.view.action.ReceiveCarAction;
import edu.practice.finalproject.view.action.RegisterAction;
import edu.practice.finalproject.view.action.RegisterNewAction;
import edu.practice.finalproject.view.action.RejectOrderAction;
import edu.practice.finalproject.view.action.SelectCarAction;
import edu.practice.finalproject.view.form.CarBrowsingForm;
import edu.practice.finalproject.view.form.CarOrderStatusForm;
import edu.practice.finalproject.view.form.CarSelectionCriteriaForm;
import edu.practice.finalproject.view.form.Form;
import edu.practice.finalproject.view.form.LeaseInvoiceDemoForm;
import edu.practice.finalproject.view.form.LoginForm;
import edu.practice.finalproject.view.form.ManagerTaskSelectionForm;
import edu.practice.finalproject.view.form.OrderForm;
import edu.practice.finalproject.view.form.RegisterForm;
import edu.practice.finalproject.view.form.RejectionNotificationForm;
import edu.practice.finalproject.view.form.ReviewOrderForm;
import edu.practice.finalproject.view.form.BrowserOrderListForm;

public class FormDispatcher {

	private final TransitionRuleMap transitions=new TransitionRuleMap();
	{
		//setup transition rule map
		//stray user should be registered or logged in first
		transitions.addRule(null, LOGIN_FORM, REGISTER_ACTION, REGISTER_FORM);
		transitions.addRule(null, REGISTER_FORM, BACK_ACTION, LOGIN_FORM);
		
		//rules for client role
		transitions.addRule(Client.class, CAR_SELECTION_CRITERIA_FORM, CONFIRM_CAR_CRITERIA_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, BACK_ACTION, CAR_SELECTION_CRITERIA_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, NEXT_PAGE_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, FIRST_PAGE_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, PREVIOUS_PAGE_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, LAST_PAGE_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_BROWSING_FORM, SELECT_CAR_ACTION, ORDER_FORM);
		transitions.addRule(Client.class, ORDER_FORM, ORDER_CAR_ACTION, CAR_ORDER_STATUS_FORM);
		transitions.addRule(Client.class, ORDER_FORM, BACK_ACTION, CAR_BROWSING_FORM);
		transitions.addRule(Client.class, CAR_ORDER_STATUS_FORM, BACK_ACTION, CAR_SELECTION_CRITERIA_FORM);
		transitions.addRule(Client.class, null, null, CAR_SELECTION_CRITERIA_FORM);//should always be last rule for Client

		//rules for manager role
		transitions.addRule(Manager.class, MANAGER_TASK_SELECTION_FORM, REVIEW_ORDER_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, BACK_ACTION, MANAGER_TASK_SELECTION_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, NEXT_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, FIRST_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, PREVIOUS_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, LAST_PAGE_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, BROWSE_ORDER_LIST_FORM, CHECK_ORDER_ACTION, REVIEW_ORDER_FORM);
		transitions.addRule(Manager.class, REVIEW_ORDER_FORM, CREATE_LEASE_INVOICE_ACTION, LEASE_INVOICE_DEMO_FORM);
		transitions.addRule(Manager.class, REVIEW_ORDER_FORM, REJECT_ORDER_ACTION, REJECTION_NOTIFICATION_FORM);
		transitions.addRule(Manager.class, REVIEW_ORDER_FORM, BACK_ACTION, BROWSE_ORDER_LIST_FORM);
		transitions.addRule(Manager.class, MANAGER_TASK_SELECTION_FORM, RECEIVE_CAR_ACTION, null);
		transitions.addRule(Manager.class, null, null, MANAGER_TASK_SELECTION_FORM);//should always be last rule for Manager
		//TODO fill up rest of the transition rule map
		//for manager and admin user roles
	}
	
	public static final SwitchLocaleAction SWITCH_LOCALE_ACTION = new SwitchLocaleAction("change_locale");
	public static final LogoutAction LOGOUT_ACTION = new LogoutAction("logout");
	public static final LoginAction LOGIN_ACTION = new LoginAction("login");
	public static final RegisterAction REGISTER_ACTION = new RegisterAction("register");
	public static final RegisterNewAction REGISTER_NEW_ACTION = new RegisterNewAction("register_new");
	public static final BackAction BACK_ACTION = new BackAction("back");
	public static final ConfirmCarCriteriaAction CONFIRM_CAR_CRITERIA_ACTION = new ConfirmCarCriteriaAction("confirm_car_criteria");
	public static final SelectCarAction SELECT_CAR_ACTION = new SelectCarAction("select_car");
	public static final NextPageAction NEXT_PAGE_ACTION = new NextPageAction("next_page");
	public static final FirstPageAction FIRST_PAGE_ACTION = new FirstPageAction("first_page");
	public static final PreviousPageAction PREVIOUS_PAGE_ACTION = new PreviousPageAction("previous_page");
	public static final LastPageAction LAST_PAGE_ACTION = new LastPageAction("last_page");
	public static final OrderCarAction ORDER_CAR_ACTION = new OrderCarAction("order_car");
	public static final NextAction NEXT_ACTION = new NextAction("next");
	public static final ReviewOrderAction REVIEW_ORDER_ACTION = new ReviewOrderAction("review_order");
	public static final ReceiveCarAction RECEIVE_CAR_ACTION = new ReceiveCarAction("receive_car");
	public static final CheckOrderAction CHECK_ORDER_ACTION = new CheckOrderAction("check_order");
	public static final CreateLeaseInvoiceAction CREATE_LEASE_INVOICE_ACTION = new CreateLeaseInvoiceAction("create_lease_invoice");
	public static final RejectOrderAction REJECT_ORDER_ACTION = new RejectOrderAction("reject_order");

	public static final LoginForm LOGIN_FORM = new LoginForm("/login.jsp");
	public static final RegisterForm REGISTER_FORM = new RegisterForm("/register.jsp");
	public static final CarSelectionCriteriaForm CAR_SELECTION_CRITERIA_FORM = new CarSelectionCriteriaForm("/car-selection.jsp");
	public static final CarBrowsingForm CAR_BROWSING_FORM = new CarBrowsingForm("/car-browsing.jsp");
	public static final OrderForm ORDER_FORM = new OrderForm("/order.jsp");
	public static final CarOrderStatusForm CAR_ORDER_STATUS_FORM = new CarOrderStatusForm("/car-order-status.jsp");
	public static final ManagerTaskSelectionForm MANAGER_TASK_SELECTION_FORM = new ManagerTaskSelectionForm("/manager-start-form.jsp");
	public static final BrowserOrderListForm BROWSE_ORDER_LIST_FORM = new BrowserOrderListForm("/browse-order-list.jsp");
	public static final ReviewOrderForm REVIEW_ORDER_FORM = new ReviewOrderForm("/review.jsp");
	public static final LeaseInvoiceDemoForm LEASE_INVOICE_DEMO_FORM = new LeaseInvoiceDemoForm("/lease-invoice-demo.jsp");
	public static final RejectionNotificationForm REJECTION_NOTIFICATION_FORM = new RejectionNotificationForm("/rejection-notification.jsp");
	
	public Form getInitialForm() { return LOGIN_FORM;}

	public Form getNextForm(final User user,final Form form,final Action action,final boolean actionSucceeded) {
		if(action==SWITCH_LOCALE_ACTION) return form;
		if(action==LOGOUT_ACTION) return getInitialForm();
		if(!actionSucceeded) return form;
		return transitions.getNextForm(user, form, action).orElse(getInitialForm());
	}
}
