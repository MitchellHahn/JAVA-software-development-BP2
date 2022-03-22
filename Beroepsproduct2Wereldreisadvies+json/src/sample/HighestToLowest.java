package sample;

import java.util.Comparator;

//Vergelijk landen op basis van threat van hoog naar laag
public class HighestToLowest implements Comparator<Land>
{
    @Override
    public int compare(Land u1, Land u2) {
        return u2.threat.compareTo(u1.threat);
    }
}