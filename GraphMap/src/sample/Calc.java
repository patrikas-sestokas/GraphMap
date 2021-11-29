package sample;
import com.sun.javafx.geom.Vec2d;
import java.util.Collection;
import static java.lang.Math.*;
import static sample.Calc.VertexPhysics.*;
public class Calc{
	public interface VertexPhysics{
		double SpringStiffness = 1.2;
		double IdealDistance = 4 * Vertex.VertexRadius;
		Vec2d[] calculateForces(Collection<? extends Vertex> vertices);
	}
	public static VertexPhysics springPhysics = vertices -> {

		Vec2d[] forces = new Vec2d[vertices.size()]; int i = 0;
		for(Vertex v: vertices){
			Vec2d vec = new Vec2d(0,0);
			for(Vertex u : vertices){
				if(v == u) continue;
				Vec2d difference = new Vec2d(v.getCenterX() - u.getCenterX(), v.getCenterY() - u.getCenterY());
				double distance = sqrt(difference.x * difference.x + difference.y * difference.y);
				if(v.NeighbourHood.keySet().contains(u)){
					difference.set(SpringStiffness * (distance - IdealDistance) * (difference.x/distance),
							SpringStiffness * (distance - IdealDistance) * (difference.y/distance));
					vec.set(vec.x - difference.x / 10,
							vec.y - difference.y / 10);
				} else{
					distance = (pow(distance,2)) / 1000;
					vec.set(vec.x + difference.x / distance,
							vec.y + difference.y / distance);
				}
			}
			forces[i++] = vec;
		}
		return forces;
	};
	public static double distance(Vertex v1, Vertex v2){
		return sqrt(pow(v1.getCenterX() - v2.getCenterX(), 2) + pow(v1.getCenterY() - v2.getCenterY(), 2));
	}
	public static double distance(Vertex v, Link l){
		double a = v.getCenterX() - l.getStartX();
		double b = v.getCenterY() - l.getStartX();
		double c = l.getEndX() - l.getStartX();
		double d = l.getEndY() - l.getStartY();
		double dot = a * c + b * d;
		double lensq = c * c + d * d;
		double param = -1;
		if(lensq != 0) param = dot / lensq;
		double xx = param < 0 ? l.getStartX() : param > 1 ? l.getEndX() : l.getStartX() + param * c;
		double yy = param < 0 ? l.getStartY() : param > 1 ? l.getEndY() : l.getStartY() + param * d;
		double dx = v.getCenterX() - xx;
		double dy = v.getCenterY() - yy;
		return sqrt(dx * dx + dy * dy);
	}
}
