import acm.program.GraphicsProgram;


public class Test {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		GraphicsProgram gp = new PurpleAmerica(args);
		gp.start();
		long endTime = System.currentTimeMillis();
		System.out.println("Total map creation time: " + (endTime-startTime) + "ms");
	}
}
