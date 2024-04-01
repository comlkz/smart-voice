package org.comlkz.ai.service.ali;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import lombok.extern.slf4j.Slf4j;
import org.comlkz.ai.pojo.AiRequest;
import org.comlkz.ai.pojo.AiResponse;
import org.comlkz.ai.pojo.SegmentInfo;
import org.comlkz.ai.provider.AiCall;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AliAiCall implements AiCall {

    private static final String apiKey  = "sk";

    private static final String modelName = "qwen-vl-plus";

    @Override
    public AiResponse callData(AiRequest request) {
       try {
           MultiModalConversation conv = new MultiModalConversation();
           List<Map<String, Object>> systemContents =  request.getSegments().stream().filter(item->item.getRole() == 1)
                   .map(item->Collections.singletonMap("text",(Object) item.getText()))
                   .collect(Collectors.toList());

           List<Map<String, Object>> userContents =  request.getSegments().stream().filter(item->item.getRole() == 0)
                   .map(item->Collections.singletonMap("text",(Object) item.getText()))
                   .collect(Collectors.toList());
           userContents.add(Collections.singletonMap("text", "请帮我梳理对话中的知识点？"));
           MultiModalMessage systemMessage = MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                   .content(systemContents).build();
           MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                   .content(userContents).build();
           List<MultiModalMessage> messages = new ArrayList<>();
           messages.add(systemMessage);
           messages.add(userMessage);


           MultiModalConversationParam param = MultiModalConversationParam.builder()
                   .model(modelName)
                   .messages(messages)
                   .apiKey(apiKey)
                   .build();
           MultiModalConversationResult result = conv.call(param);
           System.out.println(result);
           // add the result to conversation

           // new messages
           return null;
       }catch (Exception e){
          log.error("异常，",e);
       }
       return null;
    }

    public static void main(String[] args) {
        AliAiCall call = new AliAiCall();
        call.callData(null);
    }
}
