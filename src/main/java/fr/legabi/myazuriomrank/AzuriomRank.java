package fr.legabi.myazuriomrank;

import fr.legabi.myazuriomrank.utils.ConnectionUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class AzuriomRank extends JavaPlugin {

    Connection connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        try {
            Connection connection = ConnectionUtils.getMyConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
