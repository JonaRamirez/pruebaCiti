package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class TemperatureService {


    public String getTemperature(String city) {

        String retorno ="";
        try {
            String URL = "https://www.metaweather.com/api/location/search/?query=" + city;

            JSONArray array = new JSONArray(getDataURL(URL));

            retorno = recorreJSONArray(array);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (retorno == "")
            retorno = "the selected city does not exist";
        return retorno;
    }


    private String recorreJSONArray(JSONArray array) {
        String retorno = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.has("woeid")) {
                    String woeid = jsonObject.getString("woeid");
                    String Temperatura = getTemperaturaByWoeid(woeid);

                    retorno = retorno +"Weather forecast today in " + jsonObject.getString("title")  + Temperatura +" \n";

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return retorno;
    }

    private String getTemperaturaByWoeid(String valor)
    {
        String retorno = "";
        String URL = "https://www.metaweather.com/api/location/"+ valor;
        JSONArray array;
        try{
            String ret = getDataURL(URL);

            JSONObject jsonObject = new JSONObject(ret);
            JSONArray weather = jsonObject.getJSONArray("consolidated_weather");

            JSONObject weatherToDay = weather.getJSONObject(0);

            if (weatherToDay.has("the_temp")) {
                weatherToDay.getString("min_temp");
                weatherToDay.getString("max_temp");
                weatherToDay.getString("the_temp");

                retorno = ", currently " + getTempString(weatherToDay.getString("the_temp") ) + " " +
                        " Min " + getTempString( weatherToDay.getString("min_temp") ) + " " +
                        " Max " + getTempString(weatherToDay.getString("max_temp") ) +". " ;
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }


private String getTempString(String temperature)
{

    Float celciusFloat = Float.parseFloat(temperature);
    Float fahrenheit = (celciusFloat * 9/5) + 32 ;

    return  String.format("%.1f", celciusFloat)  + " C° (" +  String.format("%.1f", fahrenheit)  + " F°)";
}


    private String getDataURL(String rutaURL)
    {
        String retorno = "";
        try {
            URL url = new URL(rutaURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                retorno = output;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;

    }


}



