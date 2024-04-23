
package acme.entities.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.data.datatypes.Money;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class CurrencyService {

	@Autowired
	private CurrencyRepository repository;


	public boolean validatePrice(final Money price, final Double min, final Double max) {
		return price.getAmount() >= min && price.getAmount() < max;
	}

	public boolean validateCurrency(final Money price) {
		final String aceptedCurrencies = this.repository.findSystemConfiguration().getAcceptedCurrency();
		final List<String> currencies = Arrays.asList(aceptedCurrencies.split(","));
		return currencies.contains(price.getCurrency());
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
		final String apiBase = "https://api.freecurrencyapi.com/v1/latest?apikey=";
		final String apikey1 = "fca_live_JHDla5jfgb2Amdv36TrGtDiim2KU12yYX3f0csAw";
		Money res = new Money();

		final String requestURL = apiBase + apikey1 + "&currencies=" + currencyTo + "&base_currency=" + money.getCurrency();
		final OkHttpClient client = new OkHttpClient();
		final Request request = new Request.Builder().url(requestURL).build();
		Response response;
		if (money != null && !money.getCurrency().equals(currencyTo))
			try {
				response = client.newCall(request).execute();
				final String responseBody = response.body().string();
				final ObjectMapper mapper = new ObjectMapper();
				final JsonNode jsonNode = mapper.readTree(responseBody);
				final Double value = jsonNode.get("data").get(currencyTo).asDouble();
				if (value != null) {
					res.setCurrency(currencyTo);
					res.setAmount(value * money.getAmount());
				}
			} catch (final Exception e) {
				System.out.println(e);
				res = money;
			}
		else
			res = money;
		return res;

	}

	public Money changeCurrencyToBase(final Money money) {
		String newCurrency = this.repository.findSystemConfiguration().getCurrency();
		return this.changeCurrencyTo(money, newCurrency);
	}

}
