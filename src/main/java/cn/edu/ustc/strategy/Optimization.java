package cn.edu.ustc.strategy;

import static java.lang.Math.sqrt;
import static java.lang.Math.exp;

import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.Function;

import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Mutator;
import org.jenetics.SinglePointCrossover;
import org.jenetics.engine.Engine;

/**
 * Using the Genetic Algorithm in jenetics to find the optimal
 * availability, given the uptime of the nodes under schedule.
 *
 * @author Qiang Xu
 * @version 0.0.3
 */
public class Optimization {
    private final ArrayList<Double> nodeUptime;

    private double availability;
    private ArrayList<Integer> shardsInNode;

    public Optimization(final ArrayList<Double> nodeUptime) {
        this.nodeUptime = nodeUptime;
    }

    public void optimize(int k, int n) {

        if (nodeUptime.size() == 1) {
            this.shardsInNode = new ArrayList<Integer>();
            shardsInNode.add(n);
            this.availability = nodeUptime.get(0);
            return;
        }
        int maxShards;
        if (2*n/nodeUptime.size() > 2*k/3)
            maxShards = 2 * n / nodeUptime.size();
        else
            maxShards = 2 * k / 3;

        final FF ff = new FF(nodeUptime, k, n);
        // set up the optimizor
        final Engine<IntegerGene, Double> engine = Engine
            .builder(
                ff,
                IntegerChromosome.of(0, maxShards, nodeUptime.size()-1))
            .populationSize(500)
			.alterers(
				new Mutator<>(0.115),
				new SinglePointCrossover<>(0.16))
            .build();

        // begin to optimize and collect the results
        final Phenotype<IntegerGene, Double> best = engine.stream()
            .limit(500)
            .collect(toBestPhenotype());

        // change the format of the result
        availability = best.getFitness();
        Genotype<IntegerGene> gt = best.getGenotype();
        shardsInNode = new ArrayList<Integer>() {{ for (int a :
            ((IntegerChromosome)gt.getChromosome()).toArray())
            add(a);
        }};

        int shardsInLastNode = n;
        for (int num : shardsInNode)
            shardsInLastNode -= num;
        shardsInNode.add(shardsInLastNode);
//        System.out.println(best);
    }

    public double getAvailability() {
        return availability;
    }

    public ArrayList<Integer> getDistr() {
        return shardsInNode;
    }
}

/**
 * The wapper class for the fitness function.
 * 'cause the fitness function must be Function object or static.
 */
final class FF implements Function<Genotype<IntegerGene>, Double> {
    private final ArrayList<Double> nodeUptime;
    private final int k;
    private final int n;

    FF(ArrayList<Double> nodeUptime, int k, int n) {
        this.nodeUptime = nodeUptime;
        this.k = k;
        this.n = n;
    }

    /**
     * The fitness function, using Lyapunov's CLT as aproximation.
     */
    @Override
    public Double apply(final Genotype<IntegerGene> gt) {
        ArrayList<Integer> shardsInNode = new ArrayList<Integer>() {{ for (int a :
            ((IntegerChromosome)gt.getChromosome()).toArray())
            add(a);
        }};
        assert shardsInNode.size() == nodeUptime.size() - 1;

        int shardsInLastNode = n;
        for (int num : shardsInNode)
            shardsInLastNode -= num;
        // infeasible solution
        // the sum of shards must equal n
        if (shardsInLastNode < 0)
            return 0.0;
        shardsInNode.add(shardsInLastNode);

        // the availability obtained via CLT is not very accurate
        // when available nodes are less than 6.
        // In such situation, the availability should be calculated via
        // enumeration.
        if (nodeUptime.size() < 6) {
            double availability = 0.0;

            for (int[] selected : combinations[nodeUptime.size()-1]) {
                double probability = 1.0;
                int avail_shard = 0;
                for (int i = 0; i < nodeUptime.size(); i++) {
                    if (selected[i] == 1) {
                        probability *= nodeUptime.get(i);
                        avail_shard += shardsInNode.get(i);
                    } else {
                        probability *= 1.0 - nodeUptime.get(i);
                    }
                }
                if (avail_shard >= k)
                    availability += probability;
            }

            return availability;
        } else {
            // en & sn are parameters in CLT
            double en = 0.0;
            double sn = 0.0;
            for (int i = 0; i < nodeUptime.size(); i++) {
                en += nodeUptime.get(i) * shardsInNode.get(i);
                sn += shardsInNode.get(i) * shardsInNode.get(i)
                    * nodeUptime.get(i) * (1.0 - nodeUptime.get(i));
            }
            sn = sqrt(sn);
            return 1.0 - normp((k-en)/sn);
        }
    }

    /**
     * All of the combinations of integer 0 to size-1.
     * can only support size smaller than 6.
     */
    static final int [][][] combinations = new int [][][] {
        {{1}},

        {{1, 0}, {0, 1}, {1, 1}},

        {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {1, 1, 0}, {1, 0, 1}, {0, 1, 1}, {1, 1, 1}},

        {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}
        , {1, 0, 1, 0}, {1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 0, 1}, {0, 0, 1, 1}
        , {1, 1, 1, 0}, {1, 1, 0, 1}, {1, 0, 1, 1}, {0, 1, 1, 1}, {1, 1, 1, 1}},

