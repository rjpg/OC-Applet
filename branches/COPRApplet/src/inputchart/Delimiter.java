package inputchart;

import org.nfunk.jep.JEP;

import functions.Function;


public class Delimiter {
	public double place;
	
	public Function func=null; 
	public Function max=null;
	public Function min=null;
	
	
	
	
	public Delimiter()
	{
		//function="1";
		place=0.;
		
		func =new Function("0","x"); 
		
		max =new Function("","x");
		min =new Function("","x");
		

		// add the interface components
		
		
		// Set up the parser (more initialization in parseExpression()) 
		
	}
	
	
	
	public Delimiter(double p)
	{
		this();
		place=p;
	}

	public double getPlace() {
		return place;
	}

	public void setPlace(double place) {
		this.place = place;
	}


	//----------------------------------Function
	protected String getFunction() {
		
		return func.getFunction();
	}
	protected void setFunction(String function,String ord) {	
		func.setFunction(function,ord);
	}
	
	public double getValue(double val) {
		
		return func.f(val);
	}
	
	public String errorEval()
	{
		return func.errorEval();
	}
	
	public boolean parceOk()
	{
		return func.parceOk();	
	}

	//----------------------------------Max Function
	protected String getMaxFunction() {
		
		return max.getFunction();
	}
	protected void setMaxFunction(String function,String ord) {	
		max.setFunction(function,ord);
	}
	
	public double getMaxValue(double val) {
		return max.f(val);
	}
	
	public String errorEvalMax()
	{
		return max.errorEval();
	}
	
	public boolean parceOkMax()
	{
		return max.parceOk();	
	}


	//----------------------------------Max Function
	protected String getMinFunction() {
		
		return min.getFunction();
	}
	protected void setMinFunction(String function,String ord) {	
		min.setFunction(function,ord);
	}
	
	public double getMinValue(double val) {
		return min.f(val);
	}
	
	public String errorEvalMin()
	{
		return min.errorEval();
	}
	
	public boolean parceOkMin()
	{
		return min.parceOk();	
	}

	
	
	public static void main(String args[]) {
//		 standard initialisation
		JEP j = new JEP();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);

//		 switch assignment facilities on
		j.setAllowAssignment(true);

//		 parse assignment equations
		j.parseExpression("2+x");
//		 evaluate it - no need to save the value returned
		j.getValueAsObject();
//		 parse a second equation
		j.parseExpression("x");
		j.getValueAsObject();

//		 an equation involving above variables
		j.parseExpression("x");
		Object val3 = j.getValueAsObject();
		System.out.println("Value is " + val3);
		
		/*
		XJep j = new XJep();
		j.setAllowAssignment(true);
	
		j.restartParser("x=1; y=2; z=x+y;");
		try {
		  Node node;
		  while((node = (Node) j.continueParsing()) != null) {
		    Node simp = (Node) j.simplify(j.preprocess((org.nfunk.jep.Node) node));
		    Object value = j.evaluate((org.nfunk.jep.Node) simp);
		    j.println((org.nfunk.jep.Node) simp);
		    System.out.println(value.toString());
		  }
		} catch(Exception e) {}
		*/
	}



	public Function getFunc() {
		return func;
	}



	public Function getMax() {
		return max;
	}



	public Function getMin() {
		return min;
	}



	public void setFunc(Function func) {
		this.func = func;
	}



	public void setMax(Function max) {
		this.max = max;
	}



	public void setMin(Function min) {
		this.min = min;
	}
}
