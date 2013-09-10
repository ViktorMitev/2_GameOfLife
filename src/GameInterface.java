import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameInterface extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;     //?
	public int rws = 10;
	public int cls = 10;
	GameSimulator grid = new GameSimulator(rws, cls);
	JButton[][] buttons = new JButton[rws][cls];
	Timer timer;
	public boolean isStarted = false;

	public GameInterface() {
		initUI();
	}

	public final void initUI() {
			
	    JPanel panelNorth = new JPanel();
	    JPanel panelSouth = new JPanel();
	    
		GridLayout net = new GridLayout(rws, cls);
		panelNorth.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
		panelNorth.setLayout(net);
		panelNorth.setSize(550, 550);
		
        for (int i = 0; i < rws; i++) {
			for(int j = 0; j < cls; j++) {
				buttons[i][j] = new JButton();
				String nm = Integer.toString(i) + Integer.toString(j);
				buttons[i][j].setName(nm);
				buttons[i][j].setBackground(Color.white);
				buttons[i][j].addActionListener(this);
				panelNorth.add(buttons[i][j]);
			}
        }
        
        JButton buttonStart = new JButton("Start");
        JButton buttonPause = new JButton("Pause");
        JButton buttonStop = new JButton("Stop");
        JButton buttonStepByStep = new JButton("Step By Step");
        
        buttonStart.addActionListener(startAction);
        buttonPause.addActionListener(pauseAction);
        buttonStop.addActionListener(stopAction);
        buttonStepByStep.addActionListener(stepByStepAction);
        
        panelSouth.add(buttonStart);
        panelSouth.add(buttonPause);
        panelSouth.add(buttonStepByStep);
        panelSouth.add(buttonStop);
        
        panelSouth.setBorder(BorderFactory.createEmptyBorder(2, 2, 15, 2));
        
        add(panelNorth);
        add(panelSouth, BorderLayout.SOUTH);
 
		setTitle("Game Of Life");
		setSize(650, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

	}
	
	private ActionListener startAction = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if(!isStarted) {
				isStarted = true;
				timer = new Timer();
				class StartTask extends TimerTask {
					public void run() {
						grid.Simulate();
						grid.turn();
						for (int i = 0; i < rws; i++) {
							for (int j = 0; j < cls; j++) {
								if (grid.table[i][j].getAlive() == true) {
									buttons[i][j].setBackground(Color.black);
								}
								if (grid.table[i][j].getAlive() == false) {
									buttons[i][j].setBackground(Color.white);
								}
							}
						}
					}
				};
				StartTask task = new StartTask();
				timer.schedule(task, 100, 500);
			}
		}
	};
	
	private ActionListener pauseAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isStarted == true) {
				isStarted = false;
				timer.cancel();
			}
		}
	};
	
	private ActionListener stepByStepAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isStarted == true) {
				isStarted = false;
				timer.cancel();
			}
			grid.Simulate();
			grid.turn();
			for (int i = 0; i < rws; i++) {
				for (int j = 0; j < cls; j++) {
					if (grid.table[i][j].getAlive() == true) {
						buttons[i][j].setBackground(Color.black);
					}
					if (grid.table[i][j].getAlive() == false) {
						buttons[i][j].setBackground(Color.white);
					}
				}
			}
		}
	};
	
	private ActionListener stopAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isStarted) {
				isStarted = false;
				timer.cancel();
			}
			for (int i = 0; i < rws; i++) {
				for (int j = 0; j < cls; j++) {
					grid.table[i][j].setAlive(false);
					buttons[i][j].setBackground(Color.white);
				}
			}
		}
	};
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int x, y;
		String btnName = ((JButton)e.getSource()).getName();
		x = Integer.parseInt(btnName) / 10;
		y = Integer.parseInt(btnName) % 10;
		grid.table[x][y].setAlive(true);
		buttons[x][y].setBackground(Color.black);
	}

	public static void main(String[] args) {
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameInterface ex = new GameInterface();
                ex.setVisible(true);
            }
        });

	}

}