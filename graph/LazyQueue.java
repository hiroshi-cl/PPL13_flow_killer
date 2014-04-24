package graph;

import gov.nasa.jpf.jvm.Verify;

import java.util.*;

public abstract class LazyQueue<T> implements Iterable<T> {
	protected final Deque<T> former = new ArrayDeque<>();
	protected final Deque<T> latter = new ArrayDeque<>();
	protected final List<T> unordered = new ArrayList<>();

	public final void add(final T v) {
		unordered.add(v);
	}

	public final void offerFirst(final T v) {
		former.offerFirst(v);
	}

	public final void offerLast(final T v) {
		if (unordered.isEmpty())
			former.offerLast(v);
		else
			latter.offerLast(v);
	}

	public final T peek() {
		canonize(0);
		return former.peekFirst();
	}

	public final T get(final int idx) {
		canonize(idx);
		return new ArrayList<>(former).get(idx);
	}

	public final T poll() {
		canonize(0);
		return former.pollFirst();
	}

	public final void remove(final T v) {
		former.remove(v);
		unordered.remove(v);
		latter.remove(v);
	}

	public void canonize(int i) {
		if (unordered.size() == 1)
			former.offerLast(unordered.remove(0));
		if (unordered.isEmpty())
			while (!latter.isEmpty())
				former.offerLast(latter.pollFirst());
		while (former.size() <= i) {
			if (unordered.isEmpty())
				throw new NoSuchElementException();
			else
				former.offerLast(unordered.remove(determineOne()));
		}
	}

	protected abstract int determineOne();

	public final boolean isEmpty() {
		return former.isEmpty() && unordered.isEmpty() && latter.isEmpty();
	}

	public final int size() {
		return former.size() + unordered.size() + latter.size();
	}

	public final void forget() {
		unordered.addAll(former);
		unordered.addAll(latter);
		former.clear();
		latter.clear();
	}

	@Override
	public final String toString() {
		return former.toString() + unordered.toString() + latter.toString();
	}

	@Override
	public final Iterator<T> iterator() {
		return new Iterator<T>() {
			int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < size();
			}

			@Override
			public T next() {
				return get(idx++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static class JPF<T> extends LazyQueue<T> {
		@Override
		protected int determineOne() {
			return Verify.getInt(0, unordered.size() - 1);
		}
	}

	public static class JPFReplay<T> extends LazyQueue<T> {
		private final Queue<Integer> ii;

		public JPFReplay(Queue<Integer> ii) {
			this.ii = ii;
		}

		public JPFReplay(int[] is) {
			this.ii = Collections.asLifoQueue(new ArrayDeque<Integer>());
			for (final int i : is)
				ii.offer(i);
		}

		@Override
		protected int determineOne() {
			return ii.poll();
		}
	}
}
