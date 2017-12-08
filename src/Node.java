
/**
 * 结点定义
 * 
 * @author naplues
 * 
 */
public class Node {
	private int id; // 结点标识
	private Arc firstArc; // 连出的第一条边

	// 结点属性
	private String info; // 结点信息
	private String shape; // 结点形状
	private String fillColor; // 填充颜色

	private static int ID = 1;

	public Node() {
		this.id = Node.ID++;
		this.info = "Node " + this.id;
		this.shape = "box"; // 结点形状默认为椭圆
		this.fillColor = "blue";
		firstArc = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Arc getFirstArc() {
		return firstArc;
	}

	public void setFirstArc(Arc firstArc) {
		this.firstArc = firstArc;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
}
