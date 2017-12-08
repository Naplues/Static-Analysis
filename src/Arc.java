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
	private String info; // 边信息
	private Arc nextArc; // 边的下一条边

	public Arc() {
		this.id = Arc.ID++;
		this.info = "Arc" + this.id;
		this.nextArc = null;
	}
	/**
	 * 指定边的目标
	 * @param dest
	 */
	public Arc(int dest) {
		this();
		this.dest = dest;
	}
	/**
	 * 指定边的目标和下一条边
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

}