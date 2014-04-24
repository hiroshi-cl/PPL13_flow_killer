package graph;

import static java.lang.Math.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class FlowGraphPositiveInt {
	public static final int INF = Integer.MAX_VALUE;
	private final V[] vs;

	public FlowGraphPositiveInt(final int n, final Oracles o) {
		this.vs = new V[n];
		for (int i = 0; i < n; i++)
			vs[i] = new V(i, i == n - 1, o);
	}

	public final void addEdge(int from, int to, int cap) {
		new E(vs[from], vs[to], cap);
	}

	public final V getSource() {
		return vs[0];
	}

	public final void forget() {
		for (final V v : vs)
			v.es.forget();
	}

	public static boolean isPositive(final int delta) {
		return delta > 0;
	}

	public final String dump() {
		final StringWriter sw = new StringWriter();
		try (final PrintWriter pw = new PrintWriter(sw)) {
			for (final V v : vs)
				pw.println(v.id + "\t" + v.es);
			pw.flush();
		}
		return sw.toString();
	}

	public static enum Oracles {
		JPF {
			@Override
			public int oracle(final List<E> list, final V v) {
				return gov.nasa.jpf.jvm.Verify.getInt(0, list.size() - 1);
			}
		},
		JPFRandom {
			private static final int jpfrandom = 1000;
			private final Random random = new Random(1 + gov.nasa.jpf.jvm.Verify.getInt(0, jpfrandom));

			@Override
			public int oracle(final List<E> list, final V v) {
				return random.nextInt(list.size());
			}
		},
		Random {
			private final Random random = new Random(0);

			@Override
			public int oracle(final List<E> list, final V v) {
				return random.nextInt(list.size());
			}
		},
		EDevil {
			@Override
			public int oracle(final List<E> list, final V v) {
				E worst = null;
				int min = INF;
				final Map<E, Integer> cache = new HashMap<>();
				for (final E e : list) {
					final int f = isPositive(e.res) ? dfs(e, e.res, cache) : 0;
					if (f < min) {
						min = f;
						worst = e;
					}
				}
				return worst == null ? 0 : list.indexOf(worst);
			}

			private int dfs(final E ee, final int cap, final Map<E, Integer> cache) {
				final V s = ee.to;
				if (s.isSink)
					return cap;
				if (cache.containsKey(ee))
					return cache.get(ee);

				cache.put(ee, 0);
				s.es.canonize(-1);
				int min = INF;
				if (s.es.former.isEmpty()) {
					for (final E e : s.es.unordered) {
						final int f = dfs(e, e.pushable(cap), cache);
						if (isPositive(f) && f < min)
							min = f;
					}
				} else {
					final E e = s.es.peek();
					min = dfs(e, e.res, cache);
				}
				cache.put(ee, min < INF ? min : 0);
				return cache.get(ee);
			}
		},
		EAngel {
			@Override
			public int oracle(final List<E> list, final V v) {
				E best = null;
				int max = 0;
				for (final E e : list)
					if (!e.isFull()) {
						final int f = dfs(e, e.pushable(INF), new HashSet<E>());
						if (f > max || f == max && e.res > best.res) {
							max = f;
							best = e;
						}
					}
				return best == null ? 0 : list.indexOf(best);
			}

			private int dfs(final E ee, final int cap, final Set<E> used) {
				if (used.contains(ee))
					return 0;
				if (ee.to.isSink)
					return ee.pushable(cap);
				used.add(ee);
				final LazyQueue<E> que = ee.to.es;
				que.canonize(-1);
				int max = 0;
				if (que.former.isEmpty()) {
					for (final E e : que.unordered)
						if (!e.isFull()) {
							final int f = dfs(e, e.pushable(cap), used);
							if (f > max)
								max = f;
						}
				} else
					max = dfs(que.peek(), que.peek().pushable(cap), used);
				used.remove(ee);
				return max;
			}
		},
		VDevil {
			@Override
			public int oracle(final List<E> list, final V v) {
				E worst = null;
				int min = INF;
				for (final E e : list)
					if (!e.isFull()) {
						final Set<V> used = new HashSet<>();
						used.add(v);
						final int f = dfs(e.to, e.res, used);
						if (f < min) {
							min = f;
							worst = e;
						}
					}
				return worst == null ? 0 : list.indexOf(worst);
			}

			private int dfs(final V s, final int cap, final Set<V> used) {
				if (s.isSink)
					return cap;
				if (used.contains(s))
					return 0;

				used.add(s);
				s.es.canonize(-1);
				int min = INF;
				if (s.es.former.isEmpty()) {
					for (final E e : s.es.unordered)
						if (!e.isFull()) {
							final int f = dfs(e.to, e.pushable(cap), used);
							if (isPositive(f) && f < min)
								min = f;
						}
				} else {
					final E e = s.es.peek();
					min = dfs(e.to, e.pushable(cap), used);
				}
				final int ret = min < INF ? min : 0;
				return ret;
			}
		},
		NDevil {
			@Override
			public int oracle(final List<E> list, final V v) {
				E worst = null;
				int mcap = Integer.MAX_VALUE;
				for (final E e : list) {
					final int cap = max(e.res, e.rev.res);
					if (cap < mcap) {
						mcap = cap;
						worst = e;
					}
				}
				return worst == null ? 0 : list.indexOf(worst);
			}
		},
		NAngel {
			@Override
			public int oracle(final List<E> list, final V v) {
				E best = null;
				int mcap = 0;
				for (final E e : list) {
					final int cap = e.res;
					if (cap > mcap) {
						mcap = cap;
						best = e;
					}
				}
				return best == null ? 0 : list.indexOf(best);
			}
		},
		;
		public abstract int oracle(final List<E> list, final V v);
	}

	public static class V {
		private final V v = this;
		public final LazyQueue<E> es = new LazyQueue<E>() {
			@Override
			protected int determineOne() {
				return o.oracle(unordered, v);
			}
		};
		public int step = 0;
		public final int id;
		public final boolean isSink;
		public final Oracles o;

		private V(final int id, final boolean isSink, final Oracles o) {
			this.id = id;
			this.isSink = isSink;
			this.o = o;
		}

		@Override
		public final int hashCode() {
			return id;
		}

		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof V))
				return false;
			final V other = (V) obj;
			return id == other.id;
		}
	}

	public static class E {
		private static int nid = 0;
		public final E rev;
		public final V to;
		public final int cap;
		private int res;
		public int step = 0;
		private final int id = nid++;

		public E(final V from, final V to, final int cap) {
			this.to = to;
			this.res = this.cap = cap;
			from.es.add(this);
			rev = new E(this, from);
		}

		public E(final E rev, final V to) {
			this.to = to;
			this.res = this.cap = 0;
			this.rev = rev;
		}

		public final boolean isFull() {
			return res == 0;
		}

		public final boolean push(final int delta) {
			if (res < delta)
				return false;
			res -= delta;
			rev.res += delta;
			return true;
		}

		public final int pushable(final int delta) {
			return min(res, delta);
		}

		@Override
		public final int hashCode() {
			return id;
		}

		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof E))
				return false;
			final E other = (E) obj;
			return id == other.id;
		}

		@Override
		public final String toString() {
			return "=>" + to.id;
		}
	}
}