        {{1, 0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 0, 1, 0}
        , {0, 0, 0, 0, 1}, {1, 1, 0, 0, 0}, {1, 0, 1, 0, 0}, {1, 0, 0, 1, 0}
        , {1, 0, 0, 0, 1}, {0, 1, 1, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 0, 0, 1}
        , {0, 0, 1, 1, 0}, {0, 0, 1, 0, 1}, {0, 0, 0, 1, 1}, {1, 1, 1, 0, 0}
        , {1, 1, 0, 1, 0}, {1, 1, 0, 0, 1}, {1, 0, 1, 1, 0}, {1, 0, 1, 0, 1}
        , {1, 0, 0, 1, 1}, {0, 1, 1, 1, 0}, {0, 1, 1, 0, 1}, {0, 1, 0, 1, 1}
        , {0, 0, 1, 1, 1}, {1, 1, 1, 1, 0}, {1, 1, 1, 0, 1}, {1, 1, 0, 1, 1}
        , {1, 0, 1, 1, 1}, {0, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}
    };

    /**
     * Returns the cumulative normal distribution function (CNDF)
     * for a standard normal: N(0,1)
     * not used now.
     */
    private static double cndf(double x)
    {
        int neg = (x < 0d) ? 1 : 0;
        if ( neg == 1)
            x *= -1d;

        double k = (1d / ( 1d + 0.2316419 * x));
        double y = (((( 1.330274429 * k - 1.821255978) * k + 1.781477937) *
                       k - 0.356563782) * k + 0.319381530) * k;
        y = 1.0 - 0.398942280401 * exp(-0.5 * x * x) * y;

        return (1d - neg) * y + neg * (1d - y);
    }

    /**
    *
    *This method calculates the normal cumulative distribution function.
    *<p>
    *It is based upon algorithm 5666 for the error function, from:<p>
    *<pre>
    *       Hart, J.F. et al, 'Computer Approximations', Wiley 1968
    *</pre>
    *<p>
    *The FORTRAN programmer was Alan Miller.  The documentation
    *in the FORTRAN code claims that the function is "accurate
    *to 1.e-15."<p>
    *Steve Verrill
    *translated the FORTRAN code (the March 30, 1986 version)
    *into Java.  This translation was performed on January 10, 2001.
    *
    *@param   z   The method returns the value of the normal
    *             cumulative distribution function at z.
    *
    *@version .5 --- January 10, 2001
    *
    */


    /*
    Here is a copy of the documentation in the FORTRAN code:
    	SUBROUTINE NORMP(Z, P, Q, PDF)
    C
    C	Normal distribution probabilities accurate to 1.e-15.
    C	Z = no. of standard deviations from the mean.
    C	P, Q = probabilities to the left & right of Z.   P + Q = 1.
    C       PDF = the probability density.
    C
    C       Based upon algorithm 5666 for the error function, from:
    C       Hart, J.F. et al, 'Computer Approximations', Wiley 1968
    C
    C       Programmer: Alan Miller
    C
    C	Latest revision - 30 March 1986
    C
    */
    private static double normp(double z) {
        double zabs;
        double p;
        double expntl,pdf;

        final double p0 = 220.2068679123761;
        final double p1 = 221.2135961699311;
        final double p2 = 112.0792914978709;
        final double p3 = 33.91286607838300;
        final double p4 = 6.373962203531650;
        final double p5 = .7003830644436881;
        final double p6 = .3526249659989109E-01;

        final double q0 = 440.4137358247522;
        final double q1 = 793.8265125199484;
        final double q2 = 637.3336333788311;
        final double q3 = 296.5642487796737;
        final double q4 = 86.78073220294608;
        final double q5 = 16.06417757920695;
        final double q6 = 1.755667163182642;
        final double q7 = .8838834764831844E-1;

        final double cutoff = 7.071;
        final double root2pi = 2.506628274631001;

        zabs = Math.abs(z);

        //  |z| > 37

        if (z > 37.0) {
            p = 1.0;
            return p;
        }
        if (z < -37.0) {
            p = 0.0;
            return p;
        }

        //  |z| <= 37.

        expntl = Math.exp(-.5*zabs*zabs);

        pdf = expntl/root2pi;

        //  |z| < cutoff = 10/sqrt(2).

        if (zabs < cutoff) {
            p = expntl*((((((p6*zabs + p5)*zabs + p4)*zabs + p3)*zabs +
            p2)*zabs + p1)*zabs + p0)/(((((((q7*zabs + q6)*zabs +
            q5)*zabs + q4)*zabs + q3)*zabs + q2)*zabs + q1)*zabs +
            q0);
        } else {
            p = pdf/(zabs + 1.0/(zabs + 2.0/(zabs + 3.0/(zabs + 4.0/
            (zabs + 0.65)))));
        }

        if (z < 0.0) {
            return p;
        } else {
            p = 1.0 - p;
            return p;
        }
    }
}
