
/**
 * 结点定义
 * 
 * @author naplues
 * 
 */
public class Node {
	private int id;       // 结点标识
	private String info;  // 结点信息
	private Arc firstArc; // 连出的第一条边
	
	private static int ID = 1;
	public Node() {
		this.id = Node.ID++;
		this.info = "Node " + this.id;
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
	

}
