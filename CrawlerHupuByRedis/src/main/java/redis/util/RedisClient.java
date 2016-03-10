package redis.util;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

/**
 * @author houweitao
 * @date 2016年1月15日 上午12:24:55
 */

public class RedisClient {
	private Jedis jedis;// 非切片额客户端连接
	private JedisPool jedisPool;// 非切片连接池
	private ShardedJedis shardedJedis;// 切片额客户端连接
	private ShardedJedisPool shardedJedisPool;// 切片连接池

	private static long START_TIME;
	private static int TOTAL_COUNT = 100;
	private static int END_COUNT;
	private static final int COUNT = 10000;

	public RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}

	/**
	 * 初始化非切片池(非分布式)
	 */
	private void initialPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(128);
		config.setMinIdle(10);
		config.setMaxIdle(128);
		config.setMaxWaitMillis(30000l);
		config.setTestOnBorrow(false);

		jedisPool = new JedisPool(config, "localhost", 6379, 0);
	}

	/**
	 * 初始化切片池(分布池)
	 */
	private void initialShardedPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(128);
		config.setMinIdle(10);
		config.setMaxIdle(128);
		config.setMaxWaitMillis(30000l);
		config.setTestOnBorrow(false);
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("localhost", 6379, 0));

		// 构造池
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	public void show() {
		HashOperate();

	}

	private void HashOperate() {
		System.out.println("======================hash==========================");
		// 清空数据
		System.out.println(jedis.flushDB());
		System.out.println("=============增=============");
		Thread[] threads = new Thread[TOTAL_COUNT];
		for (int i = 0; i < TOTAL_COUNT; i++) {
			threads[i] = new TTestThread(i * COUNT, COUNT);
		}
		START_TIME = System.currentTimeMillis();
		for (int i = 0; i < TOTAL_COUNT; ++i) {
			threads[i].start();
		}
	}

	private class TTestThread extends Thread {
		private int mOffset;
		private int mCount;

		private Transaction tx;

		public TTestThread(int offset, int count) {
			mOffset = offset;
			mCount = count;
		}

		@Override
		public void run() {
			super.run();
			ShardedJedis shardedJedis = shardedJedisPool.getResource();
			try {
				for (int i = mOffset; i < mCount + mOffset; ++i) {
					shardedJedis.hset("hashs", "key" + i, String.valueOf(i));
				}

			} finally {
				shardedJedisPool.returnResource(shardedJedis);
			}
			synchronized (TTestThread.class) {
				++END_COUNT;
				if (END_COUNT == TOTAL_COUNT) {
					System.out.println("time is " + (System.currentTimeMillis() - START_TIME));
				}
			}
		}
	}
}
