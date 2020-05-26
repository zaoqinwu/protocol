package com.wuzq.netty.protocol.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Time
 * @date 2020/5/26 15:52:32
 * @description protocol
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Business implements Serializable {

    private static final long serialVersionUID = 4482886644627881364L;

    //账户、密码、版本验证

    private BusinessState state;

    private String businessData;



    public enum BusinessState{
       HEARTBEAT,IMAGES,COMMAND
    }

}
