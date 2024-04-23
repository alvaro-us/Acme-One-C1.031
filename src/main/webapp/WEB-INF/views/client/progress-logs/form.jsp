<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.ProgressLogs.form.label.recordId" path="recordId"/>
	<acme:input-double code="client.ProgressLogs.form.label.completeness" path="completeness"/>
	<acme:input-textarea code="client.ProgressLogs.form.label.comment" path="comment"/>
	<acme:input-moment code="client.ProgressLogs.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="client.ProgressLogs.form.label.responsable" path="responsable"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && published == false}">
			<acme:submit code="client.ProgressLogs.form.button.update" action="/client/progress-logs/update"/>
			<acme:submit code="client.ProgressLogs.form.button.delete" action="/client/progress-logs/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.ProgressLogs.form.button.create" action="/client/progress-logs/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>