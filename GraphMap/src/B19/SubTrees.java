package B19;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import sample.Draw;
import sample.Link;
import sample.Vertex;
import sample.VertexWalker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
public class SubTrees extends Draw{
	private final ComboBox<String> vertexColors = new ComboBox(), linkColors = new ComboBox();
	private final ComboBox<SubTree> subTrees = new ComboBox<>();
	private IdentityHashMap<String, Paint> colors = new AllColors().colors;
	public static void main(String... args){
		launch(args);
	}
	@Override
	protected void createControls(){
		super.createControls();
		scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
		colorComboBox(setNode(B1.getTranslateX() + 450, B1.getTranslateY(), vertexColors));
		colorComboBox(setNode(vertexColors.getTranslateX() + 100, B1.getTranslateY(), linkColors));
		setNode(vertexColors.getTranslateX(), vertexColors.getTranslateY() + 50, new Label("Vertex Color"));
		setNode(linkColors.getTranslateX(), linkColors.getTranslateY() + 50, new Label("Edge Color"));
		setNode(lstInput.getTranslateX() + 305, lstInput.getTranslateY(), new Button("Calculate Subtrees"))
				.setOnAction(b -> calculateSubTrees());
		setNode(lstInput.getTranslateX() + 450, lstInput.getTranslateY(), subTrees);
		subTrees.setPrefSize(500, 40);
		setNode(subTrees.getTranslateX() + 550, subTrees.getTranslateY(), new Button("To file")).setOnAction(b -> {
			try{ write(); } catch(Exception e) {System.out.println(e.getMessage());}
		});
		subTrees.setItems(allTrees);
		setNode(linkColors.getTranslateX() + 100, linkColors.getTranslateY() + 15, new Button("Paint subtree"))
				.setOnAction(b -> subTrees.getValue().paint(colors.get(vertexColors.getValue()),
						colors.get(linkColors.getValue())));
		/*setB(new int[]{   1,1,1,   2,2,   3,3,  4,4,   5,5,   6,  7,7,   8,  9, 9,     10,  11,  12,  13},
				new int[]{2,6,14,  3,11,  4,8,  5,13,  6,10,  7,  8,12,  9,  10,14,    11,  12,  13,  14});*/
		setLLst(new int[] {2, 3, 4, 3, 4, 4, 5, 6, 7, 6, 7, 7},
				new int[] {0, 3, 5, 6, 9, 11, 12, 12});
	}
	private void write() throws IOException{
		Path file = Paths.get("D:\\rez.txt");
		Files.write(file, allTrees.stream().map(t -> t.toString()).collect(Collectors.toList()), StandardCharsets.UTF_8);
	}
	@Override
	protected void clear(){
		Platform.runLater(() -> subTrees.getItems().clear());
		allTrees.clear();
		super.clear();
	}
	ObservableList<SubTree> allTrees = FXCollections.observableArrayList();
	void calculateSubTrees(){
		allTrees.clear();
		subTrees.getItems().clear();
		HashSet<Vertex> exhausted = new HashSet<>();
		long t1 = System.nanoTime();
		for(Integer v: Graph.getVertices())
			Graph.traverseVertices(v, new SubTree(allTrees, exhausted, Graph.size()));
		HashSet<SubTree> trees = new HashSet<>();
		System.out.println(System.nanoTime() - t1);
		for(SubTree tree : allTrees){
			boolean contains = false;
			for(SubTree tree1 : trees){
				if(tree == tree1) continue;
				if(tree.equals(tree1)){
					contains = true;
					break;
				}
			}
			if(!contains) trees.add(tree);
		}
		allTrees.clear();
		allTrees.addAll(trees);
		allTrees.sort(Comparator.comparingInt(t -> t.traversed.size()));
		//Sorting a long list based on item length invariably improves user experience
		//Fills the combobox with collected subtrees
	}
	public void colorComboBox(ComboBox<String> comboBox){
		comboBox.setPrefSize(80, 40);
		comboBox.getStyleClass().add("combo-box");
		comboBox.setItems(FXCollections.observableArrayList(colors.keySet()));
		Callback<ListView<String>, ListCell<String>> factory = param -> new ColorCell();
		comboBox.setCellFactory(factory);
		comboBox.setButtonCell(factory.call(null));
		comboBox.getSelectionModel().selectFirst();
	}
	class SubTree implements VertexWalker{
		final Collection<SubTree> trees;
		ArrayList<Vertex> visited = new ArrayList<>(); //visited vertices
		HashSet<Link> traversed = new HashSet<>(); //traversed edges
		final HashSet<Vertex> exhausted;
		final int GraphSize;
		public SubTree(Collection<SubTree> trees, HashSet<Vertex> exhausted, int graphSize){
			this.trees = trees;
			this.exhausted = exhausted;
			this.GraphSize = graphSize;
		}
		private SubTree(SubTree other){
			this(other.trees, other.exhausted, other.GraphSize);
			this.visited.addAll(other.visited);
			this.traversed.addAll(other.traversed);
		}
		private SubTree(SubTree other, Link link, Vertex vertex){
			this(other);
			this.traversed.add(link);
			this.visited.add(vertex);
		}
		@Override
		public void traverse(Vertex vertex){
			if(!exhausted.add(vertex)) return;
			visited.add(vertex);
			spiralSwarm(0);
		}
		private void spiralSwarm(int index){
			if(trees.contains(this) && index == visited.size() - 1) return;
			trees.add(this);
			if(visited.size() == GraphSize) return;
			List<Map.Entry<Vertex, Link>> available =
					visited.get(index).NeighbourHood.entrySet().stream().filter(e -> !visited.contains(e.getKey()) && !traversed.contains(e.getValue())).collect(Collectors.toList());
			if(available.size() == 0){
				if(index != 0) spiralSwarm(index - 1);
				return;
			}
			ArrayList<SubTree> branches = new ArrayList<>();
			for(Map.Entry<Vertex, Link> e : available){
				int size = branches.size();
				for(int i = 0; i < size; ++i)
					branches.add(new SubTree(branches.get(i), e.getValue(), e.getKey()));
				branches.add(new SubTree(this, e.getValue(), e.getKey()));
			}
			for(SubTree t : branches)
				t.spiralSwarm(t.visited.size() - 1);
			for(Vertex neighbor : visited.get(index).NeighbourHood.keySet())
				if(!exhausted.contains(neighbor)) new SubTree(trees, exhausted, GraphSize).traverse(neighbor);
		}
		@Override
		public boolean equals(Object obj){
			if(obj == null || obj == this || !(obj instanceof SubTree)) return false;
			SubTree other = (SubTree)obj;
			if(traversed.isEmpty() && other.traversed.isEmpty()) return visited.get(0).Number == other.visited.get(0).Number;
			return traversed.containsAll(other.traversed) && other.traversed.containsAll(traversed);
		}
		@Override
		public String toString(){
			if(traversed.isEmpty()) return visited.toString();
			return traversed.toString();
		}
		public void paint(Paint vertexColor, Paint edgeColor){
			visited.forEach(v -> v.setFill(vertexColor));
			traversed.forEach(l -> l.setStroke(edgeColor));
		}
	}
	class ColorCell extends ListCell<String>{
		@Override
		protected void updateItem(String item, boolean empty){
			super.updateItem(item, empty);
			Rectangle rect = new Rectangle(50, 30);
			if(item == null) return;
			Paint color = colors.get(item);
			rect.setFill(color);
			setGraphic(rect);
			setText(item);
		}
	}
}
