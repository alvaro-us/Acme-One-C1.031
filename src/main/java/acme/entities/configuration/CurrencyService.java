
package acme.entities.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;

@Service
public class CurrencyService {

	@Autowired
	private CurrencyRepository		repository;

	@Autowired
	private CurrencyRatioService	service;


	public boolean validatePrice(final Money price, final Double min, final Double max) {
		return price.getAmount() >= min && price.getAmount() < max;
	}

	public boolean validateCurrency(final Money price) {
		final String aceptedCurrencies = this.repository.findSystemConfiguration().getAcceptedCurrency();
		final List<String> currencies = Arrays.asList(aceptedCurrencies.split(","));
		return currencies.contains(price.getCurrency());
	}

	public String getAcceptedCurrency() {
		return this.repository.findSystemConfiguration().getAcceptedCurrency();
	}

	public boolean isAcceptedCurrency(final String currency) {
		return this.getAcceptedCurrency().contains(currency);
	}

	public String translateMoney(final Money money, final String lang) {
		String res;
		res = "";
		if (lang.equals("en")) {
			final double parteDecimal = money.getAmount() % 1;
			final double parteEntera = money.getAmount() - parteDecimal;
			res = parteEntera + "." + parteDecimal + " " + money.getCurrency();
		} else if (lang.equals("es"))
			res = money.getAmount() + " " + money.getCurrency();
		return res;
	}

	public Money changeCurrencyTo(final Money money, final String currencyTo) {
		Money res = new Money();
		Double value;
		value = this.service.getCurrencyRatio(money.getCurrency(), currencyTo);
		res.setCurrency(currencyTo);
		res.setAmount(value * money.getAmount());
		return res;
	}

	public Money changeCurrencyToBase(final Money money) {
		String newCurrency = this.repository.findSystemConfiguration().getCurrency();
		return this.changeCurrencyTo(money, newCurrency);
	}

}
