package com.gamesortir.gamification;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameFacadeTest {

    @Test
    public void bubbleSortValidationSkipsComparisonOnlySteps() {
        GameFacade facade = GameFacade.getInstance();
        facade.setAlgorithm("BUBBLE_SORT");
        facade.startSimulation(new int[]{1, 3, 2});

        assertFalse(facade.validateSwap(0, 1, new int[]{1, 3, 2}));
        assertTrue(facade.validateSwap(1, 2, new int[]{1, 3, 2}));
        assertFalse(facade.validateSwap(1, 2, new int[]{1, 2, 3}));
    }

    @Test
    public void selectionSortValidationAdvancesThroughRequiredSwaps() {
        GameFacade facade = GameFacade.getInstance();
        facade.setAlgorithm("SELECTION_SORT");
        facade.startSimulation(new int[]{3, 1, 2});

        assertFalse(facade.validateSwap(0, 2, new int[]{3, 1, 2}));
        assertTrue(facade.validateSwap(0, 1, new int[]{3, 1, 2}));
        assertTrue(facade.validateSwap(1, 2, new int[]{1, 3, 2}));
        assertFalse(facade.validateSwap(0, 1, new int[]{1, 2, 3}));
    }
}
