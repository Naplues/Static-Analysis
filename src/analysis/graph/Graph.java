package analysis.graph;

import java.util.Stack;

import utils.FileHandle;
import utils.Structure;

/**
 * 有向图结构：邻接链表
 * 
 * @author naplues
 *
 */
public class Graph {
	// 结点
	private String name;
	private Node[] nodes; // 结点列表
	private int nodeNumber;// 结点数目
	private int arcNumber;// 边数目
	private static int MAX_NODES_NUM = 2000;

	/**
	 * 构造图结构
	 * 
	 * @param structure
	 * @return
	 */
	public Graph constructGraph(String structure) {
		Graph graph = new Graph();
		int IF_TEMP_NO = 1;
		int WHILE_TEMP_ID = 1;
		int SWITCH_TEMP_NO = 1;
		int RETURN_NO = 1;
		boolean isSwitch = false;
		boolean isIfElse = false;
		Stack<Integer> breakStack = new Stack<>();
		Stack<Integer> nodeStack = new Stack<>();
		Stack<String> typeStack = new Stack<>();
		Stack<Integer> switchStack = new Stack<>();
		Stack<Integer> ifStack = new Stack<>();
		Stack<Integer> returnStack = new Stack<>();
		boolean entry = false;
		String lastState = "";
		nodes[0] = new Node("Start", "Msquare", "green"); // 开始结点
		nodeNumber++;
		// i: 字符串游标 j:结点游标 k:标签游标
		for (int i = 0, j = 0, k = 0; i < structure.length(); i++) {
			// 顺序语句,j指向当前最后的结点
			if (structure.charAt(i) == 'P') {
				Node newNode = new Node(); // 新建一个结点,结点数目+1
				nodeNumber++;

				if (isSwitch) {
					int switchNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[switchNode - 1].getFirstArc());
					arc.setInfo(Structure.labels.get(k++));
					nodes[switchNode - 1].setFirstArc(arc);
					isSwitch = false;

				} else if (isIfElse) {
					// else分支
					int ifNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setAttributes("No", "bold", "red");
					nodes[ifNode - 1].setFirstArc(arc);
					isIfElse = false;
				} else {
					Arc arc = new Arc(newNode.getId(), nodes[j].getFirstArc());// 边指向该结点
					// if/while真分支入口
					if (entry) {
						arc.setAttributes("Yes", "bold", "green");
						entry = false;
					}
					// 上个节点是do-while循环尾部
					if (lastState.equals("do")) {
						arc.setAttributes("", "dashed", "blue");
					}
					nodes[j].setFirstArc(arc);
				}
				newNode.setInfo(Structure.labels.get(k++));
				nodes[++j] = newNode;
				lastState = "";
				continue;
			}
			// return 结构
			if (structure.charAt(i) == 'r') {
				Node newNode = new Node("return" + RETURN_NO++); // 新建return结点
				nodeNumber++;
				Arc arc = new Arc(newNode.getId());
				nodes[j].setFirstArc(arc); // 上结点指向return结点
				nodes[++j] = newNode;
				returnStack.push(newNode.getId()); // return 结点入栈
				lastState = "return";
				continue;
			}
			// 循环中 break 结构
			if (structure.charAt(i) == 'b') {
				breakStack.push(nodes[j].getId()); // break结点压栈
				continue;
			}

			// 进入if/while结构
			if (structure.charAt(i) == 'D') {
				// 新建一个谓词结点，结点数目+1
				Node newNode = new Node();
				nodeNumber++;
				// 紧跟在switch之后循环
				if (isSwitch) {
					int switchNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[switchNode - 1].getFirstArc());
					arc.setInfo(Structure.labels.get(k++));
					nodes[switchNode - 1].setFirstArc(arc);
					isSwitch = false;
				} else if (isIfElse) {
					int ifNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setAttributes("No", "bold", "red"); // 设置边的属性
					nodes[ifNode - 1].setFirstArc(arc);
					isIfElse = false;
				}
				Arc arc = new Arc(newNode.getId()); // 指向谓词结点
				// if/while真分支入口
				if (entry) {
					arc.setAttributes("Yes", "bold", "green");
					entry = false;
				}
				nodes[j].setFirstArc(arc);
				nodeStack.push(newNode.getId());

				if (structure.charAt(i + 1) == '0') {
					typeStack.push("if"); // if结点入栈
					newNode.setAttributes(Structure.labels.get(k++), "diamond", "blue");// 菱形
					entry = true;
				} else if (structure.charAt(i + 1) == '1') {
					typeStack.push("else"); // else
					newNode.setAttributes(Structure.labels.get(k++), "diamond", "blue");// 菱形
					entry = true;
				} else if (structure.charAt(i + 1) == '2') {
					typeStack.push("while");// while结点入栈
					newNode.setAttributes(Structure.labels.get(k++), "ellipse", "blue");// 椭圆形
					entry = true;
				} else if (structure.charAt(i + 1) == '3') {
					typeStack.push("do"); // do结点入栈
					newNode.setInfo(Structure.labels.get(k++));
				}

				nodes[++j] = newNode;
				continue;
			}
			// switch谓词结点, 八边形
			if (structure.charAt(i) == 'C' && structure.charAt(i + 1) == 'A') {
				Node newNode = new Node(Structure.labels.get(k++), "octagon", "lightgreen");
				nodeNumber++;

				if (isIfElse) {
					// else分支
					int ifNode = nodeStack.peek();
					Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setAttributes("No", "bold", "red");
					nodes[ifNode - 1].setFirstArc(arc);
					isIfElse = false;
				} else {
					Arc arc = new Arc(newNode.getId()); // 指向谓词结点
					nodes[j].setFirstArc(arc);
				}

				nodeStack.push(newNode.getId()); // switch结点入栈
				typeStack.push("switch");
				nodes[++j] = newNode;
				isSwitch = true;
				continue;
			}
			// switch结点
			if (structure.charAt(i) == ',') {
				isSwitch = true;
				switchStack.push(nodes[j].getId());
				continue;
			}
			// if-else分支
			if (structure.charAt(i) == '|') {
				isIfElse = true;

				ifStack.push(nodes[j].getId()); // if-else真分支结尾
				continue;
			}
			/////////////////////////// 块结构尾部
			if (structure.charAt(i) == ')') {
				String type = typeStack.pop();

				// 获取谓词结点类型
				if (type.equals("if")) {
					// if结点
					// 临时结点
					Node newNode = new Node("if-temp" + IF_TEMP_NO++);
					nodeNumber++;
					Arc arc = new Arc(newNode.getId(), nodes[j].getFirstArc());
					if (lastState.equals("do")) {
						arc.setAttributes("", "dashed", "blue");
					}
					// 没有break跳转时连接后续结点
					if (breakStack.isEmpty()) {
						nodes[j].setFirstArc(arc);
					}

					// if结点指向新结点
					int ifNode = nodeStack.pop(); // 谓词结点出栈
					arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
					arc.setAttributes("No", "bold", "red"); // false边
					nodes[ifNode - 1].setFirstArc(arc);

					nodes[++j] = newNode; // 加入新结点
					lastState = "if";

				} else if (type.equals("else")) {
					Node newNode = new Node("ifelse-temp" + IF_TEMP_NO++);
					nodeNumber++;
					Arc arc = new Arc(newNode.getId(), nodes[j].getFirstArc());

					nodes[j].setFirstArc(arc);

					nodeStack.pop(); // 谓词结点出栈
					int ifNode = ifStack.pop();
					// if真分支指向ifelse-temp 当没有break跳转的时候
					if(breakStack.isEmpty()) {
						arc = new Arc(newNode.getId());
						nodes[ifNode - 1].setFirstArc(arc);
					}
					

					// if假分支指向ifelse-temp
					arc = nodes[j].getFirstArc();
					if (lastState.equals("do")) {
						arc.setAttributes("", "dashed", "blue");
					}
					nodes[j].setFirstArc(arc);

					nodes[++j] = newNode; // 加入新结点
					lastState = "else";
				} else if (type.equals("while")) {
					// while结点
					// 循环体 尾部指向while结点
					int whileNode = nodeStack.pop();
					Arc arc = new Arc(whileNode, nodes[j].getFirstArc());
					nodes[j].setFirstArc(arc);

					// while指向循环体后面的结点,辅助结点
					Node newNode = new Node("while-temp" + WHILE_TEMP_ID++);
					nodeNumber++;
					arc = new Arc(newNode.getId(), nodes[whileNode - 1].getFirstArc());
					arc.setAttributes("No", "bold", "red");
					nodes[whileNode - 1].setFirstArc(arc);

					// 判断是否有直接的break跳转
					if (!breakStack.isEmpty()) {
						int breakNode = breakStack.pop();
						arc = new Arc(newNode.getId(), nodes[breakNode - 1].getFirstArc()); // break指向while循环后面,break之前有出边
						arc.setAttributes("break", "dashed", "blue");
						nodes[breakNode - 1].setFirstArc(arc);
					}

					nodes[++j] = newNode;
					lastState = "while";
				} else if (type.equals("do")) {
					// do结点
					// 循环体后继结点
					Node newNode = new Node(Structure.labels.get(k++));
					newNode.setShape("ellipse"); // do循环椭圆
					nodeNumber++;
					Arc arc = new Arc(newNode.getId());
					nodes[j].setFirstArc(arc);

					// 循环体尾部指向do结点
					int doNode = nodeStack.pop();
					arc = new Arc(doNode, newNode.getFirstArc());
					arc.setAttributes("Yes", "bold", "green");
					newNode.setFirstArc(arc);
					nodes[++j] = newNode;

					lastState = "do";

				} else if (type.equals("switch")) {
					// switch
					// 将switch结点全部退出
					Node newNode = new Node("switch-temp" + SWITCH_TEMP_NO++);
					nodeNumber++;
					nodeStack.pop(); // 退出switch

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
		if (nodeStack.isEmpty()) {
			System.out.println("empty");
		}

		// 出口结点
		nodes[nodeNumber] = new Node("End", "Msquare", "pink");
		Arc arc = new Arc(nodes[nodeNumber].getId(), nodes[nodeNumber - 1].getFirstArc());
		if (lastState.equals("do")) {
			arc.setAttributes("", "dashed", "blue");
		}
		nodes[nodeNumber - 1].setFirstArc(arc);
		nodeNumber++;
		// 所有return指向出口结点
		for (; !returnStack.isEmpty();) {
			int returnNode = returnStack.pop();
			arc = new Arc(nodes[nodeNumber - 1].getId()); // 指向出口
			arc.setAttributes("Exit", "bold", "orange");
			nodes[returnNode - 1].setFirstArc(arc);
		}
		return graph;
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
			string += " " + nodes[i].getId() + "  ";
			string += "[";
			string += "shape = " + nodes[i].getShape() + ", ";
			// string += "style = filled, ";
			string += "color = " + nodes[i].getFillColor() + ", ";
			string += "label = \"" + nodes[i].getInfo() + "\", ";
			string += "]\n";
		}
		// 输出边
		for (int i = 0; i < nodeNumber; i++) {
			arc = nodes[i].getFirstArc();
			for (; null != arc; arc = arc.getNextArc()) {
				// 起点->终点 [label = "info", fontColor = color]
				string += " " + nodes[i].getId() + " ";
				string += " -> ";
				string += " " + nodes[arc.getDest() - 1].getId() + "  ";
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
		FileHandle.writeStringToFile("out/test.dot", string); // 写入文件
		Graph.drawGraph(""); // 画出文件
		return string;
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
				System.out.println("导出成功");
			} else {
				System.out.println("导出失败");
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
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

	///////////////////////////// constructor getter and setter
	public Graph() {
		nodes = new Node[Graph.MAX_NODES_NUM];
		this.setNodeNumber(0);
		this.setArcNumber(0);
	}

	public Graph(String structure) {
		this();
		this.constructGraph(structure);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
