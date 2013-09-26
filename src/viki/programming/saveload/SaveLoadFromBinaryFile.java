package viki.programming.saveload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import viki.programming.gameoflife.*;


public class SaveLoadFromBinaryFile implements SaveLoadInterface {

	private GameSimulator model;
	private GameInterface view;

	private String fileName;
	
	public SaveLoadFromBinaryFile(GameSimulator model, GameInterface view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void save(GameSimulator model) {
		try {
            ObjectOutputStream myStream = new ObjectOutputStream(new FileOutputStream(fileName));
            myStream.writeObject(model.getTable());
            myStream.close();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();        	
        } catch (IOException e) {
           	e.printStackTrace();
        }
	
	}
	
	@Override
	public GameSimulator load(String fileName) {
		ObjectInputStream mySecondStream = null;
		
		model.setAllFalse();
		
		try {	
            mySecondStream = new ObjectInputStream(new FileInputStream(fileName));
			try {
				boolean[][] coArray = (boolean[][]) mySecondStream.readObject();
				setNewModel(coArray.length, coArray[0].length, coArray);
	        	mySecondStream.close();
	        	
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
           	e.printStackTrace();
        }
		
		return model; 
	}

	private void setNewModel(int rows, int cols, boolean[][] coArray) {
		model = new GameSimulator(rows, cols);
		model.setTable(coArray);  
	}

	public String[] getAllFiles() {
		File dir = new File(System.getProperty("user.dir"));
		
		File[] txtFiles = dir.listFiles(new FilenameFilter() {		
		  public boolean accept(File dir, String name) {
		     return name.endsWith(".dat");
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
		fileName = view.inputFromSaveDialog() + ".dat";
	}
	
	public String setFileExtention(String fileName) {
		return (fileName = fileName + ".dat");
	}
	
}
