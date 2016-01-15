import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.LineMetrics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


class WordStart {
	public WordStart(int i, int j, boolean is_across, int length) {
		this.i = i;
		this.j = j;
		this.is_across = is_across;
		this.length = length;
	}
	int i;
	int j;
	boolean is_across; // if false, this is a down start
	int length;
}

public class WordStarts extends JPanel implements MouseMotionListener, MouseListener, KeyListener {
	//these variable are for making random puzzles
	
	static int xHighlight=0;
	static int yHighlight=0;
	static int xCurrSquare=0;
	static int yCurrSquare=0;
	
	static char lastKeyPressed=0;
	
	static int xss=500; //xss = x screen size
	static int yss=500; //yss = y screen size
	
	static int word_starts[][];
	static final int WALL = -1;
	static final int BLANK = 0;
	static final int ACROSS = 1;
	static final int DOWN = 2;
	static final int BOTH = 3;
	static ArrayList<String> wordsUsed = new ArrayList<String>();
	
	static ArrayList<String> words = new ArrayList<String>();
	
	static ArrayList<Integer> counts = new ArrayList<Integer>();
	
	public static void loadWords() throws IOException {
		// BufferedReader f = new BufferedReader(new FileReader("englishWords.txt"));
		BufferedReader f = new BufferedReader(new FileReader("Common English Words"));
		while(true){
			String word = f.readLine();
			if(word==null){
				break;
			}
			words.add(word);
		}
	}
	
	public void paintComponent(Graphics oldg){
		Graphics2D g = (Graphics2D)oldg;
		g.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 1000);
		
		if (word_starts == null) {
			return;
		}
		
