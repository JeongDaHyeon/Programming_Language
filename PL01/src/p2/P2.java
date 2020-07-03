package p2;

import java.util.Scanner;

public class P2 {

	// 해당하는 자리의 숫자를 리턴
	// 제일 위는 0,0자리.
	// 좌표 값
	// i는 i번째 줄.
	// j: 줄에서 몇번째 숫자인지
	static int num(int i, int j) {
		if(j == 0 || i == j) return 1;
		
		return num(i-1, j-1) + num(i-1, j); // 재귀
	}
	
	// 한줄을 출력하는 함수
	// line: n번째 줄의 숫자를 정렬
	static void printLine(int line, int j) {
		if(j != 0) printLine(line, j - 1); // 재귀
		System.out.printf("%8d", num(line, j)); // 정렬하여 출력	
	}
	
	// 계단 모양으로 만들어 주는 함수
	// 삼각형 앞의 공백을 출력
	// numOfLine: 총 줄의 수
	// orderOfLine: 몇번째 줄인지
	static void printBlank(int numOfLine , int orderOfLine) {
		if(numOfLine> orderOfLine) printBlank(numOfLine, orderOfLine + 1); // 재귀 
		
		System.out.print("    ");
	}
	
	// 삼각형을 출력하는 함수
	static void pascal(int input1, int input2) {
		if(input2 != 0) pascal(input1, input2 - 1); // 출력
		
		printBlank(input1, input2); // 공백 출력
		printLine(input2, input2); // 숫자 출력
		System.out.println();
		
	}
	
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.print("input : ");
		int input = scan.nextInt();
		
		pascal(input - 1, input - 1);
	}

}
