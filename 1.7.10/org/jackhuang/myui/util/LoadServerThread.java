/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui.util;

public class LoadServerThread {
	private static final Splitter field_147230_a = Splitter.on('\000').limit(6);

	private final List<NetworkManager> netmgrs = Collections
			.synchronizedList(new ArrayList<NetworkManager>());

	String url;

	public long online = -1, maxplayer = -1, offset;
	private long begin, end;
	public boolean isLoading = false;

	public LoadServerThread(String url) {
		this.url = url;
	}

	public void func_147224_a(final String url) {
		try {
			isLoading = true;

			final ServerAddress address = ServerAddress.func_78860_a(url);
			final NetworkManager mgr = NetworkManager.provideLanClient(
					InetAddress.getByName(address.getIP()), address.getPort());
			netmgrs.add(mgr);
			mgr.setNetHandler(new INetHandlerStatusClient() {
				
				private boolean connected = false;

				@Override
				public void onNetworkTick() {
				}

				@Override
				public void onDisconnect(IChatComponent arg0) {
					isLoading = false;
					
					if(!connected) {
						FMLLog.severe("Can't ping %s:%d", address.getIP(), address.getPort());
						func_147225_b(url);
					}
				}

				@Override
				public void onConnectionStateTransition(
						EnumConnectionState arg0, EnumConnectionState arg1) {
				}

				@Override
				public void handleServerInfo(S00PacketServerInfo arg0) {
					connected = true;
					
					ServerStatusResponse response = arg0.func_149294_c();
					if (response.func_151318_b() != null) {
						online = response.func_151318_b().func_151333_b();
						maxplayer = response.func_151318_b().func_151332_a();
					}

					mgr.scheduleOutboundPacket(new C01PacketPing(Minecraft
							.getSystemTime()));

					isLoading = false;
				}

				@Override
				public void handlePong(S01PacketPong arg0) {
					long i = arg0.func_149292_c();
					long j = Minecraft.getSystemTime();
					offset = j - i;
					mgr.closeChannel(new ChatComponentText("Finished"));
				}
			});
			mgr.scheduleOutboundPacket(new C00Handshake(4, address.getIP(),
					address.getPort(), EnumConnectionState.STATUS));
			mgr.scheduleOutboundPacket(new C00PacketServerQuery());
		} catch (Throwable t) {
			isLoading = false;
			maxplayer = online = -1;
			t.printStackTrace();
		}
	}

	private void func_147225_b(String p_147225_1_) {
		begin = Minecraft.getSystemTime();
		
		final ServerAddress serveraddress = ServerAddress
				.func_78860_a(p_147225_1_);
		new Bootstrap()
				.group(NetworkManager.eventLoops)
				.handler(new ChannelInitializer() {
					private static final String __OBFID = "CL_00000894";

					protected void initChannel(Channel p_initChannel_1_) {
						try {
							p_initChannel_1_.config().setOption(
									ChannelOption.IP_TOS, Integer.valueOf(24));
						} catch (ChannelException channelexception1) {
						}

						try {
							p_initChannel_1_.config().setOption(
									ChannelOption.TCP_NODELAY,
									Boolean.valueOf(false));
						} catch (ChannelException channelexception) {
						}

						p_initChannel_1_
								.pipeline()
								.addLast(
										new ChannelHandler[] { new SimpleChannelInboundHandler() {
											private static final String __OBFID = "CL_00000895";

											public void channelActive(
													ChannelHandlerContext p_channelActive_1_)
													throws Exception {
												super.channelActive(p_channelActive_1_);
												ByteBuf bytebuf = Unpooled
														.buffer();
												bytebuf.writeByte(254);
												bytebuf.writeByte(1);
												bytebuf.writeByte(250);
												char[] achar = "MC|PingHost"
														.toCharArray();
												bytebuf.writeShort(achar.length);
												char[] achar1 = achar;
												int i = achar.length;

												for (int j = 0; j < i; j++) {
													char c0 = achar1[j];
													bytebuf.writeChar(c0);
												}

												bytebuf.writeShort(7 + 2 * serveraddress
														.getIP().length());
												bytebuf.writeByte(127);
												achar = serveraddress.getIP()
														.toCharArray();
												bytebuf.writeShort(achar.length);
												achar1 = achar;
												i = achar.length;

												for (int j = 0; j < i; j++) {
													char c0 = achar1[j];
													bytebuf.writeChar(c0);
												}

												bytebuf.writeInt(serveraddress
														.getPort());
												p_channelActive_1_
														.channel()
														.writeAndFlush(bytebuf)
														.addListener(
																ChannelFutureListener.CLOSE_ON_FAILURE);
											}

											protected void channelRead0(
													ChannelHandlerContext p_147219_1_,
													ByteBuf p_147219_2_) {
												short short1 = p_147219_2_
														.readUnsignedByte();

												if (short1 == 255) {
													String s = new String(
															p_147219_2_
																	.readBytes(
																			p_147219_2_
																					.readShort() * 2)
																	.array(),
															Charsets.UTF_16BE);
													String[] astring = (String[]) (String[]) Iterables
															.toArray(
																	field_147230_a
																			.split(s),
																	String.class);

													if ("§1".equals(astring[0])) {
														int i = MathHelper
																.parseIntWithDefault(
																		astring[1],
																		0);
														String s1 = astring[2];
														String s2 = astring[3];
														online = MathHelper
																.parseIntWithDefault(
																		astring[4],
																		-1);
														maxplayer = MathHelper
																.parseIntWithDefault(
																		astring[5],
																		-1);
													}
												}

												p_147219_1_.close();
												end = Minecraft.getSystemTime();
												offset = end - begin;
											}

											public void exceptionCaught(
													ChannelHandlerContext p_exceptionCaught_1_,
													Throwable p_exceptionCaught_2_) {
												p_exceptionCaught_1_.close();
											}

											protected void channelRead0(
													ChannelHandlerContext p_channelRead0_1_,
													Object p_channelRead0_2_) {
												channelRead0(
														p_channelRead0_1_,
														(ByteBuf) p_channelRead0_2_);
											}

										} });
					}

				}).channel(NioSocketChannel.class)
				.connect(serveraddress.getIP(), serveraddress.getPort());
	}

	public void processReceivedMessages() {
		synchronized (netmgrs) {
			for (Iterator<NetworkManager> iterator = netmgrs.iterator(); iterator
					.hasNext();) {
				NetworkManager mgr = iterator.next();
				if (mgr.isChannelOpen()) {
					mgr.processReceivedPackets();
				} else {
					iterator.remove();
					if (mgr.getExitMessage() != null)
						mgr.getNetHandler().onDisconnect(mgr.getExitMessage());
				}
			}
		}
	}

	public void cancel() {
		synchronized (netmgrs) {
			for (Iterator<NetworkManager> iterator = netmgrs.iterator(); iterator
					.hasNext();) {
				NetworkManager mgr = iterator.next();
				if (mgr.isChannelOpen()) {
					iterator.remove();
					mgr.closeChannel(new ChatComponentText("Cancelled"));
				}
			}
			isLoading = false;
		}
	}
}