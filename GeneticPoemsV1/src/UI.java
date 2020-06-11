import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

public class UI extends JFrame {
	//JFrame window; 
	public static JProgressBar progressBar;
	Poem bestPoem;
	
	public UI() {
		setTitle("Genetic Poem");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLayout(new GridLayout(4, 1));
		JPanel mainPanel = new JPanel();
		setContentPane(mainPanel);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JLabel mainLabel = new JLabel("The Genetic Poem Creator");
		JPanel headerPanel = new JPanel();
		headerPanel.add(mainLabel);
		add(headerPanel);

		JPanel inputValuesPanel = new JPanel();
		add(inputValuesPanel);
		inputValuesPanel.setLayout(new GridLayout(5,2));
		
		JLabel pLinesLabel = new JLabel("Number of verses: ");
		inputValuesPanel.add(pLinesLabel);
		SpinnerModel pLinesValue = new SpinnerNumberModel(4, 2, 8, 1);
		JSpinner poemLinesSpinner = new JSpinner(pLinesValue);
		inputValuesPanel.add(poemLinesSpinner);
		
		JLabel metreLabel = new JLabel("Metre: ");
		inputValuesPanel.add(metreLabel);
		String[] metres = {"iambic", "trochaic", "anapestic", "dactylic"};
		JComboBox metreBox = new JComboBox(metres);
		inputValuesPanel.add(metreBox);
		
		JLabel emotionLabel = new JLabel("Emotion: ");
		inputValuesPanel.add(emotionLabel);
		String[] emotions = {"sadness", "joy", "anger", "fear"};
		JComboBox emotionBox = new JComboBox(emotions);
		inputValuesPanel.add(emotionBox);
		
		JLabel popSizeLabel = new JLabel("Population size: ");
		inputValuesPanel.add(popSizeLabel);
		SpinnerModel popSizeValue = new SpinnerNumberModel(1000, 500, 3000, 100);
		JSpinner popSizeSpinner = new JSpinner(popSizeValue);
		inputValuesPanel.add(popSizeSpinner);
		
		JLabel genNumLabel = new JLabel("Number of generations: ");
		inputValuesPanel.add(genNumLabel);
		SpinnerModel genNumValue = new SpinnerNumberModel(50, 10, 100, 1);
		JSpinner genNumSpinner = new JSpinner(genNumValue);
		inputValuesPanel.add(genNumSpinner);
		
		JPanel progressPanel = new JPanel();
		progressBar = new JProgressBar(0, (int)genNumValue.getValue());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(40,40,160,30);
		progressPanel.add(progressBar, BorderLayout.CENTER);	
		progressBar.setVisible(false);
		
		JPanel resultPanel = new JPanel();
		JLabel poemLabel = new JLabel("");
		resultPanel.add(poemLabel, BorderLayout.CENTER);
		resultPanel.setPreferredSize(new Dimension(300, 100));
		
		JPanel startBtnPanel = new JPanel();
		JButton startGABtn = new JButton("Start Genetic Algorithm");
		startGABtn.setPreferredSize(new Dimension(200, 40));
		startBtnPanel.add(startGABtn, BorderLayout.CENTER);
		add(startBtnPanel);
		add(progressPanel);
		add(resultPanel);
		
		startGABtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent ev) {
				progressBar.setVisible(true);
				progressBar.setMaximum((int)genNumValue.getValue()-1);
				startGABtn.setEnabled(false);
				Thread thread = new Thread() {
					@Override public void run() {
						bestPoem = GeneticAlgorithm.runGeneticAlgorithm((int)pLinesValue.getValue(), metres[metreBox.getSelectedIndex()] , 
								emotions[emotionBox.getSelectedIndex()], (int)popSizeValue.getValue(), (int)genNumValue.getValue());
						progressBar.setVisible(false);
						poemLabel.setText(bestPoem.toHTMLString());
					}
				};
				thread.start();
			}
		});
		
		Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);
		mainPanel.setBorder(padding);
		//getContentPane().setPreferredSize(new Dimension(300, 400));
		pack();
	}
	
	public static void main(String[] args) {
		JFrame mainWindow = new UI();
		mainWindow.setVisible(true);
	}
}
