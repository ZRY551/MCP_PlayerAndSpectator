package cn.zry551.PlayerAndSpectator.Events;

import cn.zry551.PlayerAndSpectator.PlayerAndSpectator;
import cn.zry551.PlayerAndSpectator.StringTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayerChat implements Listener {
    public List<String> PlayerList = new ArrayList<String>();
    public boolean IsUUID = false;
    public boolean DebugMode = false;
    public PlayerAndSpectator Self;
    public Logger Log;
    public FileConfiguration Cfg;

    public PlayerChat(List<String> PlayerList2,boolean ListIsUUID,PlayerAndSpectator PluginSelf, boolean isDebugMode){
        Self = PluginSelf;
        DebugMode = isDebugMode;
        PlayerList = PlayerList2;
        IsUUID = ListIsUUID;
        Log = Self.getLogger();
        Cfg = Self.Cfg;







        Log.info("Load Chat Finish !");
    }

    public boolean CheckPlayerInList(Player p){
        if(IsUUID){
            if(!PlayerList.contains(p.getUniqueId().toString())) {
                return false;
            }else{
                return true;
            }
        }else{
            if(!PlayerList.contains(p.getName())){
                return false;
            }else{
                return true;
            }
        }
    }

    @EventHandler
    public void PlayerChatEvent(PlayerChatEvent e){
        //if(Cfg.getBoolean("PlayerJoinMSG.Enable",true)){//}
        String Format = Cfg.getString("Chat.Format","&7[&d@USERNAME@&r&7](&a@PLAYER_TYPE@&r&7)>&r@MESSAGE@");
        Format = StringTools.ReplaceString(Format,"USERNAME","%1\\$s");
        Format = StringTools.ReplaceString(Format,"MESSAGE","%2\\$s");
        if (CheckPlayerInList(e.getPlayer())) {
            Format = StringTools.ReplaceString(Format,"PLAYER_TYPE",Cfg.getString("Chat.PlayerTypes.Player","Player"));
        }else{
            Format = StringTools.ReplaceString(Format,"PLAYER_TYPE",Cfg.getString("Chat.PlayerTypes.Spectator","Spectator"));
        }
        Format = StringTools.HandleColorString(Format);
        if(Cfg.getBoolean("Chat.AllowColorChat",true)){
            e.setMessage(StringTools.HandleColorString(e.getMessage()));
        }
        e.setFormat(Format);
    }



    public void Debug(String MSG){
        if(DebugMode){
            Log.info(MSG);
        }
    }
}
