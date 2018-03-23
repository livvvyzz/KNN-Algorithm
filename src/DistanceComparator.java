import java.util.Comparator;

public class DistanceComparator implements Comparator<Instance>
{
    @Override
    public int compare(Instance a, Instance b)
    {

        if (a.getDist() < b.getDist())
        {
            return -1;
        }
        if (a.getDist() > b.getDist()){
            return 1;
        }
        return 0;
    }
}
