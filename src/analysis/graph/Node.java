package analysis.graph;

/**
 * 结点定义
 * 
 * @author naplues
 * 
 */
public class Node {
	public static int SIMPLE_NODE = 0;
	public static int IF_NODE = 1;
	public static int WHILE_NODE = 2;
	public static int ELSE_NODE = 3;
	public static int SWITCH_NODE = 4;

	private int id; // 结点标识
	private Arc firstArc; // 连出的第一条边

	// 结点属性
	private String info; // 结点信息
	private String shape; // 结点形状
	private String fillColor; // 填充颜色
	private int type;// 结点类型
	private boolean canReach;
	private static int ID = 1;

	public Node() {
		this.id = Node.ID++;
		this.info = "Node " + this.id;
		this.shape = "box"; // 结点形状默认为盒形
		this.fillColor = "blue";
		this.type = SIMPLE_NODE;
		this.canReach = true;
		firstArc = null;
	}

	public Node(String info) {
		this();
		this.info = info;
	}

	// 带参构造器
	public Node(String info, String shape, String fillColor, int type) {
		this();
		this.info = info;
		this.shape = shape;
		this.fillColor = fillColor;
		this.type = type;
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

	// 设置边的属性
	public void setAttributes(String info, String shape, String fillColor, int type) {
		this.setInfo(info);
		this.setShape(shape);
		this.setFillColor(fillColor);
		this.setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isCanReach() {
		return canReach;
	}

	public void setCanReach(boolean canReach) {
		this.canReach = canReach;
	}
}
