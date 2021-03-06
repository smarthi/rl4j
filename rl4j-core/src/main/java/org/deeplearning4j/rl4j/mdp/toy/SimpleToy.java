package org.deeplearning4j.rl4j.mdp.toy;

import lombok.Getter;
import org.deeplearning4j.rl4j.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.logging.Logger;

/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) 7/18/16.
 *
 * A toy MDP where reward are given in every case.
 * Useful to debug
 */
public class SimpleToy implements MDP<SimpleToyState, Integer, DiscreteSpace> {


    final private int maxStep;
    //TODO 10 steps toy (always +1 reward2 actions), toylong (1000 steps), toyhard (7 actions, +1 only if actiion = (step/100+step)%7, and toyStoch (like last but reward has 0.10 odd to be somewhere else).
    @Getter
    private DiscreteSpace actionSpace = new DiscreteSpace(1);
    @Getter
    private ObservationSpace<SimpleToyState> observationSpace = new ArrayObservationSpace(new int[]{1});
    private SimpleToyState simpleToyState;

    public SimpleToy(int maxStep) {
        this.maxStep = maxStep;
    }

    public static void printTest(IDQN idqn, int maxStep) {
        INDArray input = Nd4j.create(maxStep, 1);
        for (int i = 0; i < maxStep; i++) {
            input.putRow(i, Nd4j.create(new SimpleToyState(0, i).toArray()));
        }
        INDArray output = idqn.output(input);
        Logger.getAnonymousLogger().info(output.toString());
    }

    public void close() {
    }

    @Override
    public boolean isDone() {
        return simpleToyState.getStep() == maxStep;
    }

    public SimpleToyState reset() {
        Logger.getAnonymousLogger().info("reset");
        return simpleToyState = new SimpleToyState(0, 0);
    }

    public StepReply<SimpleToyState> step(Integer a) {
        simpleToyState = new SimpleToyState(simpleToyState.getI(), simpleToyState.getStep() + 1);
        return new StepReply<SimpleToyState>(simpleToyState, 1, isDone(), new JSONObject("{}"));
    }

    public SimpleToy newInstance() {
        return new SimpleToy(maxStep);
    }

}
