package cn.edu.ustc.strategy;

import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.UptimeRec;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.data.ID;

import cn.edu.ustc.chordinit.ChordLogging;

/**
 * Derives the shard distribution strategy among all the nodes.
 *
 * @author Qiang Xu
 * @version 0.0.2
 */
public class Strategy {

    // /**
    //  * The size 1st level slicing is expected to be 8 MiB
    //  */
    // final private int expectedSizeOf1stLevel = 8;

    /**
     * M, K, N stands for the the number of the shards of the
     * 1st level slicing, (k, n) in erasure coding, respectively.
     */
    private int m, k, n;

    private int maxDup;

    private ArrayList<String> idSet = new ArrayList<String>();

    private Chord chord;

    /**
     * Records how many shards should be assigned for the nodes
     * under consideration.
     */
    private ArrayList<Integer> distrTable = new ArrayList<Integer>();

    private static double expectedAvailability = 0.99;

    /**
     * @param size
     *      Size of the file in Byte.
     */
    public Strategy(Chord chord, long size) {
    //    m = size / expectedSizeOf1stLevel;
    //    if (m == 0)
    //        m = 1;
        k = 9;
        n = 17;
        int expectedNumOfNode = n / 3;
        this.chord = chord;

        setMaxDup();

        ArrayList<Double> nodeUptime = new ArrayList<Double>();
        searchForNode(idSet, nodeUptime, expectedNumOfNode);

        Optimization opt = new Optimization(nodeUptime);
        opt.optimize(k, n);

        // multi solutions to get the availability higher
        if (opt.getAvailability() < expectedAvailability) {
            n = 23;
            opt.optimize(k, n);
        }
        if (opt.getAvailability() < expectedAvailability) {
            n = 37;
            opt.optimize(k, n);
        }
        if (opt.getAvailability() < expectedAvailability) {
            if (expectedNumOfNode == nodeUptime.size()) {
                searchForNode(idSet, nodeUptime, expectedNumOfNode/2);
                opt = new Optimization(nodeUptime);
                opt.optimize(k, n);
            }
        }

        distrTable = opt.getDistr();
    }

    // public int getParamM() {
    //     return m;
    // }

    public int getParamK() {
        return k;
    }

    public int getParamN() {
        return n;
    }

    public ArrayList<String> getIDSet () {
        System.out.println(idSet);
        return idSet;
    }

    public ArrayList<Integer> getDistrTable() {
        System.out.println(distrTable);
        return distrTable;
    }

    private void searchForNode(ArrayList<String> idSet, ArrayList<Double> nodeUptime
        , int num)
    {
        int found = 0;
        int dup = 0;
        UptimeRec nextUR;

        while (found < num) {
            try {
                nextUR = chord.getRandomNode();
                // System.out.println(nextUR.id);
                if (idSet.isEmpty() == false && idSet.contains(nextUR.id)) {
                    dup++;
                    if (dup >= maxDup)
                        break;
                } else if (nextUR.id.equals(ChordLogging.getID())) {
                    ;
                } else {
                    idSet.add(nextUR.id);
                    nodeUptime.add(nextUR.uptime);
                    found++;
                    // System.out.println(found);
                }
            } catch (ServiceException t) {
                // System.out.println("Catched something");
                // System.out.println(t.getMessage());
                // continue;
            }
            // if this host is the only node
            if (idSet.isEmpty()) {
                idSet.add(ChordLogging.getID());
                nodeUptime.add(1.0);
            }
        }
    }

    private void setMaxDup() {
        // FIXME: need elebration.
        maxDup = 3;
    }
}
