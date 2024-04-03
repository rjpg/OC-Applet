package jmatlinkws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import functions.Function;


public class MatlinkDirectWS {
	
	
	public static final int DGRAM_BUF_LEN = 512;     
	public String sendCmd(String cmd,String host,int port,String jws)
	{
		String ret="";
	   /*
		POST /matlinkws/Matlink.jws HTTP/1.0
		Content-Type: text/xml; charset=utf-8
		Accept: application/soap+xml, application/dime, multipart/related, text/*
		User-Agent: Axis/1.4
		Host: 127.0.0.1:8081
		Cache-Control: no-cache
		Pragma: no-cache
		SOAPAction: ""
		Content-Length: 501

		<?xml version="1.0" encoding="UTF-8"?><soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><soapenv:Body><ns1:runMatCmd soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="http://DefaultNamespace"><cmd xsi:type="xsd:string">S = dsolve('Df = 3*f+4*g', 'Dg = -4*f+3*g','f(0)=1','g(0)=2','y')
		 g = S.g</cmd></ns1:runMatCmd></soapenv:Body></soapenv:Envelope>
		 
		 */
		//tcp monitor
		
		
		String SendData="POST "+jws+" HTTP/1.0\n"+
		"Content-Type: text/xml; charset=utf-8\n"+
		"Accept: application/soap+xml, application/dime, multipart/related, text/*\n"+
		"User-Agent: Axis/1.4\n"+
		"Host: "+host+":"+port+"\n"+
		"Cache-Control: no-cache\n"+
		"Pragma: no-cache\n"+
		"SOAPAction: \"\"\n"+
		"Content-Length: "+(427+cmd.length())+"\n"+
		"\n"+
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:runMatCmd soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://DefaultNamespace\"><cmd xsi:type=\"xsd:string\">"+
		cmd+
		"</cmd></ns1:runMatCmd></soapenv:Body></soapenv:Envelope>\n";
		 
		 
		
		
		
	    Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            kkSocket = new Socket(host, port);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:"+host);
            return null;
            //System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to:"+host);
            return null;
            //System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser=SendData;

        try {
        	out.println(fromUser);
			while ((fromServer = in.readLine()) != null) {
				ret=ret+"\n"+fromServer;
			    //System.out.println("Server: " + fromServer);
		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        out.close();
        try {
			in.close();
			stdIn.close();
		    kkSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		

		return ret;
	}
	
	
	
	 public static void main(String[] args) throws Exception 
     {  
		 /*MatlinkDirectWS matws=new MatlinkDirectWS();
		 String cmd="S = dsolve('DMIN = x','x')\n";
		 String ret=matws.sendCmd(cmd,"whale.fe.up.pt",80,"/coapplet/matlinkws/Matlink.jws");
		 //String ret=matws.sendCmd(cmd,"127.0.0.1",8080,"/matlinkws/Matlink.jws");
		 System.out.println("RET---:"+ret);
		 Function g=MatParse.matParse(ret,"S","x");
		 
		 if(g!=null)
			 System.out.println("\nfunction:"+g.getFunction()+"\norder to:"+g.getOrderTo());
		 else
			 System.out.println("\nNão houve função g(y) encontrada no retorno do matlab");
		 */
		 String rui="ruir";
		 String aux=rui;
		 aux=aux.replaceAll("r","i");
		 
		 System.out.println("rui:"+rui+"  aux:"+aux);
		 
     }
}
