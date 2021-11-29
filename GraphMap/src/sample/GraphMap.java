package sample;
import com.sun.javafx.geom.Vec2d;
import javafx.scene.Node;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.*;
import static java.util.stream.IntStream.range;
public class GraphMap{
	private final Hashtable<Integer, Vertex> Vertices = new Hashtable<>();
	private final Vector<Link> Links = new Vector<>();
	double minX, maxX, minY, maxY;
	public int size() {return Vertices.size();}
	public GraphMap(double minX, double maxX, double minY, double maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	public void parseLAndLst(int[] L, int[] lst){
		range(1, lst.length).mapToObj(i -> createOrGetVertex(i)).forEach(v -> range(lst[v.Number - 1], lst[v.Number]).forEach(j -> v.addConnection(createOrGetVertex(L[j]), Links)));
		Links.sort(Comparator.comparingInt((Link a) -> a.EndPoints[0].Number).thenComparingInt(a -> a.EndPoints[1].Number));
	}
	public void parseB(int[] B1, int[] B2){
		range(0, B1.length).forEach(i -> createOrGetVertex(B1[i]).addConnection(createOrGetVertex(B2[i]), Links));
		Links.sort(Comparator.comparingInt((Link a) -> a.EndPoints[0].Number).thenComparingInt(a -> a.EndPoints[1].Number));
	}
	public Collection<Integer> getVertices(){
		return Vertices.keySet();
	}
	public Collection<String> getB1(){
		return Links.stream().map(l -> l.EndPoints[0].Number.toString()).collect(Collectors.toList());
	}
	public Collection<String> getB2(){
		return Links.stream().map(l -> l.EndPoints[1].Number.toString()).collect(Collectors.toList());
	}
	public Collection<String> getL(){
		return getB2();
	}
	public Collection<String> getLst(){
		Map<Integer, Long> grouping =
				Links.stream().map(l -> l.EndPoints[0].Number).collect(Collectors.groupingBy(Function.identity(),
						Collectors.counting()));
		final ArrayList<Long> i = new ArrayList<>();
		i.add(0l);
		range(1, Vertices.size() + 1).forEach(j -> i.add(i.get(i.size() - 1) + grouping.getOrDefault(j, 0l)));
		return i.stream().map(j -> j.toString()).collect(Collectors.toList());
	}
	void arrange(){
		Random random = new Random();
		for(Vertex v: Vertices.values())
			while(v.collidesWithVertices(Vertices.values()) || v.collidesWithLinks(Links))
				v.randomizeLocation(minX, maxX, minY, maxY, random);
	}
	Vertex createOrGetVertex(int number){
		if(Vertices.containsKey(number)) return Vertices.get(number);
		Vertex n = new Vertex((minX + maxX) / 2, (minY + maxY) / 2, number);
		Vertices.put(number, n);
		return n;
	}
	public Collection<? extends Node> clearAllNodes(){
		Collection nodes = getAllNodes();
		Links.clear();
		Vertices.clear();
		return nodes;
	}
	public Collection<? extends Node> getAllNodes(){
		return Stream.concat(Stream.concat(Links.stream(), Vertices.values().stream()),
				Vertices.values().stream().map(v -> v.Label)).collect(Collectors.toList());
	}
	void move(){
		Vec2d[] forces = Calc.springPhysics.calculateForces(Vertices.values()); int i = 0;
		for(Vertex v : Vertices.values()){
			Vec2d force = forces[i++];
			v.setCenterX(min(maxX - v.getRadius(), max(minX + v.getRadius(), v.getCenterX() + force.x)));
			v.setCenterY(min(maxY - v.getRadius(), max(minY + v.getRadius(), v.getCenterY() + force.y)));
			v.update();
		}
	}
	public void traverseVertices(int start, VertexWalker traveler){
		traveler.traverse(Vertices.get(start));
	}
}
