package org.jackhuang.myui.util;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.TimeZone;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet254ServerPing;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class LoadServerThread extends Thread {

	String url;

    public long online = -1, maxplayer = -1, offset;
    public String populationInfo = "";

    public LoadServerThread(String url) {
        this.url = url;
    }

    @Override
    public void run() {
    	String ip; int port;
        String[] split = url.split(":");
        ip = split[0];
        if (split.length >= 2) {
        	port = MD5Util.tryParseInt(split[1], 25565);
        } else {
        	port = 25565;
        }
            Socket socket = null;
            DataInputStream datainputstream = null;
            DataOutputStream dataoutputstream = null;

                long begin = MinecraftServer.getSystemTimeMillis();
                socket = new Socket();
                socket.setSoTimeout(3000);
                socket.setTcpNoDelay(true);
                socket.setTrafficClass(18);
                socket.connect(new InetSocketAddress(ip, port), 3000);
                datainputstream = new DataInputStream(socket.getInputStream());
                dataoutputstream = new DataOutputStream(socket.getOutputStream());
                Packet254ServerPing packet254serverping = new Packet254ServerPing(78, ip, port);
                dataoutputstream.writeByte(packet254serverping.getPacketId());
                packet254serverping.writePacketData(dataoutputstream);
                
                long end = MinecraftServer.getSystemTimeMillis();
                offset = (end - begin);

                if (datainputstream.read() != 255)
                {
                    throw new IOException("Bad message");
                }

                String s = Packet.readString(datainputstream, 256);
                char[] achar = s.toCharArray();

                for (int i = 0; i < achar.length; ++i)
                {
                    if (achar[i] != 167 && achar[i] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(achar[i]) < 0)
                    {
                        achar[i] = 63;
                    }
                }

                s = new String(achar);
                String[] astring;

                if (s.startsWith("\u00a7") && s.length() > 1)
                {
                    astring = s.substring(1).split("\u0000");

                    if (MathHelper.parseIntWithDefault(astring[0], 0) == 1)
                    {
                        online = MathHelper.parseIntWithDefault(astring[4], 0);
                        maxplayer = MathHelper.parseIntWithDefault(astring[5], 0);

                        if (online >= 0 && maxplayer >= 0)
                        {
                            populationInfo = EnumChatFormatting.GRAY + "" + online + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + maxplayer;
                        }
                        else
                        {
                            populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
                        }
                    }
                    else
                    {
                        populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                    }
                }
                else
                {
                    astring = s.split("\u00a7");
                    s = astring[0];
                    online = -1;
                    maxplayer = -1;

                    	online = Integer.parseInt(astring[1]);
                    	maxplayer = Integer.parseInt(astring[2]);

                    if (online >= 0 && maxplayer > 0)
                    {
                        populationInfo = EnumChatFormatting.GRAY + "" + online + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + maxplayer;
                    }
                    else
                    {
                        populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
                    }
                }
    }
}