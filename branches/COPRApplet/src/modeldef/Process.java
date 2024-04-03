package modeldef;

import java.awt.Color;
import java.awt.geom.Arc2D.Double;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants.ColorConstants;

import jmatlinkws.MatParse;
import jmatlinkws.MatlinkDirectWS;

import functions.BASE;
import functions.Function;
import inputchart.Delimiter;
import inputchart.InputChart;
import inputchart.inputChartListener;

public class Process {

	//public String MatlabIP="192.168.106.186";
	//public String MatlabIP="whale.fe.up.pt";
	//public int MatlabPort=80;
	
	public Boolean flag_error=false;
	
	public InputChart[] u_x;
	
	public String[] N_x;
	
	public String[] N_0;

	public String minPart_1;
	public String minPart_2;
	
	public double start;
	public double end;
	
    public JTextArea msgTextArea = null;
    
    public Vector<Delimiter>[] result_n;//=new Vector[2];
    //public Vector<Delimiter> result_n2=new Vector<Delimiter>();
    
    
    public Process(InputChart[] chart,String[] N_a,String[] N_0_a,String min1,String min2,double lim1,double lim2,JTextArea msg)
    {
    	N_0=N_0_a;
    	N_x=N_a;
    	minPart_1=min1;
    	minPart_2=min2;
    	start=lim1;
    	end=lim2;
    	u_x=chart;
    	msgTextArea=msg;
    }
    
