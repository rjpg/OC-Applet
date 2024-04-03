package modeldef;

import functions.BASE;
import functions.Function;
import inputchart.Delimiter;
import inputchart.InputChart;
import inputchart.inputChartListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.naming.LimitExceededException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;

import jmatlinkws.MatParse;
import jmatlinkws.MatlinkDirectWS;
import javax.swing.JTextArea;
import javax.swing.JDesktopPane;
import javax.swing.JSplitPane;

public class matlabtest extends JPanel implements inputChartListener
{
	
	private JTextField ftext = null;
	private JTextField gtext = null;
	private JButton jButton = null;
	private JLabel fLabel = null;
	private JLabel gLabel = null;
	private JLabel jLabel = null;
	private JLabel sist = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel minim = null;
	private JLabel subject = null;
	private JLabel mais = null;
	private JTextField minPart_1 = null;
	private JLabel diff = null;
	private JTextField minPart_2 = null;
	private JLabel ut = null;
	private JLabel ti = null;
	private JLabel tf = null;
	private JLabel result = null;
	
	public JTextField N_JText[];
	public JTextField N0_JText[];
	
	public InputChart input[];
	public InputChart dinamics[];
	public String[] restritions=new String[1]; 
	
	private double timemin=0.;
	private double timemax=1.;

	private JTextArea ErrorTextArea = null;
	/**
	 * This method initializes 
	 * 
	 */
	public matlabtest() {
		super();
		initialize();
	}
	
	private String N_0[];
	private String N[];
	private String part1;
	private String part2;
	private JScrollPane jScrollPane = null;
	 

