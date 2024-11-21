<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.contract.list.label.code" path="code"  width="15%"/>
	<acme:list-column code="client.contract.list.label.providerName" path="providerName" width="30%" />
	<acme:list-column code="client.contract.list.label.customerName" path="customerName" width="35%" />
	<acme:list-column code="client.contract.list.label.budget" path="budget" width="10%" />
	<acme:list-column code="client.contract.list.label.publicado" path="publicado"/>
	
	<acme:button code="client.contract.create" action="/client/contract/create"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="client.contract.create" action="/client/contract/create"/>
</jstl:if>