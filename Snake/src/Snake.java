import java.awt.Color;
import java.awt.Graphics;

public class Snake {
	private Node head = null;// ͷ
	private Node tail;// β��
	private int size = 0;// ����
	public Snake(){
		
	}
	
	public void addToTail(){
		Node node=null;
		switch (tail.dir) {
		case L:
			node =new Node(tail.row,tail.col+1,tail.dir);
			break;
		case U:
			node =new Node(tail.row-1,tail.col,tail.dir);
			break;
		case R:
			node =new Node(tail.row,tail.col-1,tail.dir);
			break;
		case D:
			node =new Node(tail.row+1,tail.col+1,tail.dir);
			break;
		}
		tail.next=tail;
		tail=tail;
		size++;
	}
	
	private class Node {
		int w = Yard.LINE;
		int h = Yard.VERTICAL;
		int row;// �ڼ���
		int col;// �ڼ���
		Dir dir =Dir.L;
		Node next=null;

		Node(int x, int y, Dir dir) {
			this.row = x;
			this.col = y;
			this.dir = dir;
		}

		private void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.WHITE);
			g.fillRect(Yard.CELL * row, Yard.CELL * col, w, h);
			g.setColor(c);
		}
	}
}