	public matlabtest(InputChart in[],InputChart din[],JTextArea error,String N_0a[],String Na[],String part1a,String part2a,String rest[]) {
		super();
		ErrorTextArea = error;
		N_0=N_0a;
		System.err.println("lenght "+Na.length);
		N=Na;
		part1=part1a;
		part2=part2a;
		restritions=rest;
		dinamics=din;
		input=in;
		
		initialize();
		
		for(InputChart i:input)
		{
			i.addInputChartListener(this);
		}
		
		
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		JPanel panel=new JPanel();
		JScrollPane js=getJScrollPane();
		js.setViewportView(panel);
		js.createHorizontalScrollBar();
		js.createVerticalScrollBar();
		ScrollPaneLayout scrLay=new ScrollPaneLayout();
		js.setLayout(scrLay);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//panel.setSize(400,100);
		//panel.setMinimumSize(new Dimension(400,100));
		panel.setPreferredSize(new Dimension(400,200));
		
		this.setLayout(new BorderLayout());
		this.add(js,BorderLayout.CENTER);
		panel.setLayout(null);
		
		this.setMinimumSize(new Dimension(100,100));
		this.setSize(new java.awt.Dimension(570,99));
		jLabel = new JLabel();
        jLabel.setBounds(new java.awt.Rectangle(164,3,155,18));
        jLabel.setText("--- Mathematical Model ---");
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		JPanel minmax=new JPanel();
		
		minim = new JLabel();
		minim.setBounds(new java.awt.Rectangle(3,23,57,16));
		minim.setText("Minimise ");
		//getMinPart_1();
		tf = new JLabel();
		tf.setBounds(new java.awt.Rectangle(200,3,157,16));
		tf.setText("1.0");
		ti = new JLabel();
		ti.setBounds(new java.awt.Rectangle(193,40,107,15));
		ti.setText("0.0");
		
		
		Image image1 = new ImageIcon(getClass().getClassLoader().getResource("images/diff.png")).getImage();
		ImageIcon icon=new ImageIcon(image1);
		diff = new JLabel(icon);
		diff.setBounds(new java.awt.Rectangle(185,5,14,44));
		diff.setText("");

		mais = new JLabel();
		mais.setBounds(new java.awt.Rectangle(181,23,11,16));
		mais.setText("+");
		//
		getMinPart_2();
		result = new JLabel();
		result.setBounds(new java.awt.Rectangle(304,22,115,16));
		result.setText("dx =");
		
		minmax.setLayout(null);
		minmax.add(minim);
		minmax.add(getMinPart_1());
		minmax.add(ti);
		minmax.add(tf);
		minmax.add(diff);
		minmax.add(mais);
		minmax.add(getMinPart_2());
		minmax.add(result);
		minmax.setBounds(10,20,400,55);
		//--------------------------------------------------
		subject = new JLabel();
		subject.setBounds(new java.awt.Rectangle(12,80,64,16));
		subject.setText("subject to");
		int startNy=90;  // offset for the N layout
		//---------------------------------------------
		panel.add(jLabel);
		panel.add(minmax);
		panel.add(subject);
		
		
		// Dynamics layout
		N_JText=new JTextField[dinamics.length];
	    N0_JText=new JTextField[dinamics.length];
		for (int i=0;i<dinamics.length;i++)
		{
			JPanel Npanel=new JPanel();
        	Npanel.setLayout(null);
        	JLabel Nlabel=new JLabel("N'"+(i+1)+"("+BASE.orderTo+")=",JLabel.LEFT);
        	Nlabel.setBounds(0,0,40,20);
        	
        	N_JText[i]=new JTextField();
        	N_JText[i].setBounds(45,0,150,20);
        	//N_JText[i].setSize(100,20);
        	//N_JText[i].setMinimumSize(new Dimension(100,20));
        	//N_JText[i].setPreferredSize(new Dimension(200,20));
        	System.err.println("lenght ciclo "+N.length);
        	if(N.length>i)
        		N_JText[i].setText(N[i]);
        	else
        		N_JText[i].setText("wedfwe");
        	Npanel.add(Nlabel);
        	Npanel.add(N_JText[i]);
        	//panel.setSize(400,20);
    		//panel.setMinimumSize(new Dimension(400,20));
    		Npanel.setPreferredSize(new Dimension(350,20));
        	Npanel.setBounds(10,startNy+10,250,20);
        	panel.add(Npanel);
        	startNy+=25;
		}
		
		for (int i=0;i<dinamics.length;i++)
		{
			JPanel Npanel=new JPanel();
        	Npanel.setLayout(null);
        	JLabel Nlabel=new JLabel("N"+(i+1)+"(0)=",JLabel.LEFT);
        	Nlabel.setBounds(0,0,40,20);
        	N0_JText[i]=new JTextField();
        	N0_JText[i].setBounds(45,0,40,20);
        	
        	//N_JText[i].setSize(100,20);
        	//N_JText[i].setMinimumSize(new Dimension(100,20));
        	//N0_JText[i].setPreferredSize(new Dimension(50,20));
        	System.err.println("lenght ciclo "+N.length);
        	if(N_0.length>i)
        		N0_JText[i].setText(N_0[i]);
        	else
        		N0_JText[i].setText("wedfwe");
        	Npanel.add(Nlabel);
        	Npanel.add(N0_JText[i]);
        	//panel.setSize(400,20);
    		//panel.setMinimumSize(new Dimension(400,20));
    		//Npanel.setPreferredSize(new Dimension(80,20));
        	Npanel.setBounds(10,startNy+10,100,20);
        	panel.add(Npanel);
        	startNy+=25;
		}
		
		JButton button=getJButton();
		button.setBounds(150,startNy+20,100,20);
		panel.add(button);
		panel.setPreferredSize(new Dimension(400,startNy+50));
		

	}

	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new java.awt.Rectangle(375,63,102,20));
			jButton.setText("Solve");
		}
		jButton.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				
				ErrorTextArea.setText("");
				
				String[] n_0=getN_0();
				String[] n=getN();
				
				Process proc=new Process(input,n,n_0,minPart_1.getText(),minPart_2.getText(),input[0].getTimemin(),input[0].getTimemax(),ErrorTextArea);
				proc.process_N();
				if(proc.flag_error)
				{
					proc.writeMsg("Solving error");
					return;
				}
				
				for(int i=0;i<proc.result_n.length;i++)
				{
					dinamics[i].cleanDelimiters();
					for(Delimiter d:proc.result_n[i])
					{
						dinamics[i].addDelimiter(d);
					}
				}
				
				
				double ret=proc.process_Min();
				
				result.setText("= "+ret);
				/*
				N1.cleanDelimiters();
				for(Delimiter d: proc.result_n1)
				{
					//System.err.println("delN1:"+d.getFunc().getFunction());
					N1.addDelimiter(d);
				}
				
				N2.cleanDelimiters();
				for(Delimiter d: proc.result_n2)
				{
					//System.err.println("delN2:"+d.getFunc().getFunction());
					N2.addDelimiter(d);
				}
				
				*/
				
				
				/*
				//S = dsolve('Df = 3*f+4*g', 'Dg = -4*f+3*g','f(0)=0','g(0)=0','x')\n g = S.g
				String cmd="S = dsolve('DN1 = "+f+"', 'DN2 = "+g+"','N1(0)=0','N2(0)=0','x')\n N1 = S.N1\n N2 = S.N2\n";
				MatlinkDirectWS matWS=new MatlinkDirectWS();
											//Df = 3*f+4*g', 'Dg = -4*f+3*g
				String ret=matWS.sendCmd(cmd,"192.168.106.186",8080);
				
				System.out.println(ret);
				
				Function func_f=MatParse.matParse(ret,"N1","x");
				Function func_g=MatParse.matParse(ret,"N2","x");
				
				String outtext="";
				if(func_f==null)
					outtext=outtext+"erro no processamento de N1\n";
				else
				{
					outtext=outtext+"DN1(x)="+func_f.getFunction()+"\n";
					Delimiter deli=new Delimiter(0);
					deli.setFunc(func_f);
					N1.cleanDelimiters();
					N1.addDelimiter(deli);
				}
				
				output_f.setText(outtext);
				outtext="";
				
				if(func_g==null)
					outtext=outtext+"erro no processamento de N2\n";
				else
				{
					outtext=outtext+"DN2(x)="+func_g.getFunction()+"\n";
					Delimiter deli=new Delimiter(0);
					deli.setFunc(func_g);
					N2.cleanDelimiters();
					N2.addDelimiter(deli);
				}
				output_g.setText(outtext);
				*/
			}
		});
		return jButton;
	}


	/**
	 * This method initializes minPart_1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMinPart_1() {
		if (minPart_1 == null) {
			minPart_1 = new JTextField();
			minPart_1.setBounds(new java.awt.Rectangle(58,22,124,20));
			minPart_1.setText(part1);
		}
		return minPart_1;
	}

	/**
	 * This method initializes minPart_2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMinPart_2() {
		if (minPart_2 == null) {
			minPart_2 = new JTextField();
			minPart_2.setBounds(new java.awt.Rectangle(200,21,96,20));
			minPart_2.setText(part2);
		}
		return minPart_2;
	}

	public void change(InputChart chart) {
		double i=((double)((int)(chart.getTimemin()*1000)))/1000;
		double f=((double)((int)(chart.getTimemax()*1000)))/1000;
		tf.setText(f+"");
		ti.setText(i+"");
		
		//por todos os graficos iguais no intervalo
		
		
		
		//ut.setText("U(x)   ["+i+" , "+f+"]"); //isto está mal não tem nada a ver
		//jLabel1.setText("N1("+i+")=");
		//jLabel2.setText("N2("+i+")=");
		
	}
	
	public String [] getN_0()
	{
		String[] n_0=new String[dinamics.length];
		for(int i=0;i<n_0.length;i++)
		{
			n_0[i]=N0_JText[i].getText();
		}
		return n_0;
	}
	
	public String [] getN()
	{
		String[] n=new String[dinamics.length];
		for(int i=0;i<n.length;i++)
		{
			n[i]=N_JText[i].getText();
		}
		return n;
	}
	
	public String getPart1()
	{
		return minPart_1.getText();
	}
	
	public String getPart2()
	{
		return minPart_2.getText();
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			//jScrollPane.setPreferredSize(new java.awt.Dimension(100,100));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes ErrorTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	/*private JScrollPane getErrorTextArea() {
		if (ErrorTextArea == null) {
			 jScrollPane = new JScrollPane();
		        
		        
		        //msgTextArea.setFont(new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 12));
		        
		        
			ErrorTextArea = new JTextArea();
			ErrorTextArea.setBounds(new java.awt.Rectangle(249,85,230,108));
			ErrorTextArea.setText("Cancer Chemotherapy example model \nr1=1\nr2=2\nq1=3\nq2=4\na1=5\na2=6");
			ErrorTextArea.setEditable(false);
			jScrollPane.setViewportView(ErrorTextArea);
			jScrollPane.setBounds(new java.awt.Rectangle(249,85,230,108));
		}
		return jScrollPane;
	}*/
	
	
}  //  @jve:decl-index=0:visual-constraint="147,46"
