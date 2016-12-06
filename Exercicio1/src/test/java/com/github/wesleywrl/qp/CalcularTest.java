/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import org.junit.Assert;
import org.junit.Test;

public class CalcularTest {

    @Test
    public void expressaoSimples() throws Exception {
        Teste teste = new Teste("7/ 2 ;; 3.5");
        Assert.assertEquals(3.5f, Calcular.resultadoExpressao(teste), 0.001f);
    }

    @Test
    public void expressaoVariavel() throws Exception {
        Teste teste = new Teste("5 * x;x=2; 10");
        Assert.assertEquals(10f, Calcular.resultadoExpressao(teste), 0.001f);
    }

    @Test(expected = Exception.class)
    public void expressaoSimplesInvalida() throws Exception {
        Teste teste = new Teste("3 + 2 + 1;; 6");
        Calcular.resultadoExpressao(teste);
    }

    @Test(expected = Exception.class)
    public void expressaoVariavelInvalida() throws Exception {
        Teste teste = new Teste("1 + x + 2;x=2; 5");
        Calcular.resultadoExpressao(teste);
    }
}