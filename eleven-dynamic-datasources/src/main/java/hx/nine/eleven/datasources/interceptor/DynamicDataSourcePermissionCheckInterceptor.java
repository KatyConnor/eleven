package hx.nine.eleven.datasources.interceptor;

/**
 * 数据源增删改权限验证
 * @author wml
 * 2020-07-14
 */
public class DynamicDataSourcePermissionCheckInterceptor {//implements InnerInterceptor {

//    @Override
//    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
//        String dataSource = DynamicDataSourceContextHolder.peek();
//        // 非默认主数据源需要校验读写权限，如果dataSource 为null则使用的是默认主数据源
//        if (StringUtils.isNotBlank(dataSource) && !DataSourcePermissionCheck.checkDefaultDataSource(dataSource)){
//            if (SqlCommandType.UPDATE == ms.getSqlCommandType() || SqlCommandType.DELETE == ms.getSqlCommandType() || SqlCommandType.INSERT == ms.getSqlCommandType()) {
//                //check 是否只读权限
//                if (DataSourcePermissionCheck.get(dataSource)) {
//                    throw new DataSourceNotPermissionException(StringUtils.format("当前数据源[{}]只有读取的权限",dataSource));
//                }
//            }
//
//            // 如果有读写权限，检查是否具有insert、update、delete、select权限
//            if (!DataSourcePermissionCheck.checkPermission(dataSource, ms.getSqlCommandType().name())) {
//                throw new DataSourceNotPermissionException(StringUtils.format("当前数据源[{}]没有[{}]权限",dataSource,ms.getSqlCommandType().name()));
//            }
//        }
//        return true;
//    }
}
