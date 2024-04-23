<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.ProgressLogs.list.label.recordId" path="recordId" width="20%"/>
	<acme:list-column code="client.ProgressLogs.list.label.completeness" path="completeness" width="20%"/>
	<acme:list-column code="client.ProgressLogs.list.label.comment" path="comment" width="20%"/>
	<acme:list-column code="client.ProgressLogs.list.label.registrationMoment" path="registrationMoment" width="20%"/>
	<acme:list-column code="client.ProgressLogs.list.label.responsable" path="responsable" width="20%"/>
</acme:list>

<acme:button test="${showCreate}" code="client.ProgressLogs.list.button.create" action="/client/progress-logs/create?masterId=${masterId}"/>