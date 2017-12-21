/**
 * 边定义
 * 
 * @author naplues
 *
 */
public class Arc {
	private static int ID = 0;
	private int id; // 边标识
	private int dest; // 边指向的结点
	private Arc nextArc; // 边的下一条边

	// 边属性
	private String info; // 边信息
	private String color; // 边的颜色
	private String style; // 边的样式 bold

	public Arc() {
		this.id = Arc.ID++;
		this.nextArc = null;
		
		this.info = "";
		this.color = "black"; // 默认黑色
		this.style = "solid";
	}

	/**
	 * 指定边的目标
	 * 
	 * @param dest
	 */
	public Arc(int dest) {
		this();
		this.dest = dest;
	}

	/**
	 * 指定边的目标和下一条边
	 * 
	 * @param dest
	 * @param nextArc
	 */
	public Arc(int dest, Arc nextArc) {
		this(dest);
		this.nextArc = nextArc;
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

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public Arc getNextArc() {
		return nextArc;
	}

	public void setNextArc(Arc nextArc) {
		this.nextArc = nextArc;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	//设置边的属性
	public void setAttributes(String info, String style, String color) {
		this.setInfo(info);
		this.setStyle(style);
		this.setColor(color);
	}
}