package hx.nine.eleven.commons.utils;

//import javax.persistence.Column;

public class ObjectCompareUtil<T> {
	
	
	/**
	 * 
	 * @param oneObject 旧的实例
	 * @param anotherObject 新的实例
	 * @param excludeFieldArr 排除字段
	 * @param cls 实例类名
	 * 新的实例字段为空的跳过
	 * @return 
	 */
//	public  List<CompareResult> ObjectCompare(T oneObject,T anotherObject, Class<T> cls,String[] excludeFieldArr){
//
//		List<CompareResult> resultList = new ArrayList<CompareResult>();
//		if(oneObject == null) {
//			throw new RuntimeException("实例对象为空");
//		}
//		if(anotherObject == null ) {
//			throw new RuntimeException("实例对象为空");
//		}
//		if(!oneObject.getClass().equals(anotherObject.getClass())) {
//			throw new RuntimeException("两实例对象不为同一类型");
//		}
//		try {
//		Field[] fields =   cls.getDeclaredFields();
//		for (Field field : fields) {
//			CompareResult compareResult = new CompareResult();
//			PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
//			Method getMethod = pd.getReadMethod();
//			Object o1 = getMethod.invoke(oneObject);
//			Object o2 = getMethod.invoke(anotherObject);
//			if (o2 == null) {
//				continue;
//			}
//			String s1 = o1 == null ? "" : o1.toString();
//			String s2 = o2 == null ? "" : o2.toString();
//			if(excludeFieldArr != null && excludeFieldArr.length >0) {
//				if(Arrays.asList(excludeFieldArr).contains(field.getName())) {
//					continue;
//				}
//			}
//
//			if (!s1.equals(s2)) {
//				String commont = "";
//				Annotation annotation = field.getAnnotation(Column.class);
//				if (annotation != null) {
//					// 强制转化为相应的注释
//					Column element = (Column) annotation;
//					commont = element.comment();
//				}
//				compareResult.setBeanField(field.getName() );
//				compareResult.setOrginValue(s1);
//				compareResult.setValue(s2);
//				compareResult.setDesc(commont);
//				resultList.add(compareResult);
//			}
//		}
//		} catch (Exception e) {
//			throw new RuntimeException("实例对象比较异常");
//		}
//		return resultList;
//
//	}
	
	
//	public static void main(String[] args) {
//		SysDataPms test = new SysDataPms();
//		test.setCreateOrgNo("1");
//		test.setCreateUserDeleteFlag("2");
//		test.setCreateUserNo("3");
//		test.setUpdateOrgNo("1213");
//		test.setDeleteFlag("111");
//		
//		SysDataPms test1 = new SysDataPms();
//		test1.setCreateOrgNo("1");
//		test1.setCreateUserDeleteFlag("4");
//		test1.setCreateUserNo("3");
//		test1.setUpdateOrgNo("12354675432");
//		ObjectCompareUtil abj = new ObjectCompareUtil();
//	//	List<CompareResult> resultList =abj.ObjectCompare(test, test1, CompareResultTest.class, null);
//		String[] sss = {"createUserNo"};
//		List<CompareResult> resultList =abj.ObjectCompare(test, test1, SysDataPms.class, sss);
//		System.out.println(resultList);
//		for (CompareResult compareResult : resultList) {
//			System.out.println("asd"+compareResult.getDesc());
//		}
//		
//	}
	
	
	

}
