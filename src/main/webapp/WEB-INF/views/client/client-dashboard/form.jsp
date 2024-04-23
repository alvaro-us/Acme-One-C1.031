<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="client.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.total-Number-Of-ProgressLogs-Below-25-Percent"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfProgressLogsBelow25Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.total-Number-Of-ProgressLogs-25-To-50-Percent"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfProgressLogs25To50Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.total-Number-Of-ProgressLogs-50-To-75-Percent"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfProgressLogs50To75Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.total-Number-Of-ProgressLogs-Above-75-Percent"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfProgressLogsAbove75Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.average-Budget-Of-Contracts"/>
		</th>
		<td>
			<acme:print value="${averageBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.deviation-Budget-Of-Contracts"/>
		</th>
		<td>
			<acme:print value="${deviationBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.min-Budget-Of-Contracts"/>
		</th>
		<td>
			<acme:print value="${minBudgetOfContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.max-Budget-Of-Contracts"/>
		</th>
		<td>
			<acme:print value="${maxBudgetOfContracts}"/>
		</td>
	</tr>
</table>


<div>
	<canvas id="canvas"></canvas>
</div>

<acme:return/>