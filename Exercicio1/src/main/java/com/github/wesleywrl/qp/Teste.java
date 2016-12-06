/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import java.util.Objects;

/**
 * Armazena as informações de um teste, além de dividir e tratar seus
 * atributos com base na expressão de sua linha.
 *
 * @author wesleywrl
 */
public class Teste {

    /**
     * Expressão do teste (primeiro bloco da linha de teste).
     */
    private final String expressao;

    /**
     * Variáveis do teste (segundo bloco da linha de teste).
     */
    private final String variaveis;

    /**
     * Nomes das variáveis definidas no bloco de variáveis.
     */
    private final String[] variaveisNome;

    /**
     * Valores das variáveis definidas no bloco de variáveis. Segue a mesma
     * ordem de variaveisNome.
     */
    private final Float[] variaveisValor;

    /**
     * Valor esperado para resultado deste teste.
     */
    private final Float esperado;

    /**
     * Valor obtido pelo Parser para a expressão e variáveis deste teste.
     */
    private Float obtido;

    /**
     * Verdadeiro se este teste teve sucesso, ou seja, o valor obtido é igual ao
     * valor esperado.
     */
    private boolean sucesso;

    /**
     * Constroi um teste e já obtém seus atributos com base na linha completa.
     *
     * @param completo Linha completa do teste, no formato
     * "expressao;variaveis;esperado". Por exemplo: "x+2 ; x=2; 4".
     */
    public Teste(final String completo) {
        String teste = completo.replace(" ", "");

        String[] partes;
        partes = teste.split(";");

        expressao = partes[0];

        variaveis = partes[1];
        if (variaveis.isEmpty()) {
            variaveisNome = null;
            variaveisValor = null;
        } else {
            String[] variaveisSeparadas = variaveis.split(",");
            variaveisNome = new String[variaveisSeparadas.length];
            variaveisValor = new Float[variaveisSeparadas.length];
            int i = 0;
            for (String variavel : variaveisSeparadas) {
                String[] partesDaVariavel = variavel.split("=");
                variaveisNome[i] = partesDaVariavel[0];
                variaveisValor[i] = Float.parseFloat(partesDaVariavel[1]);
                i++;
            }
        }

        esperado = Float.parseFloat(partes[2]);
        obtido = null;
    }

    /**
     * Obtém o valor da expressão deste teste.
     *
     * @return Expressão deste teste.
     */
    public final String getExpressao() {
        return expressao;
    }

    /**
     * Obtém se este teste tem variáveis definidas.
     *
     * @return Verdadeiro se o bloco de variáveis não for vazio.
     */
    public final boolean temVariaveis() {
        return !(variaveis.isEmpty());
    }

    /**
     * Obtém o valor do bloco de variáveis deste teste.
     *
     * @return Bloco de variáveis deste teste. Pode ser vazio.
     */
    public final String getVariaveis() {
        return variaveis;
    }

    /**
     * Obtém o nome de todas as variáveis definidas para este teste. A ordem dos
     * nomes corresponde à mesma ordem dos valores em getVariaveisValor().
     *
     * @return Vetor com nome das variáveis deste teste.
     */
    public final String[] getVariaveisNome() {
        return variaveisNome;
    }

    /**
     * Obtém o valor de todas as variáveis definidas para este teste. A ordem
     * dos valores corresponde à mesma ordem dos nomes em getVariaveisNome().
     *
     * @return Vetor com os valores das variáveis deste teste.
     */
    public final Float[] getVariaveisValor() {
        return variaveisValor;
    }

    /**
     * Obtém o valor esperado por este teste.
     *
     * @return Valor esperado deste teste.
     */
    public final Float getEsperado() {
        return esperado;
    }

    /**
     * Obtém o valor obtido por este teste. Deve ser calculado por
     * calcularValor() antes.
     *
     * @return Valor obtido pela expressão (e as variáveis) deste teste.
     */
    public final Float getObtido() {
        return obtido;
    }

    /**
     * Obtém se este teste obteve sucesso.
     *
     * @return Verdadeiro caso o teste obteve o valor esperado.
     */
    public final boolean getSucesso() {
        return sucesso;
    }

    /**
     * Executa o Parser (através da classe Calcular) para definir o valor obtido
     * por este teste, com base em sua expressão e suas variáveis. Se a
     * expressão não puder ser calculada, o valor obtido é null.
     *
     */
    public final void calcularValor() {
        try {
            this.obtido = Calcular.resultadoExpressao(this);
        } catch (Exception ex) {
            obtido = null;
            sucesso = false;
        }
    }

    /**
     * Atualiza se este Teste ocorreu com sucesso (resultado obtido é o
     * esperado) ou não, ou seja, atualiza o atributo "sucesso". Esta função não
     * é chamada logo no cálculo do valor ("calcularValor()") pois isto
     * implicaria em interferência na medição de tempo e memória do parser.
     * Desta forma, deve ser chamada após a medição do tempo.
     */
    public final void atualizarSucesso() {
        if (obtido != null) {
            this.sucesso = Objects.equals(esperado, obtido);
        } else {
            this.sucesso = false;
        }
    }

}