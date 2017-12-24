public void sample(int a, int b)
{
	while(a>0) {
		if (!line.trim().startsWith("if") && !line.trim().startsWith("while")) {
			if ( !type.isEmpty() && type.peek().equals("catch")) {
				continue;
			}
			type.push("simple");
			structure += "P";
			type.pop();
			labels.add(line);
			continue;
		}
	}

}

