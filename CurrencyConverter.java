import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your API key from Open Exchange Rates:");
        String apiKey = reader.readLine(); 

        System.out.print("Enter your base currency (e.g., USD, EUR, GBP): ");
        String baseCurrency = reader.readLine().toUpperCase();

        System.out.print("Enter your target currency (e.g., USD, EUR, GBP): ");
        String targetCurrency = reader.readLine().toUpperCase();

        System.out.print("Enter the amount to convert: ");
        double amount = Double.parseDouble(reader.readLine());

        String urlStr = "https://api.freecurrencyapi.com/" + baseCurrency + ".json?app_id=" + apiKey;

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = input.readLine()) != null) {
                response.append(line);
            }
            input.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject rates = jsonObject.getJSONObject("rates");

            if (rates.has(targetCurrency)) {
                double exchangeRate = rates.getDouble(targetCurrency);
                double convertedAmount = amount * exchangeRate;

                System.out.println("Converted amount: " + convertedAmount + " " + targetCurrency);
            } else {
                System.out.println("Target currency not found.");
            }
        } else {
            System.out.println("Error in fetching currency data. Please check your inputs or API key.");
        }

        connection.disconnect();
    }
}
