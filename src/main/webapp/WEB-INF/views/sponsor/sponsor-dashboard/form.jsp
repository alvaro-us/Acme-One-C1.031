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
	<acme:input-integer code="sponsor.sponsor-dashboard.totalNumberOfInvoicesWithTaxLessThanOrEqual21Percent" path="totalNumberOfInvoicesWithTaxLessThanOrEqual21Percent"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.totalNumberOfSponsorshipsWithLink" path="totalNumberOfSponsorshipsWithLink"/>
	
	<acme:input-integer code="sponsor.sponsor-dashboard.averageAmountOfSponsorships" path="averageAmountOfSponsorships"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.deviationAmountOfSponsorships" path="deviationAmountOfSponsorships"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.minAmountOfSponsorships" path="minAmountOfSponsorships"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.maxAmountOfSponsorships" path="maxAmountOfSponsorships"/>
	
	<acme:input-integer code="sponsor.sponsor-dashboard.averageQuantityOfInvoices" path="averageQuantityOfInvoices"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.deviationQuantityOfInvoices" path="deviationQuantityOfInvoices"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.minQuantityOfInvoices" path="minQuantityOfInvoices"/>
	<acme:input-integer code="sponsor.sponsor-dashboard.maxQuantityOfInvoices" path="maxQuantityOfInvoices"/>

	
</acme:form> 

