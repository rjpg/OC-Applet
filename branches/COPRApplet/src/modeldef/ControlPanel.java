package modeldef;

import functions.BASE;
import functions.Function;
import inputchart.Delimiter;
import inputchart.InputChart;
import inputchart.inputChartListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants.ColorConstants;


public class ControlPanel extends JPanel{
	
	private JSplitPane splitpane;
	private JPanel panelLeft;
	private JPanel panelRight;
	
	public matlabtest modelPanel;
	
	private JScrollPane jScrollPane=null;
	private JTextArea ErrorTextArea = null;
	
	private InputChart input[];
	private InputChart dinamics[];
	
	private String N_0[];
	private String N[];
	private String part1;
	private String part2;
	private String error_text_init;
	private String rest[];
	
	private Session session;
	
	public ControlPanel(InputChart in[],InputChart dim[],String N_0a[],String Na[],String part1a,String part2a,String resta[], String error,Session sessiona)
	{
		super();
		input=in;
		dinamics=dim;
		N_0=N_0a;
		N=Na;
		part1=part1a;
		part2=part2a;
		session=sessiona;
		error_text_init=error;
		rest=resta;
		//this.setResizable(true);
        this.setPreferredSize(new Dimension(200,100));
		initialaze();
	}
	
	public void initialaze()
	{
		this.setLayout(new BorderLayout());
		this.add(getSplitpane(),BorderLayout.CENTER);
		splitpane.setVisible(true);
	}
	
