package src;

import src.Equations.EquType;
import src.Utilities.Vector3D;

public class Level
{
    public EquType equType;     // Equation type in level
    public Vector3D ball;       // Ball x position, y position and radius
    public Vector3D target;     // Target x position, y position and radius
    public int ID;              // ID of level
}
