package adsccfgh.jason.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import adsccfgh.editors.ColorManager;
import adsccfgh.editors.IASColorConstants;
import adsccfgh.jason.rules.RegexRule;

public class JasonRules {
	private static JasonRules instance;
	
	protected JasonRules(){
		
	}
	
	private final IToken stringToken = new Token(new TextAttribute(new Color(null, IASColorConstants.STRING)));
	private final IToken numberToken = new Token(new TextAttribute(new Color(null, IASColorConstants.NUMBER)));
	private final IToken variableToken = new Token(new TextAttribute(new Color(null, IASColorConstants.VARIABLES)));
	private final IToken reservedSystemInheritToken = new Token(new TextAttribute(new Color(null, IASColorConstants.INHERIT_ELEM), null, SWT.BOLD));
	private final IToken functionToken = new Token(new TextAttribute(new Color(null, IASColorConstants.FUNCTION)));
	private final IToken arrowToken = new Token(new TextAttribute(new Color(null, IASColorConstants.ARROW)));
	private final IToken planToken = new Token(new TextAttribute(new Color(null,IASColorConstants.GREEN_FUNCTION)));
	private final IToken defaultToken = new Token(new TextAttribute(new Color(null, IASColorConstants.DEFAULT), null, SWT.BOLD));
	
	public final IRule parameterRule = new RegexRule("[_|a-z|A-Z]+[a-z|A-Z|_|0-9]*[,|)]", variableToken, 1);
	public final IRule numberRule = new NumberRule(numberToken);
	public final IRule stringRule = new SingleLineRule("\"", "\"", stringToken);
	public final IRule reservedSystemInheritRule = new RegexRule("true", reservedSystemInheritToken);
	public final IRule arrowRule = new RegexRule("[<][-]", arrowToken);
	public final IRule functionRule = new RegexRule("([!|.|a-z|A-Z|?|+|-]([a-z|A-Z|_]+[0-9]*)+)", functionToken);
	public final IRule functionAsParameter = new RegexRule("(([a-z|A-Z|_])+[(])", functionToken, 1);
	public final IRule planRule = new RegexRule("[+|-|!]+([a-z|A-Z|_|]+[0-9]*)*[(]", planToken, 1);
	public final IRule booleanRule = new RegexRule("&|([ ]not[ ])", defaultToken);
	
	/*	
		IToken stringInstr =
				new Token(
					new TextAttribute(
						manager.getColor(IASColorConstants.STRING)));
		
		IToken commentInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IASColorConstants.COMMENT)));
		
		IToken numberInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IASColorConstants.NUMBER)));
		
		
		
		IToken greenInstr =
				
		
		IToken defaultInstr =
				new Token(
					new TextAttribute(
							manager.getColor(IASColorConstants.DEFAULT)));
				
		IToken reservedKeywordsInstr =
				new Token(
					new TextAttribute(
							manager.getColor(IASColorConstants.DEFAULT), null, SWT.BOLD));
		
		IToken reservedSystemFuncInstr =
				new Token(
					new TextAttribute(
							manager.getColor(IASColorConstants.SYSTEM_FUNCTIONS), null, SWT.BOLD));
		
		IToken reservedSystemAFuncInstr =
				new Token(
					new TextAttribute(
							manager.getColor(IASColorConstants.A_FUNCTIONS), null, SWT.BOLD));
		
		IToken reservedSystemInheritInstr =
				new Token(
					new TextAttribute(
							manager.getColor(IASColorConstants.INHERIT_ELEM), null, SWT.BOLD));
		
	*/
	
	
	
	public static JasonRules getInstance(ColorManager manager){
		if (instance == null)
		{
			instance = new JasonRules();
			return instance;
		}
		return instance;
	}
}
