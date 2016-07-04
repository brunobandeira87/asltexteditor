package adsccfgh.jason.contentassistent;


import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * @author Phil Zoio
 */
public class JasonPlanBodyContentAssistProcessor implements IContentAssistProcessor {

    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
    	IDocument document = viewer.getDocument();
    	
    	// find the last typed word
        int index = offset - 1;
        StringBuffer prefix = new StringBuffer();
        while (index > 0) {
            try {
                char prev = document.getChar(index);
                if (Character.isWhitespace(prev)) {
                    break;
                }
                prefix.insert(0, prev);
                index--;
            } catch (BadLocationException e) {
            }
        }
        
        ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
        String[] keywords = JasonStdlibActions.ALL_ACTIONS;
        if (prefix.length() > 0) {
            String word = prefix.toString();
            for (int i = 0; i < keywords.length; i++) {
                String keyword = keywords[i];
                if (keyword.startsWith(word) && word.length() < keyword.length()) {
                    proposals.add(new CompletionProposal(keyword, index + 1, offset - (index + 1), keyword.indexOf('(')+1));
                }
            }
        } else {
            // propose all keywords
            for (int i = 0; i < keywords.length; i++) {
                String keyword = keywords[i];
                proposals.add(new CompletionProposal(keyword, offset, 0, keyword.indexOf('(')+1));
            }
        }
        if (!proposals.isEmpty()) {
            return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
        }
        return null;
    }

    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[] { '.' };
    }

    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    public String getErrorMessage() {
        return null;
    }
    
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }
}