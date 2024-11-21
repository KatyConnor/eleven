package hx.nine.eleven.core;

/**
 * @auth wml
 * @date 2024/11/21
 */
public interface UserAuthenticateProvider {

	/**
	 *  验证token的有效性
	 *
	 * @param token  token值
	 * @return
	 */
	boolean authenticate(String token);

	/**
	 * 生成token信息
	 * @param object  生成token源数据
	 * @param <T>
	 * @return
	 */
	<T> String generateToken(T object);
}
