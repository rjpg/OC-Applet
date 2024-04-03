package modeldef;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Settings extends JDialog{
	private JPanel content = null;
	private JButton Apply = null;
	private JButton cancel = null;
	private JLabel jLabelInputN = null;
	private JLabel jLabelDinN = null;
	private JLabel jLabel = null;
	private JTextField jTextIn = null;
	private JTextField jTextDyn = null;
	private JTextField jTextRest = null;
	
	ControlPanel cp;
	public Settings(ControlPanel cpa)
	{
		super();
		cp=cpa;
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(254,138));
        this.setBounds(new Rectangle(100,100,254,138));
        this.setResizable(false);
        this.setModal(true);
        this.setContentPane(getContent());
        this.setTitle("Settings");
			
	}

	public static void showSettingsDialog(ControlPanel cp) {
		Settings gctrans = new Settings(cp);
		gctrans.setVisible(true);
		gctrans.setSize(new Dimension(500, 240));
		gctrans.setModal(true);
	}

	/**
	 * This method initializes content	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContent() {
		if (content == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(11,57,183,16));
			jLabel.setText("Number of restraints equations:");
			jLabelDinN = new JLabel();
			jLabelDinN.setBounds(new java.awt.Rectangle(9,35,185,16));
			jLabelDinN.setText("Number of dynamics equations :");
			jLabelInputN = new JLabel();
			jLabelInputN.setBounds(new java.awt.Rectangle(55,14,138,16));
			jLabelInputN.setText("Number of input charts :");
			content = new JPanel();
			content.setLayout(null);
			content.add(getApply(), null);
			content.add(getCancel(), null);
			content.add(jLabelInputN, null);
			content.add(jLabelDinN, null);
			content.add(jLabel, null);
			content.add(getJTextIn(), null);
			content.add(getJTextDyn(), null);
			content.add(getJTextRest(), null);
		}
		return content;
	}

	/**
	 * This method initializes Apply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getApply() {
		if (Apply == null) {
			Apply = new JButton();
			Apply.setBounds(new java.awt.Rectangle(163,86,80,23));
			Apply.setText("Apply");
			Apply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int in;
					try{
		    			in=java.lang.Integer.valueOf(jTextIn.getText());	
		    		}catch (Exception exception) {
		    			cp.writeMsg("Error - parsing text 'Number of imputs'- Assuming last value:"+cp.modelPanel.input.length);
		    			in=cp.modelPanel.input.length;
		    		}
		    		if(in<=0)
		    		{
		    			cp.writeMsg("Error - 'Number of imputs'must be >0- Assuming last value:"+cp.modelPanel.input.length);
		    			in=cp.modelPanel.input.length;
		    		}
					int N;
					try{
		    			N=java.lang.Integer.valueOf(jTextDyn.getText());	
		    		}catch (Exception exception) {
		    			cp.writeMsg("Error - parsing text 'Number of Dynamics equations'- Assuming last value:"+cp.modelPanel.dinamics.length);
		    			N=cp.modelPanel.dinamics.length;
		    		}
		    		if(N<=0)
		    		{
		    			cp.writeMsg("Error - 'Number of Dynamics equations' must be >0- Assuming last value:"+cp.modelPanel.dinamics.length);
		    			N=cp.modelPanel.dinamics.length;
		    		}
		    		
					int rest;
					try{
		    			rest=java.lang.Integer.valueOf(jTextRest.getText());	
		    		}catch (Exception exception) {
		    			cp.writeMsg("Error - parsing text 'Number of restraints'- Assuming last value:"+cp.modelPanel.restritions.length);
		    			rest=cp.modelPanel.restritions.length;
		    		}
					cp.reset(in,N,rest);
					setVisible(false);
					dispose();
				}
			});
		}
		return Apply;
	}

	/**
	 * This method initializes cancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setBounds(new java.awt.Rectangle(78,86,77,23));
			cancel.setText("Cancel");
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//setValuesRet();
					setVisible(false);
					dispose();
				}
			});
		}
		return cancel;
	}

	/**
	 * This method initializes jTextIn	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextIn() {
		if (jTextIn == null) {
			jTextIn = new JTextField();
			jTextIn.setBounds(new java.awt.Rectangle(201,12,43,21));
			jTextIn.setText(cp.modelPanel.input.length+"");
		}
		return jTextIn;
	}

	/**
	 * This method initializes jTextDyn	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextDyn() {
		if (jTextDyn == null) {
			jTextDyn = new JTextField();
			jTextDyn.setBounds(new java.awt.Rectangle(201,34,43,20));
			jTextDyn.setText(cp.modelPanel.dinamics.length+"");
		}
		return jTextDyn;
	}

	/**
	 * This method initializes jTextRest	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextRest() {
		if (jTextRest == null) {
			jTextRest = new JTextField();
			jTextRest.setBounds(new java.awt.Rectangle(201,55,43,20));
			if(cp.modelPanel.restritions!=null)
				jTextRest.setText(cp.modelPanel.restritions.length+"");
			else
				jTextRest.setText("0");
		}
		return jTextRest;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
