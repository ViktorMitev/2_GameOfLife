package viki.programming.saveload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import viki.programming.gameoflife.*;


public class SaveLoadFromProperty implements SaveLoadInterface{

	private GameSimulator model;
	private GameInterface view;	
	private String fileName;
	private Properties prop = new Properties();

	
	public SaveLoadFromProperty(GameSimulator model, GameInterface view) {
		this.model = model;
		this.view = view;
		
	}

	public void setPropertiesValue(GameSimulator model, int rows, int cols) {		
		for(int i=0; i < rows; i++) {
			for(int j=0; j < cols; j++) {
				prop.setProperty(String.valueOf(i) + "," + 
				String.valueOf(j), String.valueOf(model.getAlive(i, j)));
			}
		}
	}
	
	@Override
	public void save(GameSimulator model) {
		
		FileOutputStream fileOut = null;
		prop.clear();
		
		try {
			File file = new File(fileName);
			fileOut = new FileOutputStream(file);
			
			prop.setProperty("rows", String.valueOf(model.getRows()));
			prop.setProperty("cols", String.valueOf(model.getCols()));
			setPropertiesValue(model, model.getRows(), model.getCols());	
			prop.store(fileOut, "Game of life property");
			
			fileOut.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	@Override
	public GameSimulator load(String fileName) {
		
		model.setAllFalse();
		prop.clear();
		
		try {
			File file = new File(fileName);
			FileInputStream fileInput = new FileInputStream(file);
			prop.load(fileInput);
			
			int rows = Integer.parseInt(prop.getProperty("rows"));
			int cols = Integer.parseInt(prop.getProperty("cols"));
			
			setNewModel(rows, cols);
		
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	private void setNewModel(int rows, int cols) {
		model = new GameSimulator(rows, cols);
		
		Enumeration<Object> enuKeys = prop.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
						
			if (!(key.equals("rows")) && !(key.equals("cols"))) {										
				String value = prop.getProperty(key);
				String[] splits = key.split(",");				
				model.setAlive(Integer.parseInt(splits[0]),
						Integer.parseInt(splits[1]),
						Boolean.parseBoolean(value));
			}
		}
	}

	public String[] getAllFiles() {	
		File dir = new File(System.getProperty("user.dir"));
		
		File[] txtFiles = dir.listFiles(new FilenameFilter() {		
		  public boolean accept(File dir, String name) {
		     return name.endsWith(".properties");
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
				titles[i] = titles[i].substring(0, titles[i].length() - 11);
			} catch(NullPointerException e) {
				System.out.println("Ex");
			}
		}
		return titles;
	}
	
	public void setFileName() {
		fileName = view.inputFromSaveDialog() + ".properties";
	}
	
	public String setFileExtention(String fileName) {
		return (fileName = fileName + ".properties");
	}
	
}
