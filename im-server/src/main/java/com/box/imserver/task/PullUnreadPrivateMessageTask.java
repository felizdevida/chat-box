package com.box.imserver.task;


import com.box.imcommon.contant.RedisKey;
import com.box.imcommon.enums.IMCmdType;
import com.box.imcommon.model.IMRecvInfo;
import com.box.imcommon.model.PrivateMessageInfo;
import com.box.imserver.netty.IMServerGroup;
import com.box.imserver.netty.processor.MessageProcessor;
import com.box.imserver.netty.processor.ProcessorFactory;
import com.box.imserver.netty.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class PullUnreadPrivateMessageTask extends  AbstractPullMessageTask {

    @Autowired
    private WebSocketServer WSServer;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void pullMessage() {
        // 从redis拉取未读消息
        String key = RedisKey.IM_UNREAD_PRIVATE_QUEUE + IMServerGroup.serverId;
        List messageInfos = redisTemplate.opsForList().range(key,0,-1);
        for(Object o: messageInfos){
            redisTemplate.opsForList().leftPop(key);
            IMRecvInfo<PrivateMessageInfo> recvInfo = (IMRecvInfo)o;
            MessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.PRIVATE_MESSAGE);
            processor.process(recvInfo);

        }
    }

}
