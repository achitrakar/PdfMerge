package com.sample.pdf;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.apache.pdfbox.util.PDFMergerUtility;

/**
 * author achitrakar, 2014
 */
public class PDFMerge {
	private static String destinationFileName = "";

	private static List<String> sourceFiles = null;

	static JButton sourceFileSelectionButton = new JButton("Choose Files to Merge");

	static JButton destinationFolderSelectionButton = new JButton("Choose Destination");
	static JButton mergeButton = new JButton("Merge");

	static JTextArea sourceTextArea = new JTextArea("", 10, 30);
	static JTextArea destTextArea = new JTextArea("", 10, 30);

	static JLabel source = new JLabel("Sources");
	static JLabel destination = new JLabel("Destination");

	public static void main(String[] args) throws Exception {
		createAndShowGUI();
	}

	public static void mergePDFs(List<String> fileName, String dest) throws Exception {
		System.out.println("************* Merging PDFs Status: Started ***************");
		List<InputStream> sourcePDFs = new ArrayList<InputStream>();
		for (String fileURL : fileName) {
			sourcePDFs.add(new FileInputStream(new File(fileURL)));
		}
		PDFMergerUtility mergerUtility = new PDFMergerUtility();
		mergerUtility.addSources(sourcePDFs);

		mergerUtility.setDestinationFileName(dest);

		mergerUtility.mergeDocuments();

		System.out.println("************* Merging PDFs Status: Completed *************");
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		final JFrame frame = new JFrame("PDF Merger");
		// Display the window.
		frame.setSize(200, 200);

		sourceTextArea.setEditable(false);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set flow layout for the frame
		frame.getContentPane().setLayout(new FlowLayout());

		sourceFileSelectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createFileChooser(frame);

			}
		});
		destinationFolderSelectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createDestinationChooser(frame);
			}
		});

		mergeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mergePDFs(sourceFiles, destinationFileName);
					JOptionPane.showMessageDialog(frame, "Successfully Merged", "Success", JOptionPane.PLAIN_MESSAGE);

				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Something wrong with the system input...", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		frame.add(source);

		frame.add(sourceTextArea);

		frame.getContentPane().add(sourceFileSelectionButton);

		frame.add(destination);

		frame.add(destTextArea);

		frame.getContentPane().add(destinationFolderSelectionButton);

		frame.getContentPane().add(mergeButton);
	}

	private static void createFileChooser(final JFrame frame) {
		String filename = File.separator + "tmp";
		JFileChooser fileChooser = new JFileChooser(new File(filename));

		fileChooser.setMultiSelectionEnabled(true);
		// pop up an "Open File" file chooser dialog
		fileChooser.showOpenDialog(null);
		File[] files = fileChooser.getSelectedFiles();
		StringBuilder sourceFileNames = new StringBuilder();
		sourceFiles = new ArrayList<String>();
		for (File file : files) {
			System.out.println("File to Merge: " + file.getAbsolutePath());
			sourceFiles.add(file.getAbsolutePath());
			sourceFileNames.append(file.getAbsolutePath()).append("\n");
		}
		sourceTextArea.setText(sourceFileNames.toString());
	}

	/*
	Create Destination Directory to save file.
	*/
	private static void createDestinationChooser(final JFrame frame) {
		String filename = File.separator + "tmp";
		JFileChooser fileChooser = new JFileChooser(new File(filename));
		// pop up an "Open File" file chooser dialog
		int userSelection = fileChooser.showSaveDialog(null);

		fileChooser.setDialogTitle("Specify a file to save");

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			destinationFileName = fileToSave.getAbsolutePath();

			System.out.println("Save as file: " + destinationFileName);
		}
		destTextArea.setText(destinationFileName);
	}

}
