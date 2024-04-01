package org.comlkz.ai.service.xunfei;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.comlkz.ai.pojo.AiRequest;
import org.comlkz.ai.pojo.SegmentInfo;
import org.comlkz.ai.service.ali.AliAiCall;
import org.comlkz.ai.service.xunfei.pojo.*;
import org.comlkz.ai.service.xunfei.utils.LfasrSignature;
import org.comlkz.common.utils.HttpUtils;
import org.comlkz.common.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class VoiceToTextService {

    private static final String appId = "233";

    private static final String secret = "2323";

    public static final String XUNFEI_HOST = "https://raasr.xfyun.cn";

    public void upload() throws Exception {
        String path = "testVoice.m4a";
        HashMap<String, Object> map = new HashMap<>(16);
        File audio = new File(path);
        String fileName = audio.getName();
        long fileSize = audio.length();
        map.put("appId", appId);
        map.put("fileSize", fileSize);
        map.put("fileName", fileName);
        map.put("duration", "200");
        LfasrSignature lfasrSignature = new LfasrSignature(appId, secret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());
        map.put("roleType",1);

        String paramString = HttpUtils.parseMapToPathParam(map);
        System.out.println("upload paramString:" + paramString);

        String url = XUNFEI_HOST + "/v2/api/upload" + "?" + paramString;
        System.out.println("upload_url:" + url);
        String response = HttpUtils.iflyrecUpload(url, new FileInputStream(audio));

        System.out.println("upload response:" + response);
    }


    public String getResult(String orderId) throws SignatureException, InterruptedException, IOException {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("orderId", orderId);
        LfasrSignature lfasrSignature = new LfasrSignature(appId, secret);
        map.put("signa", lfasrSignature.getSigna());
        map.put("ts", lfasrSignature.getTs());
        map.put("appId", appId);
        map.put("resultType", "transfer,predict");
        String paramString = HttpUtils.parseMapToPathParam(map);
        String url = XUNFEI_HOST + "/v2/api/getResult" + "?" + paramString;
        System.out.println("\nget_result_url:" + url);
        while (true) {
            String response = HttpUtils.iflyrecGet(url);
            JsonParse jsonParse = JsonUtil.parseObject(response, JsonParse.class);
            if (jsonParse.getContent().getOrderInfo().getStatus() == 4 || jsonParse.getContent().getOrderInfo().getStatus() == -1) {
                // System.out.println("订单完成:" + response);
                XunFeiOrderResult orderResult = JsonUtil.parseObject(jsonParse.getContent().getOrderResult(), XunFeiOrderResult.class);
                AiRequest request = new AiRequest();
                request.setSegments(new ArrayList<>());
                for (XunFeiLattice lattice : orderResult.getLattice()) {
                    String data = lattice.getJson_1best();
                    XunFeiBtest xunFeiBtest = JsonUtil.parseObject(data, XunFeiBtest.class);
                    StringBuilder content = new StringBuilder();
                    for (XunFeiWs xunFeiWs : xunFeiBtest.getSt().getRt()) {
                        for (XunFeiCw cw : xunFeiWs.getWs()) {
                            for (XunFeiCwInfo cwInfo : cw.getCw()) {
                                content.append(cwInfo.getW());
                            }
                        }
                    }
                    SegmentInfo segmentInfo = new SegmentInfo()
                            .setText(content.toString())
                            .setRole(Integer.parseInt(xunFeiBtest.getSt().getRl()));
                    request.getSegments().add(segmentInfo);
                }
                AliAiCall aiCall = new AliAiCall();
                aiCall.callData(request);
                write(JsonUtil.toJsonString(request));
                return response;
            } else {
                System.out.println("进行中...，状态为:" + jsonParse.getContent().getOrderInfo().getStatus());
                //建议使用回调的方式查询结果，查询接口有请求频率限制
                Thread.sleep(7000);
            }
        }
    }


    public static void write(String resp) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter("test.txt"));
        String ss = resp;
        bw.write(ss);
        //关闭流
        bw.close();
        System.out.println("写入txt成功");
    }

    public static void main(String[] args) throws Exception {
        VoiceToTextService service = new VoiceToTextService();
       //service.upload();
       String orderId = "DKHJQ20240401142129268pRCOfW47y01DBrVi";
        service.getResult(orderId);

//      XunFeiCwInfo cwInfo = new XunFeiCwInfo();
//      cwInfo.setW("ka");
//      List<XunFeiCwInfo> list = new ArrayList<>();
//      list.add(cwInfo);
//      XunFeiCw cw = new XunFeiCw();
//      cw.setCw(list);
//      cw.setWb(1);
//      XunFeiWs ws = new XunFeiWs();
//      ws.setWs(Lists.newArrayList(cw));
//        XunFeiSt st  = new XunFeiSt();
//        st.setRt(Lists.newArrayList(ws));
//        st.setEd("12");
//        System.out.println(JsonUtil.toJsonString(st));

        // rt.setItem();
    }


}
