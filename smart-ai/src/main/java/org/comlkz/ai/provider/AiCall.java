package org.comlkz.ai.provider;

import org.comlkz.ai.pojo.AiRequest;
import org.comlkz.ai.pojo.AiResponse;

public interface AiCall {

     AiResponse callData(AiRequest request);

}
