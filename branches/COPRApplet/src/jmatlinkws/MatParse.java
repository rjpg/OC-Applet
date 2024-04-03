package jmatlinkws;

import functions.Function;

public class MatParse {
	static public Function matParse(String matResult,String func,String var)
	{
		//if(matResult.contains(func)) return null;
		try{
			String aux=matResult.split(func+" =")[1].split("\n")[2];
			//System.out.println("depois da função:"+aux);
			aux=aux.replace("+C1","");
			Function ret=new Function(aux,var);
			return ret;	
		}
		catch (Exception e) {
			return null;
		}
		
	}
}
