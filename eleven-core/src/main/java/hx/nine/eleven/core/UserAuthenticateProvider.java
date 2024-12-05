package hx.nine.eleven.core;

/**
 * 用户token验证和生成接口
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

	/**
	 * 生成token信息
	 * @param object  生成token对象数据
	 * @param expires  token 过期时间
	 * @param <T>
	 * @return
	 */
	<T> String generateToken(T object,int expires);

	/**
	 * 生成token，根据原token解析后生成新的token
	 * @param token 原token数据
	 * @return
	 */
	String generateToken(String token);

	/**
	 * 生成token，根据原token解析后生成新的token
	 * @param token   原 token数据
	 * @param expires token 过期时间
	 * @return
	 */
	String generateToken(String token,int expires);

	/**
	 * 解析 token,返回token 所有信息
	 * @param token 有效token
	 * @return
	 */
	Object decodeToken(String token);

	/**
	 * 解析 token,返回用户项管基本信息
	 * @param token 有效token
	 * @return
	 */
	Object decodeTokenAuth(String token);
}
