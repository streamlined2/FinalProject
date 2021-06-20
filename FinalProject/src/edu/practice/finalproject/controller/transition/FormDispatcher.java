package edu.practice.finalproject.controller.transition;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.AddCarAction;
import edu.practice.finalproject.view.action.ReviewOrderAction;
import edu.practice.finalproject.view.action.SaveCarAction;
import edu.practice.finalproject.view.action.BackAction;
import edu.practice.finalproject.view.action.BlockUserAction;
import edu.practice.finalproject.view.action.CarInPerfectConditionAction;
import edu.practice.finalproject.view.action.CarManagementAction;
import edu.practice.finalproject.view.action.CarNeedsMaintenanceAction;
import edu.practice.finalproject.view.action.ChangeUserAction;
import edu.practice.finalproject.view.action.CheckOrderAction;
import edu.practice.finalproject.view.action.ConfirmCarCriteriaAction;
import edu.practice.finalproject.view.action.DropCarAction;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.view.action.AcceptOrderAction;
import edu.practice.finalproject.view.action.FirstPageAction;
import edu.practice.finalproject.view.action.LastPageAction;
import edu.practice.finalproject.view.action.LeaseInvoiceSubmitAction;
import edu.practice.finalproject.view.action.SwitchLocaleAction;
import edu.practice.finalproject.view.action.UserBlockingAction;
import edu.practice.finalproject.view.action.LoginAction;
import edu.practice.finalproject.view.action.LogoutAction;
import edu.practice.finalproject.view.action.MaintenanceInvoiceSubmitAction;
import edu.practice.finalproject.view.action.ModifyCarAction;
import edu.practice.finalproject.view.action.NextAction;
import edu.practice.finalproject.view.action.NextPageAction;
import edu.practice.finalproject.view.action.OrderCarAction;
import edu.practice.finalproject.view.action.PreviousPageAction;
import edu.practice.finalproject.view.action.ReceiveCarAction;
import edu.practice.finalproject.view.action.RegisterAction;
import edu.practice.finalproject.view.action.RegisterNewAction;
import edu.practice.finalproject.view.action.RejectOrderAction;
import edu.practice.finalproject.view.action.SelectCarAction;
import edu.practice.finalproject.view.action.SelectLeaseOrderAction;
import edu.practice.finalproject.view.form.CarBrowsingForm;
import edu.practice.finalproject.view.form.CarInspectionForm;
import edu.practice.finalproject.view.form.CarManagementForm;
import edu.practice.finalproject.view.form.CarOrderStatusForm;
import edu.practice.finalproject.view.form.CarSelectionCriteriaForm;
import edu.practice.finalproject.view.form.MaintenanceChargeForm;
import edu.practice.finalproject.view.form.Form;
import edu.practice.finalproject.view.form.LeaseInvoiceDemoForm;
import edu.practice.finalproject.view.form.LeaseOrderSelectionForm;
import edu.practice.finalproject.view.form.NewLeaseInvoiceForm;
import edu.practice.finalproject.view.form.LoginForm;
import edu.practice.finalproject.view.form.MaintenanceInvoiceDemoForm;
import edu.practice.finalproject.view.form.ManagerTaskSelectionForm;
import edu.practice.finalproject.view.form.NewEditCarForm;
import edu.practice.finalproject.view.form.OrderForm;
import edu.practice.finalproject.view.form.RegisterForm;
import edu.practice.finalproject.view.form.RejectionNotificationForm;
import edu.practice.finalproject.view.form.ReviewOrderForm;
import edu.practice.finalproject.view.form.UserBlockingForm;
import edu.practice.finalproject.view.form.AdminTaskSelectionForm;
import edu.practice.finalproject.view.form.BrowseOrderListForm;

public final class FormDispatcher {
	
