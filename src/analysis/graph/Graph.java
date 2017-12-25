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

	////////////////////// 构图辅助数据结构
	int IF_TEMP_NO = 1;
	int WHILE_TEMP_ID = 1;
	int DO_WHILE_TEMP_NO = 1;
	int SWITCH_TEMP_NO = 1;
	int RETURN_NO = 1;
	int BREAK_NO = 1;
	boolean isSwitch = false;
	boolean isIfElse = false;

	Stack<Integer> breakStack = new Stack<>(); // 同层break栈
	Stack<Stack<Integer>> allBreakStack = new Stack<>(); // 所有的break栈
	Stack<Integer> continueStack = new Stack<>(); // 同层continue栈
	Stack<Stack<Integer>> allContinueStack = new Stack<>();// 所有continue栈
	Stack<Integer> nodeStack = new Stack<>(); // 谓词结点栈
	Stack<String> typeStack = new Stack<>(); // 谓词类型栈
	Stack<Integer> switchStack = new Stack<>(); // switch语句尾部栈
	int noBreakSwitchNode = -1; // 未带break的switch语句尾部结点
	Stack<Integer> ifStack = new Stack<>(); //
	Stack<Integer> returnStack = new Stack<>();
	boolean entry = false;
	String lastState = "";

	int i = 0, j = 0, k = 0;
	/////////////////////////////////////////////

	/**
	 * 构造图结构
	 * 
	 * @param structure
	 * @return
	 */
	public Graph constructGraph(String structure) {
		Graph graph = new Graph();

		nodes[0] = new Node("Start", "Msquare", "green", Node.SIMPLE_NODE); // 开始结点
		nodeNumber++;
		// i: 字符串游标 j:结点游标 k:标签游标
		for (; i < structure.length(); i++) {

			// 顺序语句,j指向当前最后的结点
			if (structure.charAt(i) == 'P') {
				Node newNode = new Node(); // 新建一个结点,结点数目+1
				nodeNumber++;
				if (isSwitch) {
					int switchNode = nodeStack.peek(); // 取出switch结点
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
					setIfTrueBranchColor(arc);
					// 上个节点是do-while循环尾部
					if (lastState.equals("do"))
						arc.setAttributes("", "dashed", "blue");

					nodes[j].setFirstArc(arc);
				}
				// 之前为switch分支结尾并且未带break
				// structure.charAt(i - 1) == ',' && structure.charAt(i - 2) != 'b'
				if (noBreakSwitchNode != -1) {
					Arc arc = new Arc(newNode.getId(), nodes[noBreakSwitchNode].getFirstArc());
					System.out.println(nodes[noBreakSwitchNode].getInfo());
					nodes[noBreakSwitchNode].setFirstArc(arc);
				}
				newNode.setInfo(Structure.labels.get(k++));
				nodes[++j] = newNode;
				lastState = "";
				continue;
			}
			// return 结构
			if (structure.charAt(i) == 'r') {
				returnStatment();
				continue;
			}
			// 循环中 break 结构
			if (structure.charAt(i) == 'b') {
				breakStatment();
				continue;
			}
			// continue 结构
			if (structure.charAt(i) == 'c') {
				continueStatment();
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
				setIfTrueBranchColor(arc); // 设置if真分支线条颜色
				nodes[j].setFirstArc(arc);
				nodeStack.push(newNode.getId());

				if (structure.charAt(i + 1) == '0') {
					typeStack.push("if"); // if结点入栈
					newNode.setAttributes(Structure.labels.get(k++), "diamond", "blue", Node.IF_NODE);// 菱形
					entry = true;
				} else if (structure.charAt(i + 1) == '1') {
					typeStack.push("else"); // else
					newNode.setAttributes(Structure.labels.get(k++), "diamond", "blue", Node.ELSE_NODE);// 菱形
					entry = true;
				} else if (structure.charAt(i + 1) == '2') {
					typeStack.push("while");// while结点入栈
					newNode.setAttributes(Structure.labels.get(k++), "ellipse", "blue", Node.WHILE_NODE);// 椭圆形
					entry = true;
					breakStack = new Stack<>(); // 新建break栈
					allBreakStack.push(breakStack);
					breakStack = allBreakStack.peek();
					continueStack = new Stack<>();
					allContinueStack.push(continueStack);
					continueStack = allContinueStack.peek();
				} else if (structure.charAt(i + 1) == '3') {
					typeStack.push("do"); // do结点入栈
					newNode.setInfo(Structure.labels.get(k++));
					breakStack = new Stack<>(); // 新建break栈
					allBreakStack.push(breakStack);
					breakStack = allBreakStack.peek();
					continueStack = new Stack<>();
					allContinueStack.push(continueStack);
					continueStack = allContinueStack.peek();
				}
				nodes[++j] = newNode;
				continue;
			}
			// switch谓词结点, 八边形
			if (structure.charAt(i) == 'C' && structure.charAt(i + 1) == 'A') {
				startSwitch(); // switch开始
				continue;
			}
			// switch结点
			if (structure.charAt(i) == ',') {
				isSwitch = true;
				// 将break结尾的switch加入switch栈
				if (structure.charAt(i - 1) == 'b') {
					switchStack.push(nodes[j].getId());
					noBreakSwitchNode = -1; // 该点不起作用
				} else {
					noBreakSwitchNode = j;
				}

				continue;
			}
			// if-else分支
			if (structure.charAt(i) == '|') {
				isIfElse = true;
				ifStack.push(nodes[j].getId()); // if-else真分支结尾
				continue;
			}
			/////////////////////////// 块结构结束////////////////
			if (structure.charAt(i) == ')') {
				String type = typeStack.pop();
				// 获取谓词结点类型
				if (type.equals("if")) {
					endIf(); // if分支结束
					continue;
				}
				if (type.equals("else")) {
					endElse(); // else分支结束
					continue;
				}
				if (type.equals("while")) {
					endWhile(); // while循环结束
					continue;
				}
				if (type.equals("do")) {
					endDo(); // do-while循环结束
					continue;
				}
				if (type.equals("switch")) {
					endSwitch(); // switch结束
					continue;
				}
			}
		}
		// 出口结点
		nodes[nodeNumber] = new Node("End", "Msquare", "pink", Node.SIMPLE_NODE);
		Arc arc = new Arc(nodes[nodeNumber].getId(), nodes[nodeNumber - 1].getFirstArc());
		if (lastState.equals("do")) {
			arc.setAttributes("Exit", "dashed", "blue");
		}
		nodes[nodeNumber - 1].setFirstArc(arc);
		nodeNumber++;
		// 所有return指向出口结点
		for (; !returnStack.isEmpty();) {
			int returnNode = returnStack.pop();
			arc = new Arc(nodes[nodeNumber - 1].getId()); // 指向出口
			arc.setAttributes("Return", "bold", "orange");
			nodes[returnNode - 1].setFirstArc(arc);
		}
		getUnreachableList(); // 去除不可达结点break continue造成
		return graph;
	}

	// ************子过程**********

	/**
	 * break 语句处理
	 */
	public void breakStatment() {
		Node newNode = new Node("break" + BREAK_NO++); // 新建break结点
		nodeNumber++;
		// else分支
		if (isIfElse) {
			int ifNode = nodeStack.peek();
			Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
			arc.setAttributes("No", "bold", "red");
			nodes[ifNode - 1].setFirstArc(arc);
			isIfElse = false;
		}

		Arc arc = new Arc(newNode.getId());
		setIfTrueBranchColor(arc); // 设置if真分支线条颜色
		nodes[j].setFirstArc(arc); // 上结点指向break结点
		nodes[++j] = newNode;
		breakStack.push(nodes[j].getId()); // break结点压栈
		// lastState = "break";
	}

	/**
	 * continue 语句处理
	 */
	public void continueStatment() {
		Node newNode = new Node("continue"); // 新建continue结点
		nodeNumber++;
		// else分支
		if (isIfElse) {
			int ifNode = nodeStack.peek();
			Arc arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
			arc.setAttributes("No", "bold", "red");
			nodes[ifNode - 1].setFirstArc(arc);
			isIfElse = false;
		}
		// 指向continue的边
		Arc arc = new Arc(newNode.getId());
		setIfTrueBranchColor(arc); // 设置if真分支线条颜色
		nodes[j].setFirstArc(arc); // 上结点指向continue结点
		nodes[++j] = newNode;
		continueStack.push(nodes[j].getId()); // 结点压栈
		// lastState = "continue";
	}

	/**
	 * return 语句处理
	 */
	public void returnStatment() {
		Node newNode = new Node("return" + RETURN_NO++); // 新建return结点
		nodeNumber++;
		Arc arc = new Arc(newNode.getId());
		setIfTrueBranchColor(arc); // 设置if真分支线条颜色
		nodes[j].setFirstArc(arc); // 上结点指向return结点
		nodes[++j] = newNode;
		returnStack.push(newNode.getId()); // return 结点入栈
		lastState = "return";
	}

	/**
	 * if 单分支结束
	 */
	public void endIf() {
		// if结点 临时结点
		Node newNode = new Node("if-temp" + IF_TEMP_NO++);
		nodeNumber++;
		Arc arc = new Arc(newNode.getId(), nodes[j].getFirstArc());
		if (lastState.equals("do")) {
			arc.setAttributes("", "dashed", "blue");
		}
		// 没有break跳转时连接后续结点

		nodes[j].setFirstArc(arc);

		// if结点指向新结点
		int ifNode = nodeStack.pop(); // 谓词结点出栈
		arc = new Arc(newNode.getId(), nodes[ifNode - 1].getFirstArc());
		arc.setAttributes("No", "bold", "red"); // false边
		nodes[ifNode - 1].setFirstArc(arc);

		nodes[++j] = newNode; // 加入新结点
		lastState = "if";
	}

	/**
	 * else分支结束
	 */
	public void endElse() {
		Node newNode = new Node("ifelse-temp" + IF_TEMP_NO++);
		nodeNumber++;

		nodeStack.pop(); // 谓词结点出栈
		int ifNode = ifStack.pop();
		// if真分支指向ifelse-temp 当没有break跳转的时候

		Arc arc = new Arc(newNode.getId());
		nodes[ifNode - 1].setFirstArc(arc);

		// if假分支指向ifelse-temp 当没有break跳转时
		arc = new Arc(newNode.getId(), nodes[j].getFirstArc());
		if (lastState.equals("do")) {
			arc.setAttributes("", "dashed", "blue");
		}
		nodes[j].setFirstArc(arc);

		nodes[++j] = newNode; // 加入新结点
		lastState = "else";
	}

	/**
	 * while循环结束
	 */
	public void endWhile() {
		// while结点
		// 循环体 尾部指向while结点
		int whileNode = nodeStack.pop();
		Arc arc = new Arc(whileNode, nodes[j].getFirstArc());
		nodes[j].setFirstArc(arc);

		// while指向循环体后面的结点,辅助结点
		Node newNode = new Node("while-temp" + WHILE_TEMP_ID++);
		nodeNumber++;
		arc = new Arc(newNode.getId(), nodes[whileNode - 1].getFirstArc());
		arc.setAttributes("exit", "dashed", "blue");
		nodes[whileNode - 1].setFirstArc(arc);

		// 判断是否有直接的break跳转
		while (!breakStack.isEmpty()) {
			int breakNode = breakStack.pop(); // 取得break结点
			if (nodes[breakNode - 1].getType() == Node.SIMPLE_NODE) { // 简单结点无其他出边
				arc = new Arc(newNode.getId()); // break指向while循环后面
			} else {
				arc = new Arc(newNode.getId(), nodes[breakNode - 1].getFirstArc()); // break指向while循环后面
			}
			arc.setAttributes("break", "dashed", "blue");
			nodes[breakNode - 1].setFirstArc(arc);
		}
		allBreakStack.pop();
		if (!allBreakStack.isEmpty())
			breakStack = allBreakStack.peek();

		// 判断是否有continue跳转
		while (!continueStack.isEmpty()) {
			int continueNode = continueStack.pop();
			if (nodes[continueNode - 1].getType() == Node.SIMPLE_NODE) { // 简单结点无其他出边
				arc = new Arc(whileNode); // break指向while循环后面
			} else {
				arc = new Arc(whileNode, nodes[continueNode - 1].getFirstArc()); // break指向while循环后面
			}
			arc.setAttributes("continue", "dashed", "orange");
			nodes[continueNode - 1].setFirstArc(arc);
		}
		allContinueStack.pop();
		if (!allContinueStack.isEmpty())
			continueStack = allContinueStack.peek();

		nodes[++j] = newNode;
		lastState = "while";
	}

	/**
	 * do循环结束
	 */
	public void endDo() {
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

		// do-whiletemp结点
		newNode = new Node("do-while-temp" + DO_WHILE_TEMP_NO++);
		nodeNumber++;
		arc = new Arc(newNode.getId(), nodes[j].getFirstArc());
		nodes[j].setFirstArc(arc);

		// 判断是否有直接的break跳转
		while (!breakStack.isEmpty()) {
			int breakNode = breakStack.pop(); // 取得break结点
			if (nodes[breakNode - 1].getType() == Node.SIMPLE_NODE) { // 简单结点无其他出边
				arc = new Arc(newNode.getId()); // break指向while循环后面
			} else {
				arc = new Arc(newNode.getId(), nodes[breakNode - 1].getFirstArc()); // break指向while循环后面
			}
			arc.setAttributes("break", "dashed", "blue");
			nodes[breakNode - 1].setFirstArc(arc);
		}
		allBreakStack.pop();
		if (!allBreakStack.isEmpty())
			breakStack = allBreakStack.peek();

		// 判断是否有continue跳转
		while (!continueStack.isEmpty()) {
			int continueNode = continueStack.pop();
			if (nodes[continueNode - 1].getType() == Node.SIMPLE_NODE) { // 简单结点无其他出边
				arc = new Arc(doNode); // break指向while循环后面
			} else {
				arc = new Arc(doNode, nodes[continueNode - 1].getFirstArc()); // break指向while循环后面
			}
			arc.setAttributes("continue", "dashed", "orange");
			nodes[continueNode - 1].setFirstArc(arc);
		}
		allContinueStack.pop();
		if (!allContinueStack.isEmpty())
			continueStack = allContinueStack.peek();

		nodes[++j] = newNode;
		lastState = "do";
	}

	/**
	 * switch开始
	 */
	public void startSwitch() {
		Node newNode = new Node(Structure.labels.get(k++), "octagon", "lightgreen", Node.SWITCH_NODE);
		nodeNumber++;
		// else 分支
		if (isIfElse) {
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
	}

	/**
	 * switch结束
	 * 
	 */
	public void endSwitch() {
		// switch
		// 将switch结点全部退出
		Node newNode = new Node("switch-temp" + SWITCH_TEMP_NO++);
		nodeNumber++;
		nodeStack.pop(); // 退出switch结点
		// 将所有分支尾部是break的switch分支尾部结点指向出口
		for (; !switchStack.isEmpty();) {
			Arc newArc = new Arc(newNode.getId());
			nodes[switchStack.pop() - 1].setFirstArc(newArc);
		}
		Arc newArc = new Arc(newNode.getId());
		nodes[j].setFirstArc(newArc);
		nodes[++j] = newNode;
		lastState = "switch";
	}

	/**
	 * 获取不可达结点列表
	 * 
	 * @return
	 */
	public void getUnreachableList() {
		boolean flag = false;
		for (int i = 1; i < nodeNumber; i++) {
			flag = false;
			// i为目标结点索引,从非开始结点算起。初始时默认不可达
			for (int j = 0; j < nodeNumber; j++) {
				Arc arc = nodes[j].getFirstArc();
				while (arc != null) {
					int dest = arc.getDest();// 目标点
					if ((dest - 1) == i) {
						flag = true;
						break;
					}
					arc = arc.getNextArc();
				}
			}
			// 如果结点可达，就设置该节点不可达
			nodes[i].setCanReach(flag);
		}
	}

	/**
	 * 设置If真分支入口
	 * 
	 * @param arc
	 * @param entry
	 * @return
	 */
	public void setIfTrueBranchColor(Arc arc) {
		// if/while真分支入口
		if (entry) {
			arc.setAttributes("Yes", "bold", "green");
			entry = false;
		}

	}

	/**
	 * 生成dot格式文件,效率有待提高
	 * 
	 * @param simple
	 * @return
	 */
	public String outputGraph(boolean simple) {
		String string = "";
		Arc arc = null;
		string += "digraph CFG {\n";
		// 输出结点
		for (int i = 0; i < nodeNumber; i++) {
			if (!nodes[i].isCanReach())
				continue;
			string += " " + nodes[i].getId() + "  ";
			string += "[";
			string += "shape = " + nodes[i].getShape() + ", ";
			// string += "style = filled, ";
			string += "color = " + nodes[i].getFillColor() + ", ";
			if (simple) // 简化输出图
				string += "label = \"" + nodes[i].getId() + "\", ";
			else
				string += "label = \"" + nodes[i].getInfo() + "\", ";

			string += "]\n";
		}
		// 输出边
		for (int i = 0; i < nodeNumber; i++) {
			if (!nodes[i].isCanReach())
				continue;
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
