package main.chequewriter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class createDB {
    public static void main(String[] args) {

        try {
            DBhelper.initTableCoordinates();
            DBhelper.initOtherSettings();
            Map<String, String> map=new HashMap<>();
            map.put("CURRENCY", "Dollar");
            map.put("CENTS", "cent");
            map.put("FONTNAME","Helvetica");
            map.put("FONTSIZE","12");
            map.put("DIRECTORY", ".");
            DBhelper.addOtherSettings(map);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
