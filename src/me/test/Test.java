package me.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import me.domain.User;

public class Test {
	
	public static void main(String[] args){
		testGetUser();
		//二级缓存
		testGetUser2();
	}
	
	/**
	 * 配置文件中没有<cache/>
	 */
	public static void testGetUser(){
		String statement = "me.mapping.userMapper.getUser";
		SqlSession sqlSession = MybatisUtil.getSqlSession(true);
		User user = sqlSession.selectOne(statement, 1);
		//System.out.println(user);		
		
		/**
		 * 一级缓存默认开启
		 */
		user = sqlSession.selectOne(statement,1);
		System.out.println(user);
		sqlSession.close();
		
		/**
		 * 1.必须是同一个session，若已经close过了就不能用了 
		 */
		sqlSession = MybatisUtil.getSqlSession(true);
		user = sqlSession.selectOne(statement,1);
		System.out.println(user);
		
		/**
		 * 2.查询条件是一致的
		 */
		user = sqlSession.selectOne(statement,2);
		System.out.println(user);
		
		/**
		 * 3.没有执行过session.clearCache清理缓存
		 */
//		sqlSession.clearCache();
		user =sqlSession.selectOne(statement,2);
		System.out.println(user);
		
		/**
		 * 4.没有执行过增删改的操作（这些操作会清除缓存）
		 * getSqlSession(true) 若没有写true则不能提交更新
		 */
		int n = sqlSession.update("me.mapping.userMapper.updateUser",new User(2,"孤傲苍狼",23));
		System.out.println("影响条数： "+n +" 输出："+(User)sqlSession.selectOne(statement,2));
	}
	
	/**
	 * 开启二级缓存，在userMapper.xml文件中添加如下配置
	 * <cache/>
	 * 
	 * 注：
	 * 1.使用两个不同的session去执行相同查询条件的查询，第二次查询时不会再发送sql语句，而是直接从缓存中取出数据
	 * 2.使用二级缓存的时候，User类必须实现一个Serializable接口
	 * 3.第一个session执行完之后，一定要提交事务之后二级缓存才能起效果
	 */
	public static void testGetUser2(){
		String statement = "me.mapping.userMapper.getUser";
		SqlSessionFactory fac = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession1 = fac.openSession();
		SqlSession sqlSession2 = fac.openSession();
		User user1 = sqlSession1.selectOne(statement,1);System.out.println(user1);
		sqlSession1.commit();//暂时需要，亦可openSession(true)
		User user2 = sqlSession2.selectOne(statement,1);System.out.println(user2);
//		System.out.println("两个session中取到的是否是一样的："+ (user1.equals(user2)));//暂时不准确，因为完全有可能是取到了一个缓存对象但是定义的两个对象是不一样的
		
		
	}
}
