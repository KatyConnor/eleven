package hx.nine.eleven.bytebuddy.aop.testdemo;

/**
 * @auth wml
 * @date 2024/11/13
 */
public class DataProducerExd extends DataProducer{

	@Override
	public void create(String data) {
		super.create(data);
	}

	public void test(String s){
		System.out.println(s);
	}
}
