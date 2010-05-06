Lazerweb framework description

Action Handlers (com.lazerweb.servlet.actionhandler)

Action handlers ideally perform an independent unit of business logic.  Logging in, for instance, requires communicating with the database to check
a user's credentials, and then loading the user info into memory.  This series of steps is made into an action handler.  Previously, in Lazerweb 2.0,
most of this logic was in JSP pages.  For individual customers, different action handlers may be used, and the same action handlers may be used in
different order, or at different times to customize the business logic for a customer's specific needs.  The action handlers all sit passively in
one place, and are requested by the JSP's.  Thus, most of the customization work is restricted to JSP's.

Action handlers are called by setting a request parameter <%=RequestConstants.REQUEST_ACTION%> to the appropriate value for that action handler.  Each
action handler has an action name (the values of which are available in the ActionConstants class) that is used to call it.  JSP pages can call
as many action handlers as they like.  The order in which action handlers are executed is also controlled by the JSP page, by appending characters
or numbers onto the end of the <%=RequestConstants.REQUEST_ACTION%> parameter name (such as <%=RequestConstants.REQUEST_ACTION%>a, or <%=RequestConstants.REQUEST_ACTION%>1).

All such JSP calls are routed to Action Handlers via the com.lazerweb.servlet.JSPBase class.  It also passes a HandlerData object that encapsulates the 
ServletRequest parameters, the application controller, and access to the bean framework.

Response Beans (com.lazerweb.client.beans)

Response beans are simple data elements that hold the data gathered by the action handlers.  JSP's call action handlers, which put data into response 
beans, which are then available to JSP's to display that data.  Beans are created and retrieved by action handlers via the getBean() call in the
HandlerData class.  To get a bean, the class name of the bean, the bean storage location, and the name of the bean must be given.  The bean storage 
location, for purposes of this application, are either SESSION or REQUEST.  By choosing one or the other, you are choosing whether the bean
is persistent for the user's entire session, or only for the request.

ServiceGateway (com.lazerweb.client)

The service gateway provides access to the backend services of Lazerweb.  Lazerweb 3.0 does little to change these backend services, except to more properly
organize them into packages and well defined interfaces.  Code that used to live in BasicUserModel, BasicSearchModel, etc now lives in 
com.lazerweb.server.*  (ugd, product, commerce).  The ServiceGateway provides convenient and decoupled access to these services (decoupled is a fancy way of 
saying that should a service change dramatically in the future, such as be moved to a different server for load balancing reasons, only ServiceGateway
will be affected instead of dozens of Action Handlers).

Error Handling (com.lazerweb.client.exceptions)

It is not desirable to catch and handle errors deep within the servers or in the action handlers, because it then becomes difficult to let the user 
recover from the error, or even know what went wrong.  Instead, all errors are thrown, leading all errors to be caught in JSPBase, where they are collected
and put into an ErrorsBean object where the JSP's can access them and display them to the user.  More imporant than this, it is often necessary
to redirect users as a result of an error.  When logging in, for instance, if unsuccessful, you'd want the user to be redirected to the login page
again, rather than the page originally intended.  To do this, a mechanism is in place allowing JSP's to define error pages.  There are three ways
they can do this:

1. Specify error and redirect page.  If that error occurs, the user will be redirected to the redirect page.
2. Specify redirect page in event of any error.  If any error occurs, the user will be redirected to the redirect page.
3. Specify error and redirect page in a configuration file.  If the JSP specifies nothing, the user will be redirected to a page indicated in the
configuration file.  

The error handling framework is newer than the rest and probably not quite complete (not fully tested, in other words).  I also haven't decided
whether different errors should be different classes or just different static values.  I'm currently leaning toward static values, but that will
require some changes that haven't yet been made.

Logging (com.lazerweb.util.LoggingManager)

I've imported in an Apache logging mechanism.  Rather than calling Functions.javalog from everywhere, I'm put in place a more configurable logging
system that let's different parts of the application report errors and debugging info at different levels.

The most tedious part of the whole thing is learning the parameters that must be passed from JSP's to action handlers.  Each action handler expects 
certain parameters to be available, and each action handler makes certain data available in the beans.  Ideally, this should be documented for
every action handler.  Also, ideally, this documentation would have a formal structure.  In general, the important points are:

1.  To call an action handler, the request must name the action handler.  ie 
<input type="hidden" name="<%=RequestConstants.REQUEST_ACTION%>" value="<%=ActionConstants.ACTION_LOGIN%>">
or
<input type="hidden" name="request.action" value="action.login">

These are equivalent (I should also convert the "."'s to "_"'s in all these values).

2.  To call two action handlers, specifying an order:
<input type="hidden" name="<%=RequestConstants.REQUEST_ACTION%>a" value="<%=ActionConstants.ACTION_LOGIN%>">
<input type="hidden" name="<%=RequestConstants.REQUEST_ACTION%>b" value="<%=ActionConstants.ACTION_SIMPLE_SEARCH%>">
or
<input type="hidden" name="request.action1" value="action.login">
<input type="hidden" name="request.action2" value="action.simple_search">

This will ensure that the user is first logged in before conducting the simple search.

3.  Specifying an error redirect
<input type="hidden" name="InvalidLoginException" value="login.jsp">

pretty simple.  It can also be done as follows:
<input type="hidden" name="request.error_page" value="error.jsp">

This will send the user to error.jsp if any error occurs.  The first style will override the latter.  Thus, you could put both in the request, and
if an InvalidLoginException occurs, the user will go to login.jsp, and to error.jsp on any other error.

4. Lastly, JSP's can specify parameters and actions that must occur before the page is loaded.  The above specifies actions to be performed if a link
or form is submitted.  These are outgoing requests.  These values can also be specified on incoming requests for the JSP.  In fact, it is likely that
the majority of actions will be best specified this way.  Which way to do it is hard to explain, but it mostly doesn't matter except to make maintenance
easier.  To specify actions on incoming requests:
<%@ include file="/lazerweb/v3_base/include/global.inc" %>
<%	addParams.put(RequestConstants.REQUEST_ACTION+"x",ActionConstants.ACTION_GET_PRODUCT);   %>
<%@ include file="/lazerweb/v3_base/include/handleActions.inc" %>

By calling "addParams", any information can be added to the incoming request parameters.