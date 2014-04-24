package graph;

import gov.nasa.jpf.jvm.Verify;

public class TestP {
	private static final int C = 1000;

	private static FlowGraphPositiveInt graph1(FlowGraphPositiveInt.Oracles o) {
		final FlowGraphPositiveInt fg = new FlowGraphPositiveInt(4, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(0, 2, C);
		fg.addEdge(1, 2, 1);
		fg.addEdge(1, 3, C);
		fg.addEdge(2, 3, C);
		return fg;
	}

	private static FlowGraphPositiveInt graph2(FlowGraphPositiveInt.Oracles o) {
		final FlowGraphPositiveInt fg = new FlowGraphPositiveInt(4, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(1, 2, 1);
		fg.addEdge(2, 1, 1);
		fg.addEdge(1, 3, C);
		return fg;
	}

	private static FlowGraphPositiveInt graph3(FlowGraphPositiveInt.Oracles o) {
		final FlowGraphPositiveInt fg = new FlowGraphPositiveInt(5, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(1, 2, C);
		fg.addEdge(2, 3, 1);
		fg.addEdge(3, 1, C);
		fg.addEdge(1, 4, C);
		return fg;
	}

	private static FlowGraphPositiveInt graph4(FlowGraphPositiveInt.Oracles o) {
		final FlowGraphPositiveInt fg = new FlowGraphPositiveInt(5, o);
		fg.addEdge(0, 1, C);
		fg.addEdge(0, 2, C);
		fg.addEdge(1, 3, C);
		fg.addEdge(3, 2, 1);
		fg.addEdge(1, 4, C);
		fg.addEdge(2, 4, C);
		return fg;
	}

	public static void main(String... args) {
		new FordFulkersonPositiveInt.VCheckOfferMixed().maxflow(
				graph1(FlowGraphPositiveInt.Oracles.valueOf(args[Verify.getInt(0, args.length - 1)])), false);
	}
}
