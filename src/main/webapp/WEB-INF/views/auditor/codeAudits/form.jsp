<%--
- form.jsp
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

<acme:form>
	<acme:input-textbox code="auditor.codeaudit.form.label.code" path="code" placeholder="ABC-000"/>
	<acme:input-moment code="auditor.codeaudit.form.label.executionDate" path="executionDate"/>
	<acme:input-select code="auditor.codeaudit.form.label.type" path="type" choices="${type}" />
	<acme:input-textbox code="auditor.codeaudit.form.label.mark" path="mark" readonly="true"/>
	<acme:input-textbox code="auditor.codeaudit.form.label.proposedCorrectiveActions" path="proposedCorrectiveActions"/>
	<acme:input-url code="auditor.codeaudit.form.label.optionalLink" path="optionalLink"/>
	
<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
		<acme:input-select code="auditor.codeaudit.form.label.project" path="project" choices="${projects}"/>
			<acme:button code="auditor.codeaudit.form.button.audit-records" action="/auditor/audit-record/listForCodeAudits?codeAuditId=${id}"/>
			<acme:submit code="auditor.codeaudit.form.button.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeaudit.form.button.delete" action="/auditor/code-audit/delete"/>
			<acme:submit code="auditor.codeaudit.form.button.publish" action="/auditor/code-audit/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && published == true}">
			<acme:input-select code="auditor.codeaudit.form.label.project" path="project" choices="${projects}"/>
			<acme:button code="auditor.codeaudit.form.button.audit-records" action="/auditor/audit-record/listForCodeAudits?codeAuditId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="auditor.codeaudit.form.label.project" path="project" choices="${projects}"/>
			<acme:submit code="auditor.codeaudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>

