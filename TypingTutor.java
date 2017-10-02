// Developed by Shivang Agarwal

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class TypingTutor extends JFrame implements ActionListener, KeyListener {

	private int highScore = 0;
	private JLabel lblTimer;
	private JLabel lblScore;
	private JLabel lblWord;
	private JTextField txtWord;
	private JButton btnStart;
	private JButton btnStop;

	private Timer clocktimer = null;
	private Timer wordtimer = null;
	private boolean running = false;
	private int timeRemaining = 0;
	private int score = 0;
	private String[] words = null;

	public TypingTutor(String[] args) {
		words = args;
		GridLayout layout = new GridLayout(3, 2);
		super.setLayout(layout);

		Font font = new Font("Comic Sans MS", 1, 120);

		lblTimer = new JLabel("Time");
		lblTimer.setFont(font);
		super.add(lblTimer);

		lblScore = new JLabel("Score");
		lblScore.setFont(font);
		super.add(lblScore);

		lblWord = new JLabel("");
		lblWord.setFont(font);
		super.add(lblWord);

		txtWord = new JTextField("");
		txtWord.setFont(font);
		txtWord.addKeyListener(this);
		super.add(txtWord);

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStart.setFont(font);
		super.add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(this);
		btnStop.setFont(font);
		super.add(btnStop);

		super.setTitle("Typing Tutor");
		super.setExtendedState(MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.setVisible(true);
		setAtStartup();
	}

	private void setAtStartup() {
		clocktimer = new Timer(1000, this);
		clocktimer.setInitialDelay(0);
		wordtimer = new Timer(3000, this);
		wordtimer.setInitialDelay(0);
		timeRemaining = 10;
		score = 0;
		running = false;
		lblTimer.setText("Time: " + timeRemaining);
		lblScore.setText("Score: " + score);
		lblWord.setText("");
		txtWord.setText("");
		btnStart.setText("Start");
		btnStop.setText("Stop");

		txtWord.setEnabled(false);
		btnStop.setEnabled(false);
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			handleStart();
		} else if (e.getSource() == btnStop) {
			handleStop();
		} else if (e.getSource() == wordtimer) {
			handleWordTimer();
		} else if (e.getSource() == clocktimer) {
			handleClockTimer();
		}
	}

	private void handleStart() {
		if (running == false) {
			clocktimer.start();
			wordtimer.start();
			running = true;
			btnStart.setText("Pause");
			txtWord.setEnabled(true);
			btnStop.setEnabled(true);
			txtWord.setFocusCycleRoot(true);
			super.nextFocus();
		} else {
			clocktimer.stop();
			wordtimer.stop();
			running = false;
			btnStart.setText("Start");
			txtWord.setEnabled(false);
		}
	}

	private void handleStop() {
		clocktimer.stop();
		wordtimer.stop();
		if(highScore < score){
			highScore = score;
		}
		int choice = JOptionPane.showConfirmDialog(this, "replay ?");
		if (choice == JOptionPane.YES_OPTION) {
			setAtStartup();
		} else if (choice == JOptionPane.NO_OPTION) {
			
			JOptionPane.showMessageDialog(this, "Final Score : " + score + "\n" + "High Score : " + highScore);
			super.dispose();
		} else if (choice == JOptionPane.CANCEL_OPTION) {
			if (timeRemaining == 0) {
				setAtStartup();
			} else {
				clocktimer.start();
				wordtimer.start();
			}
		}

	}

	private void handleWordTimer() {
		String actual = lblWord.getText();
		String expected = txtWord.getText();
		if (expected.length() > 0 && actual.equals(expected)) {
			score++;
		}
		lblScore.setText("Score: " + score);
		txtWord.setText("");

		int ridx = (int) (Math.random() * words.length);
		lblWord.setText(words[ridx]);

	}

	private void handleClockTimer() {
		timeRemaining--;
		lblTimer.setText("Time: " + timeRemaining);

		if (timeRemaining == 0) {
			handleStop();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		String actual = lblWord.getText();
		String expected = txtWord.getText();
		if (expected.length() > 0 && actual.equals(expected)) {
			score++;
			lblScore.setText("Score: " + score);
			txtWord.setText("");

			int ridx = (int) (Math.random() * words.length);
			lblWord.setText(words[ridx]);
			wordtimer.restart();
		}

	}
}
