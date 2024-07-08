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
			<acme:message code="client.dashboard.form.label.percentage-Of-Total-Number-Completeness-25"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${percentageOfTotalNumberCompleteness25 != null}">
					<acme:print value="${percentageOfTotalNumberCompleteness25}"/>
				</jstl:when>
				<jstl:when test="${percentageOfTotalNumberCompleteness25 == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.percentage-Of-Total-Number-Completeness-25-At-50"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${percentageOfTotalNumberCompleteness25At50 != null}">
					<acme:print value="${percentageOfTotalNumberCompleteness25At50}"/>
				</jstl:when>
				<jstl:when test="${percentageOfTotalNumberCompleteness25At50 == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.percentage-Of-Total-Number-Completeness-50-at-75"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${percentageOfTotalNumberCompleteness50at75 != null}">
					<acme:print value="${percentageOfTotalNumberCompleteness50at75}"/>
				</jstl:when>
				<jstl:when test="${percentageOfTotalNumberCompleteness50at75 == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.percentage-Of-Total-Number-Completeness-More-75"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${percentageOfTotalNumberCompletenessMore75 != null}">
					<acme:print value="${percentageOfTotalNumberCompletenessMore75}"/>
				</jstl:when>
				<jstl:when test="${percentageOfTotalNumberCompletenessMore75 == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.average-Budget-Of-Contract"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${averageBudgetOfContract != null}">
					<acme:print value="${averageBudgetOfContract}"/>
				</jstl:when>
				<jstl:when test="${averageBudgetOfContract == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.deviation-Budget-Of-Contract"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${deviationBudgetOfContract != null}">
					<acme:print value="${deviationBudgetOfContract}"/>
				</jstl:when>
				<jstl:when test="${deviationBudgetOfContract == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.minimum-Budget-Of-Contract"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${minimumBudgetOfContract != null}">
					<acme:print value="${minimumBudgetOfContract}"/>
				</jstl:when>
				<jstl:when test="${minimumBudgetOfContract == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.maximum-Budget-Of-Contract"/>
		</th>
		<td>
			<jstl:choose>
				<jstl:when test="${maximumBudgetOfContract != null}">
					<acme:print value="${maximumBudgetOfContract}"/>
				</jstl:when>
				<jstl:when test="${maximumBudgetOfContract == null}">
					<acme:print value="N/A"/>
				</jstl:when>
			</jstl:choose>
		</td>
	</tr>
</table>

<jstl:choose>
	<jstl:when test="${percentageOfTotalNumberCompleteness25 == null && percentageOfTotalNumberCompleteness25At50 == null && percentageOfTotalNumberCompleteness50at75 == null && percentageOfTotalNumberCompletenessMore75 == null}">
		<h3><acme:message code="client.dashboard.form.label.progresslog.completeness"/></h3>
		<acme:print value="N/A"/>
	</jstl:when>
	<jstl:when test="${percentageOfTotalNumberCompleteness25 != 0.0 || percentageOfTotalNumberCompleteness25At50 != 0.0 || percentageOfTotalNumberCompleteness50at75 != 0.0 || percentageOfTotalNumberCompletenessMore75 != 0.0}">
		<h3><acme:message code="client.dashboard.form.label.progresslog.completeness"/></h3>
		<div>
			<canvas id="canvas"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
							"less25", "25to50", "50to75", "over75"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${percentageOfTotalNumberCompleteness25}"/>, 
								<jstl:out value="${percentageOfTotalNumberCompleteness25At50}"/>,
								<jstl:out value="${percentageOfTotalNumberCompleteness50at75}"/>,
								<jstl:out value="${percentageOfTotalNumberCompletenessMore75}"/>,
							],
							backgroundColor: [
								'rgb(210, 180, 222)',
						    	'rgb(174, 214, 241)',
						    	'rgb(171, 235, 198)',
						      	'rgb(245, 203, 167)'
						    ]
						}
					]
				};
	
				var canvas, context;
				canvas = document.getElementById("canvas");
				context = canvas.getContext("2d");
				new Chart(context, {
					type : "doughnut",
					data : data,
				});
			});
		</script>
	</jstl:when>
</jstl:choose>


<jstl:choose>
	<jstl:when test="${averageBudgetOfContract != null && deviationBudgetOfContract != null && minimumBudgetOfContract != null && maximumBudgetOfContract != null}">
		<h3><acme:message code="client.dashboard.form.label.contract.budget"/></h3>
		<div>
			<canvas id="canvas0"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
						"AVERAGE", "DEVIATION", "MIN","MAX"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${averageBudgetOfContract}"/>, 
								<jstl:out value="${deviationBudgetOfContract}"/>, 
								<jstl:out value="${minimumBudgetOfContract}"/>,
								<jstl:out value="${maximumBudgetOfContract}"/>
							],
							backgroundColor: [
								'rgb(210, 180, 222)',
						    	'rgb(174, 214, 241)',
						    	'rgb(171, 235, 198)',
						      	'rgb(245, 203, 167)'
						    ]
						}
					]
				};	
				
				var options = {
						scales : {
							yAxes : [
								{
									ticks : {
										suggestedMin : 0.0,
										suggestedMax : 10000.0
									}
									}
							]
						},
						legend : {
							display : false
						}
					};
				
				var canvas, context;
				canvas = document.getElementById("canvas0");
				context = canvas.getContext("2d");
				new Chart(context, {
					type : "bar",
					data : data,
					options : options
				});
			});
		</script>
	</jstl:when>
	<jstl:when test="${averageBudgetOfContract == null && deviationBudgetOfContract == null}">
		<h3><acme:message code="client.dashboard.form.label.contract.budget"/></h3>
		<acme:print value="N/A"/>
	</jstl:when>
</jstl:choose>

<acme:return/>