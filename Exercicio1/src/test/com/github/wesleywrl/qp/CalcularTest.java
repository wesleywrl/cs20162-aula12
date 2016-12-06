/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.wesleywrl.calcular;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wesley Ramos
 */
public class CalcularTest {

    @Test(expected = IllegalArgumentException.class)
    public void expressaoInexistente() {
        Calcular.calcularOperacao("+");
    }

    @Test
    public void expressaoValida() {
        Assert.assertEquals(7f, Calcular.calcularOperacao("2+5"), 0.001f);
    }

}