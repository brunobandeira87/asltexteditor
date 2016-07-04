package adsccfgh.jason.scanners;

import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.*;

import adsccfgh.jason.rules.*;

/**
 * Splits the text in various significant parts
 * @author lassade
 */
public class JasonPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JASON_COMMENT = "__comment";
//	public final static String JASON_STRING = "__string";

	public final static String JASON_BELIEF = "__belief";
	
	public final static String JASON_PLAN_TITLE = "__plan_title";
	public final static String JASON_PLAN_REQUIREMENTS = "__plan_requirements";
	public final static String JASON_PLAN_BODY = "__plan_body";
		
	public JasonPartitionScanner() {
		IToken comment = new Token(JASON_COMMENT);
//		IToken string = new Token(JASON_STRING);
		IToken belief = new Token(JASON_BELIEF);
		IToken title = new Token(JASON_PLAN_TITLE);
		IToken requirement = new Token(JASON_PLAN_REQUIREMENTS);
		IToken body = new Token(JASON_PLAN_BODY);

		IPredicateRule[] rules = new IPredicateRule[] {
			new MultiLineRule("/*", "*/", comment),
			new SingleLineRule("//", null, comment),
			
			/* breaks the below partitions */
			//new SingleLineRule("\"", "\"", string),
			
			/* parse goals and plans */
			/* head */
			new RegexRule("[-|+].*:", title, 1), // do not include ':'
			new RegexRule(":.*[\n|\r| |\t]*[<][-]", requirement, 2), // do not include '<-'
			
			/* body, TODO if u use a jason function (that always start with a dot) this rule will break the doc */
			//new MultiLineRule("<-", ".", body),
			new JasonBodyRule(body),
			
			/* parse beliefs */
			new RegexRule("^[_|a-z|A-Z]+[a-z|A-Z|_|,|0-9]*[ ]*\\(.*\\)$", Pattern.MULTILINE, belief),
		};

		setPredicateRules(rules);
	}
	
	public static String[] getContentTypes () {
		return new String[] {
			JASON_COMMENT,
			JASON_BELIEF,
			JASON_PLAN_TITLE,
			JASON_PLAN_REQUIREMENTS,
			JASON_PLAN_BODY
		};
	}
}
