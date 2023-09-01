package com.box.implatform.listener;

import com.box.imclient.annotation.IMListener;
import com.box.imclient.listener.MessageListener;
import com.box.imcommon.enums.IMListenerType;
import com.box.imcommon.enums.IMSendCode;
import com.box.imcommon.model.GroupMessageInfo;
import com.box.imcommon.model.SendResult;
import com.box.implatform.contant.RedisKey;
import com.box.implatform.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
@IMListener(type = IMListenerType.GROUP_MESSAGE)
public class GroupMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(SendResult result){
        GroupMessageInfo messageInfo = (GroupMessageInfo) result.getMessageInfo();
        if(messageInfo.getType().equals(MessageType.TIP)){
            // 提示类数据不记录
            return;
        }

        // 保存该用户已拉取的最大消息id
        if(result.getCode().equals(IMSendCode.SUCCESS)) {
            String key = RedisKey.IM_GROUP_READED_POSITION + messageInfo.getGroupId() + ":" + result.getRecvId();
            redisTemplate.opsForValue().set(key, messageInfo.getId());
        }
    }

}
