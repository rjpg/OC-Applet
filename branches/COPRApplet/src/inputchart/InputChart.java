package inputchart;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;



import functions.Function;
import functions.Vector2d;



public class InputChart 
extends JPanel
implements  MouseListener,MouseWheelListener,MouseMotionListener,inputChartListener
{

	protected Vector<inputChartListener> Listeners=new Vector<inputChartListener>();
	
	public boolean editable=true;
	
	public Graphics2D g2=null;  

	private BufferedImage bi = (BufferedImage)createImage(this.getWidth(),this.getHeight());
	
	private Vector2d view=new Vector2d(0.5,0.0);
	
	private double scalex=310,scaley=310;

	//mouse
	private int mousex, mousey;

	
	public Vector<Vector2d> points=new Vector<Vector2d>();
	public Vector<Delimiter> delimiters=new Vector<Delimiter>();
	
	
	public Delimiter delimiterSelect=null;
	public Delimiter delimiterMouseOver=null;
	
	public TextField func;//=new TextField();
	public Label funclabel=new Label("Func:");//=new TextField();
	
	public TextField max;//=new TextField();
	public Label maxlabel=new Label("Max:");//=new TextField();
	
	public TextField min;//=new TextField();
	public Label minlabel=new Label("Min:");//=new TextField();
	
	public double processMin=0.;
	public double processMax=1.;
	public int processMouseOver=0;
	
	
	public InputChart()
	{
		super();
		func=new TextField();
		func.addTextListener(
				new TextListener() {
				

					public void textValueChanged(TextEvent arg0) {
						setSelectedDelimiterFunction(func.getText());
						
					}
				}
			);
		func.addKeyListener(
			new KeyListener(){
				public void keyTyped(KeyEvent arg0) {
					System.out.println("keycode:"+arg0.getKeyChar());
					if(arg0.getKeyChar()=='\n')
						InputChart.this.repaint();
					
				}

				public void keyPressed(KeyEvent arg0) {
				
				}

				public void keyReleased(KeyEvent arg0) {
				
				}
			}
		);
		func.removeMouseWheelListener(this);
		
		max=new TextField();
		max.addTextListener(
				new TextListener() {
				

					public void textValueChanged(TextEvent arg0) {
						setSelectedDelimiterMaxFunction(max.getText());
						
					}
				}
			);
		max.addKeyListener(
				new KeyListener(){
					public void keyTyped(KeyEvent arg0) {
						System.out.println("keycode:"+arg0.getKeyChar());
						if(arg0.getKeyChar()=='\n')
							InputChart.this.repaint();
						
					}

					public void keyPressed(KeyEvent arg0) {
					
					}

					public void keyReleased(KeyEvent arg0) {
					
					}
				}
			);
		max.removeMouseWheelListener(this);
		
		min=new TextField();
		min.addTextListener(
				new TextListener() {
					public void textValueChanged(TextEvent arg0) {
						setSelectedDelimiterMinFunction(min.getText());
						
					}
				}
			);
		min.addKeyListener(
				new KeyListener(){
					public void keyTyped(KeyEvent arg0) {
						System.out.println("keycode:"+arg0.getKeyChar());
						if(arg0.getKeyChar()=='\n')
							InputChart.this.repaint();
						
					}

					public void keyPressed(KeyEvent arg0) {
					
					}

					public void keyReleased(KeyEvent arg0) {
					
					}
				}
			);
		min.removeMouseWheelListener(this);

		
		initialize();
		
		
	}
	
	private void initialize() {
		this.setLayout(null);
		this.setDoubleBuffered(true);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		
		this.add(func);
	}
	

	
	@Override
	public void paint(Graphics arg0) {	
		super.paint(arg0);
		update(arg0);
	}
	
	private int getProcessLim(Vector2d chartClic1,double toolerance)
	{
		int ret=0;
		
		
		double dist=toolerance;
		double aux=processMin-chartClic1.x;
		if(aux<0) aux*=-1;
			
		if(dist>aux) 
			{dist=aux; ret=-1;}
		
		aux=processMax-chartClic1.x;
		if(aux<0) aux*=-1;
			
		if(dist>aux) 
			{dist=aux; ret=1;}
		
		
		return ret;
	}
	private Delimiter getDelimiter(Vector2d chartClic1,double toolerance)
	{
		Delimiter ret=null;
		
		if(delimiters.size()==0)
			return null;
		double dist=toolerance;
		for(Delimiter del:delimiters)
		{
			double aux=del.getPlace()-chartClic1.x;
			if(aux<0) aux*=-1;
			
			if(dist>aux) 
				{dist=aux; ret=del;}
		}
		
		
		return ret;
	}
	
	public Delimiter findNext(double d)
	{
		Delimiter ret=null;
		if(delimiters.size()==0)
			return null;
		double dist=-1.;
		for(Delimiter del:delimiters)
		{
			double aux=del.getPlace()-d;
			if(dist<0. && aux>0) 
				{dist=aux; ret=del;}
			if(aux>0 && dist>aux) 
				{dist=aux; ret=del;}
		}
		return ret;
	}
	
	public Delimiter findPrevious(double d)
	{
		Delimiter ret=null;
		if(delimiters.size()==0)
			return null;
		double dist=-1.;
		for(Delimiter del:delimiters)
		{
			double aux=d-del.getPlace();
			if(dist<0. && aux>0) 
				{dist=aux; ret=del;}
			if(aux>0 && dist>aux) 
				{dist=aux; ret=del;}
		}
		return ret;
	}
	
	
	public Delimiter findPreviousEQ(double d)
	{
		Delimiter ret=null;
		if(delimiters.size()==0)
			return null;
		double dist=-1.;
		for(Delimiter del:delimiters)
		{
			double aux=d-del.getPlace();
			if(dist<0. && aux>=0) 
				{dist=aux; ret=del;}
			if(aux>=0 && dist>aux) 
				{dist=aux; ret=del;}
		}
		return ret;
	}
	
	private void window(int xr1, int yr1, int xr2, int yr2,Graphics2D g) {
		//---------------------------------caixa
		float alpha = 0.5f;
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setPaint(new Color(100, 100, 100));
		g.fillRect(xr1, yr1, xr2 - xr1, yr2 - yr1);
		alpha = 1.0f;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setColor(Color.BLACK);
		g.drawLine(xr1, yr1, xr2, yr1);
		g.drawLine(xr1, yr1, xr1, yr2);
		g.drawLine(xr1, yr2, xr2, yr2);
		g.drawLine(xr2, yr1, xr2, yr2);
		//----------------------------------fim caixa
	}
	
	public void drawMousePosition(Graphics2D g)
	{
		NumberFormat nf=DecimalFormat.getInstance(Locale.US);
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(6);
		
		Vector2d moused=pixelToChart(new Vector2d(mousex,mousey));
		Rectangle2D stringBounds = g2.getFontMetrics().getStringBounds("x:"+nf.format(moused.x)+" y:"+nf.format(moused.y), g2);
			
		
	
		window(this.getWidth()-((int)stringBounds.getWidth()+15),5,this.getWidth()-5,25,g);
		g.drawString("x:"+nf.format(moused.x)+" y:"+nf.format(moused.y),this.getWidth()-((int)stringBounds.getWidth()+10),20);
	}
	
	public void drawDelimiterSelect(Graphics2D g)
	{
		if(delimiterSelect==null) return;
		
		
		Color csave=g.getColor();
		
		g.setColor(Color.CYAN);
		Delimiter next=findNext(delimiterSelect.getPlace());
		if(next!=null)
		{
			g.fillRect((int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,
					0,
					(int)chartToPixel(new Vector2d(next.getPlace(),0)).x-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,
					this.getHeight());
			//System.out.println("entrinull");
			
		}
		else
		{
			g.fillRect((int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,
					0,
					this.getWidth()-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,
					this.getHeight());
			//System.out.println("entri retc:"+(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x+" "+
			//		0+" "+this.getWidth()+" "+
			//		this.getHeight());
			
		}
			
		g.setColor(csave);
		
		
	
	}
	
	public void drawInput()
	{
		if(delimiterSelect==null)
		{
			return;
		}
		
		this.setLayout(null);
		
		int locx=(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x;
		int sizex;
		
		Delimiter next=findNext(delimiterSelect.getPlace());
		if(next!=null)
			sizex=(int)chartToPixel(new Vector2d(next.getPlace(),0)).x-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x;
			//func.setSize((int)chartToPixel(new Vector2d(next.getPlace(),0)).x-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,20);
		else
			sizex=this.getWidth()-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x;
			//func.setSize(this.getWidth()-(int)chartToPixel(new Vector2d(delimiterSelect.getPlace(),0)).x,20);
		
		
		
		if(locx<0)
		{
			sizex+=locx;
			locx=0;
		}
		
		if(locx+sizex>this.getWidth())
		{
			sizex-=(locx+sizex)-this.getWidth();
		}
		
		//func
		func.setLocation(locx+35,this.getHeight()-20);
		func.setSize(sizex-35,20);
		func.setVisible(true);
		func.setText(delimiterSelect.getFunction());
		
		funclabel.setLocation(locx+1,this.getHeight()-20);
		if(sizex>35)
			funclabel.setSize(34,20);
		else
			funclabel.setSize(sizex,20);
		
		
		//max
		max.setLocation(locx+35,this.getHeight()-40);
		max.setSize(sizex-35,20);
		max.setVisible(true);
		max.setText(delimiterSelect.getMaxFunction());//---------------------ver texto
		
		maxlabel.setLocation(locx+1,this.getHeight()-40);
		if(sizex>35)
			maxlabel.setSize(34,20);
		else
			maxlabel.setSize(sizex,20);
		
		// min
		min.setLocation(locx+35,this.getHeight()-60);
		min.setSize(sizex-35,20);
		min.setVisible(true);
		min.setText(delimiterSelect.getMinFunction());//-----------------------ver
		
		minlabel.setLocation(locx+1,this.getHeight()-60);
		if(sizex>35)
			minlabel.setSize(34,20);
		else
			minlabel.setSize(sizex,20);
		
		//this.add(func);
		//func.doLayout();
		//func.repaint();
	}
	
	public void drawAxis(Graphics2D g,boolean numbers)
	{
		//origin
		g.drawLine(0,(int)chartToPixel(new Vector2d(0,0)).y,this.getWidth(),(int)chartToPixel(new Vector2d(0,0)).y);
		g.drawLine((int)chartToPixel(new Vector2d(0,0)).x,0,(int)chartToPixel(new Vector2d(0,0)).x,this.getHeight());

		if(!numbers) return; // numbers off
		
		Vector2d wp1=pixelToChart(new Vector2d(0,0));
		Vector2d wp2=pixelToChart(new Vector2d(this.getWidth(),this.getHeight()));
		
		// eixo x
		if(wp1.y*wp2.y<=0)  // se o eixo x é visivel 
		{
			double factor=10;//=(double) (((int)(scalex/10))*100);
			int digit=1;
			int auxi1=(int) (wp1.x/factor);
			double i1=auxi1*factor;
			//System.err.println("fac y:"+factor+" scale:"+scaley+" i1 :"+i1+ " int i1*factor:"+auxi1 );
			
			int auxi2=(int) (wp2.x/factor);
			double i2=auxi2*factor;

			if(scalex<=1)
			{
				double aux=scalex-(scalex/1.5);
				double x;
				for(x=1;aux<1;aux*=10)
				{
					x*=10;
					digit++;
				}
					
				factor=x*10;
				
				auxi1=(int) (wp1.x/factor);
				i1=(double)auxi1*(int)factor;
				//System.err.println("fac x:"+factor+" scale:"+scalex+" i1 :"+i1+ " int i1*factor:"+auxi1 );
				
				auxi2=(int) (wp2.x/factor);
				i2=(double)auxi2*factor;
			}
			
			if(scalex>1)
			{
				double aux=scalex-(scalex/1.5);
				double x;
				for(x=1;aux>1;aux/=10)
					x*=10;
				factor=1/(x/100);
				
				auxi1=(int) (wp1.x/factor);
				i1=auxi1*factor;
				//System.err.println("fac x:"+factor+" scale:"+scalex+" i1 :"+i1+ " int i1*factor:"+auxi1 );
				
				auxi2=(int) (wp2.x/factor);
				i2=auxi2*factor;
			}
			
			
			NumberFormat nf=DecimalFormat.getInstance(Locale.US);
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(digit+6);
			i1-=factor;
			for(;i1<(i2+factor);i1+=factor)
			{
				
				//double aux=(int)(i1/(factor/10));
				//aux*=(factor/10);
				
				
				if(!(""+nf.format(i1)).equals("0"))
				{
				Rectangle2D stringBounds = g2.getFontMetrics().getStringBounds(""+nf.format(i1), g2);
				
				g.drawString(""+nf.format(i1),(int)(chartToPixel(new Vector2d(i1,0)).x-(stringBounds.getWidth()/2)),(int)chartToPixel(new Vector2d(i1,0)).y-3);
				
				g.drawLine((int)chartToPixel(new Vector2d(i1,0)).x,(int)chartToPixel(new Vector2d(i1,0)).y-1,(int)chartToPixel(new Vector2d(i1,0)).x,(int)chartToPixel(new Vector2d(i1,0)).y+1);
				}
			}
		
		}
		
		// eixo y
		if(wp1.x*wp2.x<=0)  // se o eixo y é visivel 
		{
			
			double factor=10;//=(double) (((int)(scalex/10))*100);
			int digit=1;
			int auxi1=(int) (wp1.y/factor);
			double i1=auxi1*factor;
			//System.err.println("fac y:"+factor+" scale:"+scaley+" i1 :"+i1+ " int i1*factor:"+auxi1 );
			
			int auxi2=(int) (wp2.y/factor);
			double i2=auxi2*factor;

			if(scaley<=1)
			{
				//double aux=scaley;
				double aux=scaley-(scaley/3);
				double x;
				for(x=1;aux<1;aux*=10)
				{
					x*=10;
					digit++;
				}
					
				factor=x*10;
				
				auxi1=(int) (wp1.y/factor);
				i1=auxi1*factor;
				//System.err.println("fac y:"+factor+" scale:"+scaley+" i1 :"+i1+ " int i1*factor:"+auxi1 );
				
				auxi2=(int) (wp2.y/factor);
				i2=auxi2*factor;
			}
			
			if(scaley>1)
			{
				double aux=scaley-(scaley/2);
				double x;
				for(x=1;aux>1;aux/=10)
				{
					x*=10;
					digit++;
				}
				factor=1/(x/100);
				
				auxi1=(int) (wp1.y/factor);
				i1=auxi1*factor;
				//System.err.println("fac y:"+factor+" scale:"+scaley+" i1 :"+i1+ " int i1*factor:"+auxi1 );
				
				auxi2=(int) (wp2.y/factor);
				i2=auxi2*factor;
			}
			
			i1+=factor;
			NumberFormat nf=DecimalFormat.getInstance(Locale.US);
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(digit+6);
			for(;i1>(i2-factor);i1-=factor)
			{
				//double aux=(int)(i1/(factor/10));
				//aux*=(factor/10);
				
				if(!(""+nf.format(i1)).equals("0"))
				{
				g.drawString(""+nf.format(i1),(int)chartToPixel(new Vector2d(0,i1)).x+4,(int)chartToPixel(new Vector2d(0,i1)).y+4);
				g.drawLine((int)chartToPixel(new Vector2d(0,i1)).x-1,(int)chartToPixel(new Vector2d(0,i1)).y,(int)chartToPixel(new Vector2d(0,i1)).x+1,(int)chartToPixel(new Vector2d(0,i1)).y);
				}
			}
		}

	}
	
	public void drawDelimiters(Graphics2D g)
	{
//		delimiters
		for(Delimiter d:delimiters)
		{
			Vector2d p2=chartToPixel(new Vector2d(d.getPlace(),0));
			g.drawLine((int)p2.x,0,(int)p2.x,this.getHeight());
		}

	}
	
	public void drawPoints(Graphics2D g)
	{
		Stroke stroke = g2.getStroke();

		g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		
		
		
		//points
		for(Vector2d p : points)
		{
			//System.err.println("point:"+p.x+" , "+p.y);
			Vector2d p2=chartToPixel(p);
			g.drawLine((int)p2.x,(int)p2.y,(int)p2.x,(int)p2.y);
			
			//g.drawString("x",(int)p2.x,(int)p2.y);
		}
		
		g.setStroke(stroke);

	}
	
	public void drawChart(Graphics2D g)
	{
		
		Color csave=g.getColor();
		g.setColor(Color.BLUE);
		
		Delimiter hereDel=null;
		Vector2d hereChart;
		Vector2d herePixel;
		Vector2d herePrev=new Vector2d();
		double value;
		int i=0;
		
		hereChart=pixelToChart(new Vector2d(i,0));
		hereDel=findPrevious(hereChart.x);
		if(hereDel!=null)
		{
			
			value=hereDel.getValue(hereChart.x);
			hereChart.y=value;
			herePrev=chartToPixel(hereChart);
			g.drawLine(i,(int)herePrev.y,i,(int)herePrev.y);
		}
		
		for(i=1;i<this.getWidth();i++)
		{
			
			hereChart=pixelToChart(new Vector2d(i,0));
			hereDel=findPrevious(hereChart.x);
			if(hereDel!=null)
			{
				//System.out.println("passou");
				//System.out.println("----------sdfsdfsdf-----------");
				value=hereDel.getValue(hereChart.x);
				//System.err.println(hereDel.evalOk());
				if(hereDel.parceOk())
				{
					hereChart.y=value;
					herePixel=chartToPixel(hereChart);	
					herePixel.x=i;
					if(herePrev.x==0 && herePrev.y==0)
						g.drawLine((int)herePixel.x,(int)herePixel.y,(int)herePixel.x,(int)herePixel.y);
					else
						g.drawLine((int)herePrev.x,(int)herePrev.y,(int)herePixel.x,(int)herePixel.y);
					herePrev=herePixel;
					
				}
				else
				{
					g.setColor(Color.RED);
					g.drawString(hereDel.errorEval().split("\n")[0],(int)chartToPixel(new Vector2d(hereDel.getPlace(),0)).x,10);
					g.setColor(Color.BLUE);
					if(findNext(hereChart.x)!=null)
						i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
					else
						i=this.getWidth();
					
				}
				
				
			}
			else
			{
				if(findNext(hereChart.x)!=null)
					i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
				else
					i=this.getWidth();
			}
		}
		
				
		g.setColor(csave);
	}
	
	public void drawMaxChart(Graphics2D g)
	{
		
		Color csave=g.getColor();
		g.setColor(Color.ORANGE);
		
		Delimiter hereDel=null;
		Vector2d hereChart;
		Vector2d herePixel;
		Vector2d herePrev=new Vector2d();
		double value;
		double functionValue;
		int i=0;
		
		hereChart=pixelToChart(new Vector2d(i,0));
		hereDel=findPrevious(hereChart.x);
		if(hereDel!=null && !hereDel.getMaxFunction().equals(""))
		{
			
			
			value=hereDel.getMaxValue(hereChart.x);
			hereChart.y=value;
			herePrev=chartToPixel(hereChart);
			
			functionValue=hereDel.getValue(hereChart.x);
			if(value<functionValue)
			{
				Vector2d hereFunc=new Vector2d();
				hereFunc.y=functionValue;
				g.setColor(Color.RED);
				g.drawLine(i,(int)herePrev.y,i,(int)chartToPixel(hereFunc).y);
				g.setColor(Color.ORANGE);
			}
			
			g.drawLine(i,(int)herePrev.y,i,(int)herePrev.y);
			
			
		}
		
		
		
		for(i=1;i<this.getWidth();i++)
		{
			
			hereChart=pixelToChart(new Vector2d(i,0));
			
				
			hereDel=findPrevious(hereChart.x);
			if(hereDel!=null && !hereDel.getMaxFunction().equals(""))
			{
			
				//System.out.println("passou");
				//System.out.println("----------sdfsdfsdf-----------");
				value=hereDel.getMaxValue(hereChart.x);
				//System.err.println(hereDel.evalOk());
				if(hereDel.parceOkMax())
				{
					hereChart.y=value;
					herePixel=chartToPixel(hereChart);	
					herePixel.x=i;
					if(herePrev.x==0 && herePrev.y==0)
						g.drawLine((int)herePixel.x,(int)herePixel.y,(int)herePixel.x,(int)herePixel.y);
					else
						g.drawLine((int)herePrev.x,(int)herePrev.y,(int)herePixel.x,(int)herePixel.y);
					
					functionValue=hereDel.getValue(hereChart.x);
					if(value<functionValue)
					{
						Vector2d hereFunc=new Vector2d();
						hereFunc.y=functionValue;
						g.setColor(Color.RED);
						g.drawLine(i,(int)herePixel.y,i,(int)chartToPixel(hereFunc).y);
						g.setColor(Color.ORANGE);
						
		
						g.setColor(Color.DARK_GRAY);
						g.drawString("Function intercept Max limit",(int)chartToPixel(new Vector2d(hereDel.getPlace(),0)).x,20);
						g.setColor(Color.ORANGE);
					}
					herePrev=herePixel;
					
				}
				else
				{
		
					g.setColor(Color.DARK_GRAY);
					g.drawString(hereDel.errorEvalMax().split("\n")[0].replace("No","No Max"),(int)chartToPixel(new Vector2d(hereDel.getPlace(),0)).x,20);
					g.setColor(Color.ORANGE);
					if(findNext(hereChart.x)!=null)
						i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
					else
						i=this.getWidth();	
				}
				
			}
			else
			{
				if(findNext(hereChart.x)!=null)
					i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
				else
					i=this.getWidth();
			}
		}
		
		
		
		
		g.setColor(csave);		
	}
	
	public void drawMinChart(Graphics2D g)
	{

		Color csave=g.getColor();
		g.setColor(Color.ORANGE);
		
		Delimiter hereDel=null;
		Vector2d hereChart;
		Vector2d herePixel;
		Vector2d herePrev=new Vector2d();
		double value;
		double functionValue;
		
		int i=0;
		
		hereChart=pixelToChart(new Vector2d(i,0));
		hereDel=findPrevious(hereChart.x);
		if(hereDel!=null && !hereDel.getMinFunction().equals(""))
		{
			value=hereDel.getMinValue(hereChart.x);
			hereChart.y=value;
			herePrev=chartToPixel(hereChart);
			g.drawLine(i,(int)herePrev.y,i,(int)herePrev.y);
			
			functionValue=hereDel.getValue(hereChart.x);
			if(value>functionValue)
			{
				Vector2d hereFunc=new Vector2d();
				hereFunc.y=functionValue;
				g.setColor(Color.RED);
				g.drawLine(i,(int)herePrev.y,i,(int)chartToPixel(hereFunc).y);
				g.setColor(Color.ORANGE);
			}
		}
		
		for(i=1;i<this.getWidth();i++)
		{
			
			hereChart=pixelToChart(new Vector2d(i,0));
			hereDel=findPrevious(hereChart.x);
			if(hereDel!=null && !hereDel.getMinFunction().equals(""))
			{
				//System.out.println("passou");
				//System.out.println("----------sdfsdfsdf-----------");
				value=hereDel.getMinValue(hereChart.x);
				//System.err.println(hereDel.evalOk());
				if(hereDel.parceOkMin())
				{
					hereChart.y=value;
					herePixel=chartToPixel(hereChart);	
					herePixel.x=i;
					if(herePrev.x==0 && herePrev.y==0)
						g.drawLine((int)herePixel.x,(int)herePixel.y,(int)herePixel.x,(int)herePixel.y);
					else
						g.drawLine((int)herePrev.x,(int)herePrev.y,(int)herePixel.x,(int)herePixel.y);
					
					functionValue=hereDel.getValue(hereChart.x);
					if(value>functionValue)
					{
						Vector2d hereFunc=new Vector2d();
						hereFunc.y=functionValue;
						g.setColor(Color.RED);
						g.drawLine(i,(int)herePixel.y,i,(int)chartToPixel(hereFunc).y);
						g.setColor(Color.ORANGE);
						
						g.setColor(Color.DARK_GRAY);
						g.drawString("Function intercept Min limit",(int)chartToPixel(new Vector2d(hereDel.getPlace(),0)).x,30);
						g.setColor(Color.ORANGE);
					}						
						
						
					herePrev=herePixel;
					
				}
				else
				{
					g.setColor(Color.DARK_GRAY);
					g.drawString(hereDel.errorEvalMin().split("\n")[0].replace("No","No Min"),(int)chartToPixel(new Vector2d(hereDel.getPlace(),0)).x,30);
					g.setColor(Color.ORANGE);
					if(findNext(hereChart.x)!=null)
						i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
					else
						i=this.getWidth();
					
				}
				
				
			}
			else
			{
				if(findNext(hereChart.x)!=null)
					i=(int) chartToPixel(new Vector2d(findNext(hereChart.x).getPlace(),0)).x;
				else
					i=this.getWidth();
			}
		}
				
		g.setColor(csave);		
	}
	
	public void interceptionErrors(Graphics g) {
		Color csave = g.getColor();
		g.setColor(Color.BLACK);
		Delimiter auxDel;
		for (Delimiter del : delimiters) {

			if (!del.getFunction().equals("")) 
			{

				if (!del.getMaxFunction().equals(""))
					if ((auxDel=findNext(del.place)) != null) 
					{

						double rootMaxFunc = del.getMax().minus(del.getFunc())
								.findRoot(del.place, auxDel.place);

						if (!Double.isNaN(rootMaxFunc)
								&& !Double.isInfinite(rootMaxFunc) && rootMaxFunc>del.place && rootMaxFunc<auxDel.place) 
						{
							// g.drawString("Function intercept Max limit at
							// x:"+rootMaxFunc,10,10);
							g.drawString("Function intercept Max limit at x:"
									+ rootMaxFunc,
									(int) chartToPixel(new Vector2d(rootMaxFunc,
											0)).x,
									(int) chartToPixel(new Vector2d(0, del
											.getFunc().f(rootMaxFunc))).y);

						}
					} 
					else 
					{
						double rootMaxFunc = del.getMax().minus(del.getFunc())
						.findRoot(del.place);

				if (!Double.isNaN(rootMaxFunc)
						&& !Double.isInfinite(rootMaxFunc) && rootMaxFunc>del.place) 
				{
					// g.drawString("Function intercept Max limit at
					// x:"+rootMaxFunc,10,10);
					g.drawString("Function intercept Max limit at x:"
							+ rootMaxFunc,
							(int) chartToPixel(new Vector2d(rootMaxFunc,
									0)).x,
							(int) chartToPixel(new Vector2d(0, del
									.getFunc().f(rootMaxFunc))).y);

				}

					}
			}

		}

		g.setColor(csave);
	}
	
	public void drawPrecessArea(Graphics g)
	{
		Color csave=g.getColor();
		
		g.setColor(Color.ORANGE);
		
		Vector2d p1;
		Vector2d p2;
		
		if(processMin<processMax)
		{
			p1=chartToPixel(new Vector2d(processMin,0));
			p2=chartToPixel(new Vector2d(processMax,0));
		}
		else
		{
			p2=chartToPixel(new Vector2d(processMin,0));
			p1=chartToPixel(new Vector2d(processMax,0));
		}
		
		
		g.fillRect((int)p1.x,
					0,
					(int)p2.x-(int)p1.x,
					10);
		//((Graphics2D) g).setStroke(new BasicStroke());
		
		g.drawLine((int)p1.x,0,(int)p1.x,this.getHeight());
		g.drawLine((int)p2.x,0,(int)p2.x,this.getHeight());
		
		g.setColor(csave);
		
		
	}
	
	public void update(Graphics arg0) {
		
			try {
			
			this.g2 = (Graphics2D)arg0;
	 		
	 		if (g2 == null)
	 			return;
	 		
			//this.g2  = (Graphics2D)this.getGraphics();
		 	if (bi == null || bi.getWidth() < getWidth() || bi.getHeight() < getHeight())
		 		bi = (BufferedImage)createImage(this.getWidth(),this.getHeight());
			Graphics2D g=(Graphics2D) bi.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    			RenderingHints.VALUE_ANTIALIAS_ON);
	    	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	    			RenderingHints.VALUE_RENDER_QUALITY);
			g.setPaintMode();
			g.setBackground(this.getParent().getBackground());
			g.clearRect(0,0,this.getWidth(),this.getHeight());
			
			//g.drawString("String º"+points.size(),10,10);
			
			drawDelimiterSelect(g);
			
			if(editable)
				drawPrecessArea(g);
			
			drawAxis(g,true);
			
			drawDelimiters(g);
			
			
			drawMaxChart(g);
			drawMinChart(g);
			drawChart(g);
			interceptionErrors(g);
			
			drawPoints(g);
			drawMousePosition(g);
			
			g2.drawImage(bi,0,0,this);
			
		}
		catch (Exception e) {
				//repaint();
		}
		drawInput();
	}

	public Vector2d pixelToChart(Vector2d p)
	{
		Vector2d ret=new Vector2d(p.x,p.y);
		ret.y=this.getHeight()-ret.y;
		ret.x-=this.getWidth()/2;
		ret.y-=this.getHeight()/2;

		
		ret.x=ret.x/scalex;
		ret.y=ret.y/scaley;
		
		
		ret.x+=view.x;
		ret.y+=view.y;
		
		
		return ret;
	}
	
	public Vector2d chartToPixel(Vector2d p)
	{
		Vector2d ret=new Vector2d(p.x,p.y);
		ret.x-=view.x;
		ret.y-=view.y;
		
		ret.x=ret.x*scalex;
		ret.y=ret.y*scaley;		

		ret.x+=this.getWidth()/2;
		ret.y+=this.getHeight()/2;
		ret.y=this.getHeight()-ret.y;
		return ret;
	}
	
	public void mouseClicked(MouseEvent arg0) {
		mousex=arg0.getX();
		mousey=arg0.getY();
		int mx =  arg0.getX(), my =  arg0.getY();


		final Vector2d chartClic1=pixelToChart(new Vector2d(mx,my));
		
		
		if(editable)
		{
		if(arg0.getButton()==MouseEvent.BUTTON1)
		{
			if(findPrevious(chartClic1.x)==delimiterSelect)
			{
				delimiterSelect=null;
				
			}
			else
			{
				
				delimiterSelect=findPrevious(chartClic1.x);
			}
			
			//points.add(pixelToChart(new Vector2d((double)arg0.getX(),(double)arg0.getY())));
			/*Delimiter dn=findNext(pixelToChart(new Vector2d((double)arg0.getX(),(double)arg0.getY())).x);
			if (dn==null)
				System.out.println("next del null");
			else
				System.out.println("next del "+dn.getPlace());
			
			Delimiter dp=findPrevious(pixelToChart(new Vector2d((double)arg0.getX(),(double)arg0.getY())).x);
			if (dp==null)
				System.out.println("prev del null");
			else
				System.out.println("prev del "+dp.getPlace());
			delimiterSelect=dp;*/
			
		}
		if(arg0.getButton()==MouseEvent.BUTTON3)
		{
			if(delimiterMouseOver!=null)
			{
				JPopupMenu popup = new JPopupMenu();
				popup.setLightWeightPopupEnabled(false);
				JPopupMenu.setDefaultLightWeightPopupEnabled(false);
				
				JMenuItem item;
				
				item= new JMenuItem("Remove Function Delimiter");
				item.addActionListener(new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent e) {
		            	//double x=chartClic1.x;
		            	//double x=pixelToChart(new Vector2d(mousex,mousey));
		            
		            	delimiters.remove(delimiterMouseOver);
		            	repaint();
		            	
		            }
		        });
				popup.add(item);
				
				
				popup.show(this, arg0.getX(), arg0.getY());
			
				//ou
				/*
				delimiters.remove(delimiterMouseOver);
		    	repaint();
		    	*/
			}
			else
			{
			delimiterSelect=findPrevious(chartClic1.x);
			JPopupMenu popup = new JPopupMenu();
			popup.setLightWeightPopupEnabled(false);
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			
			JMenuItem item;
			
			item= new JMenuItem("New Function Delimiter");
			item.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent e) {
	            	//double x=chartClic1.x;
	            	//double x=pixelToChart(new Vector2d(mousex,mousey));
	            
	            	Delimiter addnew=new Delimiter(chartClic1.x);
	            	Delimiter delAux;
	            	if((delAux=findPrevious(chartClic1.x))!=null)
	            	{
	            		addnew.setMax(new Function(delAux.getMax().getFunction(),delAux.getMax().getOrderTo()));
	            		addnew.setMin(new Function(delAux.getMin().getFunction(),delAux.getMin().getOrderTo()));
	            	}
	            	delimiters.add(addnew);
	            	repaint();
	            }
	        });
			popup.add(item);
			
			
			popup.show(this, arg0.getX(), arg0.getY());
			
			//ou
			/*
			Delimiter addnew=new Delimiter(chartClic1.x);
        	Delimiter delAux;
        	if((delAux=findPrevious(chartClic1.x))!=null)
        	{
        		addnew.setMax(delAux.getMax());
        		addnew.setMin(delAux.getMin());
        	}
        	delimiters.add(addnew);
        	repaint();
			*/
			}
			
		}
		if(arg0.getButton()==MouseEvent.BUTTON2)
		{
			int i=0;
			for(Vector2d p:points)
			{
				//System.out.println("ponto:"+i++ +" :"+"("+p.x+" , "+p.y+" )");
			}
		}
		
		if(delimiterSelect!=null)
		{
			this.add(func);
			this.add(funclabel);
			this.add(max);
			this.add(maxlabel);
			this.add(min);
			this.add(minlabel);
			
		}
		else
		{
			if(func.getParent()!=null)
			{
				this.remove(func);
				this.remove(funclabel);
				this.remove(max);
				this.remove(maxlabel);
				this.remove(min);
				this.remove(minlabel);
			JPopupMenu popup = new JPopupMenu();
			//popup.setLightWeightPopupEnabled(false);
			//JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			popup.show(this, arg0.getX(), arg0.getY());
			popup.setVisible(false);
			}
						//this.setEnabled(true);
		}
		}
		repaint();
	}

	public void mousePressed(MouseEvent arg0) {
		mousex=arg0.getX();
		mousey=arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
	
		
	}	
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		int mx =  arg0.getX(), my =  arg0.getY();


		Vector2d chartClic1=pixelToChart(new Vector2d(mx,my));
		delimiterMouseOver=getDelimiter(chartClic1,2/scalex);
		if(delimiterMouseOver!=null)
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		
		if (arg0.isShiftDown())
			scalex+=((double)arg0.getWheelRotation())/10*scalex;	
		else if(arg0.isControlDown())
			scaley+=((double)arg0.getWheelRotation())/10*scaley;
		else{
			scalex+=((double)arg0.getWheelRotation())/10*scalex;
			scaley+=((double)arg0.getWheelRotation())/10*scaley;
		}	
					
			
		//System.out.println(scaley);
		repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		Vector2d chartClic0=pixelToChart(new Vector2d(mousex,mousey));
		Vector2d chartClic1=pixelToChart(new Vector2d(mx,my));
		
		
		if(delimiterMouseOver!=null)
		{
			if(editable)
			delimiterMouseOver.setPlace(delimiterMouseOver.getPlace()+(chartClic1.x-chartClic0.x));
			
		}
		else if(processMouseOver!=0)
		{
			if(processMouseOver==1)
			{
				processMax+=(chartClic1.x-chartClic0.x);
				for(inputChartListener mlistener: Listeners)
				{
					mlistener.change(this);
				}
			}
				
			if(processMouseOver==-1)
			{
				processMin+=(chartClic1.x-chartClic0.x);
				for(inputChartListener mlistener: Listeners)
				{
					mlistener.change(this);
				}
			}
		}
		else
		{
			view.x-=(chartClic1.x-chartClic0.x);
			view.y-=(chartClic1.y-chartClic0.y);
		}
		mousex = mx;
		mousey = my;
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		int mx = e.getX(), my = e.getY();

		mousex=mx; mousey=my;
		repaint();
		if(!editable) return;
		//System.out.println("moveu");
		Vector2d chartClic1=pixelToChart(new Vector2d(mx,my));
		delimiterMouseOver=getDelimiter(chartClic1,2/scalex);
		if(delimiterMouseOver!=null)
		{
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			//System.err.println("over :"+delimiterMouseOver.getPlace());
		}
		else
		{
			if((processMouseOver=getProcessLim(chartClic1,2/scalex))!=0)
				setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			else
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
			
	}
	
	public void setSelectedDelimiterFunction(String func)
	{//TODO dar a variavel 
		if(delimiterSelect!=null)
			delimiterSelect.setFunction(func,"x");
		//repaint();
	}
	
	public void setSelectedDelimiterMaxFunction(String func)
	{//TODO dar a variavel 
		if(delimiterSelect!=null)
			delimiterSelect.setMaxFunction(func,"x");
		//repaint();
	}
	
	public void setSelectedDelimiterMinFunction(String func)
	{//TODO dar a variavel 
		if(delimiterSelect!=null)
			delimiterSelect.setMinFunction(func,"x");
		//repaint();
	}
	
	public void setInput(boolean flag)
	{
		editable=flag;
	}
	
	
	public static void main(String args[]) {
		InputChart chart=new InputChart();
		JFrame a = new JFrame("input chart");
		a.setSize(500, 400);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.getContentPane().setLayout(new BorderLayout());
		a.getContentPane().add(chart, BorderLayout.CENTER);
		a.setVisible(true);
		chart.addDelimiter(new Delimiter(0.5));
		System.err.println("encontro anterior a 0.5 em :"+chart.findPrevious(0.5).getPlace());
	}

	public double getTimemax() {
		return processMax;
	}

	public void setTimemax(double timemax) {
		this.processMax = timemax;
	}

	public double getTimemin() {
		return processMin;
	}

	public void setTimemin(double timemin) {
		this.processMin = timemin;
	}

	public Vector2d getView() {
		return view;
	}

	public void setView(Vector2d view) {
		this.view = view;
	}
	
	public void addDelimiter(Delimiter addnew)
	{
		delimiters.add(addnew);
		repaint();
	}
	
	public void cleanDelimiters()
	{
		delimiters.clear();
		repaint();
	}
	
	public void addInputChartListener(inputChartListener mcl)
	{
		Listeners.add(mcl);
	}
	
	public void removeInputChartListener(inputChartListener mcl)
	{
		Listeners.remove(mcl);
	}

	public double f(double val)
	{
		Delimiter del=findPrevious(val);
		if(del==null) return 0;
		else return del.getFunc().f(val);
	}

	public void change(InputChart chart) {
		// TODO Auto-generated method stub
		this.setTimemax(chart.getTimemax());
		this.setTimemin(chart.getTimemin());
		this.repaint();
	}
}
