
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
 * @author Wesleywrl
 */
public final class Calcular {

    public Calcular() {
    }

    /**
     * O Parser usará o valor de uma expressão matemática;
     * @param teste Teste a ser analisado.
     * @return o resultado do teste.
     * @throws Exception: expressão inválida.
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