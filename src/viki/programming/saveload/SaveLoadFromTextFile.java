package viki.programming.saveload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.Scanner;

import viki.programming.gameoflife.*;


public class SaveLoadFromTextFile implements SaveLoadInterface {
	
	private GameSimulator model;
	private GameInterface view;	

	private String fileName;
	
	public SaveLoadFromTextFile(GameSimulator model, GameInterface view) {
		this.model = model;
		this.view = view;
	}
	
	@Override	
	public void save(GameSimulator model) {
		
		PrintStream fileWriter = null;

		if (!fileName.equals(".txt")) {
			try {
				fileWriter = new PrintStream(new File(fileName));
				fileWriter.println(model.getRows() + " " + model.getCols());
				
				printInFile(fileWriter);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File is not found!");
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("Unsupported encoding!");
			} finally {
				if (fileWriter != null) {
					fileWriter.close();
				}
			}
		}
		
	} 
	
	private void printInFile(PrintStream fileWriter) {
		for (int i = 0; i < model.getRows(); i++) {
			for (int j = 0; j < model.getCols(); j++) {
				fileWriter.print(((model.getAlive(i, j)) ? "*" : "-") + " ");
			}
			fileWriter.println();
		}
	}
	

	@Override
	public GameSimulator load(String fileName) {
		int lineNumber = 0;
		Scanner fileReader = null;
		
		model.setAllFalse();
		
		try {
			fileReader = new Scanner(new File(fileName));

			while (fileReader.hasNextLine()) {				
				String line = fileReader.nextLine();
				String[] splits = line.split(" ");
				
				fillTheModel(lineNumber, splits);
				lineNumber++;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			fileReader.close();
		}
		
		return model;
	}
	
	public void fillTheModel(int lineNumber, String[] splits) {
		if(lineNumber == 0) {
			model = new GameSimulator(Integer.parseInt(splits[0]), 
					Integer.parseInt(splits[1]));						
		}
		else {
			for (int i = 0; i < model.getCols(); i++) {
				boolean tmp = (splits[i].equals("*")) ? true : false;
				model.setAlive(lineNumber-1, i, tmp);
			}
		}
	}
		

	public String[] getAllFiles() {	
		File dir = new File(System.getProperty("user.dir"));
		
		File[] txtFiles = dir.listFiles(new FilenameFilter() {		
		  public boolean accept(File dir, String name) {
		     return name.endsWith(".txt");
		  }
		});
		
		String[] titles = new String[txtFiles.length];

		titles = getTitles(txtFiles, titles);
		
		return titles;
	}
	
	private String[] getTitles(File[] txtFiles, String[] titles) {
		for (int i = 0; i < txtFiles.length; i++) {
			try {
				titles[i] = txtFiles[i].getName();
				titles[i] = titles[i].substring(0, titles[i].length() - 4);
			} catch(NullPointerException e) {
				//Not going to happen
			}
		}
		return titles;
	}
	
	public void setFileName() {
		fileName = view.inputFromSaveDialog() + ".txt";
	}	
	
	public String setFileExtention(String fileName) {
		return (fileName = fileName + ".txt");
	}
	
}
