import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;

public class Graph extends JFrame {

	
	private int numOfGraphs;
	private ArrayList<JFreeChart> graphs;
	
	public Graph(int numOfGraphs) {
		this.numOfGraphs = numOfGraphs;
		graphs = new ArrayList<JFreeChart>();
	}

	public void initializeUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		getContentPane().add(panel);
		for (int i=0; i<numOfGraphs; i++) {
			ChartPanel graphPanel = new ChartPanel(graphs.get(i));
			graphPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			graphPanel.setBackground(Color.white);
			panel.add(graphPanel);
		}
		pack();
		setTitle("Line Chart");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
	public void createGraph(XYDataset dataset, String graphTitle, String graphX, String graphY, boolean saveToFile, String filename) throws IOException {
		
		JFreeChart graph = ChartFactory.createXYLineChart(
				graphTitle,
				graphX,
				graphY,
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		
		XYPlot plot = graph.getXYPlot();
		var renderer = new XYLineAndShapeRenderer(true, false);
		Color[] colors = { Color.ORANGE, Color.BLUE, Color.RED, Color.CYAN, Color.GREEN, Color.PINK };
		for (int i=0; i< dataset.getSeriesCount(); i++) {
			if (i>=colors.length) {
				break;
			}
			renderer.setSeriesPaint(i, colors[i]);
			renderer.setSeriesStroke(i, new BasicStroke(1.5f));
		}
		
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.WHITE);
		
		graph.setTitle(new TextTitle(graphTitle));
		
		if (saveToFile) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("hh-mm");
			String date = dateFormat.format(new Date());
			String time = timeFormat.format(new Date());
			File file = new File("C:\\Users\\Vera\\Documents\\Uni\\Bachelorarbeit\\Algorithm\\Diagramme\\"+date+"\\"+filename+"_"+date+"_"+time+".png");
			file.getParentFile().mkdirs();
			ChartUtils.saveChartAsPNG(file , graph, 800, 600);
		}
		
		graphs.add(graph);
	}
}
