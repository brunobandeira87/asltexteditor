package adsccfgh.jason.scanners;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import adsccfgh.editors.ColorManager;

public class JasonPlanRequirements extends RuleBasedScanner {
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
	
	public JasonPlanRequirements (ColorManager manager){

		JasonRules j = JasonRules.getInstance(manager);
		
		IRule[] rules = new IRule[]{
				j.booleanRule,
				j.reservedSystemInheritRule,
				j.parameterRule,
				j.functionRule,
				j.functionAsParameter
		};
		
		setRules(rules);
	}
}
