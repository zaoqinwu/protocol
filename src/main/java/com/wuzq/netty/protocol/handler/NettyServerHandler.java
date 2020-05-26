package com.wuzq.netty.protocol.handler;

import com.wuzq.netty.protocol.pojo.Business;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Time
 * @date 2020/5/26 15:30:01
 * @description protocol
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {


    AtomicInteger channelAtn = new AtomicInteger();

    /**
     * 可以做一个连接验证
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接的客户端地址:{}",ctx.channel().remoteAddress());
        //TODO 作为一个IP检验
        super.channelActive(ctx);
    }


    /**
     * 超时处理 如果5秒没有接受客户端的心跳，就触发;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals(event.state())){
                log.info("心跳异常超时,evt:{}",evt);
                ctx.channel().close();
            }
            return;
        }
        super.userEventTriggered(ctx, evt);
    }


    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)  {
        if (msg instanceof Business){
               Business business = (Business) msg;
               switch (business.getState()){
                   case IMAGES:log.info("图片处理过程......");
                   case COMMAND:log.info("控制指令接收......");
                   case HEARTBEAT:log.info("心跳数据接收......");
               }
               return;
        }else if (msg instanceof ByteBuf){
            ByteBuf result = (ByteBuf) msg;
            byte[] dataByte = new byte[result.readableBytes()];
            result.readBytes(dataByte);
            String test = new String(dataByte);
            log.info("收到数据:{},cnt:{}",test,channelAtn.getAndIncrement());
            return;
        }
        log.info("不符合要求异常数据:{}",msg);
    }
}
