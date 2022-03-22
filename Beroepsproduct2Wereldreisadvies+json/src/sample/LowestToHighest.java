package sample;
import java.util.Comparator;

//Vergelijk landen op basis van threat van laag naar hoog
public class LowestToHighest implements Comparator<Land>
{
    @Override
    public int compare(Land u1, Land u2) {
        return u1.threat.compareTo(u2.threat);
    }
}

