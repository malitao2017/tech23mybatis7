package me.test;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
	
	public static SqlSessionFactory getSqlSessionFactory() {
		InputStream is = MybatisUtil.class.getClassLoader().getResourceAsStream("conf.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		return sessionFactory;
	}

	public static SqlSession getSqlSession(){
		// 创建能执行映射文件中的sql的sqlSession
		SqlSession session = getSqlSessionFactory().openSession();
		return session;
	}
	/**
	 * 获取SqlSession 
	 * @param isAutoCommit
	 * 			ture 表示创建的SqlSession对象在执行完sql之后会自动提交事务
	 * 			false表示创建的SqlSession对象在执行完sql之后不会自动提交事务，这时需要我们手动调用sqlSession.commit()提交
	 * @return SqlSession
	 */
	public static SqlSession getSqlSession(boolean isAutoCommit){
		return getSqlSessionFactory().openSession(isAutoCommit);
	}
	
}
