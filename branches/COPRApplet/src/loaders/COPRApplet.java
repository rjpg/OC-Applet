package loaders;


import functions.BASE;
import functions.Vector2d;
import inputchart.InputChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;



import modeldef.ControlPanel;
import modeldef.Session;
import modeldef.matlabtest;

public class COPRApplet 
extends java.applet.Applet
	{
		  public void init() {
			  /*
			  InputChart chart=new InputChart();
			  
			  InputChart out1=new InputChart();
			  out1.setView(new Vector2d(0.25,0));
			  out1.setInput(false);
			  InputChart out2=new InputChart();
			  out2.setView(new Vector2d(0.25,0));
			  out2.setInput(false);
			  
			  
			
			  
			  JPanel panel=new JPanel();
			  panel.setLayout(new GridLayout(1,2));
			  JPanel panel_model_out=new JPanel();
			  panel_model_out.setLayout(new GridLayout(3,1));
			    
			  JPanel input_chart=new JPanel();
			  input_chart.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
						"Input Chart -U(x)"));
			  input_chart.setLayout(new BorderLayout());
			  input_chart.add(chart,BorderLayout.CENTER);
			  panel.add(input_chart);
			  
			  InputChart[] in={chart};
			  InputChart[] out={out1,out2};
			  
			  ControlPanel cp=new ControlPanel(in,out);
			  panel_model_out.add(cp);
			  
			  JPanel output_chart_N1=new JPanel();
			  output_chart_N1.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
				"N1(x)"));
			  output_chart_N1.setLayout(new BorderLayout());
			  output_chart_N1.add(out1,BorderLayout.CENTER);
			  panel_model_out.add(output_chart_N1);
			  
			  JPanel output_chart_N2=new JPanel();
			  output_chart_N2.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
				"N2(x)"));
			  output_chart_N2.setLayout(new BorderLayout());
			  output_chart_N2.add(out2,BorderLayout.CENTER);
			  panel_model_out.add(output_chart_N2);

			  //panel_model_out.add(outGeral);
			  panel.add(panel_model_out);
			  panel.setVisible(true);
			  */
			  
			  
			  //BASE.tabbed.addTab("session 1",panel);
			  
			  if(!BASE.active)
			  {
				  BASE.active=true;
				  new Session();
			  }
			  else
				  BASE.tabbed.getParent().remove(BASE.tabbed);
			  this.setLayout(new BorderLayout());
			  this.add(BASE.tabbed,BorderLayout.CENTER);
			  this.setSize(1024,500);
			  this.setVisible(true);
			  }
	}


