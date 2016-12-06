/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Lógica principal do programa .
 *
 * @author wesleywrl
 */
public final class Main {

    private Main() {
    }

    /**
     * Lógica principal do programa. Verifica os parâmetros fornecidos;
     * @param args Parâmetros. O primeiro parâmetro: local do arquivo
     * TXT de testes, local ou remoto. O segundo parâmetro deve ser "-h" 
     */
    public static void main(final String[] args) {
        boolean exportHtml = false;
        boolean localFile = true;
        List<String> testes = null;

        //Verifica a quantidade de parâmetros 
        if (args.length != 1 && args.length != 2) {
            erro("Quantidade de parâmetros inválida.");
        }

        //Verifica o segundo parâmetro (deve ser "-h")
        if (args.length == 2) {
            if (args[1].equals("-h")) {
                exportHtml = true;
            } else {
                erro("Segundo parâmetro inválido.");
            }
        }

        //Verifica se é local ou remoto
        if (args[0].startsWith("http")) {
            System.out.println("O arquivo de testes será obtido remotamente.");
            localFile = false;
        }

        //Adquire o arquivo
        System.out.println("Obtendo arquivo de testes...");
        try {
            testes = Ler.obterLinhas(args[0], localFile);
            System.out.println("Arquivo de testes obtido.");
        } catch (FileNotFoundException ex) {
            erro("Arquivo não encontrado.");
        } catch (MalformedURLException ex) {
            erro("A URL fornecida é inválida.");
        } catch (IOException ex) {
            erro("Não foi possível acessar o arquivo.");
        }

        //Obtém o diretório em que o relatório será salvo
        String diretorioQp = null;
        try {
            diretorioQp = new File(Main.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath())
                    .getParent();
        } catch (URISyntaxException ex) {
            erro("Não é possível gerar arquivo de relatório no "
                    + "diretório atual.");
        }

        //Realiza as expressões e gera o relatório
        System.out.println("Realizando expressões matemáticas...");
        RelatorioTeste gerador = new RelatorioTeste(testes, exportHtml);
        try {
            gerador.gerarRelatorio(diretorioQp);
        } catch (IOException ex) {
            erro("Não foi possível gerar arquivo de relatório.");
        }
        System.out.println("Expressões realizadas.");
        if (gerador.todosSucessos()) {
            System.out.println("Todos os testes obtiveram SUCESSO.");
        } else {
            System.out.println("Alguns testes  FALHARAM.");
        }
        if (exportHtml) {
            System.out.println("Relatório HTML salvo em \"" + diretorioQp
                    + "\\relatorio.html.\"");
        } else {
            System.out.println("Relatório JSON salvo em \"" + diretorioQp
                    + "\\relatorio.json.\"");
        }
        System.exit(0);
    }

    /**
     * Finaliza o programa com erro.
     *
     * @param mensagem Mensagem a ser exibida antes de finalizar.
     */
    public static void erro(final String mensagem) {
        System.out.println(mensagem);
        System.exit(1);
    }

}
