package adsccfgh.editors;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;


public class ASEditor extends TextEditor {

	private ColorManager colorManager;

	public ASEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ASConfiguration(colorManager, this));
		setDocumentProvider(new ASDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	private ProjectionSupport projectionSupport;

    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);
        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
        
        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
		projectionSupport.install();
		
		//turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);
		
		annotationModel = viewer.getProjectionAnnotationModel();
		
    }
		
	private Annotation[] oldAnnotations;
	private ProjectionAnnotationModel annotationModel;

	public void updateFoldingStructure(ArrayList positions)
	{
		Annotation[] annotations = new Annotation[positions.size()];
		
		//this will hold the new annotations along
		//with their corresponding positions
		HashMap newAnnotations = new HashMap();
		
		for(int i =0;i<positions.size();i++)
		{
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			
			newAnnotations.put(annotation,positions.get(i));
			
			annotations[i]=annotation;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		
		oldAnnotations=annotations;
	}

    protected ISourceViewer createSourceViewer(Composite parent,
            IVerticalRuler ruler, int styles)
    {
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

    	// ensure decoration support has been created and configured.
    	getSourceViewerDecorationSupport(viewer);
    	
    	return viewer;
    }
}
