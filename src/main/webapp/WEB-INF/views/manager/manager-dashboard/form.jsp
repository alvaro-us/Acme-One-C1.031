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
	<acme:input-integer code="manager.manager-dashboard.totalNumberOfMustUserStory" path="totalNumberOfMustUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.totalNumberOfShouldUserStory" path="totalNumberOfShouldUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.totalNumberOfCouldUserStory" path="totalNumberOfCouldUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.totalNumberOfWontUserStory" path="totalNumberOfWontUserStory"/>
	
	<acme:input-integer code="manager.manager-dashboard.averageEstimatedCostUserStory" path="averageEstimatedCostUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.deviationEstimatedCostUserStory" path="deviationEstimatedCostUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.minEstimatedCostUserStory" path="minEstimatedCostUserStory"/>
	<acme:input-integer code="manager.manager-dashboard.maxEstimatedCostUserStory" path="maxEstimatedCostUserStory"/>
	
	<table class="table table-sm">
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.averageCostProject.EUR"/>
			</th>
			<td>
				<acme:print value="${ averageCostProject.get('EUR')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.averageCostProject.GBP"/>
			</th>
			<td>
				<acme:print value="${ averageCostProject.get('GBP')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.averageCostProject.USD"/>
			</th>
			<td>
				<acme:print value="${ averageCostProject.get('USD')}"/>
			</td>
		</tr>
	</table>
	
	<table class="table table-sm">
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.deviationCostProject.EUR"/>
			</th>
			<td>
				<acme:print value="${ deviationCostProject.get('EUR')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.deviationCostProject.GBP"/>
			</th>
			<td>
				<acme:print value="${ deviationCostProject.get('GBP')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.deviationCostProject.USD"/>
			</th>
			<td>
				<acme:print value="${ deviationCostProject.get('USD')}"/>
			</td>
		</tr>
	</table>
	
	<table class="table table-sm">
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.minCostProject.EUR"/>
			</th>
			<td>
				<acme:print value="${ minCostProject.get('EUR')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.minCostProject.GBP"/>
			</th>
			<td>
				<acme:print value="${ minCostProject.get('GBP')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.minCostProject.USD"/>
			</th>
			<td>
				<acme:print value="${ minCostProject.get('USD')}"/>
			</td>
		</tr>
	</table>
	
	<table class="table table-sm">
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.maxCostProject.EUR"/>
			</th>
			<td>
				<acme:print value="${ maxCostProject.get('EUR')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.maxCostProject.GBP"/>
			</th>
			<td>
				<acme:print value="${ maxCostProject.get('GBP')}"/>
			</td>
		</tr>
		<tr>
			<th scope="row">
				<acme:message code="manager.manager-dashboard.maxCostProject.USD"/>
			</th>
			<td>
				<acme:print value="${ maxCostProject.get('USD')}"/>
			</td>
		</tr>
	</table>

	
</acme:form> 

