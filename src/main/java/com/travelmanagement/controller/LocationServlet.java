package com.travelmanagement.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/location")
public class LocationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");

        String type = request.getParameter("type");   
        String value = request.getParameter("value"); 

        String result = "[]"; 

        if("states".equals(type)) {
            
            URL url = new URL("https://countriesnow.space/api/v0.1/countries/states");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"country\":\"India\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);           
            }

            result = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        }
        else if("cities".equals(type)) {
            URL url = new URL("https://countriesnow.space/api/v0.1/countries/state/cities");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"country\":\"India\",\"state\":\"" + value + "\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);           
            }

            result = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        }
        else if("pincode".equals(type)) {
            String apiUrl = "https://api.postalpincode.in/postoffice/" + value;
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(15000); 
            con.setReadTimeout(20000);    
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
            int code = con.getResponseCode();
            if(code == 429) {
                result = "{\"error\":\"Too many requests. Please try again later.\"}";
            } else if(code >= 400) {
                result = "{\"error\":\"Server returned HTTP code: " + code + "\"}";
            } else {
                try (InputStream is = con.getInputStream()) {
                    result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        }



        response.getWriter().write(result);
    }
}
