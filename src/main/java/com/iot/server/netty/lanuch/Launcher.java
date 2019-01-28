package com.iot.server.netty.lanuch;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 加载类
 * @author feiyang.duan
 * @date 2019/1/28
 */
@Slf4j
public class Launcher {

	/**
	 * 启动标识
	 */
	private volatile AtomicBoolean start = new AtomicBoolean(false);

	private int port = 8080;
	
	private ServerBootstrap serverBootstrap;
	
	private EventLoopGroup parentGroup;
	
	private EventLoopGroup childGroup;
	
	private Map<String, Object> options;
	
	private Map<String, Object> childOptions;
	
	private List<ChannelHandler> handlers;
	
	private List<ChannelHandler> childHandlers;
	
	private void init() throws InterruptedException {
		if(!start.compareAndSet(false, true)){
			log.info("服务器已经在启动中,请勿重复启动!");
			return;
		}

		validate();
		initChildOptions();
		initOptions();
		serverBootstrap.group(parentGroup, childGroup);
		initHandlers();
		serverBootstrap.channel(NioServerSocketChannel.class);
		initChildHandlers();
		ChannelFuture future = serverBootstrap.bind(port).sync();
		log.info("【服务启动状态】：{}，【端口】：{}", future.isSuccess(), port);
		if (future.isSuccess()) {
			future.channel().closeFuture().sync();
		}
	}
	
	private void validate() {
		if(serverBootstrap == null) {
			throw new IllegalArgumentException("must be set a ServerBootstrap");
		}
		if(parentGroup == null) {
			throw new IllegalArgumentException("must be set a parentGroup");
		}
		if(childGroup == null) {
			throw new IllegalArgumentException("must be set a childGroup");
		}
		if(port <= 0 || port > 65536) {
			throw new IllegalArgumentException("port is illegal");
		}
	}
	
	private void initChildHandlers() {
		if(childHandlers != null && childHandlers.size() > 0) {
			for(ChannelHandler childHandler : childHandlers) {
				serverBootstrap.childHandler(childHandler);
			}
		}else {
			throw new IllegalArgumentException("must add one childHandler!");
		}
	}
	
	private void initHandlers() {
		if(handlers != null && handlers.size() > 0) {
			for(ChannelHandler handler : handlers) {
				serverBootstrap.handler(handler);
			}
		}
	}
	
	/*设置childOption项*/
	private void initChildOptions() {
		if(childOptions != null && childOptions.size() > 0) {
			Set<Entry<String,Object>> entrySet = childOptions.entrySet();
			for(Entry<String, Object> entry : entrySet) {
				serverBootstrap.childOption(ChannelOption.valueOf(entry.getKey()), entry.getValue());
			}
		}
	}
	
	/*设置options项*/
	private void initOptions() {
		if(options != null && options.size() > 0) {
			Set<Entry<String,Object>> entrySet = options.entrySet();
			for(Entry<String,Object> entry : entrySet) {
				serverBootstrap.option(ChannelOption.valueOf(entry.getKey()), entry.getValue());
			}
		}
	}
	
	/**
	 * 启动方法
	 */
	public void launch() {
		try {
			init();
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}


	public void close() {
		parentGroup.shutdownGracefully();
		childGroup.shutdownGracefully();
	}
	
	
	public int getPort() {
		return port;
	}
	

	public void setPort(int port) {
		this.port = port;
	}

	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	public EventLoopGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(EventLoopGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public EventLoopGroup getChildGroup() {
		return childGroup;
	}

	public void setChildGroup(EventLoopGroup childGroup) {
		this.childGroup = childGroup;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public Map<String, Object> getChildOptions() {
		return childOptions;
	}

	public void setChildOptions(Map<String, Object> childOptions) {
		this.childOptions = childOptions;
	}

	public List<ChannelHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<ChannelHandler> handlers) {
		this.handlers = handlers;
	}

	public List<ChannelHandler> getChildHandlers() {
		return childHandlers;
	}

	public void setChildHandlers(List<ChannelHandler> childHandlers) {
		this.childHandlers = childHandlers;
	}

	
	
}
