package br.com.softgran.agenda;


import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

//Clase que ira enviar uma requisição POST
public class WebClient {

    public String post(String json) {

        try {
            URL url = new URL("https://www.caelum.com.br/mobile");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            //connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            connection.connect();

            String resposta = new Scanner(connection.getInputStream()).next();

            return resposta;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
