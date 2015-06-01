package numberMaze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

public class MazeViz extends JFrame {

	private JPanel masterPane;
	private JPanel titlePane;
	private JPanel gridPane;
	private JPanel infoPane;
	private JButton btnHome;
	private JButton btnReset;
	private JButton btnNextMaze;
	private JLabel needSum;
	private JLabel sumScreen;
	private JLabel timerLabel;
	private JLabel scoreLabel;
	private Maze maze;
	private int rowSize;
	private int colSize;
	private int target;
	private int maxNum;
	private Node[][] nodes;
	private JLabel[][] gridCoord;
	private boolean isPressed;
	private int sum;
	private Timer timer;
	private int score;
	
	public MazeViz(){
		
		// Initialize the master
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		masterPane = new JPanel(new BorderLayout());
		setBounds(100, 100, 600, 500);
		isPressed = false;
		
		makeTitlePane();
		masterPane.add(titlePane, BorderLayout.CENTER);
		
		// SETTING THE CONTENT PANE HERE! IMPORTANT!
		setContentPane(masterPane);
		
	}
	
	public void makeNewMaze(){
		maze = new Maze(rowSize, colSize, maxNum);
		nodes = maze.nodeArray;
		target = maze.targetNum;
	}
	
	public void makeTitlePane(){
		// Title Pane 
		titlePane = new JPanel(new BorderLayout());
		
		// Tile animation
		JPanel animatedGrid = new JPanel();
		animatedGrid.setBorder(new EmptyBorder(5, 5, 5, 5));
		animatedGrid.setLayout(new GridLayout(6, 6, 6, 6));
		JLabel[][] animatedCoord = new JLabel[6][6];
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 6; j++){
				animatedCoord[i][j] = new JLabel();
				animatedCoord[i][j].setOpaque(true);
				animatedCoord[i][j].setForeground(Color.WHITE);
				animatedCoord[i][j].setFont(new Font("SansSerif", Font.BOLD, 48));
				animatedCoord[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				animatedCoord[i][j].setBackground(Color.DARK_GRAY);
				animatedGrid.add(animatedCoord[i][j]);
			}
		}
		Thread animate = new Thread(){
			public void run(){
				int counter = 1;
				char[] c1 = {'N', 'U', 'M', 'B', 'E', 'R'};
				char[] c2 = {' ', 'M', 'A', 'Z', 'E', ' '};
				ArrayList<Integer> rows = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
				while (true){
					
					// Reset 
					for (int i = 0; i < 6; i++){
						for (int j = 0; j < 6; j++){
							animatedCoord[i][j].setForeground(Color.WHITE);
							animatedCoord[i][j].setBackground(Color.DARK_GRAY);
							animatedCoord[i][j].setText("");
						}
					}
					try {
						sleep(250);
					} catch (InterruptedException e1) {
						System.out.println("Animation broke!");
						e1.printStackTrace();
					}
					
					int selectedRow = rows.get((int)(Math.random()*rows.size()));
					for (int i = 0; i < 6; i++){
						animatedCoord[selectedRow][i].setBackground(Color.WHITE);
						animatedCoord[selectedRow][i].setForeground(Color.DARK_GRAY);
						if (counter == 1){
							animatedCoord[selectedRow][i].setText("" + c1[i]);
						} else {
							animatedCoord[selectedRow][i].setText("" + c2[i]);
						}
					}
					counter = (counter+1)%2;
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						System.out.println("Animation broke!");
						e.printStackTrace();
					}
				}
			}
		};
		animate.start();
		titlePane.add(animatedGrid, BorderLayout.CENTER);
		titlePane.setBorder(new EmptyBorder(5, 5, 2, 2));
		
		JPanel titleUI = new JPanel();

		JLabel start = new JLabel("CLICK! Let's Go!");
		JTextField rowIn = new JTextField("5");
		JLabel rowLab = new JLabel(" Rows:");
		JTextField colIn = new JTextField("5");
		JLabel colLab = new JLabel(" Cols:");
		JTextField maxIn = new JTextField("10");
		JLabel maxLab = new JLabel(" Max:");
		
		JComponent[] titleUIComp = {rowLab, rowIn, colLab, colIn, maxLab, maxIn, start};
		for (int i = 0; i < titleUIComp.length; i++){
			titleUIComp[i].setFont(new Font("SansSerif", Font.BOLD, 18));
			titleUI.add(titleUIComp[i]);
		}
		
		start.addMouseListener(
				new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							// It has started! Initializing all maze-related instance variables
							rowSize = Integer.parseInt(rowIn.getText());
							colSize = Integer.parseInt(colIn.getText());
							maxNum = Integer.parseInt(maxIn.getText());
							makeNewMaze();
							timer = new Timer();
							
							// Changing panels
							makeGridPane();
							makeInfoPane(); 
							masterPane.remove(titlePane);
							masterPane.add(gridPane, BorderLayout.CENTER);
							masterPane.add(infoPane, BorderLayout.SOUTH);
							masterPane.updateUI();
							
						} catch (NumberFormatException e1) {
							System.out.println("Problem?");
							start.setForeground(Color.WHITE);
							start.setBackground(Color.RED);
						}
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// Do nothing
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						// Do nothing
					}
					@Override
					public void mousePressed(MouseEvent arg0) {
						// Do nothing
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// Do nothing
					}
				}
				);

		titlePane.add(titleUI, BorderLayout.SOUTH);
	}
	
	public void makeGridPane(){

		gridPane = new JPanel();
		gridPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		gridPane.setLayout(new GridLayout(rowSize, colSize, 6, 6));
		gridCoord = new JLabel[rowSize][colSize];
		// Set up the array of nodes
		for (int i = 0; i < rowSize; i++){
			for (int j = 0; j < colSize; j++){
				// The visuals
				gridCoord[i][j] = new JLabel("" + nodes[i][j].number);
				gridCoord[i][j].setOpaque(true);
				gridCoord[i][j].setForeground(Color.WHITE);
				gridCoord[i][j].setFont(new Font("SansSerif", Font.BOLD, 48));
				gridCoord[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				gridCoord[i][j].setBackground(Color.DARK_GRAY);

				// The action listeners
				final int a = i;
				final int b = j;
				gridCoord[a][b].addMouseMotionListener(
						new MouseMotionListener(){

							@Override
							public void mouseDragged(MouseEvent e) {

							}
							@Override
							public void mouseMoved(MouseEvent e) {
								// Do nothing
							}
						}
						);
				gridCoord[a][b].addMouseListener(
						new MouseListener(){

							@Override
							public void mouseClicked(MouseEvent e) {
								if (!gridCoord[a][b].getBackground().equals(Color.DARK_GRAY)&&
										!gridCoord[a][b].getBackground().equals(Color.GREEN)){
									gridCoord[a][b].setBackground(Color.DARK_GRAY);
									gridCoord[a][b].setForeground(Color.WHITE);
									System.out.println("Sum before: " + sum);
									sumScreen.firePropertyChange("sum", sum, sum - nodes[a][b].number);
									sum = sum - nodes[a][b].number;
									System.out.println("subtracted!");
									System.out.println("Sum after: " + sum);
									if (!gridCoord[0][0].getBackground().equals(Color.DARK_GRAY)&&
										!gridCoord[rowSize-1][colSize-1].getBackground().equals(Color.DARK_GRAY)){
										System.out.println(sum);
										System.out.println(target);
										if (sum != target){
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)){
														gridCoord[i][j].setBackground(Color.RED);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
										} else {
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)||
															gridCoord[i][j].getBackground().equals(Color.RED)){
														gridCoord[i][j].setBackground(Color.GREEN);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
											timer.pauseCount();
											scoreLabel.firePropertyChange("score", score, score+1);
											score++;
											btnNextMaze.setVisible(true);
											sumScreen.setForeground(Color.GREEN);
										}
									}
								}
							}
							@Override
							public void mouseEntered(MouseEvent e) {
								if (isPressed && gridCoord[a][b].getBackground().equals(Color.DARK_GRAY)){
									gridCoord[a][b].setBackground(Color.WHITE);
									gridCoord[a][b].setForeground(Color.DARK_GRAY);
									sumScreen.firePropertyChange("sum", sum, sum + nodes[a][b].number);
									sum = nodes[a][b].number + sum;
									System.out.println("new sum: " + sum);
									if (!gridCoord[0][0].getBackground().equals(Color.DARK_GRAY)&&
											!gridCoord[rowSize-1][colSize-1].getBackground().equals(Color.DARK_GRAY)){
										System.out.println(sum);
										System.out.println(target);
										if (sum != target){
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)){
														gridCoord[i][j].setBackground(Color.RED);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
										} else {
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)||
															gridCoord[i][j].getBackground().equals(Color.RED)){
														gridCoord[i][j].setBackground(Color.GREEN);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
											timer.pauseCount();
											scoreLabel.firePropertyChange("score", score, score+1);
											score++;
											btnNextMaze.setVisible(true);
											sumScreen.setForeground(Color.GREEN);
										}
									}
								}
							}
							@Override
							public void mouseExited(MouseEvent e) {
								// Do nothing

							}
							@Override
							public void mousePressed(MouseEvent e) {
								isPressed = true;
								if (gridCoord[a][b].getBackground().equals(Color.DARK_GRAY)){
									gridCoord[a][b].setBackground(Color.WHITE);
									gridCoord[a][b].setForeground(Color.DARK_GRAY);
									sumScreen.firePropertyChange("sum", sum, sum + nodes[a][b].number);
									sum = nodes[a][b].number + sum;
									System.out.println("new sum: " + sum);
									if (!gridCoord[0][0].getBackground().equals(Color.DARK_GRAY)&&
											!gridCoord[rowSize-1][colSize-1].getBackground().equals(Color.DARK_GRAY)){
										if (sum != target){
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)){
														gridCoord[i][j].setBackground(Color.RED);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
										} else {
											for (int i = 0; i < rowSize; i++){
												for (int j = 0; j < colSize; j++){
													if (gridCoord[i][j].getBackground().equals(Color.WHITE)||
															gridCoord[i][j].getBackground().equals(Color.RED)){
														gridCoord[i][j].setBackground(Color.GREEN);
														gridCoord[i][j].setForeground(Color.BLACK);
													}
												}
											}
											timer.pauseCount();
											scoreLabel.firePropertyChange("score", score, score+1);
											score++;
											btnNextMaze.setVisible(true);
											sumScreen.setForeground(Color.GREEN);
										}
									}
								}	
							}
							@Override
							public void mouseReleased(MouseEvent e) {
								isPressed = false;
							}

						}
						);
				if (i == 0 && j == 0) {
					gridCoord[i][j].setText("<html><body><center>START<br>" + nodes[i][j].number + "</center></body></html>");
					gridCoord[i][j].setFont(new Font("SansSerif", Font.BOLD, 18));
				} else if (i == rowSize-1 && j == colSize-1){
					gridCoord[i][j].setText("<html><body><center>FINISH<br>" + nodes[i][j].number + "</center></body></html>");
					gridCoord[i][j].setFont(new Font("SansSerif", Font.BOLD, 18));
				}
				gridPane.add(gridCoord[i][j]);
			}
		}
		
		// gridPane mouse listener
		gridPane.addMouseListener(
			new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < rowSize; i++){
						for (int j = 0; j < colSize; j++){
							if (!gridCoord[i][j].getBackground().equals(Color.DARK_GRAY)){
								gridCoord[i][j].setBackground(Color.DARK_GRAY);
								gridCoord[i][j].setForeground(Color.WHITE);
								sumScreen.firePropertyChange("sum", sum, 0);
								sum = 0;	
							}
						}
					}
					
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					// Do nothing
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// Do nothing	
				}
				@Override
				public void mousePressed(MouseEvent e) {
					isPressed = true;
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					isPressed = false;
				}
			}
		);
	}
	
	public void makeInfoPane(){
		
		infoPane = new JPanel();
		infoPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		timerLabel = new JLabel();
		timerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		timerLabel.setText("Time:" + timer.getCount());
		scoreLabel = new JLabel();
		scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		scoreLabel.setText(" Score:" + score);
		needSum = new JLabel();
		needSum.setFont(new Font("SansSerif", Font.BOLD, 24));
		needSum.setText("/" + target);
		sumScreen = new JLabel();
		sumScreen.setFont(new Font("SansSerif", Font.BOLD, 24));
		sumScreen.setForeground(Color.RED);
		sumScreen.setText(" " + 0);
		btnReset = new JButton("Reset");
		btnNextMaze = new JButton("Next!");
		btnNextMaze.setVisible(false);
		btnHome = new JButton("Home");
		infoPane.add(btnHome);
		infoPane.add(timerLabel);
		infoPane.add(scoreLabel);
		infoPane.add(sumScreen);
		infoPane.add(needSum);
		infoPane.add(btnReset);
		infoPane.add(btnNextMaze);
		
		// Info Pane related change listeners
		
		// Time mechanism
		timer.getPCS().addPropertyChangeListener("count", 
				new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				timerLabel.setText("Time:" + e.getNewValue());
				if ((int)e.getNewValue() == 5)
					timerLabel.setForeground(Color.RED);
				// GAME OVER BRO
				if ((int)e.getNewValue() == 0){
					scoreLabel.firePropertyChange("score", score, 0);
					score = 0;
					String[] gameOver = {"G","A","M","E","O","V","E","R"};
					int gameOverIterator = 0;
					for (int i = 0; i < rowSize; i++){
						for (int j = 0; j < colSize; j++){
							gridCoord[i][j].setBackground(Color.RED);
							gridCoord[i][j].setForeground(Color.BLACK);
							gridCoord[i][j].setFont(new Font("SansSerif", Font.BOLD, 48));
							gridCoord[i][j].setText(gameOver[gameOverIterator]);
							gameOverIterator = (gameOverIterator+1)%8;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
					btnNextMaze.setText("Try Again?");
					btnNextMaze.setVisible(true);
				}
			}
		});
		
		sumScreen.addPropertyChangeListener("sum", 
				new PropertyChangeListener(){

					@Override
					public void propertyChange(PropertyChangeEvent e) {
						sumScreen.setText(" " + e.getNewValue());
					}
			
		});
		
		scoreLabel.addPropertyChangeListener("score", 
				new PropertyChangeListener(){

					@Override
					public void propertyChange(PropertyChangeEvent e) {
						scoreLabel.setText(" Score:" + e.getNewValue());
					}
			
		});
		
		btnHome.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// Changing panels
						makeTitlePane();
						masterPane.remove(gridPane);
						masterPane.remove(infoPane);
						masterPane.add(titlePane);
						sum = 0;
						score = 0;
						timer.selfDestruct();
						timer = null;
						masterPane.updateUI();
					}
					
				});
		btnReset.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						for (int i = 0; i < rowSize; i++){
							for (int j = 0; j < colSize; j++){
								if (!gridCoord[i][j].getBackground().equals(Color.DARK_GRAY)){
									gridCoord[i][j].setBackground(Color.DARK_GRAY);
									gridCoord[i][j].setForeground(Color.WHITE);
									sumScreen.firePropertyChange("sum", sum, 0);
									sum = 0;
								}
							}
						}
					}
				}
				);
		
		btnNextMaze.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						masterPane.remove(gridPane);
						makeNewMaze();
						makeGridPane();
						masterPane.add(gridPane, BorderLayout.CENTER);
						needSum.setText("/" + target);
						sumScreen.firePropertyChange("sum", sum, 0);
						btnNextMaze.setVisible(false);
						btnNextMaze.setText("Next!");
						timerLabel.setForeground(Color.BLACK);
						sum = 0;
						timer.resetCount();
						System.out.println("RESET!?");
						masterPane.updateUI();
					}
					
				});
	}
}
