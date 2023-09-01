package com.box.imserver.netty;

import com.box.imcommon.contant.RedisKey;
import com.box.imcommon.enums.IMCmdType;
import com.box.imcommon.model.IMSendInfo;
import com.box.imserver.netty.processor.MessageProcessor;
import com.box.imserver.netty.processor.ProcessorFactory;
import com.box.imserver.util.SpringContextHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * WebSocket 长连接下 文本帧的处理器
 * 实现浏览器发送文本回写
 * 浏览器连接状态监控
 */
@Slf4j
public class IMChannelHandler extends SimpleChannelInboundHandler<IMSendInfo> {

    /**
     * 读取到消息后进行处理
     *
     * @param ctx
     * @param sendInfo
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMSendInfo sendInfo) throws  Exception {
        // 创建处理器进行处理
        MessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.fromCode(sendInfo.getCmd()));
        processor.process(ctx,processor.transForm(sendInfo.getData()));
    }

    /**
     * 出现异常的处理 打印报错日志
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws  Exception {
        log.error(cause.getMessage());
        //关闭上下文
        //ctx.close();
    }

    /**
     * 监控浏览器上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws  Exception  {
        log.info(ctx.channel().id().asLongText() + "连接");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws  Exception {
        AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
        Long userId = ctx.channel().attr(attr).get();
        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId);
        // 判断一下，避免异地登录导致的误删
        if(context != null && ctx.channel().id().equals(context.channel().id())){
            // 移除channel
            UserChannelCtxMap.removeChannelCtx(userId);
            // 用户下线
            RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplate");
            String key = RedisKey.IM_USER_SERVER_ID + userId;
            redisTemplate.delete(key);
            log.info("断开连接,userId:{}",userId);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
                Long userId = ctx.channel().attr(attr).get();
                log.info("心跳超时，即将断开连接,用户id:{} ",userId);
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}