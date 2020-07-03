package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;
import parser.ast.BinaryOpNode.BinType;


public class FunctionNode extends Node{
	// binaryOpNode 클래스를 보고 참고하여 작성함!
	public enum FunctionType { 
		ATOM_Q 	{ TokenType tokenType() {return TokenType.ATOM_Q;} }, 
		CAR 	{ TokenType tokenType() {return TokenType.CAR;} }, 
		CDR 	{ TokenType tokenType() {return TokenType.CDR;} }, 
		COND 	{ TokenType tokenType() {return TokenType.COND;} }, 
		CONS 		{ TokenType tokenType() {return TokenType.CONS;} }, 
		DEFINE 		{ TokenType tokenType() {return TokenType.DEFINE;} },
		EQ_Q 		{ TokenType tokenType() {return TokenType.EQ_Q;} },
		LAMBDA 		{ TokenType tokenType() {return TokenType.LAMBDA;} },
		NOT 		{ TokenType tokenType() {return TokenType.NOT;} },
		NULL_Q 		{ TokenType tokenType() {return TokenType.NULL_Q;} };
		
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
		
		static {
			/*
			 * FuctionType.values(): 열거된 모든 원소를 배열에 담아 반환 
			 * fType: 반환된 배열의 값
			 * 
			 * Map formTokenType:
			 * key: TokenType
			 * value: functionType
			 */
			for (FunctionType fType : FunctionType.values()){
				fromTokenType.put(fType.tokenType(), fType);
			}
		}
		
		/*
		 * TokenType tType을 사용하여 FunctionType을 얻음.
		 */
		static FunctionType getFunctionType(TokenType tType){
			return fromTokenType.get(tType);
		}
		
		abstract TokenType tokenType();
		
	}
	public FunctionType value;
	
	@Override
	public String toString(){
		// 내용 채움!
		// name(): 호출된 값의 이름을 String으로 리턴
		return value.name();
	}

	/*
	 * tType을 사용하여 FuntionType을 구함.
	 * value 설정
	 */
	public void setValue(TokenType tType) {
		// 내용 채움!
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;
	}
}
 