package graph;

import gov.nasa.jpf.jvm.Verify;

public class Test {

	private static FlowGraphInt graph1(FlowGraphInt.Oracles o) {
		final int C = 1000;
		final FlowGraphInt fg = new FlowGraphInt(4, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(0, 2, C);
		fg.addEdge(1, 2, 1);
		fg.addEdge(1, 3, C);
		fg.addEdge(2, 3, C);
		return fg;
	}

	private static FlowGraphInt graph2(FlowGraphInt.Oracles o) {
		final int C = 100;
		final FlowGraphInt fg = new FlowGraphInt(4, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(1, 2, 1);
		fg.addEdge(2, 1, 1);
		fg.addEdge(1, 3, C);
		return fg;
	}
	private static FlowGraphInt graph3(FlowGraphInt.Oracles o) {
		final int C = 100;
		final FlowGraphInt fg = new FlowGraphInt(5, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(1, 2, C);
		fg.addEdge(2, 3, 1);
		fg.addEdge(3, 1, C);
		fg.addEdge(1, 4, C);
		return fg;
	}

	private static FlowGraphInt graph4(FlowGraphInt.Oracles o) {
		final int C = 100;
		final FlowGraphInt fg = new FlowGraphInt(5, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(0, 2, C);
		fg.addEdge(1, 3, C);
		fg.addEdge(3, 2, 1);
		fg.addEdge(1, 4, C);
		fg.addEdge(2, 4, C);
		return fg;
	}

	public static void main(String... args) {
		new FordFulkersonInt.VCheckFixed().maxflow(
				graph1(FlowGraphInt.Oracles.valueOf(args[Verify.getInt(0, args.length - 1)])), false);
	}
}
