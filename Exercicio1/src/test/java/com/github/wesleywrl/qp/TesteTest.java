/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import org.junit.Assert;
import org.junit.Test;

public class TesteTest {

    @Test
    public void instanciar() {
        Teste teste = new Teste("x+2;x=3;5");
    }

    @Test
    public void getExpressao() {
        Teste teste = new Teste("x+2;x=3;5");
        Assert.assertEquals("x+2", teste.getExpressao());
    }

    @Test
    public void temVariaveis() {
        Teste teste = new Teste("x+2;x=3;5");
        Assert.assertEquals(true, teste.temVariaveis());
    }

    @Test
    public void temVariaveisFalso() {
        Teste teste = new Teste("3+2;;5");
        Assert.assertEquals(false, teste.temVariaveis());
    }

    @Test
    public void getVariaveis() {
        Teste teste = new Teste("x+2;x=3;5");
        Assert.assertEquals("x=3", teste.getVariaveis());
    }

    @Test
    public void getVariaveisNome() {
        Teste teste = new Teste("x+y;x=3,y=2;5");
        Assert.assertArrayEquals(new String[]{"x", "y"}, teste.getVariaveisNome());
    }

    @Test
    public void getVariaveisValor() {
        Teste teste = new Teste("x+y;x=3,y=2;5");
        Assert.assertArrayEquals(new Float[]{3f, 2f}, teste.getVariaveisValor());
    }

    @Test
    public void getEsperado() {
        Teste teste = new Teste("x+y;x=3,y=2;5");
        Assert.assertEquals(5f, teste.getEsperado(), 0.0001f);
    }

    @Test
    public void getObtido() {
        Teste teste = new Teste("x+y;x=3,y=2;5");
        teste.calcularValor();
        teste.atualizarSucesso();
        Assert.assertEquals(5f, teste.getObtido(), 0.0001f);
    }

    @Test
    public void getSucesso() {
        Teste teste = new Teste("x+y;x=3,y=2;5");
        teste.calcularValor();
        teste.atualizarSucesso();
        Assert.assertEquals(true, teste.getSucesso());
    }

    @Test
    public void getSucessoFalso() {
        Teste teste = new Teste("x+y;x=3,y=2;6");
        teste.calcularValor();
        teste.atualizarSucesso();
        Assert.assertEquals(false, teste.getSucesso());
    }

    @Test
    public void expressaoErradaCalcular() {
        Teste teste = new Teste("2+1+2;;6");
        teste.calcularValor();
        Assert.assertEquals(null, teste.getObtido());
    }

    @Test
    public void expressaoErradaSucesso() {
        Teste teste = new Teste("2+1+2;;5");
        teste.calcularValor();
        teste.atualizarSucesso();
        Assert.assertEquals(false, teste.getSucesso());
    }

}