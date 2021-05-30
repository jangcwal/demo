package com.dictionary.demo.controller;

import com.dictionary.demo.domain.Bill;
import com.dictionary.demo.domain.MP;
import com.dictionary.demo.service.MPService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MPController {
    private final MPService mpService;

    public MPController(MPService mpService) { this.mpService = mpService; }

    @GetMapping("/party")
    public String party() {
        return "/MPs/party";
    }

    @GetMapping("/MPPage/{HG_NM}")
    public String getApi(@PathVariable("HG_NM") String s, Model model) {

        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        String temp = DatatypeConverter.printHexBinary(bytes);
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<temp.length();i++) {
            if(i%2==0) sb.append("%");
            sb.append(temp.charAt(i));
        }

        StringBuffer result = new StringBuffer();
        StringBuffer result1 = new StringBuffer();
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
                result1.append(returnLine);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jObj = (JSONObject) jsonParser.parse(result.toString());

            JSONArray MPArray = (JSONArray) jObj.get("nwvrqwxyaytdsfvhu");
            JSONObject row = (JSONObject) MPArray.get(1);
            JSONArray rowArray = (JSONArray) row.get("row");
            JSONObject obj = (JSONObject) rowArray.get(0);

            MP mp = new MP();
            mp.setTEL_NO(obj.get("TEL_NO").toString());
            mp.setBTH_GBN_NM(obj.get("BTH_GBN_NM").toString());
            mp.setASSEM_ADDR(obj.get("ASSEM_ADDR").toString());
            mp.setHJ_NM(obj.get("HJ_NM").toString());
            mp.setHG_NM(obj.get("HG_NM").toString());
            mp.setBTH_DATE(obj.get("BTH_DATE").toString());
            mp.setELECT_GBN_NM(obj.get("ELECT_GBN_NM").toString());
            mp.setPOLY_NM(obj.get("POLY_NM").toString());
            mp.setREELE_GBN_NM(obj.get("REELE_GBN_NM").toString());
            mp.setCMITS(obj.get("CMITS").toString());
            mp.setMEM_TITLE(obj.get("MEM_TITLE").toString());
            mp.setENG_NM(obj.get("ENG_NM").toString());
            mp.setSEX_GBN_NM(obj.get("SEX_GBN_NM").toString());
            mp.setE_MAIL(obj.get("E_MAIL").toString());
            mp.setMONA_CD(obj.get("MONA_CD").toString());
            mp.setSECRETARY2(obj.get("SECRETARY2").toString());
            mp.setJOB_RES_NM(obj.get("JOB_RES_NM").toString());
            mp.setSTAFF(obj.get("STAFF").toString());
            mp.setHOMEPAGE(obj.get("HOMEPAGE").toString());
            mp.setCMIT_NM(obj.get("CMIT_NM").toString());
            mp.setSECRETARY(obj.get("SECRETARY").toString());
            mp.setORIG_NM(obj.get("ORIG_NM").toString());
            mp.setUNITS(obj.get("UNITS").toString());
            //mpService.join(mp);

            model.addAttribute("MP", mp);

            List<Bill> billList = new ArrayList<>();
            JSONObject jObj1 = (JSONObject) jsonParser.parse(result1.toString());
            JSONArray BillArray = (JSONArray) jObj1.get("nzmimeepazxkubdpn");
            JSONObject rowI = (JSONObject) BillArray.get(1);
            JSONArray rowIArray = (JSONArray) rowI.get("row");
            for(int i=0; i<rowIArray.size();i++) {
                JSONObject objI = (JSONObject) rowIArray.get(i);

                Bill bill = new Bill();
                bill.setBILL_NO(objI.get("BILL_NO").toString());
                bill.setBILL_NAME(objI.get("BILL_NAME").toString());
                bill.setCOMMITTEE(objI.get("COMMITTEE").toString());
                bill.setPROPOSE_DT(objI.get("PROPOSE_DT").toString());
                if(objI.get("PROC_RESULT")!=null) bill.setPROC_RESULT(objI.get("PROC_RESULT").toString());
                else bill.setPROC_RESULT("미처리");
                bill.setAGE(objI.get("AGE").toString());
                bill.setDETAIL_LINK(objI.get("DETAIL_LINK").toString());
                bill.setPROPOSER(objI.get("PROPOSER").toString());
                bill.setMEMBER_LIST(objI.get("MEMBER_LIST").toString());
                bill.setRST_PROPOSER(objI.get("RST_PROPOSER").toString());
                bill.setPUBL_PROPOSER(objI.get("PUBL_PROPOSER").toString());
                bill.setCOMMITTEE_ID(objI.get("COMMITTEE_ID").toString());

                billList.add(bill);
            }
            model.addAttribute("Bill", billList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "MPs/MPPage";
    }
}
