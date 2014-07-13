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
        /*try {
            Socket socket;
            if (split.length >= 2) {
                socket = new Socket(split[0], MD5Util.tryParseInt(split[1], 25565));
            } else {
                socket = new Socket(split[0], 25565);
            }
            socket.setSoTimeout(5000);
            OutputStream os = socket.getOutputStream();
            byte[] writes = new byte[1];
            writes[0] = (byte) 254;
            os.write(writes, 0, 1);
            writes = new byte[1];
            writes[0] = (byte) 1;
            os.write(writes, 0, 1);
            InputStream is = socket.getInputStream();
            byte[] recive = new byte[512];
            int bytes = is.read(recive);
            if (recive[0] != -1) {
                System.out.println(java.text.MessageFormat.format("服务器{0}的回复无效", new Object[]{url}));
                return;
            }
            String message = new String(recive, 4, bytes - 4);
            StringBuilder remessage = new StringBuilder(30);
            for (int i = 0; i < message.length(); i += 2) {
                remessage.append(message.charAt(i));
            }
            message = remessage.toString();
            os.close();
            is.close();
            socket.close();

            char[] achar = message.toCharArray();
            message = new String(achar);
            if (message.charAt(0) == (char) 253
                    || message.charAt(0) == (char) 65533) {
                message = (char) 167 + message.substring(1);
            }
            String[] astring;
            if (message.startsWith("\u00a7") && message.length() > 1) {
                astring = message.substring(1).split("\0");
                if (MD5Util.tryParseInt(astring[0], 0) == 1) {
                    //row[3] = astring[3];
                    //row[4] = astring[2];
                    online = MD5Util.tryParseInt(astring[4], 0);
                    maxplayer = MD5Util.tryParseInt(astring[5], 0);
                    //row[5] = online + "/" + maxplayer;
                }
            } else {
                //row[3] = "";
                //row[4] = "";
                //row[5] = "";
                
                online = maxplayer = -1;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            online = maxplayer = -1;
            //Logger.getLogger(I18N.class.getName()).log(Level.SEVERE, null, ex);
            //row[6] = "无法连接";
        }*/
            Socket socket = null;
            DataInputStream datainputstream = null;
            DataOutputStream dataoutputstream = null;

            try
            {
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
                //int j;
                //int k;
                String[] astring;

                if (s.startsWith("\u00a7") && s.length() > 1)
                {
                    astring = s.substring(1).split("\u0000");

                    if (MathHelper.parseIntWithDefault(astring[0], 0) == 1)
                    {
                        //par0ServerData.serverMOTD = astring[3];
                        //par0ServerData.field_82821_f = MathHelper.parseIntWithDefault(astring[1], par0ServerData.field_82821_f);
                        //par0ServerData.gameVersion = astring[2];
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
                        //par0ServerData.gameVersion = "???";
                        //par0ServerData.serverMOTD = "" + EnumChatFormatting.DARK_GRAY + "???";
                        //par0ServerData.field_82821_f = 79;
                        populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                    }
                }
                else
                {
                    astring = s.split("\u00a7");
                    s = astring[0];
                    online = -1;
                    maxplayer = -1;

                    try
                    {
                    	online = Integer.parseInt(astring[1]);
                    	maxplayer = Integer.parseInt(astring[2]);
                    }
                    catch (Exception exception)
                    {
                        ;
                    }

                    //par0ServerData.serverMOTD = EnumChatFormatting.GRAY + s;

                    if (online >= 0 && maxplayer > 0)
                    {
                        populationInfo = EnumChatFormatting.GRAY + "" + online + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + maxplayer;
                    }
                    else
                    {
                        populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
                    }

                    //par0ServerData.gameVersion = "1.3";
                    //par0ServerData.field_82821_f = 77;
                }
            } catch (Exception e) {
				e.printStackTrace();
				
				populationInfo = "???";
			}
            finally
            {
                try
                {
                    if (datainputstream != null)
                    {
                        datainputstream.close();
                    }
                }
                catch (Throwable throwable)
                {
                    ;
                }

                try
                {
                    if (dataoutputstream != null)
                    {
                        dataoutputstream.close();
                    }
                }
                catch (Throwable throwable1)
                {
                    ;
                }

                try
                {
                    if (socket != null)
                    {
                        socket.close();
                    }
                }
                catch (Throwable throwable2)
                {
                    ;
                }
            }
    }
}