	public JSplitPane getSplitpane()
	{
		if(splitpane==null)
		{
			JPanel left=getPanelLeft();
			JPanel right=getPanelRight();
			
			splitpane=new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, 
            false,  right ,left);
			splitpane.setOneTouchExpandable(true);
			splitpane.setDividerSize(7);
			splitpane.setDividerLocation(350);
			splitpane.setAutoscrolls(false);
		}
		return splitpane; 
	}
	
	public JPanel getPanelLeft()
	{
		if(panelLeft==null)
		{
			panelLeft=new JPanel();
			panelLeft.setLayout(new BorderLayout());
			panelLeft.add(getErrorTextArea(),BorderLayout.CENTER);
			
			JButton resetButton=new JButton("settings");
			resetButton.setText("Settings");
			resetButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	
	            	
	            	Settings.showSettingsDialog(ControlPanel.this);
	            }
	            });
			
			JButton sessionButton=new JButton("New Session");
			sessionButton.setText("New Session");
			sessionButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	//InputChart in[]={new InputChart()
	            	//				,new InputChart()};
	            	//InputChart dim[]={new InputChart(),
	            	//				new InputChart(),
	            	//				new InputChart(),
	            	//				new InputChart()};
	            	//String N_0a[]={"0","0","0","0"};
	            	//String Na[]={"-5*N1+2*(1-U)+6*N2","5*N1-6*N2"};
	            	//String part1a="y";
	            	//String part2a="x"; 
	            	//String error="error text";
	            	
	            	InputChart in[]=new InputChart[input.length];
	            	for(int i=0;i<input.length;i++)
	            	{
	            		in[i]=new InputChart();
	            		for(Delimiter d:input[i].delimiters)
	            		{
	            			Delimiter del=new Delimiter();
	            			del.setFunc(new Function(d.getFunc().getFunction(),BASE.orderTo));
	            			del.setMax(new Function(d.getMax().getFunction(),BASE.orderTo));
	            			del.setMin(new Function(d.getMin().getFunction(),BASE.orderTo));
	            			del.setPlace(d.getPlace());
	            			in[i].addDelimiter(del);
	            		}
	            	}
	            	
	            	InputChart dim[]=new InputChart[dinamics.length];
	            	for(int i=0;i<dinamics.length;i++)
	            	{
	            		dim[i]=new InputChart();
	            		for(Delimiter d:dinamics[i].delimiters)
	            		{
	            			Delimiter del=new Delimiter();
	            			del.setFunc(new Function(d.getFunc().getFunction(),BASE.orderTo));
	            			del.setMax(new Function(d.getMax().getFunction(),BASE.orderTo));
	            			del.setMin(new Function(d.getMin().getFunction(),BASE.orderTo));
	            			del.setPlace(d.getPlace());
	            			dim[i].addDelimiter(del);
	            		}
	            	}
	            	String N_0a[]=modelPanel.getN_0();
	            	String Na[]=modelPanel.getN();
	            	
	                new Session(in,dim,N_0a,Na,modelPanel.getPart1(),modelPanel.getPart2(),modelPanel.restritions,ErrorTextArea.getText());
	            }
	        });
			
			JPanel panelaux=new JPanel();
			panelaux.setLayout(new GridLayout(1,2));
			panelaux.add(sessionButton);
			panelaux.add(resetButton);
			
			panelLeft.add(panelaux,BorderLayout.SOUTH);
			
		}
		return panelLeft;
	}
	
	public void reset(int inputN,int dyn,int resta)
	{
		InputChart in[]=new InputChart[inputN];
		for (int i=0;i<in.length;i++)
		{
			in[i]=new InputChart();
		}
		for(int i=0;i<input.length&&i<in.length;i++)
    	{
    		in[i]=new InputChart();
    		for(Delimiter d:input[i].delimiters)
    		{
    			Delimiter del=new Delimiter();
    			del.setFunc(new Function(d.getFunc().getFunction(),BASE.orderTo));
    			del.setMax(new Function(d.getMax().getFunction(),BASE.orderTo));
    			del.setMin(new Function(d.getMin().getFunction(),BASE.orderTo));
    			del.setPlace(d.getPlace());
    			in[i].addDelimiter(del);
    		}
    	}
		
		InputChart dim[]=new InputChart[dyn];
		for (int i=0;i<dim.length;i++)
		{
			dim[i]=new InputChart();
		}
		for(int i=0;i<dinamics.length&&i<dim.length;i++)
    	{
    		dim[i]=new InputChart();
    		for(Delimiter d:dinamics[i].delimiters)
    		{
    			Delimiter del=new Delimiter();
    			del.setFunc(new Function(d.getFunc().getFunction(),BASE.orderTo));
    			del.setMax(new Function(d.getMax().getFunction(),BASE.orderTo));
    			del.setMin(new Function(d.getMin().getFunction(),BASE.orderTo));
    			del.setPlace(d.getPlace());
    			dim[i].addDelimiter(del);
    		}
    	}
	
		String N_0a[]=new String[dyn];
		for(int i=0;i<N_0a.length;i++)
		{
			if(i<modelPanel.getN_0().length)
				N_0a[i]=modelPanel.getN_0()[i];
			else
				N_0a[i]="0";
		}
		
    	String Na[]=new String[dyn];
    	for(int i=0;i<Na.length;i++)
    	{
    		if(i<modelPanel.getN().length)
    			Na[i]=modelPanel.getN()[i];
    		else
    			Na[i]="0";
    	}
    	
    	String rest[];
    	if(resta<=0)
    		rest=null;
    	else
    	{
    		rest=new String[resta];
    		for(int i=0;i<rest.length;i++)
    		{
    		
    			if(modelPanel.restritions!=null && i<modelPanel.restritions.length)
    				rest[i]=modelPanel.restritions[i];
    			else
    				rest[i]="0";
    		}
    	}
		String part1a=modelPanel.getPart1();
		String part2a=modelPanel.getPart2(); 
		session.reset(in,dim,N_0a,Na,part1a,part2a,rest,ErrorTextArea.getText());
		session.repaint();
		session.doLayout();
		session.repaint();
		BASE.tabbed.repaint();
	}
	
	public JPanel getPanelRight()
	{
		if(panelRight==null)
		{
			panelRight=new JPanel();
			panelRight.setLayout(new BorderLayout());
			modelPanel=new matlabtest(input,dinamics,ErrorTextArea,N_0,N,part1,part2,rest);
			panelRight.add(modelPanel,BorderLayout.CENTER);
		
		}
		return panelRight;
	}
	
	private JScrollPane getErrorTextArea() {
		if (ErrorTextArea == null) {
			 jScrollPane = new JScrollPane();
		        
		        
		        //msgTextArea.setFont(new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 12));
		        
		        
			ErrorTextArea = new JTextArea();
			ErrorTextArea.setBounds(new java.awt.Rectangle(249,85,230,108));
			ErrorTextArea.setText(error_text_init);
			ErrorTextArea.setEditable(false);
			jScrollPane.setViewportView(ErrorTextArea);
			jScrollPane.setBounds(new java.awt.Rectangle(249,85,230,108));
		}
		return jScrollPane;
	}
	
	 private static final int MAX_TEXT_MSG_LENGHT = 500000;
	    public static final Color DEFAULT = Color.BLACK;
	    public void writeMsg(String msg)
	    {
	    	System.err.println(msg);
	    	
	    	if(ErrorTextArea!=null)
	    	{
	    		 SimpleAttributeSet attr = new SimpleAttributeSet();
	    	        attr.addAttribute(ColorConstants.Foreground, DEFAULT);
	    	        Document doc = ErrorTextArea.getDocument();
	    	        try {
	    	            doc.insertString(doc.getLength(), msg+"\n", attr);
	    	            int docLength = doc.getLength();
	    	            if (docLength > MAX_TEXT_MSG_LENGHT)
	    	                doc.remove(0, docLength - MAX_TEXT_MSG_LENGHT);
	    	            //System.err.println("Doc. lenght " + doc.getLength());            
	    	            ErrorTextArea.setCaretPosition(doc.getLength());
	    	        }
	    	        catch (Exception e) {}
	    		
	    	}
	    }
	
}
