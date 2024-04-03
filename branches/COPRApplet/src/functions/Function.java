package functions;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class Function{ //implements MonadicFunction{
	public String function="0";
	public String orderTo=BASE.orderTo;
	private JEP myParser;
	private Boolean parceok=true;
	//private NewtonRaphson root;
	
	public Function ()
	{
		myParser = new JEP();
		myParser.initFunTab(); // clear the contents of the function table
		myParser.addStandardFunctions();
		myParser.setTraverse(true);
		
		
		myParser.addVariable(orderTo, 0);
		setFunction(function,orderTo);
		
	}
	
	public Function (String func,String ord)
	{
		myParser = new JEP();
		myParser.initFunTab(); // clear the contents of the function table
		myParser.addStandardFunctions();
		myParser.setTraverse(true);
		
		function=func;
		orderTo=ord;
		myParser.addVariable(orderTo, 0);
		setFunction(function,orderTo);
	}
	
	public void setFunction(String function,String ord) {	
		myParser.initSymTab(); // clear the contents of the symbol table
		myParser.addStandardConstants();
		myParser.addComplex(); // among other things adds i to the symbol table
		myParser.addVariable(ord, 0.0);
		myParser.parseExpression(function);
		this.function = function;
		this.orderTo=ord;
	}
	
	public double findRoot(double near)
	{
		//function f=new function("cos(x)","x");
		/*double ret=0.0;
		
		try {
			ret=NewtonRaphson.newtonRaphson(near,this,this.diff());
		} catch (Exception e) {
			ret=java.lang.Double.NaN;
		}  
		return ret;*/
		return java.lang.Double.NaN;
	}

	public double findRoot(double xi,double x2)
	{
		//function f=new function("cos(x)","x");
		/*double ret=0.0;
		
		try {
			ret=Brent.brent(xi,x2,this);
		} catch (Exception e) {
			ret=java.lang.Double.NaN;
		}  
			
		
		return ret;*/
		return java.lang.Double.NaN;
	}

	
	public double f(double val) {
		//		System.out.println("fazendo evaluação");
		Object result;
		//String errorInfo;
		parceok=true;
		// Get the value
		myParser.addVariable(this.orderTo, val);
		result = myParser.getValueAsObject();
		
		// Is the result ok?
		if (result!=null)
			 return myParser.getValue();
		else
			parceok=false;
		
		// Get the error information
		//if ((errorInfo = myParser.getErrorInfo()) != null) {
			//System.err.println(errorInfo);
		//}
		
		
		return 0.0;
	}
	
	public String errorEval()
	{
		return myParser.getErrorInfo();
	}
	
	public boolean parceOk()
	{
		return parceok;	
	}
	
	public Function diff()
	{
		Function ret=null;
		DJep j = new DJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		j.addStandardDiffRules();


		// Sets up standard rules for differentiating sin(x) etc.
		 try
		    {
		Node node = j.parse(this.getFunction());
        // differentiate wrt x
        Node diff = j.differentiate(node,this.getOrderTo());
        //j.println(diff);
        // simplify
        Node simp = j.simplify(diff);
        // print
        j.print(simp);
        //System.out.println("diff-"+node);
        ret=new Function(j.toString(simp),this.getOrderTo());
	
	  }
		 catch(ParseException e)
		 {
			 System.out.println("Error with parsing");
			 return null;
		 }
        
		 return ret;	
	}

	public String getOrderTo() {
		return orderTo;
	}

	public void setOrderTo(String orderTo) {
		this.orderTo = orderTo;
	}

	public String getFunction() {
		return function;
	}
	
	public Function plus(Function f)
	{
		return new Function(this.getFunction()+"+("+f.getFunction()+")",this.getOrderTo());
	}
	
	public Function minus(Function f)
	{
		return new Function(this.getFunction()+"-("+f.getFunction()+")",this.getOrderTo());
	}
	
	public Function mult(Function f)
	{
		return new Function(this.getFunction()+"*("+f.getFunction()+")",this.getOrderTo());
	}
	
	public Function div(Function f)
	{
		return new Function(this.getFunction()+"/("+f.getFunction()+")",this.getOrderTo());
	}

	public static void main(String args[]) {
		
		Function f1=new Function("sin(x)","x");
		Function f2=new Function("0.5","x");

		//	function
		System.err.println(java.lang.Double.NEGATIVE_INFINITY);
		System.out.println("f1="+f1.getFunction()+"\nf2="+f2.getFunction()
				+"\nf1'="+f1.diff().getFunction()
				+"\nf2'="+f2.diff().getFunction()
				+"\nf1-f2="+f1.minus(f2).getFunction()
				+"\ninterception near to "+0+" is x="+f1.minus(f2).findRoot(0));
		
	}
}
