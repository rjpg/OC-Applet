package modeldef;

import functions.BASE;
import functions.Vector2d;
import inputchart.InputChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Session extends JPanel {

	public Session() {
		super();
		InputChart chart = new InputChart();

		InputChart out1 = new InputChart();
		out1.setView(new Vector2d(0.25, 0));
		out1.setInput(false);
		InputChart out2 = new InputChart();
		out2.setView(new Vector2d(0.25, 0));
		out2.setInput(false);

		//this.setLayout(new GridLayout(1,2));
		this.setLayout(new BorderLayout());
		JPanel panel_model_out = new JPanel();
		panel_model_out.setLayout(new GridLayout(2, 1));

		JPanel input_chart = new JPanel();
		input_chart.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
				"Input Chart -U1(x)"));
		input_chart.setLayout(new BorderLayout());
		input_chart.add(chart, BorderLayout.CENTER);

		//this.add(input_chart);

		InputChart[] in = { chart };
		InputChart[] out = { out1, out2 };
		String[] N_0 = { "0", "0" };
		String[] N = { "-5*N1+2*(1-U1)+6*N2", "5*N1-6*N2" };
		String part1 = "1*N1+2*N2";
		String part2 = "3*N1+4*N2+U1";
		String error_init = "Cancer Chemotherapy example model \nr1=1\nr2=2\nq1=3\nq2=4\na1=5\na2=6";
		String rest[] = null;
		ControlPanel cp = new ControlPanel(in, out, N_0, N, part1, part2, rest,
				error_init, this);
		//right.add(cp,BorderLayout.NORTH);

		JPanel output_chart_N1 = new JPanel();
		output_chart_N1.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
				"N1(x)"));
		output_chart_N1.setLayout(new BorderLayout());
		output_chart_N1.add(out1, BorderLayout.CENTER);
		panel_model_out.add(output_chart_N1);

		JPanel output_chart_N2 = new JPanel();
		output_chart_N2.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
				"N2(x)"));
		output_chart_N2.setLayout(new BorderLayout());
		output_chart_N2.add(out2, BorderLayout.CENTER);
		panel_model_out.add(output_chart_N2);

		//panel_model_out.add(outGeral);
		JSplitPane splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				cp, panel_model_out);
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(350);
		splitpane.setDividerSize(7);
		JSplitPane main_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, input_chart, splitpane);
		main_splitpane.setOneTouchExpandable(true);
		main_splitpane.setDividerLocation(350);
		main_splitpane.setDividerSize(7);
		this.add(main_splitpane, BorderLayout.CENTER);
		//this.add(main_splitpane);
		this.setVisible(true);
		BASE.tabbed.addTab("session " + (BASE.tabbed.getComponentCount() + 1),
				this);
	}

	public Session(InputChart in[], InputChart dim[], String N_0a[],
			String Na[], String part1a, String part2a, String resta[],
			String error) {
		for (InputChart c : dim) {
			c.setView(new Vector2d(0.25, 0));
			c.setInput(false);
		}
		this.setLayout(new BorderLayout());

		for (int i = 0; i < in.length; i++)
			for (int j = 0; j < in.length; j++)
				in[i].addInputChartListener(in[j]);

		JPanel input = new JPanel();
		input.setLayout(new GridLayout(in.length, 1));
		for (int i = 0; i < in.length; i++) {
			JPanel input_chart = new JPanel();
			input_chart.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
					"Input Chart -U" + (i + 1) + "(x)"));
			input_chart.setLayout(new BorderLayout());
			input_chart.add(in[i], BorderLayout.CENTER);
			input.add(input_chart);
		}
		this.add(input);

		ControlPanel cp = new ControlPanel(in, dim, N_0a, Na, part1a, part2a,
				resta, error, this);

		JPanel panel_model_out = new JPanel();
		if (dim.length > 3)
			panel_model_out.setLayout(new GridLayout(dim.length / 2, 2));
		else
			panel_model_out.setLayout(new GridLayout(2, 1));
		for (int i = 0; i < dim.length; i++) {
			JPanel output_chart_N1 = new JPanel();
			output_chart_N1.setBorder(new TitledBorder(new LineBorder(
					Color.BLACK), "N" + (i + 1) + "(x)"));
			output_chart_N1.setLayout(new BorderLayout());
			output_chart_N1.add(dim[i], BorderLayout.CENTER);
			panel_model_out.add(output_chart_N1);
		}

		JSplitPane splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				cp, panel_model_out);
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(350);
		splitpane.setDividerSize(7);
		JSplitPane main_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, input, splitpane);
		main_splitpane.setOneTouchExpandable(true);
		main_splitpane.setDividerLocation(350);
		main_splitpane.setDividerSize(7);
		this.add(main_splitpane, BorderLayout.CENTER);
		//this.add(splitpane);
		this.setVisible(true);
		BASE.tabbed.addTab("session " + (BASE.tabbed.getComponentCount() + 1),
				this);
	}

	public void reset(InputChart in[], InputChart dim[], String N_0a[],
			String Na[], String part1a, String part2a, String resta[],
			String error) {
		this.removeAll();
		doLayout();
		repaint();

		for (InputChart c : dim) {
			c.setView(new Vector2d(0.25, 0));
			c.setInput(false);
		}
		this.setLayout(new BorderLayout());

		for (int i = 0; i < in.length; i++)
			for (int j = 0; j < in.length; j++)
				in[i].addInputChartListener(in[j]);

		JPanel input = new JPanel();
		input.setLayout(new GridLayout(in.length, 1));
		for (int i = 0; i < in.length; i++) {
			JPanel input_chart = new JPanel();
			input_chart.setBorder(new TitledBorder(new LineBorder(Color.BLACK),
					"Input Chart -U" + (i + 1) + "(x)"));
			input_chart.setLayout(new BorderLayout());
			input_chart.add(in[i], BorderLayout.CENTER);
			input.add(input_chart);
		}
		this.add(input);

		ControlPanel cp = new ControlPanel(in, dim, N_0a, Na, part1a, part2a,
				resta, error, this);

		JPanel panel_model_out = new JPanel();
		if (dim.length > 3)
			panel_model_out.setLayout(new GridLayout(dim.length / 2, 2));
		else
			panel_model_out.setLayout(new GridLayout(2, 1));
		for (int i = 0; i < dim.length; i++) {
			JPanel output_chart_N1 = new JPanel();
			output_chart_N1.setBorder(new TitledBorder(new LineBorder(
					Color.BLACK), "N" + (i + 1) + "(x)"));
			output_chart_N1.setLayout(new BorderLayout());
			output_chart_N1.add(dim[i], BorderLayout.CENTER);
			panel_model_out.add(output_chart_N1);
		}

		JSplitPane splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				cp, panel_model_out);
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(350);
		splitpane.setDividerSize(7);
		
		JSplitPane main_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, input, splitpane);
		main_splitpane.setOneTouchExpandable(true);
		main_splitpane.setDividerLocation(350);
		main_splitpane.setDividerSize(7);
		this.add(main_splitpane, BorderLayout.CENTER);
		//this.add(splitpane);
		this.setVisible(true);

		repaint();
		doLayout();
		//BASE.tabbed.addTab("session "+(BASE.tabbed.getComponentCount()+1),this);
	}

}
