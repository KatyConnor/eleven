package com.hx.nine.eleven.doop.jdbc.executor;


import com.hx.nine.eleven.core.utils.HXLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库执行器
 * @author wml
 * @date 2024/3/14
 */
public class BatchExecutor extends BaseExecutor {

    // 默认一次插入10万条sql
    private final static int defaultInsertCount = 100000;

    @Override
    public int executeBatchInsert(String sql, List<Object[]> valueList) {
		Connection conn = this.getConnection();
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int size = valueList.size();
			if (size <= defaultInsertCount) {
				for (int i = 0; i < size; i++) {
					Object[] values = valueList.get(i);
					int length = values.length;
					for (int j = 1; j <= length; j++) {
						pstm.setObject(j, values[i]);
					}
				}
				// 执行批处理
				pstm.addBatch();
				pstm.executeBatch();
				conn.commit();
				return size;
			}

			int batchCount = defaultInsertCount;
			for (int i = 0; i < size; i++) {
				Object[] values = valueList.get(i);
				int length = values.length;
				for (int j = 1; j <= length; j++) {
					pstm.setObject(j, values[i]);
				}
				if (i == batchCount) {
					pstm.addBatch();
					pstm.executeBatch();
					batchCount = batchCount + defaultInsertCount;
					conn.commit();
				}
			}
			return batchCount;
		} catch (SQLException e) {
			HXLogger.build(this).error("执行数据库操作异常！", e);
		} finally {
			close(conn, pstm);
		}
		return 0;
    }

    @Override
    public int executeBatchUpdate(String sql, List<Object[]> valueList) {
		return 0;
    }
}
