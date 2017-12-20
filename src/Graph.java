
import java.util.List;
import java.util.Stack;

public class Graph {
	// 结点
	private Node[] nodes;
	private int nodeNumber;
	private int arcNumber;
	private static int MAX_NODES_NUM = 1000;

	public Graph() {
		nodes = new Node[Graph.MAX_NODES_NUM];
		this.setNodeNumber(0);
		this.setArcNumber(0);
	}

	/**
	 * 创建图
	 * 
	 * @param data
	 */
	public void createGraph(int[][] data) {
		for (int i = 0; i < Graph.MAX_NODES_NUM; i++) {
			nodes[i] = new Node();
		}
		Arc arc = null;
		for (int[] d : data) {
			int s = d[0] - 1; // 开始索引-1
			int e = d[1]; // 结束标识不用-1
			arc = new Arc(e); // 设置终点e
			arc.setNextArc(nodes[s].getFirstArc()); // 头插法
			nodes[s].setFirstArc(arc);

			if (s > nodeNumber)
				nodeNumber = s + 1;
			if (e > nodeNumber)
				nodeNumber = e + 1;
		}
	}

	/**
	 * 创建图
	 * 
	 * @param data
	 */
	public void createGraph(List<Integer[]> data) {
		int[][] a = new int[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			a[i] = new int[2];
			a[i][0] = data.get(i)[0];
			a[i][1] = data.get(i)[1];
		}
		createGraph(a);
	}

	/**
	 * 按结点遍历图
	 */
	public void printByNodes() {
		Arc arc = null;
		for (int i = 0; i < nodeNumber; i++) {
			System.out.println(nodes[i].getId() + ":");
			arc = nodes[i].getFirstArc();
			while (null != arc) {
				System.out.print(arc.getId() + "  ");
				arc = arc.getNextArc();
			}
			System.out.println();
		}
	}

	/**
	 * 生成dot格式文件,效率有待提高
	 */
	public String outputGraph() {
		String string = "";
		Arc arc = null;
		string += "digraph CFG {\n";

		// 输出结点
		for (int i = 0; i < nodeNumber; i++) {
			string += "\"" + nodes[i].getInfo() + "\"";
			string += "[";
			string += "shape = " + nodes[i].getShape() + ", ";
			// string += "style = filled, ";
			string += "color = " + nodes[i].getFillColor() + ", ";
			string += "]\n";
		}
		// 输出边
		for (int i = 0; i < nodeNumber; i++) {
			arc = nodes[i].getFirstArc();
			for (; null != arc; arc = arc.getNextArc()) {
				// 起点->终点 [label = "info", fontColor = color]
				string += "\"" + nodes[i].getInfo() + "\"";
				string += " -> ";
				string += "\"" + nodes[arc.getDest() - 1].getInfo() + "\" ";
				string += "[";
				string += "style = " + arc.getStyle() + ", ";
				string += "label=\"" + arc.getInfo() + "\", ";
				string += "fillcolor = " + arc.getColor() + ", ";
				string += "color = " + arc.getColor() + ", ";
				string += "fontcolor = " + arc.getColor();
				string += "]\n";
			}
			string += "\n";
		}
		string += "}";
		return string;
	}

