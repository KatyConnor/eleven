package hx.nine.eleven.core.auth;

/**
 * @auth wml
 * @date 2024/12/5
 */
public interface UserAuthResponseBody {

	/**
	 * 将token放入body中
	 * @param token
	 * @param data
	 * @param <D>
	 */
	<D> void addAuthorizedTokenToBody(String token,D data);

}
