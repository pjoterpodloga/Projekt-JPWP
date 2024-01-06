package src.Equations;

import java.util.HashMap;
import java.util.Map;

public enum EquType {
    NONE(0),
    POLY1(1),
    POLY2(2),
    POLY3(3),
    POLY4(4),
    POLY5(-1),
    POLY6(-1),
    SIN(5),
    TAN(-1),
    EXP(6),
    LOG(-1);

    private int value;
    private static Map map = new HashMap<>();
    // Mapping types and values
    EquType(int value)
    {
        this.value = value;
    }
    static
    {
        for(EquType equType : EquType.values())
        {
            map.put(equType.value, equType);
        }
    }
    public static EquType valueOf(int equ)
    {
        return (EquType) map.get(equ);
    }
    public int getValue()
    {
        return value;
    }
}
