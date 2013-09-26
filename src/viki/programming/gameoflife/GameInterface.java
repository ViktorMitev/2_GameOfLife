package viki.programming.gameoflife;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@Target(ElementType.METHOD)
 @interface Action {
	 String actionName();
 }

public class GameInterface {
	
	public static final Color COLOR_ALIVE = Color.black;
	public static final Color COLOR_DEAD = Color.white;
	
	private JButton[][] buttons;
	
    private JFrame board;
    
    private JPanel panelNorth = new JPanel();

	private JPanel panelSouth = new JPanel();
    private JPanel panelInput = new JPanel();
    
    private JButton buttonStart = new JButton("Start");
    private JButton buttonPause = new JButton("Pause");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonStepByStep = new JButton("Step By Step");

	private JMenuItem saveItem = new JMenuItem("Save As");
    private JMenuItem loadItem = new JMenuItem("Load");
    private JMenuItem exitItem = new JMenuItem("Exit");
    
    private JTextField inputRows = new JTextField(3);
	private JTextField inputCols = new JTextField(3);
    private JLabel labelRows = new JLabel("Rows: ");
	private JLabel labelCols = new JLabel("Columns: ");
    private JButton buttonChange = new JButton("Change size");
    
    
    public JFrame getBoard() {
		return board;
	}
    
    public JPanel getPanelNorth() {
		return panelNorth;
	}

	public void setBoard(JFrame board) {
		this.board = board;
	}

	public void display() {
		board.setVisible(true);
	}
	
    public String getInputRows() {
		return inputRows.getText();
	}

	public void setInputRows(String text) {
		this.inputRows.setText(text);
	}
	
    public String getInputCols() {
		return inputCols.getText();
	}

	public void setInputCols(String text) {
		this.inputCols.setText(text);
	}
	
	
	public GameInterface(GameSimulator grid) {
		initUI(grid);
	}
	
	public ImageIcon getIcon() {
		File dir = new File(System.getProperty("user.dir"));
		
		File[] iconFiles = dir.listFiles(new FilenameFilter() {		
			  public boolean accept(File dir, String name) {
			     return name.matches("irregular-game-of-life.jpg");
			  }
		});
		
		ImageIcon img = new ImageIcon(iconFiles[0].toString());
		return img;
	}
	
	public final void initUI(GameSimulator grid) {
					
		board = new JFrame();
			
		ImageIcon image = this.getIcon();
		board.setIconImage(image.getImage());

        JMenuBar menubar = new JMenuBar();
        
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setToolTipText("Save Game Of Life");
        
        loadItem.setMnemonic(KeyEvent.VK_L);
        loadItem.setToolTipText("Load Game Of Life");

        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit from Game Of Life");
	    
		GridLayout net = new GridLayout(grid.getRows(), grid.getCols());
		panelNorth.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
		panelNorth.setLayout(net);
		panelNorth.setSize(550, 550);
		
		buttons = new JButton[grid.getRows()][grid.getCols()];
		
		setButtonsToPanel(grid);
        
        file.add(saveItem);
        file.add(loadItem);
        file.addSeparator();
        file.add(exitItem);
        
        menubar.add(file);
        board.setJMenuBar(menubar);
  
        panelSouth.add(buttonStart);
        panelSouth.add(buttonPause);
        panelSouth.add(buttonStepByStep);
        panelSouth.add(buttonStop);
        
        panelSouth.setBorder(BorderFactory.createEmptyBorder(2, 2, 20, 2));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
              
        panelInput.add(labelRows);
        panelInput.add(inputRows);
        panelInput.add(labelCols);
        panelInput.add(inputCols);
        panelInput.add(buttonChange);
        
        board.add(panelNorth, BorderLayout.CENTER);
        board.add(panelInput, BorderLayout.NORTH);
        board.add(panelSouth, BorderLayout.SOUTH);

        board.setTitle("Game Of Life");
        board.setSize(700, 600);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setLocationRelativeTo(null);
        board.setResizable(false);

	}

	private void setButtonsToPanel(GameSimulator grid) {
		for (int i = 0; i < grid.getRows(); i++) {
			for (int j = 0; j < grid.getCols(); j++) {
				buttons[i][j] = new JButton();
				String nm = Integer.toString(i) + "," + Integer.toString(j);
				buttons[i][j].setName(nm);
				buttons[i][j].setBackground(Color.white);
				panelNorth.add(buttons[i][j]);
			}
		}	
	}
	
	public String showSaveDialog(String input) {
		input = JOptionPane.showInputDialog(board, "Save as: ", "Save",
				JOptionPane.PLAIN_MESSAGE);
		
		if (input.equals("")) {
			JOptionPane.showMessageDialog(board,
					"Must input save name!", "Save Error",
					JOptionPane.WARNING_MESSAGE);
		}
		
		return input;
	}
	
	public String inputFromSaveDialog() {
		String input = "";
		try {
			while (input.equals("")) {
				input = showSaveDialog(input);
			}
			return input;
		} catch (NullPointerException e) {

		}
		
		return "";
	}
	
	public String showLoadDialog(String name, String[] titles) {
		try {
			name = JOptionPane.showInputDialog(board, "Load from:",
					"Load", JOptionPane.PLAIN_MESSAGE, null, titles,
					null).toString();
		} catch (NullPointerException e) {
			System.out.println("NullPointerException, but OK!");
		}	
		return name;
	}
	
	public String inputFromLoadDialog(String[] titles) {
		String name = "";

		if (titles.length != 0) {
			name = showLoadDialog(name, titles);					
		} else {
			JOptionPane.showMessageDialog(board, "There's no loadable files!",
					"Load Error", JOptionPane.WARNING_MESSAGE);
		}
		return name;
	}
	
	
	public void paint(GameSimulator grid) {
		for (int i = 0; i < grid.getRows(); i++) {
			for (int j = 0; j < grid.getCols(); j++) {
				buttons[i][j].setBackground((grid.getAlive(i, j)) ? COLOR_ALIVE : COLOR_DEAD);
			}
		}
	}
	
	public void updateCell(GameSimulator grid, int i, int j) {		
		buttons[i][j].setBackground((grid.getAlive(i, j)) ? COLOR_ALIVE : COLOR_DEAD);
	}
	
	
	@Action(actionName = "save")
	public void addSaveMenuListener(ActionListener save) {
		saveItem.addActionListener(save);
	}
	
	@Action(actionName = "load")
	public void addLoadMenuListener(ActionListener load) {
		loadItem.addActionListener(load);
	}
	
	@Action(actionName = "exit")
	public void addExitMenuListener(ActionListener exit) {
		exitItem.addActionListener(exit);
	}
	
	@Action(actionName = "click")
	public void addClickListener(int i, int j, ActionListener click) {
        buttons[i][j].addActionListener(click);
    }
	
	@Action(actionName = "start")
	public void addStartListener(ActionListener start) {
        buttonStart.addActionListener(start);
    }
    
	@Action(actionName = "pause")
	public void addPauseListener(ActionListener pause) {
        buttonPause.addActionListener(pause);
    }
    
	@Action(actionName = "step")
	public void addStepByStepListener(ActionListener step) {
    	buttonStepByStep.addActionListener(step);
    }
    
	@Action(actionName = "stop")
	public void addStopListener(ActionListener stop) {
        buttonStop.addActionListener(stop);
    }
    
	@Action(actionName = "change")
	public void addChangeSizeListener(ActionListener changeSizeListener) {
        buttonChange.addActionListener(changeSizeListener);
    }

}