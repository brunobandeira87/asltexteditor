package adsccfgh.jason.rules;

import java.util.regex.*;
import org.eclipse.jface.text.rules.*;

public class JasonBodyRule implements IPredicateRule {

	IToken token;
	
	public JasonBodyRule(IToken token) {
		this.token = token;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		int count = 1;
		
		String word = "";
		int state = 0;

		while (c != ICharacterScanner.EOF) {
			if (state == 0) {
				word += (char) c;
				if (count == 2) {
					if (word.equals("<-")) {
						word = "";  
						state = 1;
					} else {
						break;
					}
				}
			} else if (state == 1) {
				if (c == '.') {
					word = ".";
				} else if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
					// do nothing
				} else if (c == '+' || c == '-') {
					// fount our body end but first unread the + or - character
					scanner.unread();
					return token;
				}
			}
			
			count++;
			c = scanner.read();
		}
		
		if (state == 1 && c == ICharacterScanner.EOF) {
			 return token;
		}

		// put the scanner back to the original position if no match
		for (int i = 0; i < count; i++) {
			scanner.unread();
		}

		return Token.UNDEFINED;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

	@Override
	public IToken getSuccessToken() {
		// TODO Auto-generated method stub
		return token;
	}
}