	private FormDispatcher() {}
	
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
	public static final AcceptOrderAction ACCEPT_ORDER_ACTION = new AcceptOrderAction("accept_order");
	public static final RejectOrderAction REJECT_ORDER_ACTION = new RejectOrderAction("reject_order");
	public static final LeaseInvoiceSubmitAction LEASE_INVOICE_SUBMISSION_ACTION = new LeaseInvoiceSubmitAction("lease_invoice_submission");
	public static final MaintenanceInvoiceSubmitAction MAINTENANCE_INVOICE_SUBMIT_ACTION = new MaintenanceInvoiceSubmitAction("maintenance_invoice_submission");
	public static final SelectLeaseOrderAction SELECT_LEASE_ORDER_ACTION = new SelectLeaseOrderAction("select_lease_order");
	public static final CarManagementAction CAR_MANAGEMENT_ACTION = new CarManagementAction("car_management");
	public static final UserBlockingAction USER_BLOCKING_ACTION = new UserBlockingAction("user_blocking");
	public static final AddCarAction ADD_CAR_ACTION = new AddCarAction("add_car");
	public static final ModifyCarAction MODIFY_CAR_ACTION = new ModifyCarAction("modify_car");
	public static final DropCarAction DROP_CAR_ACTION = new DropCarAction("drop_car");
	public static final SaveCarAction SAVE_CAR_ACTION = new SaveCarAction("save_car");
	public static final BlockUserAction BLOCK_USER_ACTION = new BlockUserAction("block_user");
	public static final ChangeUserAction CHANGE_USER_ACTION = new ChangeUserAction("change_user");
	public static final CarInPerfectConditionAction CAR_IN_PERFECT_CONDITION_ACTION = new CarInPerfectConditionAction("car_in_perfect_condition");
	public static final CarNeedsMaintenanceAction CAR_NEEDS_MAINTENANCE_ACTION = new CarNeedsMaintenanceAction("car_needs_maintenance");
	
	public static final LoginForm LOGIN_FORM = new LoginForm("/WEB-INF/pages/login.jsp");
	public static final RegisterForm REGISTER_FORM = new RegisterForm("/WEB-INF/pages/register.jsp");
	public static final CarSelectionCriteriaForm CAR_SELECTION_CRITERIA_FORM = new CarSelectionCriteriaForm("/WEB-INF/pages/car-selection.jsp");
	public static final CarBrowsingForm CAR_BROWSING_FORM = new CarBrowsingForm("/WEB-INF/pages/car-browsing.jsp");
	public static final OrderForm ORDER_FORM = new OrderForm("/WEB-INF/pages/order.jsp");
	public static final CarOrderStatusForm CAR_ORDER_STATUS_FORM = new CarOrderStatusForm("/WEB-INF/pages/car-order-status.jsp");
	public static final ManagerTaskSelectionForm MANAGER_TASK_SELECTION_FORM = new ManagerTaskSelectionForm("/WEB-INF/pages/manager-start-form.jsp");
	public static final BrowseOrderListForm BROWSE_ORDER_LIST_FORM = new BrowseOrderListForm("/WEB-INF/pages/browse-order-list.jsp");
	public static final ReviewOrderForm REVIEW_ORDER_FORM = new ReviewOrderForm("/WEB-INF/pages/order-review.jsp");
	public static final NewLeaseInvoiceForm NEW_LEASE_INVOICE_FORM = new NewLeaseInvoiceForm("/WEB-INF/pages/new-lease-invoice.jsp");
	public static final RejectionNotificationForm REJECTION_NOTIFICATION_FORM = new RejectionNotificationForm("/WEB-INF/pages/rejection-notification.jsp");
	public static final LeaseInvoiceDemoForm LEASE_INVOICE_DEMO_FORM = new LeaseInvoiceDemoForm("/WEB-INF/pages/lease-invoice-demo.jsp");
	public static final CarInspectionForm CAR_INSPECTION_FORM = new CarInspectionForm("/WEB-INF/pages/car-inspection.jsp");
	public static final MaintenanceChargeForm MAINTENANCE_CHARGE_FORM = new MaintenanceChargeForm("/WEB-INF/pages/maintenance-charge.jsp");
	public static final MaintenanceInvoiceDemoForm MAINTENANCE_INVOICE_DEMO_FORM = new MaintenanceInvoiceDemoForm("/WEB-INF/pages/maintenance-invoice-demo.jsp");
	public static final LeaseOrderSelectionForm LEASE_ORDER_SELECTION_FORM = new LeaseOrderSelectionForm("/WEB-INF/pages/lease-order-selection.jsp");
	public static final AdminTaskSelectionForm ADMIN_TASK_SELECTION_FORM = new AdminTaskSelectionForm("/WEB-INF/pages/admin-start-form.jsp");
	public static final CarManagementForm CAR_MANAGEMENT_FORM = new CarManagementForm("/WEB-INF/pages/car-management.jsp");
	public static final UserBlockingForm USER_BLOCKING_FORM = new UserBlockingForm("/WEB-INF/pages/user-blocking.jsp");
	public static final NewEditCarForm NEW_EDIT_CAR_FORM = new NewEditCarForm("/WEB-INF/pages/new-edit-car.jsp");
	
	public static Form getInitialForm() { return LOGIN_FORM;}

	public static Form getNextForm(final User user,final Form form,final Action action,final boolean actionSucceeded) {
		if(action==SWITCH_LOCALE_ACTION) return form;
		if(action==LOGOUT_ACTION) return getInitialForm();
		if(!actionSucceeded) return form;
		return TransitionRuleMap.getInstance().getNextForm(user, form, action).orElse(getInitialForm());
	}
}
