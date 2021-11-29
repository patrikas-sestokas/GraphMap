package sample;
import com.sun.javafx.geom.Vec2d;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import static sample.Calc.*;
public class Vertex extends Circle{
	public static double VertexRadius = 25;
	public static Paint LabelColor = Color.BROWN;
	public static Font LabelFont = new Font("Arial", 20);
	public Integer Number;
	public IdentityHashMap<Vertex, Link> NeighbourHood = new IdentityHashMap<>();
	public Text Label;
	public Vertex(double x, double y, int number){
		super(x, y, VertexRadius);
		setSmooth(true);
		Label = new Text(getCenterX(), getCenterY(), (Number = number).toString());
		Label.setFont(LabelFont);
		Label.setStroke(LabelColor);
		Label.setStrokeWidth(2);
		Label.setSmooth(true);
	}
	void update(){
		Label.setX(getCenterX());
		Label.setY(getCenterY());
		NeighbourHood.values().stream().forEach(l -> l.update());
	}
	public void addConnection(Vertex vertex, Collection<Link> links){
		if(NeighbourHood.containsKey(vertex)) return;
		Link l = new Link(this, vertex);
		NeighbourHood.put(vertex, l);
		vertex.NeighbourHood.put(this, l);
		links.add(l);
	}
	public boolean collidesWithVertices(Collection<? extends Vertex> vertices){
		return vertices.stream().filter(v -> v != this).anyMatch(v -> distance(this, v) < getRadius() + v.getRadius());
	}
	public boolean collidesWithLinks(Collection<? extends Link> links){
		return links.stream().filter(l -> !NeighbourHood.values().contains(l)).anyMatch(l -> distance(this, l) < getRadius());
	}
	public void randomizeLocation(double minX, double maxX, double minY, double maxY, Random random){
		setCenterX(minX + VertexRadius + random.nextInt((int)(maxX - minX - getRadius() * 2)));
		setCenterY(minY + VertexRadius + random.nextInt((int)(maxY - minY - getRadius() * 2)));
		update();
	}
	@Override
	public String toString(){
		return Number.toString();
	}
}
