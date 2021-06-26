# FinalProject

<h2>Application architecture.</h2>

<h3>Controller.</h3>
The application uses single servlet FCServlet as front controller to handle each user request. Controller delegates user form navigation to FormDispatcher which determines next form by checking transition rule map TransitionRuleMap. Authenticated user, current form and taken action objects are used as map key to search for next user form. Concrete descendants of Form class (part of Controller component) fetch and initialize data that are to be rendered by Java server pages, map user actions to Action objects that change data and set session/request attributes that contain intermediate results.
<p>Auxiliary servlet filter AuthFilter helps to prevent mistakes should JSP page contain link to some external resource and doesn't take control back to controller servlet.
<p>Action class is common ancestor for ClientAction, ManagerAction, AdminAction which correspond to acceptable user roles represented by Client, Manager, Admin classes. Every action is checked against user permissions before its execution (User.checkPermission(action)).
<p>User session attributes are cleared by CleanSessionListener every time the session expires for security reasons.

<h3>View.</h3>
The application avoids using scriptlets and builds view layer as set of JSP pages with standard JSTL tags and custom tags for table view (TableViewTag), select/option element (OptionSelectorTag) and tag files for small interface elements (such as logout button, order option, locale selection, etc). Every page usually contains of one or more HTML forms that are to be submitted when user presses button. Submitted parameters then would be analyzed by assigned Form subclass (which is part of Controller component) and converted into appropriate Action objects and data to operate on.

<h3>Model.</h3>
Model component consists of abstract class Entity (root of entity class hierarchy), composite natural key class NaturalKeyEntity, domain entity class Car, set of document classes LeaseOrder, OrderReview, CarReview, Invoice (LeaseInvoice, MaintenanceInvoice), that represent internal state of enterprise business process, and set of user role classes User (Client, Manager, Admin). Entities are operated upon by generic DAO EntityManager with aid of Inspector class which employes Java Reflection API. StatementBuilder helps to build SQL statements for entity fetching, creation, modification and deletion.

<h3>Employed design patterns:</h3>
Model-View-Controller, Front controller, Action/Command, Observer, Singleton, Generic DAO.
<p>
<i>
<h5>P.S. It helps to recall principles of final state machine paradigm to understand and explain user interface form dispatching. Forms are states of such FSM automaton and actions help to make transitions between forms.</h5>
</i>
