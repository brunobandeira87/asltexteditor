/*******************************************************************************
 * Copyright (c) 2005 Prashant Deva and Gerd Castan
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package adsccfgh.editors;

import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;

public class ASReconcilingStrategy implements IReconcilingStrategy,
IReconcilingStrategyExtension {

	private ASEditor editor;

	private IDocument fDocument;

	protected final ArrayList fPositions = new ArrayList();
	protected int fOffset;
	protected int fRangeEnd;
	public ASEditor getEditor() {
		return editor;
	}

	public void setEditor(ASEditor editor) {
		this.editor = editor;
	}

	public void setDocument(IDocument document) {
		this.fDocument = document;
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		initialReconcile();
	}

	public void reconcile(IRegion partition) {
		initialReconcile();
	}

	public void setProgressMonitor(IProgressMonitor monitor) {}

	public void initialReconcile() {
		fOffset = 0;
		fRangeEnd = fDocument.getLength();
		calculatePositions();
	}

	protected int cNextPos = 0;
	protected int cNewLines = 0;
	protected char cLastNLChar = ' ';
	protected static final int START_TAG = 1;
	protected static final int LEAF_TAG = 2;
	protected static final int END_TAG = 3;
	protected static final int EOR_TAG = 4;
	protected static final int COMMENT_TAG = 5;
	protected static final int PI_TAG = 6;

	protected void calculatePositions() {
		fPositions.clear();
		cNextPos = fOffset;

		try {
			recursiveTokens(0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				editor.updateFoldingStructure(fPositions);
			}
		});
	}

	//Regras para inserir Folding
	protected int recursiveTokens(int depth) throws BadLocationException {

		//Se está fazendo a primeira leitura, zere o array que marca os caracteres finais

		//Loop que lê todos os caracteres
		while (cNextPos < fRangeEnd) {

			char ch = fDocument.getChar(cNextPos++);
			switch (ch) {

			//Caso ache a '<' da "<-"
			case '<':
				//Caso o caractere seguinte da '<' seja '-' para virar "<-"
				if (fDocument.getChar(cNextPos)=='-'){
					int startOffset = cNextPos - 1;
					for (int i=cNextPos; i<fRangeEnd; i++){	
						//procurar após o "<-" um ".\n" ou ". " ou ".\r"
						if (i+1 < fRangeEnd && fDocument.getChar(i) == '.' &&
								(i == fRangeEnd || fDocument.getChar(i+1) == ' ' ||
								fDocument.getChar(i+1) == '\n' || fDocument.getChar(i+1) == '\r')){
							//Só adicionar ao folding se já não pertencer a outro folding
							while (fDocument.getChar(i) != '\n' && fDocument.getChar(i)!='\r'){
								i++;
							}
							//adicionar aos "Foldados"
							emitPosition(startOffset, (i-startOffset + 2));
							//Prosseguir com a busca após encontrar o primeiro fold
							recursiveTokens(depth + 1);
							break;
						}
					}
				}

			case '.':
				try{
					if (fDocument.getChar(depth) == '\n' || fDocument.getChar(depth) == ' ')
						return 0;
				} catch (Exception e){

				}
				break;

			case '/':
				if (fDocument.getChar(cNextPos)=='/'){

				}
				break;
			}
		}
		return 0;
	}

	protected void emitPosition(int startOffset, int length) {
		fPositions.add(new Position(startOffset, length));
	}

	protected int classifyTag() {
		try {
			char ch = fDocument.getChar(cNextPos++);
			cNewLines = 0;

			if (ch == '-') return START_TAG;

			if (ch == '\n') return END_TAG;

			return EOR_TAG;
		} catch (BadLocationException e) {
			return EOR_TAG;
		}
	}

	protected int eatToEndOfLine() throws BadLocationException {
		if (cNextPos >= fRangeEnd) {
			return 0;
		}
		char ch = fDocument.getChar(cNextPos++);

		while ((cNextPos < fRangeEnd) && ((' ' == ch) || ('\t' == ch))) {
			ch = fDocument.getChar(cNextPos++);
		}
		if (cNextPos >= fRangeEnd) {
			cNextPos--;
			return 0;
		}

		if ('\n' == ch) {
			if (cNextPos < fRangeEnd) {
				ch = fDocument.getChar(cNextPos++);
				if ('\r' != ch) {
					cNextPos--;
				}
			} else {
				cNextPos--;
			}
			return 1;
		}

		if ('\r' == ch) {
			if (cNextPos < fRangeEnd) {
				ch = fDocument.getChar(cNextPos++);
				if ('\n' != ch) {
					cNextPos--;
				}
			} else {
				cNextPos--;
			}
			return 1;
		}

		return 0;
	}
}

