package com.dictionary.demo.service;

import com.dictionary.demo.domain.MP;
import com.dictionary.demo.repository.MPRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class MPService {
    private final MPRepository mpRepository;

    public MPService(MPRepository mpRepository) { this.mpRepository = mpRepository; }

    public void init(String jsonData) {
        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jObj = (JSONObject) jsonParser.parse(jsonData.toString());

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
            if(!mpRepository.findByMona(mp.getMONA_CD()).isPresent()) {
                mpRepository.save(mp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<MP> findOne(String s) {
        return mpRepository.findByMona(s);
    }
}
