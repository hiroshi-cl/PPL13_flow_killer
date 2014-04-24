package graph;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.Path;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Transition;
import gov.nasa.jpf.search.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtremeCaseDetector extends gov.nasa.jpf.ListenerAdapter {

	private final Map<Integer, Integer> map = new HashMap<>();

	private int c = 0;
	private int max = Integer.MIN_VALUE;
	private String maxs = "";
	private int min = Integer.MAX_VALUE;
	private String mins = "";

	private final Set<String> countedMethods;
	private final Set<String> submittingMethods;

	public ExtremeCaseDetector(final Config config, final JPF jpf) {
		countedMethods = config.getNonEmptyStringSet("ecd.counted");
		submittingMethods = config.getNonEmptyStringSet("ecd.submitting");
	}

	@Override
	public void methodExited(JVM vm) {
		final MethodInfo mi = vm.getLastMethodInfo();
		if (submittingMethods.contains(mi.getBaseName())) {
			if (max < c) {
				max = c;
				maxs = path2string(vm.getPath());
			}
			if (min > c) {
				min = c;
				mins = path2string(vm.getPath());
			}
		} else if (countedMethods.contains(mi.getBaseName()))
			c++;
	}

	private static String path2string(final Path p) {
		final List<Object> list = new ArrayList<>();
		for (final Transition t : p) {
			final Object o = t.getChoiceGenerator().getNextChoice();
			if (!(o instanceof ThreadInfo))
				list.add(o);
		}
		return list.toString();
	}

	@Override
	public void searchStarted(Search search) {
		map.put(search.getDepth(), c);
	}

	@Override
	public void stateAdvanced(Search search) {
		map.put(search.getDepth(), c);
	}

	@Override
	public void stateStored(Search search) {
		map.put(search.getDepth(), c);
	}

	@Override
	public void stateRestored(Search search) {
		c = map.get(search.getDepth());
	}

	@Override
	public void stateBacktracked(Search search) {
		c = map.get(search.getDepth());
	}

	@Override
	public void searchFinished(Search search) {
		System.out.println("max:\t" + max + "\t" + maxs);
		System.out.println("min:\t" + min + "\t" + mins);
	}
}
