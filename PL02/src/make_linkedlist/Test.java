package make_linkedlist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		RecursionLinkedList list = new RecursionLinkedList();
		FileReader fr;
		
		try {
			fr = new FileReader("C:\\Users\\jeong\\eclipse-workspace\\PL_02\\src\\make_linkedlist\\hw01.txt");
			BufferedReader br = new BufferedReader(fr);
			String inputString = br.readLine();
			for(int i = 0; i < inputString.length(); i++)
				list.add(inputString.charAt(i));
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(list); // programming
		System.out.println(list.size()); // 11
		System.out.println(list.get(3)); // g
		list.reverse(); // 거꾸로 연결
		System.out.println(list); // gnimmargorp
		list.reverse(); // 거꾸로 연결
		System.out.println(list); // programming

		RecursionLinkedList list2 = new RecursionLinkedList(); // list2: language
		list2.add('l');	list2.add('a');	list2.add('n');
		list2.add('g');	list2.add('u');	list2.add('a');
		list2.add('g');	list2.add('e');
		System.out.println(list2); // language
		
		list.addAll(list2); //list에 list2를 연결
		System.out.println(list); // programminglanguage
		System.out.println(list.size()); // list의 size 출력
		
		list2.remove(0); // l을 제거
		System.out.println(list); // programminglanguage
		System.out.println(list2); // anguage
		
	}

}
