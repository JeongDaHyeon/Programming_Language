package token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Scanner {

	// ID, INT �긽�깭留� �엳�쓬.
	// finalState: �쁽�옱 留덉�留� �긽�깭瑜� �굹���깂.
	public enum TokenType {
		ID(3), INT(2);

		private final int finalState;

		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}

	// class Token
	// type: Token�쓽 �쁽�옱 �긽�깭
	// lexme: 泥섏쓬�뿉 諛쏆� 臾몄옄�뿴
	public static class Token {
		public final TokenType type; // Token�쓽 �긽�깭
		public final String lexme; // �엯�젰 諛쏆� 臾몄옄�뿴

		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}
		// Token 媛앹껜 異쒕젰 �젙�쓽
		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}

	/*
	 * transM: [�씠�쟾 �긽�깭][�엯�젰 諛쏆� 臾몄옄]瑜� �씤�뜳�뒪濡� �븯�뒗 �옄由ъ뿉 �쁽�옱 �긽�깭 媛믪쓣 ���옣  
	 * �뻾: �씠�쟾 �긽�깭
	 * �뿴: �엯�젰 諛쏆� 臾몄옄
	 * 
	 * source: �엯�젰 諛쏆� �븳以�
	 * 
	 * st: source瑜� 怨듬갚�쓣 湲곗��쑝濡� 遺꾨━�븳 臾몄옄�뿴 由ъ뒪�듃
	 */
	private int transM[][];
	private String source; // �엯�젰 諛쏆� 臾몄옄�븳以�.
	private StringTokenizer st; // 紐⑤뱺 臾몄옄�뿴�뱾�씠 �엳�쓬

	// source: �엯�젰 諛쏆� �븳 以�
	public Scanner(String source) {
		this.transM = new int[4][128]; // 4: 4媛쒖쓽 �긽�깭/ 128 : �븘�뒪�궎 肄붾뱶 媛쒖닔
		this.source = source == null ? "" : source; // source�뿉 臾몄옄�뿴 ���옣
		this.st = new StringTokenizer(this.source, " "); // �븳以꾩쓣 �굹�닎.
		initTM();
	}


	/*
	 * -1: 
	 * 0: 珥덇린 �긽�깭
	 * 1: '-'媛� �뱾�뼱�삩 �긽�깭
	 * 2: INT
	 * 3: ID
	 * 
	 * <�븘�뒪�궎 肄붾뱶>
	 * - : 45
	 * 
	 * �닽�옄: 48 <= �닽�옄 <= 57
	 * 
	 * 65 <= �쁺�뼱 ��臾몄옄 <= 90
	 * 
	 * 
	 */
	private void initTM() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 128; j++) {
				if(i == 0) { // �븘臾닿쾬�룄 �엯�젰 諛쏆� �븡�� �긽�깭�씪 �븣
					if(48 <= j && j <= 57) { // �닽�옄瑜� �엯�젰 諛쏆븯�쓣 �븣
						transM[i][j] = 2;
					}
					else if(j == 45) { // '-'瑜� �엯�젰 諛쏆븯�쓣 �븣
						transM[i][j] = 1;
					}
					else if((65 <= j && j <= 90) || (97<=j && j<=122)) { // �븣�뙆踰녹쓣 �엯�젰 諛쏆븮�쓣 �븣
						transM[i][j] = 3;
					}
				}
				else if(i == 1 || i == 2) { // 泥섏쓬�뿉 '-'留� �엯�젰 諛쏆� �긽�깭�씠嫄곕굹 �닽�옄留� �엯�젰 諛쏆� �긽�깭�씪�븣 
					if(48 <= j && j <= 57) { // �닽�옄瑜� �엯�젰 諛쏆븯�쓣 �븣
						transM[i][j] = 2;
					}
					else {
						transM[i][j] = -1; // 誘명넻怨�
					}
				}
				else if(i == 3) { // �븣�뙆踰녹쓣 �엯�젰 諛쏆� �긽�깭�씪 �븣
					if((48 <= j && j <= 57)||(65 <= j && j <= 90) || (97<=j && j<=122)) { // �븣�뙆踰�, �닽�옄 �엯�젰 諛쏆� 寃쎌슦
						transM[i][j] = 3; // �긽�깭 �쑀吏�
					}
					else {
						transM[i][j] = -1; // 誘명넻怨�
					}
				}
				else { // 洹� �쇅�쓽 臾몄옄�뒗 誘명넻怨�
					transM[i][j] = -1;					
				}
			}
		}
		
	}

	// �옉�꽦
	private Token nextToken() {
		// stateOld: �쁽�옱 �긽�깭
		// sateNew: �깉濡쒖슫 �긽�깭
		int stateOld = 0, stateNew;
		// �넗�겙�씠 �뜑 �엳�뒗吏� 寃��궗
		if (!st.hasMoreTokens())
			return null;
		// 洹� �떎�쓬 �넗�겙�쓣 諛쏆쓬
		String temp = st.nextToken();
		Token result = null; // 諛섑솚�븷 Token 媛앹껜
		
		for (int i = 0; i < temp.length(); i++) {
			// 臾몄옄�뿴�쓽 臾몄옄瑜� �븯�굹�뵫 媛��졇�� �쁽�옱�긽�깭�� TransM瑜� �씠�슜�븯�뿬 �떎�쓬�긽�깭瑜� �뙋蹂�
			// 留뚯빟 �엯�젰�맂 臾몄옄�쓽 �긽�깭媛� reject �씠硫� �뿉�윭硫붿꽭吏� 異쒕젰 �썑 return�븿
			// �깉濡� �뼸�� �긽�깭瑜� �쁽�옱 �긽�깭濡� ���옣
			stateNew = transM[stateOld][temp.charAt(i)];
			
			// �넻怨� �븯吏� 紐삵븳 寃쎌슦
			if(stateNew == -1) {
				return null;
			}
			else { // �넻怨� �븳 寃쎌슦 �떎�쓬 臾몄옄 寃��궗瑜� �쐞�빐 �긽�깭瑜� 諛붽퓞 
				stateOld = stateNew;
			}
			
		}
		
		// values(): �쟾�떖�맂 媛앹껜�쓽 �냽�꽦 媛믩뱾�쓣 �룷�븿�븯�뒗 諛곗뿴
		// 2, 3 : INT �삉�뒗 ID
		for (TokenType t : TokenType.values()) {
			/*
			 *  TokenType�뿉 �씪移섑븯�뒗 �긽�깭: ID �굹 INT�씤 寃쎌슦
			 *  result: �깉濡쒖슫 Token 媛앹껜
			 *  
			 *  留뚯빟, �긽�깭媛� 洹� �씠�쇅�쓽 寃쎌슦�씪硫� result�뒗 洹몃�濡� null媛� 由ы꽩
			 */
			if (t.finalState == stateOld) {
				// �깉濡쒖슫 token 媛앹껜 �깮�꽦
				result = new Token(t, temp);
				break;
				
			}
		}
		// 媛앹껜 由ы꽩
		return result;
	}
	
	// �옉�꽦
	public List<Token> tokenize() {
		//�엯�젰�쑝濡� �뱾�뼱�삩 紐⑤뱺 token�뿉 ���빐
		//nextToken()�씠�슜�빐 �떇蹂꾪븳 �썑 list�뿉 異붽��빐 諛섑솚
		List<Token> list = new LinkedList<Scanner.Token>();
		
		while(true) {
			// nextToken �븿�닔濡� �떎�쓬 臾몄옄�뿴�쓣 諛쏆븘�샂
			Token tmp = nextToken();
			/*
			 * tmp媛� null�씤 寃쎌슦: Token�씠 �넻怨쇳븯吏� 紐삵븿
			 * while臾몄쓣 醫낅즺
			 * 
			 * 洹� �쇅�쓽 寃쎌슦�뿉�뒗 list�뿉 Token 媛앹껜 異붽�
			 * 
			 */
			if(tmp == null) {
				break;
			}
			else {
				list.add(tmp); // 媛앹껜 異붽�
			}
		}
		
		
		
		return list; // 由ъ뒪�듃 由ы꽩
	}
	

	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("c:/as03.txt");
		//FileReader fr = new FileReader("C:\\Users\\jeong\\Desktop\\2020_1\\�봽濡쒓렇�옒諛띿뼵�뼱媛쒕줎\\eclipse_workspace_for_pl\\ProgrammingLanguage\\src\\hw03_recognizing_tokens\\as03.txt");
		BufferedReader br = new BufferedReader(fr);
		String source = br.readLine();
		Scanner s = new Scanner(source);
		List<Token> tokens = s.tokenize();
		System.out.println(tokens); // 由ъ뒪�듃 異쒕젰
	}

}
