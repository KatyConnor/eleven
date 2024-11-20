package hx.nine.eleven.datasources;

/**
 * @auth wml
 * @date 2024/11/18
 */
public interface Ordered {

	int HIGHEST_PRECEDENCE = -2147483648;
	int LOWEST_PRECEDENCE = 2147483647;

	int getOrder();
}
