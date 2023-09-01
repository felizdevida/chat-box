package com.box.imclient.task;

import com.box.imclient.listener.MessageListenerMulticaster;
import com.box.imcommon.contant.RedisKey;
import com.box.imcommon.enums.IMListenerType;
import com.box.imcommon.model.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

;

@Slf4j
@Component
public class PullSendResultPrivateMessageTask extends  AbstractPullMessageTask{


    @Qualifier("IMRedisTemplate")
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MessageListenerMulticaster listenerMulticaster;

    @Override
    public void pullMessage() {
        String key = RedisKey.IM_RESULT_PRIVATE_QUEUE;
        SendResult result =  (SendResult)redisTemplate.opsForList().leftPop(key,10, TimeUnit.SECONDS);
        if(result != null) {
            listenerMulticaster.multicast(IMListenerType.PRIVATE_MESSAGE, result);
        }
    }

}
