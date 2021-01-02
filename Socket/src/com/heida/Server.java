package com.heida;

import java.awt.Label;
import java.awt.ScrollPane;
import java.io.*;  
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Scrollable;  
  
public class Server  extends Thread{
     private boolean OutServer = false;   
     private static ServerSocket server ;   
     private static  int ServerPort = 9878;  
     String ans = new String();
     JFrame frame;
     JTextArea t1;
     ScrollPane panel;
     public Server()   
     {   
        try   
        {     
          server = new ServerSocket(ServerPort);
          frame = new JFrame("Sever");
          panel =  new ScrollPane();
          t1 = new JTextArea();
          panel.add(t1);
          frame.add(panel);
          frame.pack();
          frame.setVisible(true);
        }   
        catch(java.io.IOException e)   
        {   
        	t1.append("Socket连接有问题!");
        	t1.append("IOException :" + e.toString());   
        }    
      }       
      
     public void run()   
     {   		
    	Socket socket ;   
    	ObjectInputStream in ;   
    	t1.append("服务器已启动!\n");   
        while(!OutServer)   
        {   
          socket = null;   
          try   
          {   
            synchronized(server)   
            {   
              socket = server.accept();   
            }   
            //System.out.println("取得连接: InetAddress = " + socket.getInetAddress());   
            socket.setSoTimeout(15000);  
            in = new ObjectInputStream(socket.getInputStream());   
            Msg data = (Msg)in.readObject();
            t1.append("发送的内容"+ data.getorz()+"  状态："+data.getInfo()+"\n");
            if(data.getInfo().equals("losePackage"))  
            {  
                t1.append("发送超时");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());  
                dos.writeBytes("resend1"+'\n');  
                dos.close();  
            }  
            else if(data.getInfo().equals("loseAck"))  
            {  
            	t1.append("ACK丢失");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());  
                dos.writeBytes("resend2"+'\n');  
                dos.close();  
            } 
            else if(data.getInfo().equals("finish")){
            	ans += data.getorz();
            	t1.append("接受结束 结果为：" + ans);
            	ans = "";
            	DataOutputStream dos = new DataOutputStream(socket.getOutputStream());  
                dos.writeBytes("ok"+'\n');  
                dos.close();
            }
            else  
            {  
            	//int o=1-data.getN_msg();
            	char o = data.getorz();
            	ans += o;
            	t1.append("接收分组 "+data.getorz()+"   发送ACK "+o+"\n");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());  
                dos.writeBytes("ok"+'\n');  
                dos.close();  
            }  
            t1.append("\n");      
            in.close();   
            in = null ;   
            socket.close();  
       
          }   
          catch(java.io.IOException e)   
          {   
        	  t1.append("Socket连接出现问题!\n");
               
              System.out.println("IOException :" + e.toString());   
          }
          catch(java.lang.ClassNotFoundException e)   
          {   
              System.out.println("ClassNotFoundException :" + e.toString());   
          }   
        }   
      }   
     public static void main(String[] args) {  
        new Server().start();  
     }  
      
}  