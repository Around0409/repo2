package com.heida;

import java.awt.ScrollPane;
import java.io.*;  
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;  
  
public class Client extends Thread{  
    private String address = "127.0.0.1";   
    private int port = 9878;   
    JFrame frame = new JFrame("Client");
    JTextArea t2 = new JTextArea();
    ScrollPane panel = new ScrollPane();
    public Client(String s) {  
         Socket client = null;
         panel.add(t2);
         frame.add(panel);
         frame.pack();
         frame.setVisible(true);
         int i = 0;
         int j = 0;
         for(j = 0; j <= s.length(); j++)
         {  
             client = new Socket();
             char k = s.charAt(j);
             Msg data = new Msg(k); /*发送一个数据这帧0*/  
             if(j == s.length() - 1) data.setInfo("finish");
             InetSocketAddress isa = new InetSocketAddress(this.address,this.port);   
            try   
            {   
                client.connect(isa,10000);////   
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());               
                out.writeObject(data);   
                t2.append("\n发送分组 "+data.getorz()+"\n");
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));  
                String str = br.readLine();  
                if(str.equals("resend1"))  
                {  
                	t2.append("\n发送超时，重发\n");
                    i++;  
                    j--;
                    out.flush();   
                    out.close();   
                    out = null ;   
                    data = null ;  
                    client.close();  
                    client = null ;   
                    continue;  

                }  
                else if(str.equals("resend2"))  
                {  
                	t2.append("\nACK丢失，重发\n");
                    i++;  
                    j--;
                    out.flush();   
                    out.close();   
                    out = null ;   
                    data = null ;  
                    client.close();  
                    client = null ;   
                    continue;  

                }  
                else {
                	i=0;
                	char o = data.getorz();
                	if(j == s.length() - 1){
                		t2.append("\n" + "发送完成" + "hahaha" + "\n");
                		break;
                	}
                	t2.append("\n"+"接收ACK"+o+"发送分组"+s.charAt(j+1));
                	System.out.println();                  	
                }    
                out.flush();   
                out.close();   
                out = null ;   
                data = null ;  
                client.close();  
                client = null ;   
       
            }   
            catch(java.io.IOException e)   
            {   
              t2.append("(Client)Socket连接出现了问题!");
              System.out.println();
              t2.append("IOException :" + e.toString());
              System.out.println();   
            }   
            try {  
                this.sleep(8000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
             if(i==16){
            	t2.append("重发超过16次,放弃数据");
            	break;
            }
         }  
    }  
       
    public static void main(String[] args) {  
    	Scanner cin = new Scanner(System.in);
        System.out.println("please input data////////////////////////////");
        String c = cin.next();
        new Client(c);  
        System.out.println("transport finish/////////////////////////////");
    }  
    
}  