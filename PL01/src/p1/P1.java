package p1;

import java.util.Scanner;

public class P1 {

	// 이 함수는 삼각형 변의 개수를 계산
	// input: 단계
	static int numOfTriangle(int input) {

		if(input == 0) return 1;

		return numOfTriangle(input - 1) * 3; // 재귀

	}

	// 이 함수는 변의 길이를 계산
	// 이 함수의 역수가 변의 길이
	// input: 단계
	static int length(int input) {

		if(input == 1) return 1;
		return length(input - 1) * 2; // 재귀
	}

	

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		

		System.out.print("input : ");

		int input = scan.nextInt();

		
		// 변의 길이 * 삼각형의 변의 수 = 모든 길이의 합.
		System.out.println("S" + input + " = (" + numOfTriangle(input) +"/" + length(input) + ")L");

		

	}

 

}