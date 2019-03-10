import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	protected final Board board;
	protected Thread[] threads;

	// TODO update with number of group label. See practice statement.
	private final int N_BALL = 6;
	private Ball[] balls;
	ExecutorService executor= Executors.newCachedThreadPool();

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Practica programacion concurrente objetos moviles independientes");
		setResizable(false);
		setVisible(true);
	}

	public void initBalls() {
		// TODOinitballs
		Ball[] balls= new Ball[N_BALL];
		for(int i=0;i<N_BALL;i++) {
			balls[i]= new Ball();
		}
		board.setBalls(balls);
	
	}
	
	protected Thread makeThread(final Ball balls) { // utility
	      Runnable runloop = new Runnable() {
	        public void run() {
	          try {
	            for(;;) {
	              balls.move();
	              board.repaint();
	              Thread.sleep(100); // 100msec is arbitrary
	              if (Thread.interrupted()){
	            	  System.out.println("FIN");
	              }
	            }
	          }
	          catch (InterruptedException e) {
	        	  return; }
	        }
	      };
	      return new Thread(runloop);
	    }

	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when start button is pushed
			Ball[] balls= new Ball[N_BALL];
			for(int i=0;i<N_BALL;i++) {
				balls[i]= new Ball();
			}
			board.setBalls(balls);
			threads = new Thread[N_BALL];
			for(int i=0;i<N_BALL;i++) {
				threads[i]= makeThread(balls[i]);
				threads[i].start();
			}
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			if (threads != null) { // bypass if already stopped
		        for (int i = 0; i < threads.length; ++i)
		          threads[i].interrupt();
		        threads = null;
		      }
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}

}