		g.setColor(Color.black);
		graphics(g);
		drawOutline(g);
		

	}

	
	private void drawOutline(Graphics2D g) {
		// System.out.println("drawOutline");
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		//g.drawRect(xStart, yStart, xss*7/11, yss*7/11);
		//g.drawRect(100, 100, 1*7/11, 100);
		for(int i=0;i<=word_starts[0].length;i++){
			int x = (int)(xStart+7.0/11*xss*i/word_starts[0].length);
			g.drawLine(x, yStart, x , (int)(yStart+yss*7.0/11));
		}
		for(int j=0;j<=word_starts[1].length;j++){
			int y=(int) (yStart+7.0/11*yss*j/word_starts[1].length);
			g.drawLine(xStart, y, (int)(xStart+xss*7.0/11), y);
		}
		drawBox(xCurrSquare,yCurrSquare,g,Color.red);
		
	}
	
	public static void highlightWord(int dir, int i, int j, Graphics2D g){
		System.out.println("highlightWord " + dir + " i " + i + " j " + j);
		boolean is_across=false;
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		if (dir==ACROSS){
			is_across=true;
		}
		if(dir==ACROSS || dir==DOWN){
			int len;
			for (len = 1; len < word_starts.length; len++) {
				if (is_across) {
					fillBox(i,j,g,Color.gray);
					j++;
				} else {
					fillBox(i,j,g,Color.gray);
					i++;
				}
				if (i >= word_starts.length || j >= word_starts.length || word_starts[i][j] == WALL) {
					break;
				}
			}
		}
	}
	public static void fillBox(int i, int j, Graphics2D g, Color color){
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		g.setColor(color);
		g.fillRect((int)(xStart+j*xss*7.0/11/word_starts[0].length), yStart+(int)(i*yss*7.0/11/word_starts[0].length), (int)(7.0/11*xss/word_starts[0].length), (int)(7.0/11*yss/word_starts[0].length));
	}
	public static void drawBox(int i, int j, Graphics2D g, Color color){
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		g.setColor(color);
		g.drawRect((int)(xStart+j*xss*7.0/11/word_starts[0].length), yStart+(int)(i*yss*7.0/11/word_starts[0].length), (int)(7.0/11*xss/word_starts[0].length), (int)(7.0/11*yss/word_starts[0].length));
	}
	private void graphics(Graphics2D g) {
		
		Font f = new Font("Arial", Font.PLAIN, 12);
		g.setFont(f);
		int count=0;
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		int highlighted_word_start = word_starts[xHighlight][yHighlight];
		if(highlighted_word_start != BLANK &&
				highlighted_word_start != WALL){
			if (highlighted_word_start == BOTH) {
				highlightWord(ACROSS,xHighlight,yHighlight,g);
				highlightWord(DOWN,xHighlight,yHighlight,g);
			} else {
				highlightWord(highlighted_word_start,xHighlight,yHighlight,g);
			}
		}
		g.setColor(Color.black);
		for(int i=0;i<word_starts.length;i++){
			for(int j=0;j<word_starts[0].length;j++){
				if(word_starts[i][j]==WALL){
					fillBox(i,j,g,Color.black);
				} else if(word_starts[i][j]==BLANK) {
				}else{
					count++;
					LineMetrics lm = f.getLineMetrics(".", g.getFontRenderContext());
					g.drawString(count+".", (int)(2+xStart+j*xss*7.0/11/word_starts[0].length), (int)(2+lm.getAscent() + yStart+i*yss*7.0/11/word_starts[0].length));
				}

			}
		}
		
	}
	public static void main(String[] args) throws IOException {
		loadWords();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setSize(xss, yss);
				
				WordStarts game = new WordStarts();
				frame.add(game);
				
				game.addMouseListener(game);
				frame.addKeyListener(game);

				frame.validate();
			}
		});
		
		boolean hasLetter[][] = {
				{false, false, true, true, true, true},
				{true, true, true, false, true, false},
				{true, true, true, true, true, false},
				{true, false, true, true, true, true},
				{false, true, true, true, false, true},
				{true, true, true, false, false, true},
		};

		
		word_starts = new int[hasLetter.length][hasLetter.length];
		char board[][] = new char[hasLetter.length][hasLetter.length];
		
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				if (hasLetter[i][j] == false) {
					word_starts[i][j] = WALL;
				} else {
					word_starts[i][j] = BLANK;
				}
			}
		}
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				int thing=0;
				if(hasLetter[i][j]==false){
					continue;
				}
				if((i==0 || hasLetter[i-1][j]==false) && // there is a wall above us AND
						(i < 4 && hasLetter[i+1][j]==true)) { // no wall below us
					word_starts[i][j]=DOWN;
					thing++;
				}
				if((j==0 || hasLetter[i][j-1]==false) &&
						(j < 4 && hasLetter[i][j+1]==true)){
					if(thing==1){
						word_starts[i][j]=BOTH;
					}
					else{
						word_starts[i][j]=ACROSS;
					}
				}
			}
		}
		
		System.out.println("Word starts:");
		printStarts(word_starts);
		System.out.println();
		
		ArrayList<WordStart> word_start_list = new ArrayList<WordStart>();
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				if (word_starts[i][j] == ACROSS || word_starts[i][j] == BOTH) {
					word_start_list.add(new WordStart(i, j, true, findWordLength(word_starts, i, j, true)));
				}
				if (word_starts[i][j] == DOWN || word_starts[i][j] == BOTH) {
					word_start_list.add(new WordStart(i, j, false, findWordLength(word_starts, i, j, false)));
				}
			}
		}
		
		counts = new ArrayList<Integer>();
		for (int i = 0; i < word_start_list.size(); i++) {
			counts.add(0);
		}
		
		if (tryToSolve(word_start_list, board, 0)) {
			System.out.println("Solution:");
			printSolution(word_starts, board);
			System.out.println();
		} else {
			System.out.println("NO SOLUTION FOUND");
		}

		for (int i = 0; i < word_start_list.size(); i++) {
			System.out.println("Counts[" + i + "]: " + counts.get(i));
		}
	}

	static int findWordLength(int[][] word_starts, int i, int j,
			boolean is_across) {
		int len;
		for (len = 1; len < word_starts.length; len++) {
			if (is_across) {
				j++;
			} else {
				i++;
			}
			if (i >= word_starts.length || j >= word_starts.length || word_starts[i][j] == WALL) {
				break;
			}
		}
		return len;
	}

	private static boolean tryToSolve(ArrayList<WordStart> word_starts, char[][] board, int cur_word_start) {
		if (cur_word_start >= word_starts.size()) {
			return true;
		}
		
		counts.set(cur_word_start, counts.get(cur_word_start) + 1);
		WordStart ws = word_starts.get(cur_word_start);
		//System.out.println("Finding a word for " + ws.i + ", " + ws.j + ", with length " + ws.length + " -- across? " + ws.is_across);
		
		// Remember which characters in this word were already set
		boolean had_letter[] = new boolean[ws.length];
		for (int i = 0; i < ws.length; i++) {
			had_letter[i] = true;
		}
		int r=ws.i;
		int c=ws.j;
		for (int i = 0; i < ws.length; i++) {
			if (board[r][c] == 0) {
				had_letter[i] = false;
			}
			if (ws.is_across)
				c++;
			else
				r++;
		}
		
		// Find a word that fits here, given the letters already on the board
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			
			if (!wordsUsed.contains(word) && word.length() == ws.length) {
				boolean word_fits = true;
				r=ws.i;
				c=ws.j;
				for (int j = 0; j < ws.length; j++) {
					if (board[r][c] != 0 && board[r][c] != word.charAt(j)) {
						word_fits = false;
						break;
					}
					if (ws.is_across)
						c++;
					else
						r++;
				}
				
				if (word_fits) {
					// Place this word on the board
					wordsUsed.add(word);
					r=ws.i;
					c=ws.j;
					for (int j = 0; j < ws.length; j++) {
						board[r][c] = word.charAt(j);
						if (ws.is_across)
							c++;
						else
							r++;
					}
					
					// If puzzle can be solved this way, we're done
					if (tryToSolve(word_starts, board, cur_word_start + 1)) {
						return true;
					}
					
					// If not, take up letters that we placed and try a different word
					r=ws.i;
					c=ws.j;
					for (int j = 0; j < ws.length; j++) {
						if (!had_letter[j])
							board[r][c] = 0;
						if (ws.is_across)
							c++;
						else
							r++;
					}
					
					wordsUsed.remove(word);
				}
			}
		}
			
		// If no word can work, return false.
		return false;
		
	}

	private static void printSolution(int[][] word_starts, char[][] board) {
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				int ws=word_starts[i][j];
				if(ws==WALL){
					System.out.print("_ ");
				} else {
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	static void printStarts(int[][] word_starts) {
		for (int i = 0; i < word_starts.length; i++) {
			for (int j = 0; j < word_starts[0].length; j++) {
				int ws=word_starts[i][j];
				if(ws==WALL){
					System.out.print("W ");
				}
				if(ws==BLANK){
					System.out.print("  ");
				}
				if(ws==ACROSS){
					System.out.print("- ");
				}
				if(ws==DOWN){
					System.out.print("| ");
				}
				if(ws==BOTH){
					System.out.print("+ ");
				}
			}
			System.out.println();
		}
	}
	@Override
	public void keyPressed(KeyEvent ev) {
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent ev) {
		System.out.println("You pressed " + ev.getKeyChar());
		lastKeyPressed=ev.getKeyChar();
		int dir = word_starts[xHighlight][yHighlight];
		if (dir==ACROSS){
			yCurrSquare++;
		}
		else if(dir==DOWN){
			xCurrSquare++;
		}else{
			
		}
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent ev) {
		System.out.println("Mouse clicked at " + ev.getX() + ", " + ev.getY());
		
		// xcoord of top left: xStart+j*xss*7.0/11/word_starts[0].length
		// ycoord of top left: yStart+(int)(i*yss*7.0/11/word_starts[0].length)
		
		int squareSize = (int)(7.0/11*xss/word_starts[0].length);
		
		int xStart =xss*2/11;
		int yStart =yss*2/11;
		int i = (ev.getY() - yStart) / squareSize;
		int j = (ev.getX() - xStart) / squareSize;
		
		if (ev.getY() >= yStart && ev.getX() >= xStart &&
				i < word_starts.length && j < word_starts[0].length) {
			System.out.println("Click at row " + i + ", col " + j);
			xHighlight=xCurrSquare=i;
			yHighlight=yCurrSquare=j;
		}

		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
