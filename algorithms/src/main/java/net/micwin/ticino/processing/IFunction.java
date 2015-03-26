package net.micwin.ticino.processing;

/**
 * A function that computes a result basing on an input.
 * 
 * @author micwin
 *
 */
public interface IFunction<InputType, OutputType> {

	/**
	 * Computes a result on basis of given value.
	 * 
	 * @param pInput
	 * @return
	 */
	public OutputType compute(InputType pInput);

}