	/**
	 * 构造图
	 * 
	 * @param structure
	 * @return
	 */
	public Graph constructGraph(String structure) {
		Graph graph = new Graph();

		int IF_NO = 1;
		int IF_ELSE_NO = 1;
		int IF_TEMP_NO = 1;
		int WHILE_NO = 1;
		int WHILE_TEMP_ID = 1;
		int DO_NO = 1;
		int SWITCH_NO = 1;
		int SWITCH_TEMP_NO = 1;
		int RETURN_NO = 1;
		boolean isSwitch = false;
		boolean isIfElse = false;
		Stack<Integer> nodeStack = new Stack<>();
		Stack<String> typeStack = new Stack<>();
		Stack<Integer> switchStack = new Stack<>();
		Stack<Integer> ifStack = new Stack<>();
		Stack<Integer> returnStack = new Stack<>();
		boolean entry = false;
		String lastState = "";
		nodes[0] = new Node(); // 开始结点
		nodes[0].setInfo("Start");
		nodes[0].setShape("Msquare");
		nodes[0].setFillColor("green");
		nodeNumber++;
		for (int i = 0, j = 0; i < structure.length(); i++) {
			// 顺序语句,j指向当前最后的结点
			if (structure.charAt(i) == 'P') {

				Node newNode = new Node(); // 新建一个结点,结点数目+1
				nodeNumber++;
				if (isSwitch) {
					int switchNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[switchNode - 1].getFirstArc());
					nodes[switchNode - 1].setFirstArc(arc);
					isSwitch = false;
				} else if (isIfElse) {
					// else分支
					int ifNode = nodeStack.peek();
					nodes[ifNode - 1].setInfo("if-else" + IF_ELSE_NO++);
					Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setInfo("No");
					arc.setStyle("bold");
					arc.setColor("red");
					nodes[ifNode - 1].setFirstArc(arc);
					isIfElse = false;
				} else {

					Arc arc = new Arc(newNode.getId());// 边指向该结点
					// if真分支入口
					if (entry) {
						arc.setInfo("Yes");
						arc.setStyle("bold");
						arc.setColor("green");
						entry = false;
					}
					nodes[j].setFirstArc(arc);

				}
				nodes[++j] = newNode;
				lastState = "";
			} else if (structure.charAt(i) == 'r') {
				// return 模块
				Node newNode = new Node(); // 新建return结点
				newNode.setInfo("return" + RETURN_NO++);
				nodeNumber++;
				Arc arc = new Arc(newNode.getId());
				nodes[j].setFirstArc(arc); // 上结点指向return结点
				nodes[++j] = newNode;
				returnStack.push(newNode.getId()); // return 结点入栈
				lastState = "return";

			} else if (structure.charAt(i) == 'D') {
				// 新建一个谓词结点，结点数目+1
				Node newNode = new Node();
				nodeNumber++;
				// 紧跟在switch之后循环
				if (isSwitch) {
					int switchNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[switchNode - 1].getFirstArc());
					nodes[switchNode - 1].setFirstArc(arc);
					isSwitch = false;
				} else if (isIfElse) {
					int ifNode = nodeStack.peek();
					nodes[ifNode - 1].setInfo("if-else" + IF_ELSE_NO++);
					Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setInfo("No");
					arc.setStyle("bold");
					arc.setColor("red");
					nodes[ifNode - 1].setFirstArc(arc);
					isIfElse = false;
				}
				Arc arc = new Arc(newNode.getId()); // 指向谓词结点
				nodes[j].setFirstArc(arc);

				nodeStack.push(newNode.getId());
				if (structure.charAt(i + 1) == '0') {
					typeStack.push("if"); // if结点入栈
					newNode.setInfo("if" + IF_NO++);
					newNode.setShape("diamond"); // 菱形
				} else if (structure.charAt(i + 1) == '2') {
					typeStack.push("while");// while结点入栈
					newNode.setInfo("while" + WHILE_NO++);
					newNode.setShape("ellipse"); // 椭圆形
				} else if (structure.charAt(i + 1) == '3') {
					typeStack.push("do"); // do结点入栈
					newNode.setInfo("do" + DO_NO++);

				}
				entry = true;
				nodes[++j] = newNode;

			} else if (structure.charAt(i) == 'C') {
				// switch谓词结点
				Node newNode = new Node();
				newNode.setInfo("switch" + SWITCH_NO++);
				newNode.setShape("octagon"); // 八边形
				newNode.setFillColor("lightgreen");
				nodeNumber++;
				Arc arc = new Arc(newNode.getId()); // 指向谓词结点
				nodes[j].setFirstArc(arc);

				nodeStack.push(newNode.getId()); // switch结点入栈
				typeStack.push("switch");

				nodes[++j] = newNode;
				isSwitch = true;
			} else if (structure.charAt(i) == ',') {
				// switch结点
				isSwitch = true;
				switchStack.push(nodes[j].getId());

			} else if (structure.charAt(i) == '|') {
				// if-else分支
				isIfElse = true;
				// nodeStack.push(nodeStack.peek()); //if-else时栈顶两个元素相等
				ifStack.push(nodes[j].getId()); // if-else真分支结尾

			} else if (structure.charAt(i) == ')') {
				String type = typeStack.pop();
				// 获取谓词结点类型
				if (type.equals("if")) {
					// if结点
					// 临时结点
					Node newNode = new Node();
					newNode.setInfo("if-temp" + IF_TEMP_NO++);
					nodeNumber++;
					Arc arc = new Arc(newNode.getId());
					nodes[j].setFirstArc(arc);

					if (ifStack.isEmpty()) {
						// 单分支if
						// if结点指向新结点
						int ifNode = nodeStack.pop(); // 谓词结点出栈
						/*
						 * if(ifNode == nodeStack.peek()) nodeStack.pop();
						 */
						arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
						arc.setInfo("No");
						arc.setStyle("bold");
						arc.setColor("red");
						nodes[ifNode - 1].setFirstArc(arc);

					} else {
						// if-else双分支
						// nodeStack.pop(); //谓词结点出栈
						arc = new Arc(newNode.getId());

						nodes[ifStack.pop() - 1].setFirstArc(arc);
						arc = new Arc(newNode.getId());

						nodes[j].setFirstArc(arc);
					}
					nodes[++j] = newNode; // 加入新结点
					lastState = "if";

				} else if (type.equals("while")) {
					// while结点

					// 循环体尾部指向while结点
					int whileNode = nodeStack.pop();

					Arc arc = new Arc(whileNode, nodes[j].getFirstArc());

					nodes[j].setFirstArc(arc);

					// while指向循环体后面的结点,辅助结点
					Node newNode = new Node();
					newNode.setInfo("while-temp" + WHILE_TEMP_ID++);
					nodeNumber++;
					arc = new Arc(newNode.getId(), nodes[whileNode - 1].getFirstArc());

					arc.setInfo("No");
					arc.setStyle("bold");
					arc.setColor("red");
					nodes[whileNode - 1].setFirstArc(arc);

					nodes[++j] = newNode;
					lastState = "while";
				} else if (type.equals("do")) {
					// do结点
					// 循环体后继结点
					Node newNode = new Node();
					nodeNumber++;
					Arc arc = new Arc(newNode.getId());

					nodes[j].setFirstArc(arc);

					// 循环体尾部指向do结点
					int doNode = nodeStack.pop();
					arc = new Arc(doNode, nodes[j].getFirstArc());

					nodes[j].setFirstArc(arc);

					nodes[++j] = newNode;
					lastState = "do";

				} else if (type.equals("switch")) {
					// switch
					// 将switch结点全部退出
					Node newNode = new Node();
					newNode.setInfo("switch-temp" + SWITCH_TEMP_NO++);
					nodeNumber++;
					// int switchNode = nodeStack.pop();

					for (; !switchStack.isEmpty();) {
						Arc newArc = new Arc(newNode.getId());

						nodes[switchStack.pop() - 1].setFirstArc(newArc);
					}

					Arc newArc = new Arc(newNode.getId());

					nodes[j].setFirstArc(newArc);

					nodes[++j] = newNode;
					lastState = "switch";
				}

			} 
		}
		// 出口结点
		nodes[nodeNumber] = new Node();
		Arc arc = new Arc(nodes[nodeNumber].getId());

		nodes[nodeNumber - 1].setFirstArc(arc);
		nodes[nodeNumber].setInfo("End");
		nodes[nodeNumber].setShape("Msquare");
		nodes[nodeNumber].setFillColor("pink");
		nodeNumber++;
		// 所有return指向出口结点
		for (; !returnStack.isEmpty();) {
			int returnNode = returnStack.pop();
			arc = new Arc(nodes[nodeNumber - 1].getId()); // 指向出口

			arc.setInfo("Exit");
			arc.setStyle("bold");
			arc.setColor("orange");
			nodes[returnNode - 1].setFirstArc(arc);
		}
		return graph;
	}

	/**
	 * 画出CFG图
	 */
	public static void drawGraph(String filePath) {

		String cmd = "cmd /c start  test.bat";
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			ps.waitFor();
			int i = ps.exitValue();
			if (i == 0) {
				System.out.println("执行成功");
			} else {
				System.out.println("执行失败");
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
	
	public int getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public int getArcNumber() {
		Arc arc = null;
		for (int i = 0; i < nodeNumber; i++)
			for (arc = nodes[i].getFirstArc(); null != arc; arc = arc.getNextArc())
				arcNumber++;
		return arcNumber;
	}

	public void setArcNumber(int arcNumber) {
		this.arcNumber = arcNumber;
	}
}
