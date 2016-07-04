package adsccfgh.jason.scanners;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import adsccfgh.editors.ColorManager;
public class JasonBeliefScanner extends RuleBasedScanner {

	IWordDetector getStandardWordDetector () {
		return new IWordDetector() {
			@Override
			public boolean isWordStart(char arg0) {
				return Character.isJavaIdentifierStart(arg0);
			}
			@Override
			public boolean isWordPart(char arg0) {
				return Character.isJavaIdentifierPart(arg0);
			}
		};
	}
	
	public JasonBeliefScanner (ColorManager manager){

		JasonRules j = JasonRules.getInstance(manager);
		
		IRule[] rules = new IRule[]{
				j.parameterRule,
				j.numberRule,
				j.stringRule,
				j.reservedSystemInheritRule,
		};
		
		setRules(rules);
	}
}
