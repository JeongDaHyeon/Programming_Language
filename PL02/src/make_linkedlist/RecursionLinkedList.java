package make_linkedlist;

public class RecursionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;
	/*
	 * 새롭게 생성된 노드를 리스트의 처음으로 연결
	 */
	private void linkFirst(char element) {
		head = new Node(element, head);
	}
	
	/*
	 * (1) 주어진 Node의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
	 * 
	 * @param element
	 * 			데이터
	 * @param x
	 * 			노드
	 */
	private void  linkLast(char element, Node x) {
		// 마지막 노드: next가 null
		if(x.next == null) {
			x.next = new Node(element, null); // 마지막 노드에 새로운 노드 연결
		} else {
			linkLast(element, x.next); // recursion
		}
	}
	
	/*
	 * 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
	 * 
	 * @param element 원소
	 * 
	 * @param pred 이전노드
	 */
	private void linkNext(char element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	/*
	 * 리스트의 첫번째 원소 해제(삭제)
	 * 
	 * @return 첫번째 원소의 데이터
	 */
	private char unlinkFirst() {
		Node x = head;
		char element = x.item;
		head = head.next;
		x.item = UNDEF;
		x.next = null;
		return element;
	}
	
	/*
	 * 이전 Node의 다음 Node 연결 해제(삭제)
	 * 
	 * @param pred
	 * 			이전 노드
	 * @return 다음 노드의 데이터
	 */
	private char unlinkNext(Node pred) {
		Node x = pred.next;
		Node next = x.next;
		char element = x.item;
		x.item = UNDEF;
		x.next = null;
		pred.next = next;
		return element;
	}
	
	/*
	 * (2) x 노드에서 index만큼 떨어진 Node 반환
	 * 
	 * @param index
	 * 			노드 x로부터 인덱스 차이
	 * @param x
	 * 			기준이 되는 노드
	 * 
	 * 
	 */
	private Node node(int index, Node x) {
		if(index == 0) return x; // index가 0이면 해당 노드를 리턴
		else return node(index-1, x.next); // recursion: index-1(차이 - 1)/x.next(다음 노드)
	}
	
	/*
	 * (3) 노드로부터 끝까지의 리스트의 노드 갯수 반환
	 */
	private int length(Node x) {
		if(x.next == null) return 1; // 다음 노드가 없을 때
		
		else return length(x.next) + 1; // recursion: 다음 노드가 있을 때.
	}
	
	/*
	 * (4) 노드로부터 시작하는 리스트의 내용 반환
	 */
	private String toString(Node x) {
		/*
		 * if: 다음 노드가 없을 때(=마지막 노드일 때)
		 * else: 다음노드가 존재할 때
		 */
		if(x.next == null) return Character.toString(x.item); // recursion
		else return Character.toString(x.item) + " " + toString(x.next); // 지금 노드의 item + 이전 노드들의 item을 연결한 것
	}
	
	/*
	 * (5) 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬
	 */
	private void reverse(Node x, Node pred) {
		if(x.next == null) { // 다음 노드가 없을 때
			head = x; // 마지막 노드를 head로 설정
			x.next = pred; // 이전 노드를 다음 노드로 설정
		} else { // 다음 노드가 있을 때
			reverse(x.next, x); // recursion
			x.next = pred; // 이전노드를 다음 노드 설정
		}
	}
	
	public void reverse() {
		reverse(head, null);
	}
	
	/*
	 * (6) 두 리스트를 합침
	 * 
	 * x: list1의 head
	 */
	private void addAll(Node x, Node y) {
		linkLast(y.item, x); // x를 head로 가지는 list의 마지막에 y의 item을 item으로 가지는 Node를 추가
		if(y.next != null) addAll(x, y.next); // recursion: list1의 head / 추가하지 않은 list2의 Node중 제일 앞에 있는것 
	}
	
	/*
	 * 두 리스트를 합침 (this + B)
	 */
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head);
	}
	
	
	/*
	 * 원소를 리스트의 마지막에 추가
	 */
	public boolean add(char element) {
		if(head == null) {
			linkFirst(element);
		} else {
			linkLast(element, head);
		}
		
		return true;
	}
	
	/*
	 * 원소를 주어진 index 위치에 추가
	 * 
	 * @param index
	 * 			리스트에 추가될 위치
	 * @param element
	 * 			추가될 데이터
	 */
	public void add(int index, char element) {
		if(!(index >= 0 && index <= size())) throw new IndexOutOfBoundsException("" + index);
		
		if(index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index-1, head));
	}
	
	/*
	 * 리스트에서 index 위치의 원소 반환
	 */
	public char get(int index) {
		if(!(index >= 0 && index < size())) throw new IndexOutOfBoundsException("" + index);
		
		return node(index, head).item;
	}
	
	/*
	 * 리스트에서 index 위치의 원소 삭제
	 */
	public char remove(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) {
			return unlinkFirst();
		}
		return unlinkNext(node(index - 1, head));
	}
	
	/*
	 * 리스트의 원소 갯수 반환
	 */
	public int size() {
		return length(head);
	}
	
	public String toString() {
		if(head == null)
			return "[]";
		
		return "[" + toString(head) + "]";
	}
	
	/*
	 * 리스트에 사용될 자료구조
	 */
	private static class Node {
		char item; // 데이터
		Node next; // 다음 노드
		
		Node(char element, Node next) {
			this.item = element;
			this.next = next;
		}
	}
}
