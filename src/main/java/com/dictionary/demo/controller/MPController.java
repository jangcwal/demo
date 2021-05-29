package com.dictionary.demo.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
public class MPController {

    @GetMapping("/party")
    public String party() {
        return "/MPs/party";
    }

    @GetMapping("/MPPage/{HG_NM}")
    public String getApi(@PathVariable("HG_NM") String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        String temp = DatatypeConverter.printHexBinary(bytes);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<temp.length();i++) {
            if(i%2==0) sb.append("%");
            sb.append(temp.charAt(i));
        }
        StringBuffer result = new StringBuffer();
        try{
            String urlStr = "https://open.assembly.go.kr/portal/openapi/nwvrqwxyaytdsfvhu?" +
                    "Key=fc034b86fe884eb299b8fc089cdc78d4" +
                    "&Type=json" +
                    "&pIndex=1" +
                    "&pSize=1" +
                    "&HG_NM=" +
                    sb.toString();

            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));

            String returnLine = "";
            while((returnLine = br.readLine()) != null) {
                result.append(returnLine);
            }
            urlConnection.disconnect();

            urlStr = "https://open.assembly.go.kr/portal/openapi/nzmimeepazxkubdpn?" +
                    "Key=fc034b86fe884eb299b8fc089cdc78d4" +
                    "&Type=json" +
                    "&pIndex=1" +
                    "&pSize=10" +
                    "&AGE=21" +
                    "&PROPOSER=" +
                    sb.toString();
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));

            returnLine = "";
            while((returnLine = br.readLine()) != null) {
                result.append(returnLine);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
