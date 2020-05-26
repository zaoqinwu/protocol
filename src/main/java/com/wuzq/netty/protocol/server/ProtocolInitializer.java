package com.wuzq.netty.protocol.server;

import com.wuzq.netty.protocol.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author Time
 * @date 2020/5/26 15:23:59
 * @description protocol
 */
public class ProtocolInitializer extends ChannelInitializer<SocketChannel> {

    //     protobuf 序列化
    //       ph.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
    //        ph.addLast(new ProtobufVarint32FrameDecoder());
    //        ph.addLast(new ProtobufDecoder(UserMsg.User.getDefaultInstance()));
    //        ph.addLast(new ProtobufVarint32LengthFieldPrepender());
    //        ph.addLast(new ProtobufEncoder());
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline cp = ch.pipeline();
        cp.addLast(new IdleStateHandler(60,0,0, TimeUnit.SECONDS));
        cp.addLast(new NettyServerHandler());
    }
}
