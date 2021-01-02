package com.heida;

import java.io.Serializable;
import java.util.*;  
  
public class Msg implements Serializable{  
    private int n_msg;  
    char orz;
    private String info;  
    private Random random;   
    private int rdNum;  
    public Msg(char n_msg) {  
        this.orz = n_msg;  
        MsgState();  
    }
    public void MsgState(){//用于模拟分组丢失，ACK丢失等情况  
        random = new Random();  
        rdNum = Math.abs(random.nextInt())%100;  
        if (rdNum<20) info = "losePackage";  
        else if(rdNum>80) info ="loseAck";  
        else info ="right";  
    }    
    public int getN_msg() {  
        return n_msg;  
    }  
    public char getorz(){
    	return orz;
    }
    public void setN_msg(int n_msg) {  
        this.n_msg = n_msg;  
    }  
    public String getInfo() {  
        return info;  
    }  
    public void setInfo(String info) {  
        this.info = info;  
    }  
}  
