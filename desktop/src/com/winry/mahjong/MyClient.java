package com.winry.mahjong;

import com.winry.mahjong.Message.LoginReq;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by User on 12/22/2016.
 */
public class MyClient {

	Channel channel;

	ExecutorService executor = Executors.newSingleThreadExecutor();

	public MyClient() {
		executor.execute(() -> {
			EventLoopGroup workerGroup = new NioEventLoopGroup();

			try {
				Bootstrap b = new Bootstrap();
				b.group(workerGroup); // (2)
				b.channel(NioSocketChannel.class); // (3)
				b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
				b.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();

						p.addLast(new ProtobufVarint32FrameDecoder());
						p.addLast(new ProtobufDecoder(Message.LoginResp.getDefaultInstance()));

						p.addLast(new ProtobufVarint32LengthFieldPrepender());
						p.addLast(new ProtobufEncoder());

						p.addLast(new MyClientHanlder());
					}
				});

				// Start the client.
				ChannelFuture f = b.connect("127.0.0.1", 8888).sync();
				channel = f.channel();
				// Wait until the connection is closed.
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				workerGroup.shutdownGracefully();
			}
		});
	}

	public void login(String username) {
		LoginReq loginReq = LoginReq.newBuilder().setName(username).build();
		channel.writeAndFlush(loginReq);
	}
}
