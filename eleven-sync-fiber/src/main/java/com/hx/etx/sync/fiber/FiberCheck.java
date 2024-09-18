package com.hx.etx.sync.fiber;

import co.paralleluniverse.fibers.Fiber;

/**
 * @author wml
 * @Discription
 * @Date 2023-07-12
 */
public class FiberCheck {

    public static boolean checkIsFiber(){
        return Fiber.currentFiber() != null;
    }
}
