package graph;

public abstract class FordFulkersonInt {
	public final void dummy() {

	}

	public final int maxflow(final FlowGraphInt fg, final boolean stepShuffle) {
		int flow = 0;
		for (int step = 1, f = push(fg, step); FlowGraphInt.isPositive(f); f = push(fg, ++step)) {
			dummy();
			flow += f;
			if (stepShuffle)
				fg.forget();
		}
		return flow;
	}

	protected abstract int push(final FlowGraphInt fg, final int step);

	public static class VCheckFixed extends FordFulkersonInt {
		@Override
		protected int push(final FlowGraphInt fg, final int step) {
			return dfs(fg.getSource(), FlowGraphInt.INF, step);
		}

		private static int dfs(final FlowGraphInt.V s, final int max, final int step) {
			if (s.isSink)
				return max;
			s.step = step;
			for (final FlowGraphInt.E e : s.es)
				if (e.to.step < step && !e.isFull()) {
					final int delta = dfs(e.to, e.pushable(max), step);
					if (FlowGraphInt.isPositive(delta)) {
						e.push(delta);
						return delta;
					}
				}
			return 0;
		}
	}

	public static class ECheck extends FordFulkersonInt {
		@Override
		protected int push(final FlowGraphInt fg, final int step) {
			return dfs(fg.getSource(), FlowGraphInt.INF, step);
		}

		private static int dfs(final FlowGraphInt.V s, final int max, final int step) {
			if (s.isSink)
				return max;
			for (final FlowGraphInt.E e : s.es)
				if (e.step < step && !e.isFull()) {
					e.step = step;
					final int delta = dfs(e.to, e.pushable(max), step);
					if (FlowGraphInt.isPositive(delta)) {
						e.push(delta);
						return delta;
					}
				}
			return 0;
		}
	}

}
