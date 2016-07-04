package adsccfgh.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import adsccfgh.jason.scanners.JasonPartitionScanner;

public class ASDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new JasonPartitionScanner(),
					JasonPartitionScanner.getContentTypes());
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			
			// TODO debug only
			if (true) {
				ITypedRegion[] partitions = partitioner.computePartitioning(0, document.getLength());
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < partitions.length; i++)
				{
					try
					{
						buffer.append("Partition type: " + partitions[i].getType() + ", offset: " + partitions[i].getOffset()
								+ ", length: " + partitions[i].getLength());
						buffer.append("\n");
						buffer.append("Text:\n");
						buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
						buffer.append("\n---------------------------\n\n\n");
					}
					catch (BadLocationException e)
					{
						e.printStackTrace();
					}
				}
				System.out.print(buffer);
			}
		}
		
		return document;
	}
}