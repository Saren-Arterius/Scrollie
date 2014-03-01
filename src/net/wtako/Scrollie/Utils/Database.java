package net.wtako.Scrollie.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.wtako.Scrollie.Main;
import lib.PatPeter.SQLibrary.SQLite;

public class Database {

    private SQLite conn;

    public Database() throws SQLException {
        this.conn = new SQLite(Main.log, "[Scrollie] ", Main.getInstance().getDataFolder().getAbsolutePath(),
                "scrollie", ".db");
        this.conn.open();
        check();
    }

    public boolean addScroll(String owner, String name, Integer scrollDestination, Integer warmUpTime,
            Integer coolDownTime, boolean allowCrossWorldTP, Integer timesBeUsed) throws SQLException {
        PreparedStatement stmt = conn
                .prepare("INSERT INTO `scrolls` (`owner`, `name`, `scroll_destination`, `warm_up_time`, `cool_down_time`, `allow_cross_world_tp`, `times_be_used`) VALUES (?, ?, ?, ?, ?, ?, ?)");
        stmt.setString(0, owner);
        stmt.setString(1, name);
        stmt.setInt(2, scrollDestination);
        stmt.setInt(3, warmUpTime);
        stmt.setInt(4, coolDownTime);
        stmt.setInt(6, timesBeUsed);
        if (allowCrossWorldTP) {
            stmt.setInt(5, 1);
        } else {
            stmt.setInt(5, 0);
        }
        return stmt.execute();
    }

    private boolean addConfig(String config, String value) throws SQLException {
        PreparedStatement stmt = conn.prepare("INSERT INTO `configs` (`config`, `value`) VALUES (?, ?)");
        stmt.setString(0, config);
        stmt.setString(1, value);
        return stmt.execute();
    }

    public boolean createTables() throws SQLException {
        String stmt1 = "CREATE TABLE `scrolls` (`id` INT NOT NULL, `owner` VARCHAR(20) NULL, `name` VARCHAR(45) NULL, `scroll_destination` INT NULL, `warm_up_time` INT NULL, `cool_down_time` INT NULL, `allow_cross_world_tp` INT NULL, `times_be_used` INT NULL, PRIMARY KEY (`id`))";
        String stmt2 = "CREATE TABLE `configs` (`config` VARCHAR(128) NOT NULL, `value` VARCHAR(128) NULL, PRIMARY KEY (`config`))";
        conn.query(stmt1);
        conn.query(stmt2);
        return addConfig("database_version", "1");
    }

    private boolean areTablesExist() {
        try {
            String stmt1 = "SELECT * FROM `scrolls` LIMIT 0";
            String stmt2 = "SELECT * FROM `configs` LIMIT 0";
            conn.query(stmt1);
            conn.query(stmt2);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void check() throws SQLException {
        if (!areTablesExist()) {
            Main.log.info("Creating tables...");
            createTables();
        }
    }

    public void updateDatabase() {
    }
}