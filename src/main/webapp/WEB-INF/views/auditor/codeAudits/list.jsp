<%--
- list.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.codeaudit.list.label.code" path="code" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.executionDate" path="executionDate" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.type" path="type" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.proposedCorrectiveActions" path="proposedCorrectiveActions" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.optionalLink" path="optionalLink" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.mark" path="mark" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.published" path="published" width="5%"/>
	<acme:list-column code="auditor.codeaudit.list.label.project" path="project" width="5%"/>
	
</acme:list>

   <acme:button code="auditor.codeaudit.list.button.create" action="/auditor/code-audit/create"/>