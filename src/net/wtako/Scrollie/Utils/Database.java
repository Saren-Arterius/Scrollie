package net.wtako.Scrollie.Utils;

import java.sql.*;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;

public class Database {

    public Connection conn;

    public Database() throws SQLException {
        String path = MessageFormat.format("jdbc:sqlite:{0}/{1}", Main.getInstance().getDataFolder().getAbsolutePath(),
                "scrollie.db");
        this.conn = DriverManager.getConnection(path);
        check();
    }

    private void addConfig(String config, String value) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO `configs` (`config`, `value`) VALUES (?, ?)");
        stmt.setString(1, config);
        stmt.setString(2, value);
        stmt.execute();
        return;
    }

    public void createTables() throws SQLException {
        Statement cur = conn.createStatement();
        cur.execute("CREATE TABLE `scrolls` (`rowid` INTEGER PRIMARY KEY AUTOINCREMENT, `owner` VARCHAR(20) NULL, `scroll_id` INT NULL, `name` VARCHAR(45) NULL, `scroll_destination` INT NULL, `warm_up_time` INT NULL, `cool_down_time` INT NULL, `allow_cross_world_tp` INT NULL, `times_be_used` INT NULL)");
        cur.execute("CREATE TABLE `inverted_areas` (`rowid` INTEGER PRIMARY KEY AUTOINCREMENT, `world` NOT NULL, `region` VARCHAR(128) NULL)");
        cur.execute("CREATE TABLE `configs` (`config` VARCHAR(128) PRIMARY KEY, `value` VARCHAR(128) NULL)");
        cur.close();
        addConfig("database_version", "1");
        return;
    }

    private boolean areTablesExist() {
        try {
            Statement cur = conn.createStatement();
            cur.execute("SELECT * FROM `scrolls` LIMIT 0");
            cur.execute("SELECT * FROM `inverted_areas` LIMIT 0");
            cur.execute("SELECT * FROM `configs` LIMIT 0");
            cur.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    private void check() throws SQLException {
        if (!areTablesExist()) {
            Main.log.info("Creating database files...");
            createTables();
        }
    }

    public void updateDatabase() {
    }
}