import java.awt.Color;
import java.awt.Graphics;

public class Snake {
	private Node head = null;// ͷ
	private Node tail;// β��
	private int size = 0;// ����
	private Node n=new Node(20,20,Dir.L);
	public Snake(){
		head=n;
		tail=n;
		size=1;
	}
	
	public void addToTail(){
		Node node=null;
		switch (tail.dir) {
		case L:
			node =new Node(tail.row,tail.col+1,tail.dir);
			break;
		case U:
			node =new Node(tail.row+1,tail.col,tail.dir);
			break;
		case R:
			node =new Node(tail.row,tail.col-1,tail.dir);
			break;
		case D:
			node =new Node(tail.row-1,tail.col,tail.dir);
			break;
		}
		tail.next=node;
		tail=node;
		size++;
	}
	
	public void addTohead(){
		Node node=null;
		switch (head.dir) {
		case L:
			node =new Node(head.row,head.col-1,head.dir);
			break;
		case U:
			node =new Node(head.row-1,head.col,head.dir);
			break;
		case R:
			node =new Node(head.row,head.col+1,head.dir);
			break;
		case D:
			node =new Node(head.row+1,head.col,head.dir);
			break;
		}
		node.next=head;
		head=node;
		size++;
	}
	public void draw(Graphics g){
		if(size<=0){
			return;
		}
		for(Node n=head;n!=null;n=n.next){
			n.draw(g);
		}
	}
	
	private class Node {
		int w = Yard.CELL;
		int h = Yard.CELL;
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
			g.fillRect(Yard.CELL * col, Yard.CELL * row, w, h);
			g.setColor(c);
		}
	}
}
