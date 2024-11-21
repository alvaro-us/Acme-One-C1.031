<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.contract.form.label.code" path="code"/>	
	<acme:input-moment code="client.contract.form.label.instationMoment" path="instationMoment" readonly="true"/>	
	<acme:input-textbox code="client.contract.form.label.providerName" path="providerName"/>	
	<acme:input-textbox code="client.contract.form.label.customerName" path="customerName"/>	
	<acme:input-textbox code="client.contract.form.label.goals" path="goals"/>
	<acme:input-money code="client.contract.form.label.budget" path="budget"/>	
	<acme:message code="${budgetBase }" />
	<acme:input-select code="client.contract.form.label.project" path="project" choices="${projects}"/>	
	
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:input-money code="client.contract.form.label.budget" path="budget" readonly="true"/>	
			<acme:button code="client.contract.progressLogs" action="/client/progress-logs/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false }">
			<acme:input-money code="client.contract.form.label.budget" path="budget" readonly="true"/>
			<acme:button code="client.contract.progressLogs" action="/client/progress-logs/list?masterId=${id}"/>
			<acme:submit code="client.contract.form.button.update" action="/client/contract/update"/>
			<acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
			<acme:submit code="client.contract.form.button.publish" action="/client/contract/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.contract.form.button.create" action="/client/contract/create"/>
		</jstl:when>		
	</jstl:choose>
	
</acme:form>