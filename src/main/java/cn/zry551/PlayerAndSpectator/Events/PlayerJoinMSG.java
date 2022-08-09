package cn.zry551.PlayerAndSpectator.Events;

import cn.zry551.PlayerAndSpectator.PlayerAndSpectator;
import cn.zry551.PlayerAndSpectator.StringTools;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.logging.Logger;

public class PlayerJoinMSG implements Listener {
    public boolean DebugMode = false;
    public PlayerAndSpectator Self;
    public Logger Log;
    public FileConfiguration Cfg;

    public PlayerJoinMSG(PlayerAndSpectator PluginSelf, boolean isDebugMode){
        Self = PluginSelf;
        DebugMode = isDebugMode;
        Log = Self.getLogger();
        Cfg = Self.Cfg;







        Log.info("Load Join&Quit Messages Finish !");
    }

    @EventHandler
    public void PlayerJoinServer(PlayerJoinEvent e){
        //if(Cfg.getBoolean("PlayerJoinMSG.Enable",true)){
            e.setJoinMessage(StringTools.ReplaceStringWithColor(
                    Cfg.getString("PlayerJoinMSG.Messages.Join","&f[&6+&r&f]&r&d @USERNAME@ &r&e")
                    ,"USERNAME",e.getPlayer().getName()));
        //}
    }

    @EventHandler
    public void PlayerQuitServer(PlayerQuitEvent e){
        //if(Cfg.getBoolean("PlayerJoinMSG.Enable",true)){
            e.setQuitMessage(StringTools.ReplaceStringWithColor(
                    Cfg.getString("PlayerJoinMSG.Messages.Quit","&f[&b-&r&f]&r&d @USERNAME@ &r&b")
                    ,"USERNAME",e.getPlayer().getName()));
        //}

    }



    public void Debug(String MSG){
        if(DebugMode){
            Log.info(MSG);
        }
    }






}
