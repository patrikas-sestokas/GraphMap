package B19;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.Arrays;
import java.util.IdentityHashMap;
public class AllColors{
	IdentityHashMap<String, Paint> colors = new IdentityHashMap<>();
 	public AllColors(){
		Arrays.stream(Color.class.getDeclaredFields()).filter(f -> f.getType().equals(Color.class) && java.lang.reflect.Modifier.isStatic(f.getModifiers())).forEach(f -> {
			try{
				colors.put(f.getName(), (Paint)f.get(null));
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		});
	}
}
