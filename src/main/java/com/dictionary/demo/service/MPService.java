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

    public void join(MP mp) {
        if(!mpRepository.findByMona(mp.getMONA_CD()).isPresent()) {
                mpRepository.save(mp);
        }
    }

    public Optional<MP> findOne(String s) {
        return mpRepository.findByMona(s);
    }
}
