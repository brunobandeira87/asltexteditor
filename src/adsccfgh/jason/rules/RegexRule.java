package adsccfgh.jason.rules;

import java.util.regex.*;
import org.eclipse.jface.text.rules.*;

public class RegexRule implements IPredicateRule {

	IToken token;
	Pattern pattern;
	boolean spanNewLine = false;
	int ignore;
	
	public RegexRule(String pattern, IToken token) {
		this.token = token;
		this.pattern = Pattern.compile(pattern);
		this.spanNewLine = true;
		this.ignore = 0;
	}
	
	public RegexRule(String pattern, int regexFlags, IToken token) {
		this.token = token;
		this.pattern = Pattern.compile(pattern, regexFlags);
		this.spanNewLine = true;
		this.ignore = 0;
	}
	
	public RegexRule(String pattern, IToken token, int ignoreLastCharacters) {
		this.token = token;
		this.pattern = Pattern.compile(pattern);
		this.spanNewLine = true;
		this.ignore = ignoreLastCharacters;
	}
	
	public RegexRule(String pattern, int regexFlags, IToken token, int ignoreLastCharacters) {
		this.token = token;
		this.pattern = Pattern.compile(pattern, regexFlags);
		this.spanNewLine = true;
		this.ignore = ignoreLastCharacters;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		String stream = "";
		int c = scanner.read();
		int count = 1;

		while (c != ICharacterScanner.EOF) {

			stream += (char) c;
			
			// return on the first match
			Matcher m = pattern.matcher(stream);
			if (m.matches()) {
				// do not include the end sequence in this partition
				for (int i = 0; i < ignore; i++) {
					scanner.unread();
				}
				return token;
			}

			if (!spanNewLine && ('\n' == c || '\r' == c)) {
				break;
			}

			count++;
			c = scanner.read();
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
