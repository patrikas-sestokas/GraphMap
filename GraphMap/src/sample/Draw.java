package sample;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Stack;
public class Draw extends SimpleDraw{
	public static TextField LInput = new TextField(), lstInput = new TextField(), modified;
	public static void main(String... args){
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception{
		super.start(primaryStage);
		gc.setFill(Color.SLATEGRAY);
		gc.fillRect(0,0,canvas.getWidth(), 150);
		Graph.minY = 150;
	}
	public void setLLst(int[] l, int[] lst){
		toTextField(l, LInput);
		toTextField(lst, lstInput);
		parse();
		simulation.start();
	}
	@Override
	protected void createControls(){
		super.createControls();
		setTextField(10, 90, "L:  ", LInput);
		setTextField(10, 120, "lst:   ", lstInput);
	}
	@Override
	void setTextField(double x, double y, String text, TextField field){
		super.setTextField(x, y, text, field);
		field.setOnKeyTyped(i -> modified = ((TextField)i.getSource()));
	}
	@Override
	void toTextField(int[] values, TextField field){
		super.toTextField(values, field);
		modified = field;
	}
	@Override
	protected void parse(){
		clear();
		if(modified == LInput || modified == lstInput)
			Graph.parseLAndLst(fromTextField(LInput), fromTextField(lstInput));
		else Graph.parseB(fromTextField(B1), fromTextField(B2));
		Graph.getVertices().forEach(v -> {
			Stack<Paint> stack = new Stack<>();
			stack.addAll(colors);
			Graph.traverseVertices(v, new VertexWalker.Chrome(stack, new HashSet<>(), new HashSet<>()));
		});
		Graph.arrange();
		nodes.addAll(Graph.getAllNodes());
		fillLLst();
		fillB();
	}
	void fillLLst(){
		LInput.setText(String.join(" ", Graph.getL()));
		lstInput.setText(String.join(" ", Graph.getLst()));
	}
}
