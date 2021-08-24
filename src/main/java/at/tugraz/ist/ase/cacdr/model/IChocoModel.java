/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.model;

import org.chocosolver.solver.Model;

public interface IChocoModel {
//    Model newChocoModelInstance();
    Model getModel();
}
