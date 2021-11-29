package sample;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;
public interface VertexWalker{
	void traverse(Vertex vertex);
	class Chrome implements VertexWalker{
		final Stack<Paint> colors;
		final HashSet<Integer> painted;
		final HashSet<Paint> used;
		public Chrome(Stack<Paint> colors, HashSet<Integer> painted, HashSet<Paint> used){this.colors = colors;
			this.painted = painted;
			this.used = used;
		}
		@Override
		public void traverse(Vertex vertex){
			if(vertex.getFill() != Color.BLACK || !painted.add(vertex.Number)) return;
			Optional<Paint> possible =
					used.stream().filter(p -> !vertex.NeighbourHood.keySet().stream().anyMatch(n -> n.getFill() == p)).findAny();
			Paint p = possible.isPresent() ? possible.get() : colors.pop();
			used.add(p);
			vertex.setFill(p);
			vertex.NeighbourHood.keySet().forEach(v -> traverse(v));
		}
	}
}
