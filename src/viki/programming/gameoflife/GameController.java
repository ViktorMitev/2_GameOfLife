package viki.programming.gameoflife;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import viki.programming.saveload.*;


public class GameController {
		
	private GameSimulator model;
	private GameInterface view;

	private SaveLoadFromProperty saveLoad;
	//private SaveLoadFromBinaryFile saveLoad;
	//private SaveLoadFromXML saveLoad;
	//private SaveLoadFromTextFile saveLoad;
	
	public Timer timer;
	public boolean isStarted = false; 
	
	public GameController(GameSimulator model, GameInterface view, SaveLoadFromProperty saveLoad) {
		
		this.view = view;
		this.model = model;
		this.saveLoad = saveLoad;

		this.addGridButtonsListeners();
		this.addControlButtonsListeners();
		
	}
	
	public void addGridButtonsListeners() {
	     for (int i = 0; i < model.getRows(); i++) {
				for(int j = 0; j < model.getCols(); j++) {
					view.addClickListener(i, j, clickListener);
				}
	     }
	}
	
	public void addControlButtonsListeners() {
		view.addStartListener(startListener);
		view.addStepByStepListener(stepByStepListener);
		view.addStopListener(stopListener);
		view.addPauseListener(pauseListener);
		view.addChangeSizeListener(changeSizeListener);
		
		view.addSaveMenuListener(saveListener);
		view.addLoadMenuListener(loadListener);
		view.addExitMenuListener(exitListener);
	}
	
	public void stopGame() {
			timer.cancel();
			isStarted = false;
	}
	
	private ActionListener saveListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(isStarted) {
				stopGame();
			}
			saveLoad.setFileName();
			saveLoad.save(model);
			
			model.setAllFalse();
			view.paint(model);
		}
		
	};
	
	private ActionListener loadListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(isStarted) {
				stopGame();
			}
			String titles[] = saveLoad.getAllFiles();
			String fileName = view.inputFromLoadDialog(titles);
			if(fileName.compareTo("") != 0) {
				fileName = saveLoad.setFileExtention(fileName); 
				model = saveLoad.load(fileName);
					
				createNewWindow();
				view.paint(model);
				
			}
		}
		
	};
	
	
	private ActionListener exitListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	};

	
	private ActionListener clickListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			int x, y;
			String[] splits;
			
			String btnName = ((JButton)e.getSource()).getName();
			splits = btnName.split(",");		
			x = Integer.parseInt(splits[0]);
			y = Integer.parseInt(splits[1]);
			
			if(!isStarted) {
				model.setAlive(x, y, !model.getAlive(x, y));
				view.updateCell(model, x, y);
			}
			else {
				JOptionPane.showMessageDialog(view.getBoard(),
						" The game is started! Click \"Stop\" to press new keys! ", "Op!",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		
	};
	
	private ActionListener startListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(!isStarted) {
				isStarted = true;
				timer = new Timer();
				StartTask task = new StartTask();
				timer.schedule(task, 10, 500);
			}
		}
	};
	
	class StartTask extends TimerTask {
		
		public void run() {
			model.simulate();
			view.paint(model);
		}
		
	};
	
	private ActionListener pauseListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(isStarted == true) {
				isStarted = false;
				timer.cancel();
			}
		}
	};
	
	private ActionListener stepByStepListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(isStarted == true) {
				isStarted = false;
				timer.cancel();
			}
			model.simulate();
			view.paint(model);
		}
	};
	
	private ActionListener stopListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(isStarted) {
				isStarted = false;
				timer.cancel();
			}
			model.setAllFalse();
			view.paint(model);
		}
	};
	
	private ActionListener changeSizeListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(!isStarted) {		
				int rs = 0, cs = 0;
				String buf;
				
				buf = view.getInputRows();
				rs = checkForDigits(buf);
				
				buf = view.getInputCols();
				cs = checkForDigits(buf);
				
				checkForRightInput(rs, cs);
			}
		}
		
	};
	
	public void checkForRightInput(int rs, int cs) {
		boolean isTooBig = checkForTooBigInput(rs, cs);
		
		if(isTooBig) {
			showTooBigInputError();
		}
		if(rs > 0 && cs > 0 && !isTooBig) {
			model = new GameSimulator(rs, cs);
			createNewWindow();
		}
		if(rs == 0 || cs == 0) {
			showCharacterInputError();
		}

	}
	
	private void showTooBigInputError() {
		JOptionPane.showMessageDialog(view.getBoard(),
				" Values must be less or equal than 40! ", "Op!",
				JOptionPane.WARNING_MESSAGE);
		view.setInputRows("");
		view.setInputCols("");
	}
	
	private void showCharacterInputError() {
		JOptionPane.showMessageDialog(view.getBoard(),
				"-Please, input only digits \n-Don't leave empty fields!", "Op!",
				JOptionPane.WARNING_MESSAGE);
		view.setInputRows("");
		view.setInputCols("");
	}
	
	public boolean checkForTooBigInput(int rs, int cs) {
		if (rs > 40 || cs > 40) {
			return true;
		}
		return false;
	}
	
	public int checkForDigits(String buf) {
		int num = 0;
		if (buf.matches("[0-9]+") && !buf.isEmpty()) {
			num = Integer.parseInt(buf);
		}
		return num;
	}
	
	public void createNewWindow() {
		view.getBoard().setVisible(false);
		view = new GameInterface(model);
		view.display();
		
		addGridButtonsListeners();
		addControlButtonsListeners();
		
	}

}
