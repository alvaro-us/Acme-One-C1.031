<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.progress-logs.list.label.recordId" path="recordId"  width="40%"/>
	<acme:list-column code="client.progress-logs.list.label.completeness" path="completeness" width="40%" />
	<acme:list-column code="client.progress-logs.list.label.responsable" path="responsable" width="40%" />


</acme:list>
<jstl:if test="${_command == 'list'}">

	<acme:button code="client.progress-logs.list.button.create" action="/client/progress-logs/create?masterId=${masterId}"/>
	
</jstl:if>	
