package com.box.imserver.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import com.box.imcommon.contant.Constant;
import com.box.imcommon.contant.RedisKey;
import com.box.imcommon.enums.IMCmdType;
import com.box.imcommon.model.HeartbeatInfo;
import com.box.imcommon.model.IMSendInfo;
import com.box.imserver.netty.ws.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeartbeatProcessor extends   MessageProcessor<HeartbeatInfo> {


    @Autowired
    private WebSocketServer WSServer;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, HeartbeatInfo beatInfo) {
        // 响应ws
        IMSendInfo sendInfo = new IMSendInfo();
        sendInfo.setCmd(IMCmdType.HEART_BEAT.code());
        ctx.channel().writeAndFlush(sendInfo);

        // 设置属性
        AttributeKey<Long> attr = AttributeKey.valueOf("HEARTBEAt_TIMES");
        Long heartbeatTimes = ctx.channel().attr(attr).get();
        ctx.channel().attr(attr).set(++heartbeatTimes);
        if(heartbeatTimes%10 == 0){
            // 每心跳10次，用户在线状态续一次命
            attr = AttributeKey.valueOf("USER_ID");
            Long userId = ctx.channel().attr(attr).get();
            String key = RedisKey.IM_USER_SERVER_ID+userId;
            redisTemplate.expire(key, Constant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }
    }


    @Override
    public HeartbeatInfo transForm(Object o) {
        HashMap map = (HashMap)o;
        HeartbeatInfo heartbeatInfo = BeanUtil.fillBeanWithMap(map, new HeartbeatInfo(), false);
        return  heartbeatInfo;
    }
}
