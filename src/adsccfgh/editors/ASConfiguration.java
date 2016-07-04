package adsccfgh.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import adsccfgh.jason.contentassistent.*;
import adsccfgh.jason.scanners.*;

import org.eclipse.jface.text.reconciler.*;

public class ASConfiguration extends SourceViewerConfiguration {
	private ASDoubleClickStrategy doubleClickStrategy;
	private ColorManager colorManager;
	private ASEditor editor;

	public ASConfiguration(ColorManager colorManager, ASEditor editor) {
		this.colorManager = colorManager;
		this.editor = editor;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new ASDoubleClickStrategy();
		return doubleClickStrategy;
	}
	
	public IReconciler getReconciler(ISourceViewer sourceViewer)
    {
        ASReconcilingStrategy strategy = new ASReconcilingStrategy();
        strategy.setEditor(editor);
        
        MonoReconciler reconciler = new MonoReconciler(strategy,false);
        
        return reconciler;
    }
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	{
		// keep this code right here!
	    ContentAssistant assistant = new ContentAssistant();

	    IContentAssistProcessor body = new JasonPlanBodyContentAssistProcessor();
	    assistant.setContentAssistProcessor(body, JasonPartitionScanner.JASON_PLAN_BODY);
	    
	    assistant.enableAutoActivation(true);
	    assistant.setAutoActivationDelay(100);
	    assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    return assistant;
	}
	
	// do not keep this !!! method
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr;
		NonRuleBasedDamagerRepairer ndr;

		dr = new DefaultDamagerRepairer(new JasonPlanBodyScanner(colorManager));
		reconciler.setDamager(dr, JasonPartitionScanner.JASON_PLAN_BODY);
		reconciler.setRepairer(dr, JasonPartitionScanner.JASON_PLAN_BODY);
		
		dr = new DefaultDamagerRepairer(new JasonBeliefScanner(colorManager));
		reconciler.setDamager(dr, JasonPartitionScanner.JASON_BELIEF);
		reconciler.setRepairer(dr, JasonPartitionScanner.JASON_BELIEF);
		
		dr = new DefaultDamagerRepairer(new JasonPlanRequirements(colorManager));
		reconciler.setDamager(dr, JasonPartitionScanner.JASON_PLAN_REQUIREMENTS);
		reconciler.setRepairer(dr, JasonPartitionScanner.JASON_PLAN_REQUIREMENTS);

		dr = new DefaultDamagerRepairer(new JasonPlanTitle(colorManager));
		reconciler.setDamager(dr, JasonPartitionScanner.JASON_PLAN_TITLE);
		reconciler.setRepairer(dr, JasonPartitionScanner.JASON_PLAN_TITLE);
		
		/* scanner for all comments partitions */
		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(IASColorConstants.COMMENT)));
		reconciler.setDamager(ndr, JasonPartitionScanner.JASON_COMMENT);
		reconciler.setRepairer(ndr, JasonPartitionScanner.JASON_COMMENT);
		
		/* default is black for every thing else */
		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(IASColorConstants.DEFAULT)));
		reconciler.setDamager(ndr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(ndr, IDocument.DEFAULT_CONTENT_TYPE);


		return reconciler;
	}
}