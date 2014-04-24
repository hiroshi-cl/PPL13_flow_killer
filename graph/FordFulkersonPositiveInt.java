package graph;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class FordFulkersonPositiveInt {
	public final void dummy() {

	}

	public final int maxflow(final FlowGraphPositiveInt fg, final boolean stepShuffle) {
		int flow = 0;
		for (int step = 1, f = push(fg, step); FlowGraphPositiveInt.isPositive(f); f = push(fg, ++step)) {
			dummy();
			flow += f;
			if (stepShuffle)
				fg.forget();
		}
		return flow;
	}

	protected abstract int push(final FlowGraphPositiveInt fg, final int step);

	public static class VCheckOfferLast extends FordFulkersonPositiveInt {
		@Override
		protected int push(final FlowGraphPositiveInt fg, final int step) {
			return dfs(fg.getSource(), FlowGraphPositiveInt.INF, step);
		}

		private static int dfs(final FlowGraphPositiveInt.V s, final int max, final int step) {
			if (s.isSink)
				return max;
			s.step = step;
			final Deque<FlowGraphPositiveInt.E> deque = new ArrayDeque<FlowGraphPositiveInt.E>();
			while (!s.es.isEmpty()) {
				final FlowGraphPositiveInt.E e = s.es.poll();
				if (e.to.step < step) {
					final int delta = dfs(e.to, e.pushable(max), step);
					if (FlowGraphPositiveInt.isPositive(delta)) {
						while (!deque.isEmpty())
							s.es.offerLast(deque.pollFirst());
						if (e.rev.isFull())
							e.to.es.offerLast(e.rev);
						e.push(delta);
						if (!e.isFull())
							s.es.offerLast(e);
						return delta;
					} else
						deque.offerLast(e);
				} else
					deque.offerLast(e);
			}
			return 0;
		}
	}

	public static class VCheckOfferMixed extends FordFulkersonPositiveInt {
		@Override
		protected int push(final FlowGraphPositiveInt fg, final int step) {
			return dfs(fg.getSource(), FlowGraphPositiveInt.INF, step);
		}

		private static int dfs(final FlowGraphPositiveInt.V s, final int max, final int step) {
			if (s.isSink)
				return max;
			s.step = step;
			final Deque<FlowGraphPositiveInt.E> deque = new ArrayDeque<FlowGraphPositiveInt.E>();
			while (!s.es.isEmpty()) {
				final FlowGraphPositiveInt.E e = s.es.poll();
				if (e.to.step < step) {
					final int delta = dfs(e.to, e.pushable(max), step);
					if (FlowGraphPositiveInt.isPositive(delta)) {
						while (!deque.isEmpty())
							s.es.offerLast(deque.pollFirst());
						if (e.rev.isFull())
							e.to.es.offerFirst(e.rev);
						e.push(delta);
						if (!e.isFull())
							s.es.offerLast(e);
						return delta;
					} else
						deque.offerLast(e);
				} else
					deque.offerLast(e);
			}
			return 0;
		}
	}
	public static class ECheck extends FordFulkersonPositiveInt {
		@Override
		protected int push(final FlowGraphPositiveInt fg, final int step) {
			return dfs(fg.getSource(), FlowGraphPositiveInt.INF, step);
		}

		private static int dfs(final FlowGraphPositiveInt.V s, final int max, final int step) {
			if (s.isSink)
				return max;
			for (final FlowGraphPositiveInt.E e : s.es)
				if (e.step < step && !e.isFull()) {
					e.step = step;
					final int delta = dfs(e.to, e.pushable(max), step);
					if (FlowGraphPositiveInt.isPositive(delta)) {
						e.push(delta);
						return delta;
					}
				}
			return 0;
		}
	}

}
