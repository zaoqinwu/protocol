package com.wuzq.netty.protocol.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
@Configurable
public class ProtocolServer {


     /**
     * 端口
     */
     @Value("${server.bind_port}")
    private Integer port;

    /**
     * BOSS 组的线程个数
     */
    @Value("${server.netty.boss_group_thread_count}")
    private Integer boosGroupThreadCnt;


    /**
     * WORK 组的线程个数
     * */
    @Value("${server.netty.worker_group_thread_count}")
    private Integer workGroupThreadCnt;


    private ChannelFuture channelFuture;

    private EventLoopGroup boos;

    private EventLoopGroup worker;


    @PostConstruct
    public void initServer() throws Exception{
        log.info("------------Protocol Server Init--------------");
        boos = new NioEventLoopGroup(boosGroupThreadCnt);
        worker=new NioEventLoopGroup(workGroupThreadCnt);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boos,worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ProtocolInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);;
        channelFuture = bootstrap.bind(port).sync();
        log.info("Protocol Server started !!!");
    }


    @PreDestroy
    public void shutdownServer() throws InterruptedException {
        log.info("stopping server");
        try {
            channelFuture.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
            boos.shutdownGracefully();
        }
        log.info("server stopped !!!");
    }






}
