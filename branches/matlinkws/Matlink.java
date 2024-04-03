import jmatlink.*;


public class Matlink {

	IOMatLab mt=null;
	int i=0;
	public String runMatCmd(String cmd)
    {
		
		mt = Singleton.getInstance().getValue();
		
		/*mt=new IOMatLab();
		/*
		System.out.println(i+" chamadas");
		if (mt==null)
		{
			System.out.println("é null ");
			
			
		}
		*/
		
		//mt.openMatLab();
		//mt.openMatLab();
		String result=mt.runMatLabCmd(cmd);
		//mt.closeMatLab();
		return result;   
		
    }
	
	
	public static void main(String[] args) {	 
		 
		
		
		Matlink teste=new Matlink();
		 
		 String result;
		 result=teste.runMatCmd("S = dsolve('Df = 3*f+4*g', 'Dg = -4*f+3*g','f(0)=1','g(0)=2','y')");
		 System.out.println(result);
		 
		 //MatParse.matParse(result,"f","x");
		 //System.out.println("F="+f);
		 
		 //teste.closeMatLab();
	 }

}

