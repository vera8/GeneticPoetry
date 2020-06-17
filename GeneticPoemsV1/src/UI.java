import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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

		JPanel poemInputValuesPanel = new JPanel();
		Border grayBorder = BorderFactory.createLineBorder(Color.gray);
		Border pBorder = BorderFactory.createTitledBorder("Poem Parameters");
		Border margin = new EmptyBorder(10,10,10,10);
		poemInputValuesPanel.setBorder(new CompoundBorder(pBorder, margin));
		add(poemInputValuesPanel);
		poemInputValuesPanel.setLayout(new GridLayout(3,2));
		//poemInputValuesPanel.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
//		c.weightx = 1;
//		c.weighty = 1;
//		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel pLinesLabel = new JLabel("Number of verses: ");
//		c.gridx = 0; c.gridwidth = 1; c.gridy = 1;
		poemInputValuesPanel.add(pLinesLabel);
		SpinnerModel pLinesValue = new SpinnerNumberModel(4, 2, 8, 1);
		JSpinner poemLinesSpinner = new JSpinner(pLinesValue);
//		c.gridx = 1; c.gridwidth = 1; c.gridy = 1;
		poemInputValuesPanel.add(poemLinesSpinner);
		
		JLabel metreLabel = new JLabel("Metre: ");
//		c.gridx = 0; c.gridwidth = 1; c.gridy = 2;
		poemInputValuesPanel.add(metreLabel);
		String[] metres = {"iambic", "trochaic", "anapestic", "dactylic"};
		JComboBox metreBox = new JComboBox(metres);
//		c.gridx = 1; c.gridwidth = 1; c.gridy = 2;
		poemInputValuesPanel.add(metreBox);
		
		JLabel emotionLabel = new JLabel("Emotion: ");
//		c.gridx = 0; c.gridwidth = 1; c.gridy = 3;
		poemInputValuesPanel.add(emotionLabel);
		String[] emotions = {"sadness", "joy", "anger", "fear"};
		JComboBox emotionBox = new JComboBox(emotions);
//		c.gridx = 1; c.gridwidth = 1; c.gridy = 3;
		poemInputValuesPanel.add(emotionBox);
		
		add(new JLabel(" "));
		
		JPanel gaInputValuesPanel = new JPanel();
		gaInputValuesPanel.setLayout(new GridLayout(6,2));
		Border gaBorder = BorderFactory.createTitledBorder("Genetic Algorithm Parameters");
		gaInputValuesPanel.setBorder(new CompoundBorder(gaBorder, margin));
		add(gaInputValuesPanel);
		//gaLabel.setPreferredSize(new Dimension(200, 40));
//		c.gridx = 0; c.gridwidth = 2; c.gridy = 0;
		
		JLabel popSizeLabel = new JLabel("Population size: ");
//		c.gridx = 0; c.gridwidth = 1; c.gridy = 1;
		gaInputValuesPanel.add(popSizeLabel);
		SpinnerModel popSizeValue = new SpinnerNumberModel(1000, 50, 3000, 50);
		JSpinner popSizeSpinner = new JSpinner(popSizeValue);
//		c.gridx = 1; c.gridwidth = 1; c.gridy = 1;
		gaInputValuesPanel.add(popSizeSpinner);
		
		JLabel genNumLabel = new JLabel("Number of generations: ");
//		c.gridx = 0; c.gridwidth = 1; c.gridy = 2;
		gaInputValuesPanel.add(genNumLabel);
		SpinnerModel genNumValue = new SpinnerNumberModel(50, 10, 100, 1);
		JSpinner genNumSpinner = new JSpinner(genNumValue);
//		c.gridx = 1; c.gridwidth = 1; c.gridy = 2;
		gaInputValuesPanel.add(genNumSpinner);
		
		JLabel recombRateLabel = new JLabel("Recombination probablilty: ");
		gaInputValuesPanel.add(recombRateLabel);
		SpinnerModel recombRateValue = new SpinnerNumberModel(0.90, 0.0, 1.0, 0.01);
		JSpinner recombRateSpinner = new JSpinner(recombRateValue);
		gaInputValuesPanel.add(recombRateSpinner);
		
		JLabel mutateRateLabel = new JLabel("Mutation probablilty: ");
		gaInputValuesPanel.add(mutateRateLabel);
		SpinnerModel mutateRateValue = new SpinnerNumberModel(0.1, 0.0, 1.0, 0.001);
		JSpinner mutateRateSpinner = new JSpinner(mutateRateValue);
		gaInputValuesPanel.add(mutateRateSpinner);
		
		JLabel runsLabel = new JLabel("Number of runs: ");
		gaInputValuesPanel.add(runsLabel);
		SpinnerModel runsValue = new SpinnerNumberModel(1, 1, 20, 1);
		JSpinner runsSpinner = new JSpinner(runsValue);
		gaInputValuesPanel.add(runsSpinner);
		
		JCheckBox showGraphs = new JCheckBox("Show graphs", true);
		gaInputValuesPanel.add(showGraphs);
		gaInputValuesPanel.add(new JLabel(" "));
		
		JPanel progressPanel = new JPanel();
		progressBar = new JProgressBar(0, (int)genNumValue.getValue());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(40,40,160,30);
		progressPanel.add(progressBar, BorderLayout.CENTER);	
		progressBar.setVisible(false);
		
		JPanel resultPanel = new JPanel();
		JLabel poemLabel = new JLabel("");
		JTextArea poemArea = new JTextArea();
		poemArea.setPreferredSize(new Dimension(300, 200));
		poemArea.setEditable(false);
		resultPanel.add(poemArea, BorderLayout.CENTER);
		//resultPanel.setPreferredSize(new Dimension(300, 100));
		
		JButton saveBtn = new JButton("Save Poem");
		saveBtn.setEnabled(false);
		
		JPanel startBtnPanel = new JPanel();
		JButton startGABtn = new JButton("Start Genetic Algorithm");
		startGABtn.setPreferredSize(new Dimension(200, 40));
		startBtnPanel.add(startGABtn, BorderLayout.CENTER);
		add(startBtnPanel);
		add(progressPanel);
		add(resultPanel);
		add(saveBtn);
		
		startGABtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent ev) {
				progressBar.setVisible(true);
				progressBar.setMaximum(((int)genNumValue.getValue()-1)*(int)runsValue.getValue());
				startGABtn.setEnabled(false);
				saveBtn.setEnabled(false);

				Thread thread = new Thread() {
					@Override public void run() {
						bestPoem = GeneticAlgorithm.runGeneticAlgorithm((int)pLinesValue.getValue(), metres[metreBox.getSelectedIndex()] , 
								emotions[emotionBox.getSelectedIndex()], (int)popSizeValue.getValue(), (int)genNumValue.getValue(), 
								(double)recombRateValue.getValue(), (double)mutateRateValue.getValue(), (int)runsValue.getValue(), 
								showGraphs.isSelected());
						progressBar.setVisible(false);
						poemArea.setText(bestPoem.toString());
						startGABtn.setEnabled(true);
						saveBtn.setEnabled(true);
					}
				};
				thread.start();
			}
		});
		
		saveBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent ev) {
				JFrame parentFrame = new JFrame();
				JFileChooser filechooser = new JFileChooser();
				int selection = filechooser.showSaveDialog(parentFrame);
				if (selection == JFileChooser.APPROVE_OPTION) {
					File saveFile = filechooser.getSelectedFile();
					try {
						PrintWriter writer = new PrintWriter(saveFile);
						writer.println(poemArea.getText());
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
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
