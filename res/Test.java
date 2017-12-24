
public void sample(int a, int b)
{
	for (int i = 0; i < structure.length(); i++) {
		if (structure.charAt(i) == '|') {
			System.out.println(i);
			for (int j = i - 1; j >= 0; j--) {
				if (structure.charAt(j) == ')') {
					stack.push(')');
					continue;
				}
				if (structure.charAt(j) == '(') {
					if (!stack.isEmpty()) {
						stack.pop();
						continue;
					} else {
						// j-1处0改为1
						structure.setCharAt(j - 1, '1');
						break;
					}

				}
			}
		}
	}
}

