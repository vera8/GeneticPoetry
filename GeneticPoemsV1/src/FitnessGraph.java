import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;

public class FitnessGraph extends JFrame {

	public void initializeUI(XYDataset fitnessData) {
		JFreeChart graph = createGraph(fitnessData);
		ChartPanel graphPanel = new ChartPanel(graph);
		graphPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		graphPanel.setBackground(Color.white);
		add(graphPanel);
		pack();
		setTitle("Line Chart");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	private JFreeChart createGraph(XYDataset dataset) {
		JFreeChart graph = ChartFactory.createXYLineChart(
				"Generational Fintess",
				"Generation number",
				"Fitness",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		
		XYPlot plot = graph.getXYPlot();
		var renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesPaint(0, Color.ORANGE);
		renderer.setSeriesStroke(0, new BasicStroke(1.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(1.0f));
		
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.WHITE);
		
		graph.setTitle(new TextTitle("Generational Fintess"));
		
		return graph;
	}
}
