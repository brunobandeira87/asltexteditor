package adsccfgh.textEditor;


import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class ASEditorTextEditor extends TextEditor {
	private ProjectionSupport projectionSupport;
	private AnnotationModel annotationModel;
	
	public void createPartControol(Composite parent){
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer)getSourceViewer();
		projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		 
		viewer.doOperation(ProjectionViewer.TOGGLE);
		
		annotationModel = viewer.getProjectionAnnotationModel();

	}
	
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles){
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		
		getSourceViewerDecorationSupport(viewer);
		
		return viewer;
	}
}