    public void process_N()
    {
    	flag_error=false;
    	System.err.println("LOG: Criando array de retorno com "+ N_x.length+"elementos");
    	result_n=new Vector[N_x.length];
    	// -------------------process 1º delimiter of N1----------------- 
    	Delimiter[] del=new Delimiter[u_x.length];
    	for(int i=0;i<u_x.length;i++)
    	{
    		del[i]=u_x[i].findPrevious(start);
    	}
    	
    	String[] fauxN=new String[N_x.length];
    	
    	for(int i=0;i<N_x.length;i++)
    	{
    		fauxN[i]=N_x[i];
    	}
    	for(int i=0;i<del.length;i++)
    	{
    		if(del[i]!=null)
    		{
    			String func=del[i].getFunc().getFunction();
    			for(int j=0;j<fauxN.length;j++)
    			{
    				fauxN[j]=fauxN[j].replaceAll("U"+(i+1),"("+func+")");
    				//N_x[j]=fauxN[j];
    			}
    		
    		}
    		else
    		{
    			writeMsg("Warning - part of the interval process is not defined at U"+(i+1)+"("+start+")[assuming U"+(i+1)+"("+start+")=0]");
    			//writeMsg("Warning - part of the interval process is not defined [default function is U"+(i+1)+"("+start+")]");
    			for(int j=0;j<fauxN.length;j++)
    			{
    				fauxN[j]=fauxN[j].replaceAll("U"+(i+1),"(0)");
    				//N_x[j]=fauxN[j];
    				//System.err.println("-------------------------\n"+fauxN[j]);
    			}
    		}
    	}
    	
    	double[] N_0_aux=new double[N_0.length];
    	for(int i=0;i<N_0.length;i++)
    	{
    		try{
    			N_0_aux[i]=java.lang.Double.valueOf(N_0[i]);	
    		}catch (Exception e) {
    			writeMsg("Error - parsing N"+i+"(0) [assuming N("+i+")=0.0]");
    			N_0_aux[i]=0.0;
    		}
    	}
    	
    	//writeMsg("N1'(x)="+fauxN1+"\nN2'(x)="+fauxN2+"\nN1(0)="+N1_0_aux+"\nN2(0):"+N2_0_aux);
    	//String cmd="S = dsolve('DN1 = "+fauxN1+"', 'DN2 = "+fauxN2+"','N1(0)="+N1_0_aux+"','N2(0)="+N2_0_aux+"','x')\n N1 = S.N1\n N2 = S.N2\n";
    	String cmd="S = dsolve(";
    	for(int i=0;i<fauxN.length;i++)
    	{
    		cmd=cmd+"'DN"+(i+1)+" = "+fauxN[i]+"', ";
    	}
    	
    	for(int i=0;i<N_0_aux.length;i++)
    	{
    		cmd=cmd+"'N"+(i+1)+"(0)= "+N_0_aux[i]+"', ";
    	}
    	cmd=cmd+"'"+BASE.orderTo+"')\n";
    	for(int i=0;i<fauxN.length;i++)
    	{
    		cmd=cmd+" N"+(i+1)+" = S.N"+(i+1)+"\n";
    	}
    	
    	System.out.println("LOG: comando a ser enviado para o mat lab\n"+cmd);
    	
		MatlinkDirectWS matWS=new MatlinkDirectWS();
		
		String ret=matWS.sendCmd(cmd,BASE.matServerIP,BASE.matPort,BASE.matJWS);
		System.out.println("LOG: Resposta do mat lab\n"+ret);
		
		//System.err.println(ret);
		//parse do resultado do mat
		
		Function[] func_N=new Function[fauxN.length];
		for(int i=0;i<func_N.length;i++)
		{
			func_N[i]=MatParse.matParse(ret,"N"+(i+1),BASE.orderTo);
			if(func_N[i]==null)
			{
				flag_error=true;
				writeMsg("error processing diff N"+(i+1)+"\n");
			}
		}
		
		if(flag_error)
		{
			writeMsg("Impossible to continue solving");
			return;
		}
		
		// calculo da constante de integração...
		double[] ConstN=new double[func_N.length];
		for(int i=0;i<ConstN.length;i++)
		{
			ConstN[i]=N_0_aux[i]-func_N[i].f(start);
		}
		
		// criar novos delimitadores de resultado
		Delimiter[]  dauxN=new Delimiter[func_N.length]; 
		for(int i=0;i<func_N.length;i++)
		{
			result_n[i]=new Vector<Delimiter>();
			dauxN[i]=new Delimiter(start);
			dauxN[i].setFunc(new Function(func_N[i].getFunction()+"+("+ConstN[i]+")",BASE.orderTo));
			result_n[i].add(dauxN[i]);
			writeMsg("N"+i+"(x)="+dauxN[i].getFunc().getFunction()+"\n");
		}
		
		//encontrar o seguinte
		Delimiter delprox=u_x[0].findNext(start); 
		for(int i=1;i<u_x.length;i++)
		{
			if(delprox!=null && u_x[i].findNext(start)!=null)
				if(delprox.getPlace()>u_x[i].findNext(start).getPlace())
					delprox=u_x[i].findNext(start);
			if(delprox==null && u_x[i].findNext(start)!=null)
				delprox=u_x[i].findNext(start);
		}
		
    	while(delprox!=null && delprox.getPlace()<end)
    	{
    		
    		//actualizar os del seguintes
    		for(int i=0;i<u_x.length;i++)  
    		{
    			del[i]=u_x[i].findPreviousEQ(delprox.getPlace());
    		}
        	// sacar a função do del currente
        	// substituir a função do del onde está "U1..."
    		fauxN=new String[N_x.length];
    		for(int i=0;i<N_x.length;i++)
        	{
        		fauxN[i]=N_x[i];
        	}
        	for(int i=0;i<del.length;i++)
        	{
        		if(del[i]!=null)
        		{
        			String func=del[i].getFunc().getFunction();
        			for(int j=0;j<fauxN.length;j++)
        			{
        				fauxN[j]=fauxN[j].replace("U"+(i+1),"("+func+")");
        				//N_x[j]=fauxN[j];
        			}
        		
        		}
        		else
        		{
        			writeMsg("Warning - part of the interval process is not defined at U"+(i+1)+"("+delprox.getPlace()+")[assuming U"+(i+1)+"("+delprox.getPlace()+")=0]");
        			for(int j=0;j<fauxN.length;j++)
        			{
        				fauxN[j]=fauxN[j].replace("U"+(i+1),"(0)");
        				//N_x[j]=fauxN[j];
        			}
        		}
        	}
            		
        	// tirar os valores "N1(0)" para este del
        	for(int i=0;i<N_0_aux.length;i++)
        	{
        		N_0_aux[i]=dauxN[i].getFunc().f(delprox.getPlace());
        	}
        	
        
        	//resolver o integral deste del no matlab
        	cmd="S = dsolve(";
        	for(int i=0;i<fauxN.length;i++)
        	{
        		cmd=cmd+"'DN"+(i+1)+" = "+fauxN[i]+"', ";
        	}
        	
        	for(int i=0;i<N_0_aux.length;i++)
        	{
        		cmd=cmd+"'N"+(i+1)+"(0)= "+N_0_aux[i]+"', ";
        	}
        	cmd=cmd+"'"+BASE.orderTo+"')\n";
        	for(int i=0;i<fauxN.length;i++)
        	{
        		cmd=cmd+" N"+(i+1)+" = S.N"+(i+1)+"\n";
        	}
        	
        	System.out.println("LOG: comando a ser enviado para o mat lab\n"+cmd);
        	
        	//enviar o comando e obter resposta
        	ret=matWS.sendCmd(cmd,BASE.matServerIP,BASE.matPort,BASE.matJWS);
    		//ret=matWS.sendCmd(cmd,MatlabIP,MatlabPort,"/coapplet/matlinkws/Matlink.jws");
        	System.out.println("LOG: Resposta do mat lab\n"+ret);
        	
    		// parce de resultado
    		func_N=new Function[fauxN.length];
    		for(int i=0;i<func_N.length;i++)
    		{
    			func_N[i]=MatParse.matParse(ret,"N"+(i+1),BASE.orderTo);
    			if(func_N[i]==null)
    				writeMsg("error processing diff N"+(i+1)+"\n");
    		}
    	
    		// Calculo da constante de integração
    		ConstN=new double[func_N.length];
    		for(int i=0;i<ConstN.length;i++)
    		{
    			ConstN[i]=N_0_aux[i]-func_N[i].f(delprox.getPlace());
    		}
    		
    		// criar novos delimitadores de resultado
    		dauxN=new Delimiter[func_N.length]; 
    		for(int i=0;i<func_N.length;i++)
    		{
    			dauxN[i]=new Delimiter(delprox.getPlace());
    			dauxN[i].setFunc(new Function(func_N[i].getFunction()+"+("+ConstN[i]+")",BASE.orderTo));
    			result_n[i].add(dauxN[i]);
    			writeMsg(delprox.getPlace()+"-> N"+i+"(x)="+dauxN[i].getFunc().getFunction()+"\n");
    		}
    		

    		//encontrar o seguinte
    		double value_aux=delprox.getPlace();
    		delprox=u_x[0].findNext(value_aux); 
    		for(int i=1;i<u_x.length;i++)
    		{
    			if(delprox!=null && u_x[i].findNext(value_aux)!=null)
    				if(delprox.getPlace()>u_x[i].findNext(value_aux).getPlace())
    					delprox=u_x[i].findNext(value_aux);
    			if(delprox==null && u_x[i].findNext(value_aux)!=null)
    				delprox=u_x[i].findNext(value_aux);
    		}
    	}

    }
    
