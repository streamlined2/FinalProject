package edu.practice.finalproject.controller;

/**
 * Container of various named constants for attributes, request parameters, etc 
 * @author Serhii Pylypenko
 *
 */
public final class Names {
	
	private Names() {}

	public static final String USER_ATTRIBUTE = "user";
	public static final String FORM_ATTRIBUTE = "form";
	public static final String ERROR_ATTRIBUTE = "error";
	public static final String MESSAGE_ATTRIBUTE = "message";
	public static final String LOCALE_ATTRIBUTE = "locale";
	public static final String ENTITY_MANAGER_ATTRIBUTE = "entityManager";
	public static final String PRIMARY_ADMIN_ATTRIBUTE = "primaryAdmin";
	public static final String LOGIN_PATTERN_ATTRIBUTE = "loginPattern"; 
	public static final String PASSWORD_PATTERN_ATTRIBUTE = "passwordPattern"; 
	public static final String NAME_PATTERN_ATTRIBUTE = "namePattern"; 
	public static final String PASSPORT_PATTERN_ATTRIBUTE = "passportPattern";
	public static final String CAR_MODEL_PATTERN_ATTRIBUTE = "carModelPattern";
	public static final String AVAILABLE_LOCALES_ATTRIBUTE = "availableLocales";
	public static final String MANUFACTURER_VALUES_ATTRIBUTE = "manufacturerValues";
	public static final String QUALITY_GRADE_VALUES_ATTRIBUTE = "qualityGradeValues";
	public static final String COLOR_VALUES_ATTRIBUTE = "colorValues";
	public static final String STYLE_VALUES_ATTRIBUTE = "styleValues";
	public static final String FILTER_KEY_PAIRS_ATTRIBUTE = "filterKeyPairs";
	public static final String ORDER_KEYS_ATTRIBUTE = "orderKeys";
	public static final String QUERY_DATA_ATTRIBUTE = "queryData";
	public static final String QUERY_HEADER_ATTRIBUTE = "queryHeader";
	public static final String QUERY_BUTTONS_MAP_ATTRIBUTE = "buttonsMap";
	public static final String FIRST_PAGE_ELEMENT_ATTRIBUTE = "firstPageElement";
	public static final String LAST_PAGE_ELEMENT_ATTRIBUTE = "lastPageElement";
	public static final String PAGE_ELEMENTS_NUMBER_ATTRIBUTE = "pageElementsNumber";
	public static final String QUERY_ELEMENTS_NUMBER_ATTRIBUTE = "queryElementsNumber";
	public static final String PAGE_ITEMS_ATTRIBUTE = "pageItems"; 
	public static final String SELECTED_CAR_ATTRIBUTE = "selectedCar";
	public static final String SELECTED_ORDER_ATTRIBUTE = "selectedOrder";
	public static final String ORDER_REVIEW_ATTRIBUTE = "orderReview";
	public static final String ACCOUNT_PATTERN_ATTRIBUTE = "accountPattern";
	public static final String INVOICE_SUM_ATTRIBUTE = "initialInvoiceSum";
	public static final String LEASE_INVOICE_ATTRIBUTE = "leaseInvoice";
	public static final String MAINTENANCE_INVOICE_ATTRIBUTE = "maintenanceInvoice";
	public static final String NEW_CAR_ATTRIBUTE = "createCar";
	public static final String SELECTED_USER_ATTRIBUTE = "selectedUser";
	public static final String SELECTED_ROLE_ATTRIBUTE = "selectedRole";
	public static final String ADMINISTRATIVE_TASKS_ATTRIBUTE = "administrativeTasks";
	public static final String CAR_REVIEW_ATTRIBUTE = "carReview";
	public static final String ROLE_VALUES_ATTRIBUTE = "roleValues";

