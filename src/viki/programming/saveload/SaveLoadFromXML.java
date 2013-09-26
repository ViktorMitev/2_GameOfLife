package viki.programming.saveload;

import java.io.FilenameFilter;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;


import viki.programming.gameoflife.*;


public class SaveLoadFromXML implements SaveLoadInterface {

	private GameSimulator model;
	private GameInterface view;	

	private String fileName;
	
	public SaveLoadFromXML(GameSimulator model, GameInterface view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void save(GameSimulator model) {
		try {			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("matrix");
			doc.appendChild(rootElement);
	 	 
			Element rows = doc.createElement("rows");
			rows.appendChild(doc.createTextNode(String.valueOf(model.getRows())));
			rootElement.appendChild(rows);
			
			Element cols = doc.createElement("cols");
			cols.appendChild(doc.createTextNode(String.valueOf(model.getCols())));
			rootElement.appendChild(cols);
			
			String newCol = "";
			for(int i=0; i < model.getRows(); i++) {
				Element row = doc.createElement("row" + Integer.toString(i));
				rootElement.appendChild(row);
				for(int j=0; j < model.getCols(); j++) {
					newCol = "col" + Integer.toString(i) + Integer.toString(j);
					Element col = doc.createElement(newCol);
					col.appendChild(doc.createTextNode(Boolean.toString(model.getAlive(i,j))));
					row.appendChild(col);
				}
			}
	 
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
	 
			transformer.transform(source, result);
	 
		  } catch (ParserConfigurationException e) {
			e.printStackTrace();
		  } catch (TransformerException e) {
			e.printStackTrace();
		  }
	}


	@Override
	public GameSimulator load(String fileName) {
        Document dom;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(fileName);

            Element doc = dom.getDocumentElement();
            
            int rows = Integer.parseInt(getTextValue(doc, "rows"));
            int cols = Integer.parseInt(getTextValue(doc, "cols"));
            
            setNewModel(doc, rows, cols);

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
		return model;
	}
	
	private void setNewModel(Element doc, int rows, int cols) {
        String state;
        model = new GameSimulator(rows, cols);
        
		for(int i=0; i < model.getRows(); i++) {
			for(int j=0; j < model.getCols(); j++) {
				state = getTextValue(doc, "col" + Integer.toString(i) + 
						Integer.toString(j));
				model.setAlive(i, j, Boolean.parseBoolean(state));
			}
		}
	}
	
	private String getTextValue(Element doc, String tag) {
	    String value = "";
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}

	public String[] getAllFiles() {
		File dir = new File(System.getProperty("user.dir"));
		
		File[] txtFiles = dir.listFiles(new FilenameFilter() {		
		  public boolean accept(File dir, String name) {
		     return name.endsWith(".xml");
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
		fileName = view.inputFromSaveDialog() + ".xml";
	}
	
	public String setFileExtention(String fileName) {
		return (fileName = fileName + ".xml");
	}

}
