package zhou.test;

import java.io.IOException;

import com.zhou.compare.GetRelative;

public class GetRelativeTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String word="个税发票注意事项";
		System.out.println(new GetRelative().getRelative(word));
	}

}
