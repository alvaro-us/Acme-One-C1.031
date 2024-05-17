
package acme.entities.configuration;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class CurrencyRatioService {

	@Autowired
	private CurrencyRatioRepository repository;


	public CurrencyRatio createCurrencyRatio(final String from, final String to) {
		CurrencyRatio res = new CurrencyRatio();
		res.setDate(new Date()); // Aquí usamos new Date() para obtener la fecha y hora actuales
		res.setFromCurrency(from);
		res.setToCurrency(to);

		final String apiBase = "https://api.freecurrencyapi.com/v1/latest?apikey=";
		final String apikey1 = "fca_live_JHDla5jfgb2Amdv36TrGtDiim2KU12yYX3f0csAw";
		final String requestURL = apiBase + apikey1 + "&currencies=" + to + "&base_currency=" + from;
		final OkHttpClient client = new OkHttpClient();
		final Request request = new Request.Builder().url(requestURL).build();
		Response response;

		if (Objects.equals(from, to))
			res = null;
		else
			try {
				response = client.newCall(request).execute();
				final String responseBody = response.body().string();
				final ObjectMapper mapper = new ObjectMapper();
				final JsonNode jsonNode = mapper.readTree(responseBody);
				final Double value = jsonNode.get("data").get(to).asDouble();
				if (value != null) {
					res.setRatio(value);
					this.repository.save(res);
					return res;
				}
			} catch (final Exception e) {
				res = null;
			}

		return res;

	}

	public Double getCurrencyRatio(final String from, final String to) {
		CurrencyRatio cr = this.repository.findCurrencyRatioFromTo(from, to);
		CurrencyRatio currencyRatio;
		if (cr != null) {
			Date currentDate = new Date();
			long differenceInMilliseconds = currentDate.getTime() - cr.getDate().getTime();
			long differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);
			boolean masDeUnDiaDeDiferencia = differenceInDays > 90;
			if (masDeUnDiaDeDiferencia) {
				this.repository.deleteById(cr.getId());
				currencyRatio = this.createCurrencyRatio(from, to);
				return currencyRatio.getRatio();
			} else
				return cr.getRatio();

		} else {
			currencyRatio = this.createCurrencyRatio(from, to);
			if (currencyRatio == null)
				return 1.;
			return currencyRatio.getRatio();
		}
	}
}
