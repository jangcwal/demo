package com.dictionary.demo.controller;

import com.dictionary.demo.domain.Bill;
import com.dictionary.demo.domain.MP;
import com.dictionary.demo.domain.Member;
import com.dictionary.demo.service.MemberService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.ls.LSInput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
        member.setName(form.getName());
        member.setBirthday(form.getBirthday());
        member.setGender(form.getGender());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/recommend/{email}")
    public String recommend(@PathVariable("email") String s, Model model) {
        Member target = memberService.findOneByEmail(s).orElseThrow();

        /*
        * 유저 유사도 판별
        * */
        List<Member> members = memberService.findMembers();
        List<Double> similarity = new ArrayList<>();
        for(int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if(target.getEmail().equals(member.getEmail())) {
                similarity.add((double)0);
                continue;
            }

            double k = cos(target, member);
            similarity.add(k);
        }

        /*
        * 유사도로 점수 근사치 찾음
        * */
        double demo = 0;
        for(int i = 0; i < similarity.size(); i++) demo += similarity.get(i);

        List<Double> numer = new ArrayList<>();
        for(int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            String str = member.getScore();

            for(int j = 0; j < str.length(); j++) {
                int a = str.charAt(j) - '0';
                if(i == 0) numer.add(similarity.get(i) * a);
                else {
                    double temp = numer.get(j) + similarity.get(i) * a;
                    numer.set(j, temp);
                }
            }
        }

        double maxScore = 0;
        int maxIdx = 0;
        List<Double> scores = new ArrayList<>();
        for(int i = 0; i < numer.size(); i++) {
            scores.add(numer.get(i)/demo);
            if(maxScore < scores.get(i)) {
                maxScore = scores.get(i);
                maxIdx = i;
            }
        }

        int billNum = 2100000 + maxIdx + 1;
        StringBuffer result = new StringBuffer();
        try{
            String urlStr = "https://open.assembly.go.kr/portal/openapi/nzmimeepazxkubdpn?" +
                    "Key=fc034b86fe884eb299b8fc089cdc78d4" +
                    "&Type=json" +
                    "&pIndex=1" +
                    "&pSize=1" +
                    "&AGE=21" +
                    "&BILL_NO=" +
                    Integer.toString(billNum);
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));

            String returnLine = "";
            while((returnLine = br.readLine()) != null) {
                result.append(returnLine);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            JSONParser jsonParser = new JSONParser();

            JSONObject jobj = (JSONObject) jsonParser.parse(result.toString());
            JSONArray BillArray = (JSONArray) jobj.get("nzmimeepazxkubdpn");
            JSONObject row = (JSONObject) BillArray.get(1);
            JSONArray rowArray = (JSONArray) row.get("row");
            JSONObject obj = (JSONObject) rowArray.get(0);

            System.out.println(obj);
            Bill bill = new Bill();
            bill.setBILL_NO(obj.get("BILL_NO").toString());
            bill.setBILL_NAME(obj.get("BILL_NAME").toString());
            bill.setCOMMITTEE(obj.get("COMMITTEE").toString());
            bill.setPROPOSE_DT(obj.get("PROPOSE_DT").toString());
            if(obj.get("PROC_RESULT")!=null) bill.setPROC_RESULT(obj.get("PROC_RESULT").toString());
            else bill.setPROC_RESULT("미처리");
            bill.setAGE(obj.get("AGE").toString());
            bill.setDETAIL_LINK(obj.get("DETAIL_LINK").toString());
            bill.setPROPOSER(obj.get("PROPOSER").toString());
            bill.setMEMBER_LIST(obj.get("MEMBER_LIST").toString());
            bill.setRST_PROPOSER(obj.get("RST_PROPOSER").toString());
            bill.setPUBL_PROPOSER(obj.get("PUBL_PROPOSER").toString());
            bill.setCOMMITTEE_ID(obj.get("COMMITTEE_ID").toString());

            System.out.println(bill.getBILL_ID());
            model.addAttribute("Bill", bill);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/members/recommend";
    }

    private double cos(Member target, Member member) {
        int multi = 0, temp1 = 0, temp2 = 0;
        String score1 = target.getScore();
        String score2 = member.getScore();

        for(int i = 0; i < score1.length(); i++) {
            int a = score1.charAt(i) - '0';
            int b = score2.charAt(i) - '0';

            multi += a*b;
            temp1 += a*a;
            temp2 += b*b;
        }

        return multi/(Math.sqrt(temp1)*Math.sqrt(temp2));
    }
}
