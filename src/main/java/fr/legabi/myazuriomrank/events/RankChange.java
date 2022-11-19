package fr.legabi.myazuriomrank.events;

import fr.legabi.myazuriomrank.AzuriomRank;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import org.bukkit.event.Listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RankChange implements Listener {

    private LuckPerms api;
    private AzuriomRank plugin;
    private Connection connection;

    public RankChange(LuckPerms api, AzuriomRank plugin, Connection connection) {
        this.api = api;
        this.plugin = plugin;
        this.connection = connection;

        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(this.plugin, UserPromoteEvent.class, this::onUserPromote);
    }

    private void onUserPromote(UserPromoteEvent event) {
        // get the user
        String user = event.getUser().getUsername();

        //get target group
        String group = event.getGroupTo().get();

        // check if group is in azuriom database
        // if yes, update the rank
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `roles` WHERE name = '" + group + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                statement.executeUpdate("UPDATE `users` SET `role_id` = '" + id + "' WHERE `name` = '" + user + "'");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
