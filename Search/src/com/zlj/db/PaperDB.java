package com.zlj.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shenji.common.action.ConnectionPool;
import com.shenji.common.exception.ConnectionPoolException;
import com.shenji.common.log.Log;

public class PaperDB {

	public String getPaperMsg(int paperid) throws ConnectionPoolException {
		Connection connection = null;
		Paper paper = null;
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (ConnectionPoolException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection
					.prepareStatement("select * from paper where paperid=?");
			statement.setInt(1, paperid);
			rs = statement.executeQuery();
			while (rs.next()) {
				paper = new Paper();
				paper.setPaperid(paperid);
				paper.setPaperid(rs.getInt("paperid"));
				paper.setTitle(rs.getString("title"));
				paper.setWriter(rs.getString("writer"));
				paper.setWrite_time(rs.getTimestamp("write_time"));
				paper.setWhere_from(rs.getString("where_from"));
			}

		} catch (Exception e) {
			Log.getLogger(this.getClass()).error(e.getMessage(), e);
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
		}

		return (paper == null ? ""
				: String.format(
						"<FONT COLOR=GREEN>文章标题:%s</FONT>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
								+ "<FONT COLOR=GREEN>作者:%s</FONT>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
								+ "<FONT COLOR=GREEN>发表时间%s</FONT>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
								+ "<FONT COLOR=GREEN>发表杂志%s</FONT><br/>",
						paper.getTitle(), paper.getWriter(),
						paper.getWrite_time(), paper.getWhere_from()));
	}

}
