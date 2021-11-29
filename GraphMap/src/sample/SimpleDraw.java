package sample;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
public class SimpleDraw extends Application{
	protected static List<? extends Paint> colors = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE,
			Color.BLACK, Color.YELLOW, Color.MAGENTA, Color.AQUAMARINE, Color.CYAN);
	protected static TextField B1 = new TextField(), B2 = new TextField();
	protected static Canvas canvas = new Canvas(1800, 850);
	protected GraphicsContext gc = canvas.getGraphicsContext2D();
	protected static GraphMap Graph = new GraphMap(0, canvas.getWidth(), 100, canvas.getHeight());
	protected static ObservableList<Node> nodes;
	protected static Scene scene;
	public GraphMap getGraph(){return Graph;}
	protected final StatusTimer simulation = new StatusTimer(){
		@Override
		public void handle(long now){
			Graph.move();
		}
	};
	public static void main(String... args){
            launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception{
		gc.setFill(Color.SLATEGRAY);
		gc.fillRect(0, 0, canvas.getWidth(), 100);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 100, canvas.getWidth(), canvas.getHeight());
		Group base = new Group(), shapes = new Group();
		nodes = shapes.getChildren();
		base.getChildren().addAll(canvas, shapes);
		HBox current = new HBox(3.0);
		current.getChildren().add(base);
		current.setPadding(new Insets(3.0));
		current.setAlignment(Pos.CENTER_LEFT);
		VBox root = new VBox(current);
		primaryStage.setTitle("Graph");
		primaryStage.setScene(scene = new Scene(root));
		primaryStage.show();
		createControls();
	}
	protected void createControls(){
		setTextField(10, 30, "B: ", B1);
		setTextField(10, 60, "   ", B2);
		Button drawButton = setNode(B1.getTranslateX() + 305, B1.getTranslateY(), new Button("Parse " +
				"and Draw"));
		drawButton.setOnAction(button -> parse());
		setNode(drawButton.getTranslateX(), drawButton.getTranslateY() + 35, new Button(
				"Play")).setOnAction(b -> {
			Button button = (Button)b.getSource();
			if(simulation.isRunning()){
				simulation.stop();
				button.setText("Play");
			}else{
				simulation.start();
				button.setText("Pause");
			}
		});
	}
	public int[] getB1(){
		return fromTextField(B1);
	}
	public int[] getB2(){
		return fromTextField(B2);
	}
	public void setB(int[] b1, int[] b2){
		toTextField(b1, B1);
		toTextField(b2, B2);
		parse();
		simulation.start();
	}
	protected <E extends Node> E setNode(double x, double y, E node){
		node.setTranslateX(x);
		node.setTranslateY(y);
		nodes.add(node);
		return node;
	}
	void setTextField(double x, double y, String text, TextField field){
		Text label = setNode(x, y, new Text(text));
		label.setFont(new Font("Arial", 20));
		label.setStroke(Color.WHITE);
		label.setStrokeWidth(1);
		setNode(x + 50, y - 20, field);
	}
	void toTextField(int[] values, TextField field){
		field.setText(Arrays.stream(values).mapToObj(v -> String.valueOf(v)).collect(Collectors.joining(" ")));
	}
	protected void parse(){
		clear();
		Graph.parseB(fromTextField(B1), fromTextField(B2));
		Graph.getVertices().forEach(v -> {
			Stack<Paint> stack = new Stack<>();
			stack.addAll(colors);
			Graph.traverseVertices(v, new VertexWalker.Chrome(stack, new HashSet<>(), new HashSet<>()));
		});
		Graph.arrange();
		nodes.addAll(Graph.getAllNodes());
	}
	protected void clear(){
		nodes.removeAll(Graph.clearAllNodes());
	}
	protected int[] fromTextField(TextField field){
		String[] split = field.getText().trim().split("\\s+");
		int[] values = new int[split.length];
		for(int i = 0; i < split.length; ++i)
			values[i] = Integer.parseInt(split[i]);
		return values;
	}
	void fillB(){
		B1.setText(String.join(" ", Graph.getB1()));
		B2.setText(String.join(" ", Graph.getB2()));
	}
	protected abstract class StatusTimer extends AnimationTimer{
		private volatile boolean Running = false;
		public boolean isRunning(){return Running;}
		@Override
		public void start(){
			Running = true;
			super.start();
		}
		@Override
		public void stop(){
			super.stop();
			Running = false;
		}
	}
}