    public double process_Min()
    {
    	double ret=0.0;
    	
    	if(flag_error)
    	{
    		writeMsg("ERROR: Can't calculate minimize function with out precalculated N's");
    	}
    	
    	
    	// econtrar o 1º de todos os n's
    	String funcN[]=new String[result_n.length];
    	Delimiter delN[]=new Delimiter[result_n.length];
    	
    	for(int i=0;i<result_n.length;i++)
    	{
    		for(Delimiter d: result_n[i])
    		{
    			if(d.getPlace()==start)
    			{
    				delN[i]=d;
    				//	System.err.println("encontrou ");
    			}
    		}
    		if(delN[i]==null)
    		{
    			writeMsg("Warning: N"+(i+1)+" function definition not found at "+BASE.orderTo+"="+start);
    			funcN[i]="(0)";
    		}
    		else
    			funcN[i]=delN[i].getFunc().getFunction();
    	}

    	
    	//Econtrar o 1º de todos os U's
    	String funcU[]=new String[u_x.length];
    	Delimiter delU[]= new Delimiter[u_x.length];
    	
    	for(int i=0;i<u_x.length;i++)
    	{
    		delU[i]=u_x[i].findPrevious(start);
    		if(delU[i]==null)
    		{
    			writeMsg("Warning: U"+(i+1)+" function definition not found at "+BASE.orderTo+"="+start);
    			funcU[i]="(0)";
    		}
    		else
    			funcU[i]=delU[i].getFunc().getFunction();
    	}
    	
    	String faux=minPart_2;
    	//replace N's
    	for(int i=0;i<funcN.length;i++)
    	{
    		faux=faux.replaceAll("N"+(i+1),"("+funcN[i]+")");
    	}
    	//replace U's
    	for(int i=0;i<funcU.length;i++)
    	{
    		faux=faux.replaceAll("U"+(i+1),"("+funcU[i]+")");
    	}
    	

    	//writeMsg("\n"+faux);
    	
    	//writeMsg("MIN:"+fauxN1+"\nN2:"+fauxN2+"\nN1(0)="+N1_0_aux+"\nN2(0):"+N2_0_aux);
    	String cmd="S = dsolve('DMIN = "+faux+"','"+BASE.orderTo+"')\n";
    	//writeMsg("\n"+cmd);
		MatlinkDirectWS matWS=new MatlinkDirectWS();
		String retmatlab=matWS.sendCmd(cmd,BASE.matServerIP,BASE.matPort,BASE.matJWS);
		Function func_min_2=MatParse.matParse(retmatlab,"S",BASE.orderTo);
		if(func_min_2==null)
		{
			writeMsg("Error processing diff of Minimise function at "+start+", replaced by \"0\"");
			func_min_2=new Function("0",BASE.orderTo);
		}
		
    	//-------------------------vou aqui--------------------		
		
		//writeMsg("\n funtion :"+func_min_2.getFunction());
		
		double pre=start;
		
		InputChart auxChart=new InputChart();
		for(Delimiter d:result_n[0])
			auxChart.addDelimiter(d);
		
		Delimiter delNext=auxChart.findNext(start);
    	
    	while(delNext!=null && delNext.place<end)
    	{
    		//writeMsg("-"+pre+"-----------------entra-----------------"+delNext.getPlace()+"--");
    		//writeMsg("func:"+func_min_2.function);
    		// ----------------------calcular com o que há
    		double ant=func_min_2.f(pre);
    		double post=func_min_2.f(delNext.getPlace());
    		ret+=(post-ant);
    		//writeMsg(":"+ret);
    		// --------------------- retirar N1 nesta posição
    		funcN=new String[result_n.length];
        	delN=new Delimiter[result_n.length];
        	
        	for(int i=0;i<result_n.length;i++)
        	{
        		for(Delimiter d: result_n[i])
        		{
        			if(d.getPlace()==delNext.getPlace())
        			{
        				delN[i]=d;
        				//	System.err.println("encontrou ");
        			}
        		}
        		if(delN[i]==null)
        		{
        			writeMsg("Warning: N"+(i+1)+" function definition not found at "+BASE.orderTo+"="+start);
        			funcN[i]="(0)";
        		}
        		else
        			funcN[i]=delN[i].getFunc().getFunction();
        	}


        	
        	//  --- retirar u(x) neste ponto 
        	funcU=new String[u_x.length];
        	delU= new Delimiter[u_x.length];
        	
        	for(int i=0;i<u_x.length;i++)
        	{
        		delU[i]=u_x[i].findPreviousEQ(delNext.getPlace());
        		if(delU[i]==null)
        		{
        			writeMsg("Warning: U"+(i+1)+" function definition not found at "+BASE.orderTo+"="+start);
        			funcU[i]="(0)";
        		}
        		else
        			funcU[i]=delU[i].getFunc().getFunction();
        	}
       		
       		// repreparar a função para o matlab
       		faux=minPart_2;
        	//replace N's
        	for(int i=0;i<funcN.length;i++)
        	{
        		faux=faux.replaceAll("N"+(i+1),"("+funcN[i]+")");
        	}
        	//replace U's
        	for(int i=0;i<funcU.length;i++)
        	{
        		faux=faux.replaceAll("U"+(i+1),"("+funcU[i]+")");
        	}
        
        	// resolver no matlab o integal e por em finc_min_2
        	cmd="S = dsolve('DMIN = "+faux+"','"+BASE.orderTo+"')\n";
        	//writeMsg("\n"+cmd);
    		matWS=new MatlinkDirectWS();
    		retmatlab=matWS.sendCmd(cmd,BASE.matServerIP,BASE.matPort,BASE.matJWS);
    		func_min_2=MatParse.matParse(retmatlab,"S",BASE.orderTo);
    		if(func_min_2==null)
    		{
    			writeMsg("Error processing diff of Minimise function at "+delNext.getPlace()+", replaced by \"0\"");
    			func_min_2=new Function("0",BASE.orderTo);
    		}
        	    		
    		//ok
    		pre=delNext.getPlace();
        	delNext=auxChart.findNext(pre);
    	}
		
    	// calcular com o que sobra 
    	//writeMsg("-"+pre+"-----------------saida-----------------"+end+"--");
		//writeMsg("func:"+func_min_2.function);
		double ant=func_min_2.f(pre);
		double post=func_min_2.f(end);
		ret+=(post-ant);
    	
		//writeMsg("################################### = "+ret);

		//------------------------processar a 1º parte e somar
		faux=minPart_1;
    	//replace N's
    	for(int i=0;i<funcN.length;i++)
    	{
    		faux=faux.replaceAll("N"+(i+1),"("+funcN[i]+")");
    	}
    	//replace U's
    	for(int i=0;i<funcU.length;i++)
    	{
    		faux=faux.replaceAll("U"+(i+1),"("+funcU[i]+")");
    	}
    	
    	Function funcaux=new Function(faux,BASE.orderTo);
    	
    	double value1=funcaux.f(end); // se houver x é o maximo 
    	if(funcaux.parceOk())
    		ret+=value1;
    	else
    		writeMsg("Error parsing minimise function 1º part, replaced by \"0\"\n");
   
		return ret;
    }
	
    
    private static final int MAX_TEXT_MSG_LENGHT = 500000;
    public static final Color DEFAULT = Color.BLACK;
    public void writeMsg(String msg)
    {
    	System.err.println(msg);
    	
    	if(msgTextArea!=null)
    	{
    		 SimpleAttributeSet attr = new SimpleAttributeSet();
    	        attr.addAttribute(ColorConstants.Foreground, DEFAULT);
    	        Document doc = msgTextArea.getDocument();
    	        try {
    	            doc.insertString(doc.getLength(), msg+"\n", attr);
    	            int docLength = doc.getLength();
    	            if (docLength > MAX_TEXT_MSG_LENGHT)
    	                doc.remove(0, docLength - MAX_TEXT_MSG_LENGHT);
    	            //System.err.println("Doc. lenght " + doc.getLength());            
    	            msgTextArea.setCaretPosition(doc.getLength());
    	        }
    	        catch (Exception e) {}
    		
    	}
    }
}