	public static final String BACK_PARAMETER = "back";
	public static final String ACTION_PARAMETER = "action";
	public static final String USER_PARAMETER = "user";
	public static final String ROLE_PARAMETER = "role";
	public static final String CLIENT_ROLE_PARAMETER = "client";
	public static final String MANAGER_ROLE_PARAMETER = "manager";
	public static final String ADMIN_ROLE_PARAMETER = "admin";
	public static final String LOCALE_PARAMETER = "locale";
	public static final String LOGOUT_PARAMETER = "logout";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String PASSWORD2_PARAMETER = "password2";
	public static final String PASSPORT_PARAMETER = "passport";
	public static final String REGISTER_PARAMETER = "register";
	public static final String REGISTER_NEW_PARAMETER = "register_new";
	public static final String FIRSTNAME_PARAMETER = "firstname";
	public static final String LASTNAME_PARAMETER = "lastname";
	public static final String CAR_CRITERIA_SELECTION_PARAMETER = "select_car_criteria";
	public static final String CONFIRM_CAR_CRITERIA_PARAMETER = "confirm_car_criteria";
	public static final String SELECT_CAR_PARAMETER = "select_car";
	public static final String SELECT_BY_MANUFACTURER_PARAMETER = "selectByManufacturer";
	public static final String MANUFACTURER_PARAMETER = "manufacturer";
	public static final String SELECT_BY_QUALITY_GRADE_PARAMETER = "selectByQualityGrade";
	public static final String QUALITY_GRADE_PARAMETER = "qualityGrade";
	public static final String SELECT_BY_COLOR_PARAMETER = "selectByColor";
	public static final String COLOR_PARAMETER = "color";
	public static final String SELECT_BY_STYLE_PARAMETER = "selectByStyle";
	public static final String STYLE_PARAMETER = "style";
	public static final String ASCENDING_ORDER = "asc";
	public static final String DESCENDING_ORDER = "desc";
	public static final String ORDER_BY_RENT_COST_PARAMETER = "orderByRentCost";
	public static final String RENT_COST_ORDER_PARAMETER = "costSort";
	public static final String ORDER_BY_MODEL_PARAMETER = "orderByModel";
	public static final String MODEL_PARAMETER = "modelSort";
	public static final String ORDER_BY_PRODUCTION_DATE = "orderByProductionDate";
	public static final String PRODUCTION_DATE_PARAMETER = "productionDateSort";
	public static final String NEXT_PAGE_PARAMETER = "nextPage"; 
	public static final String PREVIOUS_PAGE_PARAMETER = "previousPage"; 
	public static final String FIRST_PAGE_PARAMETER = "firstPage"; 
	public static final String LAST_PAGE_PARAMETER = "lastPage";
	public static final String ORDER_CAR_PARAMETER = "orderCar";
	public static final String DRIVER_OPTION_PARAMETER = "driver";
	public static final String LEASE_DUE_TIME_PARAMETER = "returnTime";
	public static final String LEASE_START_TIME_PARAMETER = "startTime";
	public static final String ORDER_APPROVEMENT_PARAMETER = "orderApprovement";
	public static final String CAR_RECEPTION_PARAMETER = "carReception";
	public static final String MANAGER_TASK_PARAMETER = "managerTask";
	public static final String REVIEW_ORDER_PARAMETER = "reviewOrder";
	public static final String ACCEPT_ORDER_PARAMETER = "acceptOrder";
	public static final String REJECT_ORDER_PARAMETER = "rejectOrder";
	public static final String ORDER_NUMBER_PARAMETER = "orderNumber";
	public static final String REJECTION_REASON_PARAMETER = "rejectionReason";
	public static final String SEND_LEASE_INVOICE_PARAMETER = "sendLeaseInvoice";
	public static final String ACCOUNT_PARAMETER = "account";
	public static final String INVOICE_SUM_PARAMETER = "invoiceSum";
	public static final String CAR_INSPECTION_RESULT_PARAMETER = "carEvaluationResult";
	public static final String CAR_IN_PERFECT_CONDITION_PARAMETER = "perfectCondition";
	public static final String CAR_NEEDS_MAINTENANCE_PARAMETER = "needsMaintenance";
	public static final String SEND_MAINTENANCE_INVOICE_PARAMETER = "sendMaintenanceInvoice";
	public static final String REPAIRS_PARAMETER = "repairs";
	public static final String ADMIN_TASK_PARAMETER = "adminTask";
	public static final String CAR_MANAGEMENT_PARAMETER = "carManagement";
	public static final String USER_BLOCKING_PARAMETER = "userBlocking";
	public static final String MANAGER_REGISTRATION_PARAMETER = "managerRegistration";
	public static final String MODIFY_CAR_PARAMETER = "modifyCar";
	public static final String DELETE_CAR_PARAMETER = "deleteCar";
	public static final String NEW_CAR_PARAMETER = "newCar";
	public static final String CAR_NUMBER_PARAMETER = "carNumber";
	public static final String CREATE_MODIFY_CAR_PARAMETER = "createModifyCar";
	public static final String CAR_MODEL_PARAMETER = "model";
	public static final String CAR_RENT_COST_PARAMETER = "cost";
	public static final String CAR_PRODUCTION_DATE_PARAMETER = "productionDate";
	public static final String BLOCK_USER_PARAMETER = "blockUser";
	public static final String USER_NUMBER_PARAMETER = "userNumber";
	public static final String SELECT_USER_PARAMETER = "selectUser";
	public static final String SELECTED_USER_PARAMETER = "selectedUser";
	
	public static final String LOGIN_PATTERN = "[0-9A-Za-z?-??-?????????]+";
	public static final String PASSWORD_PATTERN = "[0-9A-Za-z?-??-?????????]+";
	public static final String NAME_PATTERN = "[0-9A-Za-z?-??-?????????]+";
	public static final String PASSPORT_PATTERN = "[ 0-9A-Za-z?-??-?????????]+";
	public static final String CAR_MODEL_PATTERN = "[0-9A-Za-z?-??-?????????]+";
	public static final String ACCOUNT_PATTERN = "[A-Z]{2}[0-9]{2}[0-9]{1,30}";

}
