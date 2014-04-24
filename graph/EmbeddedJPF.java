package graph;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.Config;

public class EmbeddedJPF {

	public static void runJPF(String... args) {
		final Config config = JPF.createConfig(args);
		final JPF jpf = new JPF(config);
		jpf.addListener(new ExtremeCaseDetector(config, jpf));
		jpf.run();
	}

	public static void main(String... args) {
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "JPF");
		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
				"+ecd.counted=graph.FordFulkersonInt.dummy", "JPFRandom");
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "VDevil");
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "EDevil");
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "EAngel");
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "NDevil");
//		runJPF("test.jpf", "+target=graph.Test", "+classpath=bin", "+ecd.submitting=graph.FordFulkersonInt.maxflow",
//				"+ecd.counted=graph.FordFulkersonInt.dummy", "NAngel");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "JPF");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "JPFRandom");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "VDevil");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "EDevil");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "EAngel");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "NDevil");
		// runJPF("test.jpf", "+target=graph.TestP", "+classpath=bin",
		// "+ecd.submitting=graph.FordFulkersonPositiveInt.maxflow",
		// "+ecd.counted=graph.FordFulkersonPositiveInt.dummy", "NAngel");
		// runJPF("Rand");
	}
}
