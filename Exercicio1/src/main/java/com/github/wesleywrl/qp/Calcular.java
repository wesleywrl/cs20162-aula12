/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import com.github.kyriosdata.parser.Lexer;
import com.github.kyriosdata.parser.Parser;
import com.github.kyriosdata.parser.Token;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utiliza o Parser a ser testado para o cálculo de expressões matemáticas com
 * ou sem variáveis.
 *
 * @author Wesleywrl
 */
public final class Calcular {

    /**
     * Impede instancialização de classe utilitária.
     */
    private Calcular() {
    }

    /**
     * Usa o Parser o valor de uma expressão matemática, com base nos valores
     * das variáveis fornecidas (no formato "x=1" separadas por vírgula).
     *
     * @param teste Teste a ser analisado (deve possuir expressão e talvez
     * variáveis).
     * @return Resultado da expressão contida no teste fornecido, com base em
     * suas variáveis.
     * @throws Exception Quando a expressão é inválida.
     */
    public static float resultadoExpressao(final Teste teste)
            throws Exception {

        if (teste.getVariaveis().isEmpty()) {
            List<Token> tokens = new Lexer(teste.getExpressao()).tokenize();
            Parser parser = new Parser(tokens);
            float result;
            try {
                result = parser.expressao().valor();
            } catch (IllegalArgumentException iae) {
                throw new Exception();
            }
            return result;

        } else {
            Map<String, Float> ctx = new HashMap<>();
            for (int i = 0; i < teste.getVariaveisNome().length; i++) {
                ctx.put(teste.getVariaveisNome()[i],
                        teste.getVariaveisValor()[i]);
            }
            List<Token> tokens = new Lexer(teste.getExpressao()).tokenize();
            Parser parser = new Parser(tokens);
            float result;
            try {
                result = parser.expressao().valor(ctx);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException();
            }
            return result;
        }
    }

}
