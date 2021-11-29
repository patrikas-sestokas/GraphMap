package sample;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
public class Link extends Line{
	public Vertex[] EndPoints = new Vertex[2];
	public Link(Vertex source, Vertex destination){
		super(source.getCenterX(), source.getCenterY(), destination.getCenterX(), destination.getCenterY());
		setStroke(Color.WHITE);
		setSmooth(true);
		setStrokeWidth(5);
		EndPoints[0] = source;
		EndPoints[1] = destination;
	}
	public void update(){
		setStartX(EndPoints[0].getCenterX());
		setStartY(EndPoints[0].getCenterY());
		setEndX(EndPoints[1].getCenterX());
		setEndY(EndPoints[1].getCenterY());
	}
	@Override
	public String toString(){
		return "[" + EndPoints[0] + ", " + EndPoints[1] + "]";
	}
}
